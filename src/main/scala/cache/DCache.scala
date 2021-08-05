package cache
//TODO
//可以用xpm_memory的byte enable信号
//从fifo装载回cache
//fifo判断命中也在stage1完成

import ip.{BRamIPConfig, SimpleDualPortBram, DualPortLutram, LutRamIPConfig}
import cpu.defs.ConstantVal
import lib.Updating
import spinal.core._
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi.Axi4
import scala.language.postfixOps

class CPUDCacheInterface(config: CacheRamConfig) extends Bundle with IMasterSlave {
  val stage1 = new Bundle {
    val paddr     = UInt(32 bits) //EX阶段查TLB，在第一阶段就能拿到物理地址
    val keepRData = Bool          //是否保持住第一阶段读出来的cache值
  }

  val stage2 = new Bundle {
    val read             = Bool          //对应load
    val write            = Bool          //对应store
    val uncache          = Bool          //是否是uncache操作
    val paddr            = UInt(32 bits) //物理地址
    val wdata: Bits      = Bits(32 bits) //希望写入内存的内容
    val byteEnable: Bits = Bits(4 bits)  //???1 -> enable byte0, ??1? -> enable byte1....

    val rdata       = Bits(32 bits) //读出来的内容
    val stall: Bool = Bool          //是否暂停
  }

  // dcache的invalidate不需要地址，因为地址就是stage2.paddr
  val invalidate = new Bundle {
    val en = Bool.default(False)
  }

  override def asMaster(): Unit = {
    out(
      stage1,
      stage2.read,
      stage2.write,
      stage2.paddr,
      stage2.wdata,
      stage2.byteEnable,
      stage2.uncache,
      invalidate
    )
    in(stage2.rdata, stage2.stall)
  }
}

//关于uncache.byteEnable的说明：
//AXI支持不对齐传输，我们awsize是2，对应一次传输4 byte，cache会始终把给AXI的地址对齐4byte
//但是你的地址(uncache.addr)可以不对齐，比如sb指令末两bit是10
//那么这个时候你wdata的第2个byte（编号从0开始算的）才会写入
//byteEnable的作用是告诉AXI哪些byte是有效的，会给到strb，上面的例子中sb对应的byteEnable应该是0100
//wdata(16, 8 bits)才是应该写入byte

//关于cache.byteEnable的说明：
//为了和uncache.byteEnable一致，
//比如你sb要写的地址末两位是10，传入的地址不必对齐
//那么你传入的wdata应该是wdata(16, 8 bits)是要写入的数据（寄存器的低8位应该放到这里）
//并且byteEnable应该是0100

class DCache(config: CacheRamConfig, fifoDepth: Int = 16) extends Component {
  private implicit class ParseAddr(addr: UInt) {
    assert(addr.getBitsWidth == 32)
    def index: UInt = addr(config.offsetWidth, config.indexWidth bits)

    def cacheTag: UInt = addr(config.offsetWidth + config.indexWidth, config.tagWidth bits)

    def wordOffset: UInt = addr(2, config.wordOffsetWidth bits)
  }

  /** 第一阶段和第二阶段之间需要寄存的 */
  class PipeS1S2 extends Bundle {
    val hitPerWay = Bits(config.wayNum bits)
    val hit       = Bool
    val fifo = new Bundle {
      val hit     = Bool
      val hitAddr = UInt(log2Up(writeBufferConfig.depth) bit)
      val rdata   = Bits(writeBufferConfig.dataWidth bits)
    }
  }
  val io = new Bundle {
    val cpu = slave(new CPUDCacheInterface(config))
    val axi = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
    //之所以和DCache放在一起，是保证uncached和cached的一致（受一个状态机管理）
    val uncacheAXI = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
  }

  val writeBufferConfig =
    WriteBufferConfig(config.blockSize, config.tagWidth + config.indexWidth, fifoDepth)
  val dataRamConfig    = BRamIPConfig(Block.getBitWidth(config.blockSize))
  val ramDepth: Int    = dataRamConfig.depth
  val tagBitWidth: Int = DMeta.getBitWidth(config.tagWidth)
  val tagRamConfig     = LutRamIPConfig(tagBitWidth, ramDepth * tagBitWidth)

