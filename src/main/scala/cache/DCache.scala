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

  override def asMaster(): Unit = {
    out(
      stage1,
      stage2.read,
      stage2.write,
      stage2.paddr,
      stage2.wdata,
      stage2.byteEnable,
      stage2.uncache
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
  val io = new Bundle {
    val cpu = slave(new CPUDCacheInterface(config))
    val axi = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
    //之所以和DCache放在一起，是保证uncached和cached的一致（受一个状态机管理）
    val uncacheAXI = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
  }

  val writeBufferConfig =
    WriteBufferConfig(config.blockSize, config.tagWidth + config.indexWidth, fifoDepth)
  val dataRamConfig    = BRamIPConfig(Block.getBitWidth(config.blockSize))
  val ramDepth: Int       = dataRamConfig.depth
  val tagBitWidth: Int = DMeta.getBitWidth(config.tagWidth)
  val tagRamConfig     = LutRamIPConfig(tagBitWidth, ramDepth * tagBitWidth)

  // 解析第一阶段的物理地址
  val stage1 = new Area {
    val index = io.cpu.stage1.paddr(config.offsetWidth, config.indexWidth bits)
    val tag   = io.cpu.stage1.paddr(config.offsetWidth + config.indexWidth, config.tagWidth bits)
    val fifo = new Area {
      val queryTag = Bits(writeBufferConfig.tagWidth bits)
      val hit = Reg(Bool) init False
      val hitAddr = Reg(UInt(log2Up(writeBufferConfig.depth) bit)) init 0
      val rdata = Reg(Bits(writeBufferConfig.dataWidth bits)) init 0
    }
  }

  //解析Stage 2的物理地址
  val stage2 = new Area {
    val wordOffset = io.cpu.stage2.paddr(2, config.wordOffsetWidth bits)
    val index      = io.cpu.stage2.paddr(config.offsetWidth, config.indexWidth bits)
    val tag        = io.cpu.stage2.paddr(config.offsetWidth + config.indexWidth, config.tagWidth bits)
    val fifo = new Area {
      val wdata = Bits(writeBufferConfig.dataWidth bits)
      val be = Bits(writeBufferConfig.beWidth bits)
      val waddr = UInt(log2Up(writeBufferConfig.depth) bits)
      val we = Bool
    }
  }

  /** **********************
    * WRITE BUFFER
    * **********************
    */
  // write buffer需要记录cache的tag和index
  val writeBuffer = new WriteBuffer(writeBufferConfig)
  writeBuffer.io.query.tag := stage1.fifo.queryTag
  when(!io.cpu.stage1.keepRData) {
    // 寄存器在不需要保持时更新
    stage1.fifo.hit := writeBuffer.io.query.hit
    stage1.fifo.hitAddr := writeBuffer.io.query.hitAddr
    stage1.fifo.rdata := writeBuffer.io.query.rdata
  }
  writeBuffer.io.merge.waddr := stage2.fifo.waddr
  writeBuffer.io.merge.wdata := stage2.fifo.wdata
  writeBuffer.io.merge.byteEnable := stage2.fifo.be
  writeBuffer.io.merge.write := stage2.fifo.we

  /** **********************
    * CACHE RAM
    * **********************
    */
  val cacheRam = new Area {
    val tags             = Array.fill(config.wayNum)(new DualPortLutram(tagRamConfig))
    val datas            = Array.fill(config.wayNum)(new SimpleDualPortBram(dataRamConfig))
  }

  /** **********************
    * LRU
    * **********************
    */
  val LRU = new Area {
    val mem        = new LRUManegr(config.wayNum, config.setSize)
    val calculator = new LRUCalculator(config.wayNum)
    // LRU读端口
    mem.io.read.addr := stage1.index
    //如果需要保持住当前阶段读出的值（比如EX.stall住了），那么直接保持replaceAddr
    val rindex = RegNextWhen(
      calculator.leastRecentUsedIndex(mem.io.read.data),
      !io.cpu.stage1.keepRData
    ) init 0
    // LRU写端口
    val access = U(0, log2Up(config.wayNum) bits)
    val we    = False //默认为False
    mem.io.write.access := access
    mem.io.write.en := we
    mem.io.write.addr := stage2.index
  }

  /** *********************************
    * ************ STAGE   1 **********
    * *********************************
    */
  //only drive portB for read
  //第一阶段组合逻辑读tag，打一拍读data
//  val keepRData = RegNext(io.cpu.stage1.keepRData) init (False)
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portB.en := !io.cpu.stage1.keepRData
    cacheRam.tags(i).io.portB.addr := stage1.index

    cacheRam.datas(i).io.portB.en := !io.cpu.stage1.keepRData
    cacheRam.datas(i).io.portB.addr := stage1.index
  }

  val cacheTags  = Vec(DMeta(config.tagWidth), config.wayNum)
  val cacheDatas = Vec(Block(config.blockSize), config.wayNum)
  for (i <- 0 until config.wayNum) {
    cacheTags(i).assignFromBits(cacheRam.tags(i).io.portB.dout)
    cacheDatas(i).assignFromBits(cacheRam.datas(i).io.portB.dout)
  }

  // 同时根据tag判断是否命中，当不需要keepRData更新
  // 注意这里tag是组合逻辑读，因此保持数据时使用io.cpu.stage1.keepRData，而不是打了一拍的keepRData
  //
  //值得一提的是，第二阶段不会用到cacheTags
  val hitPerWay: Bits = Reg(Bits(config.wayNum bits)) init (0) //cache每一路是否命中
  for (i <- 0 until config.wayNum) {
    hitPerWay(i) := (cacheTags(i).tag === stage1.tag.asBits) & cacheTags(i).valid
  }

  //选出可能替换的那一路的地址
  val replace = new Area {
    val wayIndex = Reg(UInt(log2Up(config.wayNum) bits)) init (0)
    when(!io.cpu.stage1.keepRData) {
      wayIndex := LRU.rindex
      for (i <- 0 until config.wayNum) {
        when(!cacheTags(i).valid)(wayIndex := i) //如果有未使用的cache line，优先替换它
      }
    }
  }
  stage1.fifo.queryTag := stage1.tag ## stage1.index

  /** *********************************
    * ************ STAGE   2 **********
    * *********************************
    */

  //only drive portA for write
  val dataWE   = Bits(config.wayNum bits)                   //写哪一路cache.data的使能
  val tagWE    = Bits(config.wayNum bits)                   //写哪一路cache.meta的使能
  val pushTags = Vec(DMeta(config.tagWidth), config.wayNum) //被替换行的Tag信息
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portA.en := True
    cacheRam.tags(i).io.portA.we := tagWE(i)
    cacheRam.tags(i).io.portA.addr := stage2.index
    pushTags(i).assignFromBits(cacheRam.tags(i).io.portA.dout)

    cacheRam.datas(i).io.portA.en := True
    cacheRam.datas(i).io.portA.we := dataWE(i)
    cacheRam.datas(i).io.portA.addr := stage2.index
  }
  val writeMeta = DMeta(config.tagWidth)    //要写入cache的Meta
  val writeData = Bits(config.bitSize bits) //要写入cache的Block
  //TODO 加入invalidate功能后需要修改valid的逻辑
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portA.din := writeMeta.asBits
    cacheRam.datas(i).io.portA.din := writeData.asBits
  }

  val cache = new Area {
    val read: Bool  = io.cpu.stage2.read & !io.cpu.stage2.uncache
    val write: Bool = io.cpu.stage2.write & !io.cpu.stage2.uncache
    val wdata       = io.cpu.stage2.wdata
    val rdata       = Bits(32 bits)

    /** cache是否直接命中 */
    val hit: Bool = hitPerWay.orR
    //把hitPerWay中为1的那一路cache对应数据拿出来
    val hitLine: Block = MuxOH(hitPerWay, for (i <- 0 until config.wayNum) yield cacheDatas(i))
    //读或写命中时更新LRU
    when((read | write) & hit) {
      LRU.we := True
      LRU.access := OHToUInt(hitPerWay)
    }
  }

  val uncache = new Area {
    val read: Bool  = io.cpu.stage2.read & io.cpu.stage2.uncache
    val write: Bool = io.cpu.stage2.write & io.cpu.stage2.uncache
    val wdata       = io.cpu.stage2.wdata
    val rdata       = Bits(32 bits)
  }

  val wb = new Area {
    val tag           = Reg(UInt(config.tagWidth bits)) init (0)
    val index         = Reg(UInt(config.indexWidth bits)) init (0)
    val data          = Reg(Block(config.blockSize)) init (Block.fromBits(0, config.blockSize))
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
    io.axi.ar.len := config.wordSize - 1
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
    io.axi.aw.len := config.wordSize - 1
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
  val recvBlock = Reg(new Block(config.blockSize)) init (Block.fromBits(B(0), config.blockSize))
  val counter = new Area {
    val recv = Counter(0 until config.wordSize) //读内存的寄存器
    val send = Counter(0 until config.wordSize) //写内存的计数器
  }

  readMiss := !cache.hit & !stage1.fifo.hit & cache.read    //读缺失
  writeMiss := !cache.hit & !stage1.fifo.hit & cache.write //写缺失

  tagWE := 0
  dataWE := 0
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
      }
    }

    //TODO 可以稍加优化，跳过这个阶段
    waitAXIReady.whenIsActive {
      when(io.axi.ar.ready)(goto(readMem))
    }
    readMem.whenIsActive {
      when(io.axi.r.valid & io.axi.r.last)(goto(stateBoot))
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
      io.axi.w.last := (counter.send.value === config.wordSize - 1)
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
        tagWE(replace.wayIndex) := True
        dataWE(replace.wayIndex) := True
        //发生行替换的时候需要更新LRU
        when(cache.read | cache.write) {
          LRU.we := True
          LRU.access := replace.wayIndex
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
  //1.如果读miss，那么只能是来自内存的数据送回cpu（此时数据在writeData中要写回ram）
  //2.中cache: hitLine
  //3.命中Fifo中的数据
  //4.命中**正在写回**的wb.data
  cache.rdata := cache.hitLine.banks(stage2.wordOffset)
  when(readMiss) {
    cache.rdata := writeData(stage2.wordOffset << 5, 32 bits)
  }.elsewhen(!cache.hit & stage1.fifo.hit) {
//    cache.rdata := fifo.queryRData(stage2.wordOffset << 5, 32 bits)
    cache.rdata := stage1.fifo.rdata(stage2.wordOffset << 5, 32 bits)
  }.elsewhen(!cache.hit & wb.hit) {
    cache.rdata := wb.data.banks(stage2.wordOffset)
  }
  //uncache读出的数据，直接从总线上拿
  uncache.rdata := io.uncacheAXI.r.data

  wb.writing := !wbFSM.isActive(wbFSM.stateBoot) //write buffer正在写回

  //Stage 2中和fifo相关的信号，fifo用来存放从cache中替换出去的行
  writeBuffer.io.push := dcacheFSM.isActive(dcacheFSM.waitWb) &
    pushTags(replace.wayIndex).valid & pushTags(replace.wayIndex).dirty
  writeBuffer.io.pushTag := pushTags(replace.wayIndex).tag ## stage2.index
  writeBuffer.io.pushData := cacheDatas(replace.wayIndex).asBits

  stage2.fifo.wdata := 0
  stage2.fifo.wdata(stage2.wordOffset << 5, 32 bits) := cache.wdata //byteOffset << 3对应cpu.addr的bit偏移
  stage2.fifo.be := 0
  stage2.fifo.be(stage2.wordOffset << 2, 4 bits) := io.cpu.stage2.byteEnable
  stage2.fifo.we := cache.write & !io.cpu.stage2.stall & stage1.fifo.hit
  stage2.fifo.waddr := stage1.fifo.hitAddr


  //写入dcache ram的信号
  writeMeta.dirty := False
  writeMeta.valid := True
  writeMeta.tag := stage2.tag.asBits
  dcacheFSM.waitWb.whenIsActive {
    when(wb.hit) {
      tagWE(replace.wayIndex) := True
      dataWE(replace.wayIndex) := True
      //发生行替换的时候需要更新LRU
      when(cache.read | cache.write) {
        LRU.we := True
        LRU.access := replace.wayIndex
      }
    }
  }

  dcacheFSM.stateBoot.whenIsActive {
    //当直接写cache命中时，会开启dcache的写使能
    //写DCache命中的时候cache.hit为真，LRU已经默认更新了
    when(cache.write & cache.hit) {
      tagWE := hitPerWay
      dataWE := hitPerWay
    }
  }
  //写入dache ram的数据
  //1.从内存读出来数据
  //2.从wb的data中命中的数据
  //3.TODO 从fifo中命中的数据？
  writeData := recvBlock.asBits
  when(dcacheFSM.isActive(dcacheFSM.readMem)) {
    writeData((config.wordSize - 1) << log2Up(32), 32 bits) := io.axi.r.data
  }.elsewhen(dcacheFSM.isActive(dcacheFSM.waitWb)) {
    writeData := wb.data.asBits //在waitWb阶段也可能写cache ram，此时命中正在写回内存的数据
  }.elsewhen(dcacheFSM.isActive(dcacheFSM.stateBoot)) {
    writeData := cache.hitLine.asBits //直接cache命中则使用cache的值
  }
  //如果是写指令，那么还需要通过byteEnable修改dcache.writeData
  when(cache.write) {
    writeMeta.dirty := True
    for (i <- 0 until 4) {
      when(io.cpu.stage2.byteEnable(i)) {
        writeData((stage2.wordOffset << 5) + (i * 8), 8 bits) := cache.wdata(i * 8, 8 bits)
      }
    }
  }

//  io.uncacheAXI.w.ready.addAttribute("mark_debug", "true")
//  io.uncacheAXI.w.valid.addAttribute("mark_debug", "true")
//  io.uncacheAXI.w.data.addAttribute("mark_debug", "true")
//  io.uncacheAXI.w.strb.addAttribute("mark_debug", "true")
//  io.uncacheAXI.w.last.addAttribute("mark_debug", "true")
//
//  io.uncacheAXI.aw.addr.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.len.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.size.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.valid.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.ready.addAttribute("mark_debug", "true")
//
//  io.uncacheAXI.b.ready.addAttribute("mark_debug", "true")
//  io.uncacheAXI.b.valid.addAttribute("mark_debug", "true")
}

object DCache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DCache(CacheRamConfig(), 16)).printPruned()
  }
}