  // 解析第一阶段的物理地址
  val stage1 = new Area {
    val index = io.cpu.stage1.paddr.index
    val tag   = io.cpu.stage1.paddr.cacheTag
//    val fifo = new Area {
//      val queryTag = Bits(writeBufferConfig.tagWidth bits)
//      val hit      = Reg(Bool) init False
//      val hitAddr  = Reg(UInt(log2Up(writeBufferConfig.depth) bit)) init 0
//      val rdata    = Reg(Bits(writeBufferConfig.dataWidth bits)) init 0
//    }
  }

  //解析Stage 2的物理地址
  val stage2 = new Area {
    val wordOffset = io.cpu.stage2.paddr.wordOffset
    val index      = io.cpu.stage2.paddr.index
    val tag        = io.cpu.stage2.paddr.cacheTag
    val fifo = new Area {
      val wdata = Bits(writeBufferConfig.dataWidth bits)
      val be    = Bits(writeBufferConfig.beWidth bits)
      val waddr = UInt(log2Up(writeBufferConfig.depth) bits)
      val we    = Bool
    }
  }

  /** **********************
    * WRITE BUFFER
    * **********************
    */
  // write buffer需要记录cache的tag和index
  val writeBuffer = new WriteBuffer(writeBufferConfig)
//  writeBuffer.io.query.tag := stage1.fifo.queryTag

  writeBuffer.io.merge.waddr := stage2.fifo.waddr
  writeBuffer.io.merge.wdata := stage2.fifo.wdata
  writeBuffer.io.merge.byteEnable := stage2.fifo.be
  writeBuffer.io.merge.write := stage2.fifo.we

  /** **********************
    * CACHE RAM
    * **********************
    */
  val ram = new Area {
    val tags  = Array.fill(config.wayNum)(new DualPortLutram(tagRamConfig))
    val datas = Array.fill(config.wayNum)(new SimpleDualPortBram(dataRamConfig))

    val read = new Bundle {
      val addr = UInt(config.indexWidth bits)
      val tags  = Vec(DMeta(config.tagWidth), config.wayNum)  //仅在stage1使用
      val datas = Vec(Block(config.blockSize), config.wayNum) //会在stage2使用
    }
    val we = new Bundle {
      val tag = Bits(config.wayNum bits)
      val data = Bits(config.wayNum bits)
    }
    val write = new Bundle {
      val addr = UInt(config.indexWidth bits)
      val tag = DMeta(config.tagWidth)
      val data = Bits(config.bitNum bits)
    }
    val _write_ = write
    val prev = new Area {
      val write = new Bundle {
        val data = RegNext(_write_.data)
        val addr = RegNext(_write_.addr)
        val valid = RegNext(we.data)
      }
    }
    val extra = new Bundle {
      val tags = Vec(DMeta(config.tagWidth), config.wayNum)
    }

    for(i <- 0 until config.wayNum) {
      /** Port B */
      tags(i).io.portB.en := True
      tags(i).io.portB.addr := read.addr
      read.tags(i).assignFromBits(tags(i).io.portB.dout)

      datas(i).io.portB.en := True
      datas(i).io.portB.addr := read.addr
      read.datas(i).assignFromBits(datas(i).io.portB.dout)
      /** Port A */
      tags(i).io.portA.en := True
      tags(i).io.portA.we := we.tag(i)
      tags(i).io.portA.addr := write.addr
      tags(i).io.portA.din.assignFromBits(write.tag.asBits)
      extra.tags(i).assignFromBits(tags(i).io.portA.dout)

      datas(i).io.portA.en := True
      datas(i).io.portA.we := we.data(i)
      datas(i).io.portA.addr := write.addr
      datas(i).io.portA.din := write.data
    }
  }

  /** **********************
    * LRU
    * **********************
    */
  val LRU = new Area {
    val mem = new LRUManegr(config.wayNum, config.setNum)
    // LRU读端口
    val rindex = RegNextWhen(mem.latestWayIndex(stage1.index), !io.cpu.stage1.keepRData)
    // LRU写端口
    val access = U(0, log2Up(config.wayNum) bits)
    val we     = False //默认为False
    mem.write.access := access
    mem.write.en := we
    mem.write.addr := stage2.index
  }

  /** *********************************
    * ************ STAGE   1 **********
    * *********************************
    */
  val sendToS2 = Stream(new PipeS1S2)
  //only drive portB for read
  ram.read.addr := stage1.index
  //第一阶段组合逻辑读tag，打一拍读data
  // 根据tag判断是否命中，当不需要keepRData时才更新
  for (i <- 0 until config.wayNum) {
    sendToS2.hitPerWay(i) := (ram.read.tags(i).tag === stage1.tag.asBits) & ram.read.tags(i).valid
  }
  sendToS2.hit := sendToS2.hitPerWay.orR
  writeBuffer.io.query.tag := stage1.tag ## stage1.index
  sendToS2.fifo.hit := writeBuffer.io.query.hit
  sendToS2.fifo.hitAddr := writeBuffer.io.query.hitAddr
  sendToS2.fifo.rdata := writeBuffer.io.query.rdata
  sendToS2.valid := True

  /** *********************************
    * ************ STAGE   2 **********
    * *********************************
    */
  val recvFromS1 = sendToS2.m2sPipe()
  recvFromS1.ready := !io.cpu.stage2.stall

  //only drive portA for write
  ram.write.addr := stage2.index
  val cache = new Area {
    val read: Bool  = io.cpu.stage2.read & !io.cpu.stage2.uncache
    val write: Bool = io.cpu.stage2.write & !io.cpu.stage2.uncache
    val wdata       = io.cpu.stage2.wdata
    val rdata       = Bits(32 bits)

    /** cache是否直接命中 */
    val hit: Bool = recvFromS1.hit
    //把hitPerWay中为1的那一路cache对应数据拿出来
    val hitLine: Block = Block.fromBits(0, config.blockSize, 4)
    for(i <- 0 until config.wayNum) {
      when(recvFromS1.hitPerWay(i)) {
        LRU.access := i
        when(ram.prev.write.addr === stage2.index & ram.prev.write.valid(i)) {
          hitLine.assignFromBits(ram.prev.write.data)
        }.otherwise(hitLine := ram.read.datas(i))
      }
    }
    //读或写命中时更新LRU
    when((read | write) & hit) (LRU.we := True)

    val replaceIndex = LRU.mem.latestWayIndex(stage2.index)
  }

  val uncache = new Area {
    val read: Bool  = io.cpu.stage2.read & io.cpu.stage2.uncache
    val write: Bool = io.cpu.stage2.write & io.cpu.stage2.uncache
    val wdata       = io.cpu.stage2.wdata
    val rdata       = Bits(32 bits)
  }

  val wb = new Area {
    val tag   = Reg(UInt(config.tagWidth bits)) init (0)
    val index = Reg(UInt(config.indexWidth bits)) init (0)
    val data = Reg(Block(config.blockSize)) init Block.fromBits(
      B(0, config.bitNum bits),
      config.blockSize,
      4
    )
    val writing: Bool = Bool //当前是否正在写回内存
    //正在写回内存的数据命中读请求
    val hit: Bool = tag === stage2.tag & index === stage2.index & writing
  }

  io.cpu.stage2.rdata := io.cpu.stage2.uncache ? uncache.rdata | cache.rdata
  /*
   * Default value
   */
  val cacheAXIDefault = new Area {
    //Default Value of Cached AXI
    //ar
    io.axi.ar.addr := stage2.tag @@ stage2.index @@ U(0, config.offsetWidth bits)
    io.axi.ar.id := U"4'b0001"
    io.axi.ar.lock := 0
    io.axi.ar.cache := 0
    io.axi.ar.prot := 0
    io.axi.ar.len := config.wordNum - 1
    io.axi.ar.size := U"3'b010" //2^2 = 4Bytes
    io.axi.ar.burst := B"2'b01" //INCR
    io.axi.ar.valid := False
    //r
    io.axi.r.ready := False
    //Write
    //aw
    io.axi.aw.id := U"4'b0001"
    io.axi.aw.lock := 0
    io.axi.aw.cache := 0
    io.axi.aw.prot := 0
    io.axi.aw.len := config.wordNum - 1
    io.axi.aw.size := U"3'b010" //2^2 = 4Bytes
    io.axi.aw.burst := B"2'b01" //INCR
    io.axi.aw.valid := False
    io.axi.aw.addr := wb.tag @@ wb.index @@ U(0, config.offsetWidth bits)
    //w
    io.axi.w.data := 0
    io.axi.w.valid := False
    io.axi.w.last := False
    io.axi.w.strb := B"4'b1111" //cache写回内存一定是4 byte都是有效数据
    //b
    io.axi.b.ready := True
  }

  // 由于串口等外设只能支持单字节读取，因此需要给出正确的size
  val uncacheAXIDefault = new Area {
    val size: UInt = CountOne(io.cpu.stage2.byteEnable)
    //Default Value of UnCached AXI
    when(size === 1) {
      io.uncacheAXI.ar.addr := io.cpu.stage2.paddr
      io.uncacheAXI.aw.addr := io.cpu.stage2.paddr
      io.uncacheAXI.ar.size := U"3'b000" //2^0 = 1Bytes
      io.uncacheAXI.aw.size := U"3'b000" //2^0 = 1Bytes
    }.elsewhen(size === 2) {
      io.uncacheAXI.ar.addr := io.cpu.stage2.paddr(31 downto 1) @@ U(0, 1 bits)
      io.uncacheAXI.aw.addr := io.cpu.stage2.paddr(31 downto 1) @@ U(0, 1 bits)
      io.uncacheAXI.ar.size := U"3'b001" //2^1 = 2Bytes
      io.uncacheAXI.aw.size := U"3'b001" //2^1 = 2Bytes
    }.otherwise {
      //size === 4
      io.uncacheAXI.ar.addr := io.cpu.stage2.paddr(31 downto 2) @@ U(0, 2 bits)
      io.uncacheAXI.aw.addr := io.cpu.stage2.paddr(31 downto 2) @@ U(0, 2 bits)
      io.uncacheAXI.ar.size := U"3'b010" //2^2 = 4Bytes
      io.uncacheAXI.aw.size := U"3'b010" //2^2 = 4Bytes
    }
//    io.uncacheAXI.ar.size := U"3'b010" //2^2 = 4Bytes
//    io.uncacheAXI.aw.size := U"3'b010" //2^2 = 4Bytes

    io.uncacheAXI.ar.id := U"4'b0010"
    io.uncacheAXI.ar.lock := 0
    io.uncacheAXI.ar.cache := 0
    io.uncacheAXI.ar.prot := 0
    io.uncacheAXI.ar.len := 0
    io.uncacheAXI.ar.burst := B"2'b01" //INCR
    io.uncacheAXI.ar.valid := False
    //r
    io.uncacheAXI.r.ready := False
    //Write
    //aw
    io.uncacheAXI.aw.id := U"4'b0010"
    io.uncacheAXI.aw.lock := 0
    io.uncacheAXI.aw.cache := 0
    io.uncacheAXI.aw.prot := 0
    io.uncacheAXI.aw.len := 0
    io.uncacheAXI.aw.burst := B"2'b01" //INCR
    io.uncacheAXI.aw.valid := False
    //w
    io.uncacheAXI.w.data := uncache.wdata
    io.uncacheAXI.w.valid := False
    io.uncacheAXI.w.last := False
    io.uncacheAXI.w.strb := io.cpu.stage2.byteEnable
    //b
    io.uncacheAXI.b.ready := True
  }

  /*
   * Logic
   */
  val readMiss  = Bool
  val writeMiss = Bool
  val recvBlock = Reg(new Block(config.blockSize))
  val counter = new Area {
    val recv = Counter(0 until config.wordNum) //读内存的寄存器
    val send = Counter(0 until config.wordNum) //写内存的计数器
    val inv  = Counter(0 to config.wayNum)     //刷新cache某个index时，用来的计算写回数的计数器
  }

  readMiss := !cache.hit & !recvFromS1.fifo.hit & cache.read   //读缺失
  writeMiss := !cache.hit & !recvFromS1.fifo.hit & cache.write //写缺失

  ram.we.assignFromBits(B(0, ram.we.getBitsWidth bits))
  writeBuffer.io.pop := False
  io.cpu.stage2.stall := True

  //这个状态机不包括写内存
  //写内存的逻辑是单独一个状态机，直接和fifo进行交互
  val dcacheFSM = new StateMachine {
    val waitWb       = new State //用来等待write_back的状态，并且会从cache中移动对应的数据到FIFO中
    val waitAXIReady = new State //等待arready信号
    val readMem      = new State //读内存

    val uncacheReadWaitAXIReady  = new State
    val uncacheReadMem           = new State
    val uncacheWriteWaitAXIReady = new State
    val uncacheWriteMem          = new State
    val uncacheWaitAXIBValid     = new State

    val invalidate = new State //清除某个Cache Index

    val refill = new State
    val finish = new State

    setEntry(stateBoot)
    disableAutoStart()

    stateBoot
      .whenIsActive {
        when(readMiss | writeMiss)(goto(waitWb))
        when(uncache.read) {
          when(io.uncacheAXI.ar.ready)(goto(uncacheReadMem))
            .otherwise(goto(uncacheReadWaitAXIReady))
        }
        when(uncache.write) {
          when(io.uncacheAXI.aw.ready)(goto(uncacheWriteMem))
            .otherwise(goto(uncacheWriteWaitAXIReady))
        }
        when(io.cpu.invalidate.en)(goto(invalidate)) //当要刷新时，可能需要进行写回
      }

    //这个状态是开始读一个cache line的前置状态，进入这个状态说明ram和fifo都没有命中
    //miss时写回脏行（实际上是push到write buffer中）同样经过这个状态
    //
    //如果能够在wb中命中，同样需要经过这个阶段
    //因为必须要用一个阶段来查是否需要写回替换行
    // TODO 之后要把write buffer命中的写回cache也需要安排到这个周期，原因也是为了替换可能的脏行
    waitWb.whenIsActive {
      //当FIFO要push（也就是要替换脏行时）并且FIFO已经满了的时候，需要等待
      when(!(writeBuffer.io.push & writeBuffer.io.full)) {
        when(wb.hit)(goto(stateBoot))
          .otherwise(goto(waitAXIReady))
        when(io.cpu.invalidate.en)(goto(stateBoot))
      }
    }

    //TODO 可以稍加优化，跳过这个阶段
    waitAXIReady.whenIsActive {
      when(io.axi.ar.ready)(goto(readMem))
    }
    readMem.whenIsActive {
      when(io.axi.r.valid & io.axi.r.last)(goto(refill))
    }

    //uncache state
    uncacheReadWaitAXIReady.whenIsActive {
      when(io.uncacheAXI.ar.ready)(goto(uncacheReadMem))
    }
    uncacheReadMem.whenIsActive {
      when(io.uncacheAXI.r.valid)(goto(stateBoot))
    }
    uncacheWriteWaitAXIReady.whenIsActive {
      when(io.uncacheAXI.aw.ready)(goto(uncacheWriteMem))
    }
    uncacheWriteMem.whenIsActive {
      when(io.uncacheAXI.w.ready)(goto(uncacheWaitAXIBValid))
    }
    uncacheWaitAXIBValid.whenIsActive {
      when(io.uncacheAXI.b.valid)(goto(stateBoot))
    }

    invalidate.whenIsActive {
      when(counter.inv.value === config.wayNum)(goto(stateBoot))
    }

    refill.whenIsActive {
      ram.read.addr := stage2.index
      goto(finish)
    }
    finish.whenIsActive(goto(stateBoot))
  }

  val wbFSM = new StateMachine {
    val waitAXIReady  = new State //等待aw.ready
    val writeMem      = new State
    val waitAXIBValid = new State //等待b.value
    setEntry(stateBoot)
    disableAutoStart()

    stateBoot.whenIsActive {
      writeBuffer.io.pop := True
      when(!writeBuffer.io.empty) {
        wb.data.assignFromBits(writeBuffer.io.popData)
        wb.index := writeBuffer.io.popTag(0, config.indexWidth bits).asUInt
        wb.tag := writeBuffer.io.popTag(config.indexWidth, config.tagWidth bits).asUInt
        goto(waitAXIReady)
      }
    }

    waitAXIReady.whenIsActive {
      counter.send.clear()
      io.axi.aw.valid := True //TODO 可以稍加优化
      when(io.axi.aw.ready)(goto(writeMem))
    }

    writeMem.whenIsActive {
      io.axi.w.valid := True
      io.axi.w.data := wb.data.banks(counter.send.value)
      io.axi.w.last := (counter.send.value === config.wordNum - 1)
      when(io.axi.w.ready)(counter.send.increment())
      when(io.axi.w.ready & io.axi.w.last)(goto(waitAXIBValid))
    }

    waitAXIBValid.whenIsActive {
      when(io.axi.b.valid)(goto(stateBoot))
    }
  }

  //dcache状态机的逻辑
  val dcacheFSMLogic = new Area {
    dcacheFSM.stateBoot.whenIsNext(io.cpu.stage2.stall := False)
    dcacheFSM.stateBoot.whenIsActive {
      io.uncacheAXI.ar.valid := uncache.read
      io.uncacheAXI.aw.valid := uncache.write
      counter.inv.clear()
    }
    dcacheFSM.waitAXIReady.whenIsActive {
      counter.recv.clear()
      io.axi.ar.valid := True
    }
    dcacheFSM.readMem.whenIsActive {
      io.axi.r.ready := True
      when(io.axi.r.valid) {
        counter.recv.increment()
        recvBlock.banks(counter.recv.value) := io.axi.r.data
      }
      when(io.axi.r.valid & io.axi.r.last) {
        ram.we.tag(cache.replaceIndex) := True
        ram.we.data(cache.replaceIndex) := True
        //发生行替换的时候需要更新LRU
        when(cache.read | cache.write) {
          LRU.we := True
          LRU.access := cache.replaceIndex
        }
      }
    }
    //uncache dcache状态
    dcacheFSM.uncacheReadWaitAXIReady.whenIsActive {
      io.uncacheAXI.ar.valid := True
    }
    dcacheFSM.uncacheReadMem.whenIsActive {
      io.uncacheAXI.r.ready := True
    }
    dcacheFSM.uncacheWriteWaitAXIReady.whenIsActive {
      io.uncacheAXI.aw.valid := True
    }
    dcacheFSM.uncacheWriteMem.whenIsActive {
      io.uncacheAXI.w.valid := True
      io.uncacheAXI.w.last := True
    }
  }

  //判断是否前传
  //前传发生在：两个阶段索引相同，第二个阶段没有stall，并且即将写ram的阶段
  // 值得一提的是：这里cacheTags和cacheDatas需要分成直接前传和打一拍前传
  // 因为cacheTags是组合逻辑读，而cacheDatas是空了一拍读；cacheTags需要当前修改的周期就前传
//  val tagForward: Bool = stage1.index === stage2.index & !io.cpu.stage2.stall &
//    (dcacheFSM.isActive(dcacheFSM.readMem) | dcacheFSM.isActive(dcacheFSM.waitWb) | cache.write)
//  when(tagForward) {
//    cacheTags(modifiedWayIndex.next) := writeMeta
//  }

  //cache读出的数据可能是
  //1.如果读miss，此时多打2个周期，能够从hitLine中读到
  //2.命中cache: hitLine
  //3.命中Fifo中的数据
  //4.命中**正在写回**的wb.data
  cache.rdata := cache.hitLine.banks(stage2.wordOffset)
  when(!cache.hit & recvFromS1.fifo.hit) {
    cache.rdata := recvFromS1.fifo.rdata(stage2.wordOffset << 5, 32 bits)
  }.elsewhen(!cache.hit & wb.hit) {
    cache.rdata := wb.data.banks(stage2.wordOffset)
  }
  //uncache读出的数据，直接从总线上拿
  uncache.rdata := io.uncacheAXI.r.data

  wb.writing := !wbFSM.isActive(wbFSM.stateBoot) //write buffer正在写回

  //Stage 2中和fifo相关的信号，fifo用来存放从cache中替换出去的行
  writeBuffer.io.push := dcacheFSM.isActive(dcacheFSM.waitWb) &
    ram.extra.tags(cache.replaceIndex).valid & ram.extra.tags(cache.replaceIndex).dirty
  writeBuffer.io.pushTag := ram.extra.tags(cache.replaceIndex).tag ## stage2.index
  writeBuffer.io.pushData := ram.read.datas(cache.replaceIndex).asBits

  stage2.fifo.wdata := 0
  stage2.fifo.wdata(
    stage2.wordOffset << 5,
    32 bits
  ) := cache.wdata //byteOffset << 3对应cpu.addr的bit偏移
  stage2.fifo.be := 0
  stage2.fifo.be(stage2.wordOffset << 2, 4 bits) := io.cpu.stage2.byteEnable
  stage2.fifo.we := cache.write & !io.cpu.stage2.stall & recvFromS1.fifo.hit
  stage2.fifo.waddr := recvFromS1.fifo.hitAddr

  //写入dcache ram的信号
  ram.write.tag.dirty := False
  ram.write.tag.valid := True
  ram.write.tag.tag := stage2.tag.asBits
  dcacheFSM.waitWb.whenIsActive {
    when(wb.hit) {
      ram.we.tag(cache.replaceIndex) := True
      ram.we.data(cache.replaceIndex) := True
      //发生行替换的时候需要更新LRU
      when(cache.read | cache.write) {
        LRU.we := True
        LRU.access := cache.replaceIndex
      }
    }
  }

  dcacheFSM.stateBoot.whenIsActive {
    //当直接写cache命中时，会开启dcache的写使能
    //写DCache命中的时候cache.hit为真，LRU已经默认更新了
    when(cache.write & cache.hit) {
      ram.we.tag := recvFromS1.hitPerWay
      ram.we.data := recvFromS1.hitPerWay
    }
  }
  //写入dache ram的数据
  //1.从内存读出来数据
  //2.从wb的data中命中的数据
  //3.TODO 从fifo中命中的数据？
  ram.write.data := recvBlock.asBits
  when(dcacheFSM.isActive(dcacheFSM.readMem)) {
    ram.write.data((config.wordNum - 1) << log2Up(32), 32 bits) := io.axi.r.data
  }.elsewhen(dcacheFSM.isActive(dcacheFSM.waitWb)) {
    ram.write.data := wb.data.asBits //在waitWb阶段也可能写cache ram，此时命中正在写回内存的数据
  }.elsewhen(dcacheFSM.isActive(dcacheFSM.stateBoot)) {
    ram.write.data := cache.hitLine.asBits //直接cache命中则使用cache的值
  }
  //如果是写指令，那么还需要通过byteEnable修改dcache.writeData
  when(cache.write) {
    ram.write.tag.dirty := True
    for (i <- 0 until 4) {
      when(io.cpu.stage2.byteEnable(i)) {
        ram.write.data(stage2.wordOffset @@ U(i * 8, 5 bits), 8 bits) := cache.wdata(i * 8, 8 bits)
      }
    }
  }
  // 刷新Cache某个index的写回检测逻辑
  dcacheFSM.invalidate.whenIsActive {
    val wayIndex = counter.inv.value.resize(log2Up(config.wayNum))
    when(ram.extra.tags(wayIndex).valid & ram.extra.tags(wayIndex).dirty) {
      writeBuffer.io.push := True
      writeBuffer.io.pushTag := ram.extra.tags(wayIndex).tag ## stage2.index
      writeBuffer.io.pushData := ram.read.datas(wayIndex).asBits
      when(!writeBuffer.io.full)(counter.inv.increment())
    }.otherwise(counter.inv.increment())

    // 最后一拍时写回
    for (i <- 0 until config.wayNum) {
      when(counter.inv.valueNext === config.wayNum) {
        ram.we.tag(i) := True
        ram.write.tag.valid := False
      }
    }
  }
}

object DCache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DCache(CacheRamConfig(), 16)).printPruned()
  }
}
