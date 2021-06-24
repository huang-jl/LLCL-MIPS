package cache
//TODO
//1.写cache ram的使能 V
//2.写fifo的使能 V
//3.什么时候stall V
//4.更新lru的使能 V

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi.{Axi4, Axi4Config}

import ip.SinglePortRam

class CPUDCacheInterface extends Bundle with IMasterSlave {
  val read: Bool = Bool
  val write: Bool = Bool
  val addr: UInt = UInt(32 bits)
  val rdata: Bits = Bits(32 bits)
  val wdata: Bits = Bits(32 bits)
  val byteEnable: Bits = Bits(4 bits) //???1 -> enable byte0, ??1? -> enable byte1....
  val stall: Bool = Bool

  override def asMaster(): Unit = {
    out(read, write, addr, byteEnable, wdata)
    in(rdata, stall)
  }

  def <<(that: CPUDCacheInterface): Unit = that >> this

  def >>(that: CPUDCacheInterface): Unit = {
    //This is master
    that.read := this.read
    that.write := this.write
    that.addr := this.addr
    that.wdata := this.wdata
    that.byteEnable := this.byteEnable
    this.rdata := that.rdata
    this.stall := that.stall
  }
}

object DCache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DCache(CacheRamConfig(), 16)).printPruned()
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
  val writeBufferConfig = WriteBufferConfig(config.blockSize, config.tagWidth + config.indexWidth, fifoDepth, sim = config.sim)
  val io = new Bundle {
    val cpu = slave(new CPUDCacheInterface)
    val axi = master(new Axi4(Axi4Config(
      addressWidth = 32, dataWidth = 32, idWidth = 4,
      useRegion = false, useQos = false
    )))
    //uncached
    //之所以和DCache放在一起，是保证uncached和cached的一致（受一个状态机管理）
    val uncache = slave(new CPUDCacheInterface)
    val uncacheAXI = master(new Axi4(Axi4Config(
      addressWidth = 32, dataWidth = 32, idWidth = 4,
      useRegion = false, useQos = false
    )))
  }
  /*
   * Signal and Component Definition
   */
  val writeBuffer = new WriteBuffer(writeBufferConfig)
  val fifo = new WriteBufferInterface(writeBufferConfig)
  fifo <> writeBuffer.io

  val inputAddr = new Area {
    val addr: UInt = (io.uncache.read | io.uncache.write) ? io.uncache.addr | io.cpu.addr
    val tag: UInt = addr(config.offsetWidth + config.indexWidth, config.tagWidth bits)
    val index: UInt = addr(config.offsetWidth, config.indexWidth bits)
    val wordOffset: UInt = addr(2 until config.offsetWidth)
    //offsetWidth = 5为例，此时cache line大小为32，所以byteOffset在0-31
    val byteOffset: UInt = (wordOffset ## B(0, 2 bits)).asUInt
  }

  val cacheRam = new Area {
    val simTags = if (config.sim) Array.fill(config.wayNum)(new Mem(
      Bits(DMeta.getBitWidth(config.tagWidth) bits), config.setSize)) else null
    val simDatas = if (config.sim) Array.fill(config.wayNum)(new Mem(
      Bits(Block.getBitWidth(config.blockSize) bits), config.setSize)) else null
    val tags = if (!config.sim) Array.fill(config.wayNum)(new SinglePortRam(
      DMeta.getBitWidth(config.tagWidth), 1 << config.indexWidth, "distributed")) else null
    val datas = if (!config.sim) Array.fill(config.wayNum)(new SinglePortRam(
      Block.getBitWidth(config.blockSize), 1 << config.indexWidth, "block")) else null
    if (config.sim) {
      //仿真时初值
      for (i <- 0 until config.wayNum) {
        simTags(i).init(for (i <- 0 until config.setSize) yield B(0))
        simDatas(i).init(for (i <- 0 until config.setSize) yield B(0))
      }
    }
  }

  val dcache = new Area {
    val dataWE = Bits(config.wayNum bits) //写哪一路cache.data的使能
    val tagWE = Bits(config.wayNum bits) //写哪一路cache.meta的使能
    /*
     * Cache地址
     */
    if (!config.sim) {
      for (i <- 0 until config.wayNum) {
        cacheRam.tags(i).io.addr := inputAddr.index
        cacheRam.datas(i).io.addr := inputAddr.index
      }
    }
    /*
     * Cache读端口
     */
    val rdata = Vec(Block(config.blockSize), config.wayNum)
    val rmeta = Vec(DMeta(config.tagWidth), config.wayNum)
    for (i <- 0 until config.wayNum) {
      if (config.sim) {
        rmeta(i).assignFromBits(cacheRam.simTags(i).readAsync(inputAddr.index))
        rdata(i).assignFromBits(cacheRam.simDatas(i).readAsync(inputAddr.index))
      } else {
        rdata(i).assignFromBits(cacheRam.datas(i).io.dout)
        rmeta(i).assignFromBits(cacheRam.tags(i).io.dout)
      }
    }
    /*
     * Cache写端口
     */
    val writeMeta = DMeta(config.tagWidth) //要写入cache的Meta
    val writeData = Bits(config.bitSize bits) //要写入cache的Block
    //TODO 加入invalidate功能后需要修改valid的逻辑
    for (i <- 0 until config.wayNum) {
      if (config.sim) {
        cacheRam.simTags(i).write(inputAddr.index, writeMeta.asBits, enable = tagWE(i))
        cacheRam.simDatas(i).write(inputAddr.index, writeData, enable = dataWE(i))
      } else {
        cacheRam.tags(i).io.we := tagWE(i)
        cacheRam.datas(i).io.we := dataWE(i)
        cacheRam.tags(i).io.din := writeMeta.asBits
        cacheRam.datas(i).io.din := writeData.asBits
      }
    }

    val hitPerWay: Bits = Bits(config.wayNum bits) //cache每一路是否命中
    val hit: Bool = hitPerWay.orR //cache整体是否命中
    val readHit: Bool = hit & io.cpu.read
    val writeHit: Bool = hit & io.cpu.write
    //把hitPerWay中为1的那一路cache对应数据拿出来
    val hitLine: Block = MuxOH(hitPerWay, for (i <- 0 until config.wayNum) yield rdata(i))
    for (i <- 0 until config.wayNum) {
      hitPerWay(i) := (rmeta(i).tag.asUInt === inputAddr.tag) & rmeta(i).valid
    }
  }

  val plru = Array.fill(config.setSize)(new LRUManegr(config.wayNum)) //setSize是组的个数，不是“设置大小”的意思
  val plruAddr = Vec(UInt(log2Up(config.wayNum) bits), config.setSize)
  for (i <- 0 until config.setSize) {
    plruAddr(i) := plru(i).io.next
    plru(i).io.update := (!io.cpu.stall) & (inputAddr.index === i) & (io.cpu.read | io.cpu.write)
    plru(i).io.access := dcache.hitPerWay
  }

  //要替换的那一路的地址
  val replace = new Area {
    val addr = UInt(log2Up(config.wayNum) bits)
    addr := plruAddr(inputAddr.index)
    for (i <- 0 until config.wayNum) {
      when(!dcache.rmeta(i).valid)(addr := i) //如果有未使用的cache line，优先替换它
    }
  }

  val wb = new Area {
    val addr = Reg(UInt(32 bits)) init (0)
    val data = Reg(Block(config.blockSize)) init (Block.fromBits(0, config.blockSize))
    val writing: Bool = Bool //当前是否正在写回内存
    //正在写回内存的数据命中读请求
    val hit: Bool = (addr(31 downto config.offsetWidth).asBits === (inputAddr.tag ## inputAddr.index)) & writing
  }

  val readMiss = Bool
  val writeMiss = Bool
  val recvBlock = Reg(new Block(config.blockSize)) init (Block.fromBits(B(0), config.blockSize))
  val counter = new Area {
    val recv = Counter(0 until config.wordSize)
    val send = Counter(0 until config.wordSize)
  }
  /*
   * Default value
   */
  val default = new Area {
    dcache.tagWE := 0
    dcache.dataWE := 0
    fifo.pop := False
    io.cpu.stall := True
    //Default Value of Cached AXI
    //ar
    io.axi.ar.addr := (inputAddr.tag ## inputAddr.index ## B(0, config.offsetWidth bits)).asUInt
    io.axi.ar.id := U"4'b0000"
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
    io.axi.aw.id := U"4'b0000"
    io.axi.aw.lock := 0
    io.axi.aw.cache := 0
    io.axi.aw.prot := 0
    io.axi.aw.len := config.wordSize - 1
    io.axi.aw.size := U"3'b010" //2^2 = 4Bytes
    io.axi.aw.burst := B"2'b01" //INCR
    io.axi.aw.valid := False
    io.axi.aw.addr := wb.addr
    //w
    io.axi.w.data := 0
    io.axi.w.valid := False
    io.axi.w.last := False
    io.axi.w.strb := B"4'b1111" //cache写回内存一定是4 byte都是有效数据
    //b
    io.axi.b.ready := True

    //Default Value of UnCached AXI
    io.uncacheAXI.ar.addr := (inputAddr.addr(31 downto 2) ## B(0, 2 bits)).asUInt
    io.uncacheAXI.ar.id := U"4'b0000"
    io.uncacheAXI.ar.lock := 0
    io.uncacheAXI.ar.cache := 0
    io.uncacheAXI.ar.prot := 0
    io.uncacheAXI.ar.len := 0
    io.uncacheAXI.ar.size := U"3'b010" //2^2 = 4Bytes
    io.uncacheAXI.ar.burst := B"2'b01" //INCR
    io.uncacheAXI.ar.valid := False
    //r
    io.uncacheAXI.r.ready := False
    //Write
    //aw
    io.uncacheAXI.aw.id := U"4'b0000"
    io.uncacheAXI.aw.lock := 0
    io.uncacheAXI.aw.cache := 0
    io.uncacheAXI.aw.prot := 0
    io.uncacheAXI.aw.len := 0
    io.uncacheAXI.aw.size := U"3'b010" //2^2 = 4Bytes
    io.uncacheAXI.aw.burst := B"2'b01" //INCR
    io.uncacheAXI.aw.valid := False
    io.uncacheAXI.aw.addr := (inputAddr.addr(31 downto 2) ## B(0, 2 bits)).asUInt
    //w
    io.uncacheAXI.w.data := io.uncache.wdata
    io.uncacheAXI.w.valid := False
    io.uncacheAXI.w.last := False
    io.uncacheAXI.w.strb := io.uncache.byteEnable
    //b
    io.uncacheAXI.b.ready := True
  }

  /*
   * Logic
   */

  //这个状态机不包括写内存
  //写内存的逻辑是单独一个状态机，直接和fifo进行交互
  val dcacheFSM = new StateMachine {
    val waitWb = new State //用来等待write_back的状态，并且会从cache中移动对应的数据到FIFO中
    val waitAXIReady = new State //等待arready信号
    val readMem = new State //读内存

    val uncacheReadWaitAXIReady = new State
    val uncacheReadMem = new State
    val uncacheWriteWaitAXIReady = new State
    val uncacheWriteMem = new State
    val uncacheWaitAXIBValid = new State

    val done = new State
    setEntry(stateBoot)
    disableAutoStart()
    stateBoot
      .whenIsActive {
        when(readMiss | writeMiss)(goto(waitWb))
        when(io.uncache.read) {
          when(io.uncacheAXI.ar.ready)(goto(uncacheReadMem))
            .otherwise(goto(uncacheReadWaitAXIReady))
        }
        when(io.uncache.write) {
          when(io.uncacheAXI.aw.ready)(goto(uncacheWriteMem))
            .otherwise(goto(uncacheWriteWaitAXIReady))
        }
      }

    //这个状态是开始读一个cache line的前置状态
    waitWb.whenIsActive {
      //当FIFO要push并且FIFO已经满了的时候，需要等待
      when(!(fifo.push & fifo.full)) {
        when(wb.hit)(goto(stateBoot)) //在写回的数据中命中了，这个判断必须单独一个状态，否则没法stall
          .otherwise(goto(waitAXIReady))
      }
    }

    waitAXIReady.whenIsActive {
      when(io.axi.ar.ready)(goto(readMem))
    }
    readMem.whenIsActive {
      when(io.axi.r.valid & io.axi.r.last)(goto(done))
    }

    //uncache state
    uncacheReadWaitAXIReady.whenIsActive {
      when(io.uncacheAXI.ar.ready)(goto(uncacheReadMem))
    }
    uncacheReadMem.whenIsActive {
      when(io.uncacheAXI.r.valid)(goto(done))
    }
    uncacheWriteWaitAXIReady.whenIsActive {
      when(io.uncacheAXI.aw.ready)(goto(uncacheWriteMem))
    }
    uncacheWriteMem.whenIsActive {
      when(io.uncacheAXI.w.ready)(goto(uncacheWaitAXIBValid))
    }
    uncacheWaitAXIBValid.whenIsActive {
      when(io.uncacheAXI.b.valid)(goto(done))
    }

    done.whenIsActive(goto(stateBoot))
  }

  val wbFSM = new StateMachine {
    val waitAXIReady = new State //等待aw.ready
    val writeMem = new State
    val waitAXIBValid = new State //等待b.value
    setEntry(stateBoot)
    disableAutoStart()

    stateBoot.whenIsActive {
      fifo.pop := True
      when(!fifo.empty) {
        wb.data.assignFromBits(fifo.popData)
        wb.addr := (fifo.popTag ## B(0, config.offsetWidth bits)).asUInt
        goto(waitAXIReady)
      }
    }

    waitAXIReady.whenIsActive {
      counter.send.clear()
      io.axi.aw.valid := True
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
    dcacheFSM.stateBoot.whenIsNext(io.cpu.stall := False)
    dcacheFSM.stateBoot.whenIsActive {
      io.uncacheAXI.ar.valid := io.uncache.read
      io.uncacheAXI.aw.valid := io.uncache.write
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
        dcache.tagWE(replace.addr) := True
        dcache.dataWE(replace.addr) := True
      }
    }
    dcacheFSM.done.whenIsActive(io.cpu.stall := False)
    //uncache dcache状态
    dcacheFSM.uncacheReadWaitAXIReady.whenIsActive {
      io.uncacheAXI.ar.valid := True
    }
    dcacheFSM.uncacheReadMem.whenIsActive {
      io.uncacheAXI.r.ready := True
      when(io.uncacheAXI.r.valid) {
        recvBlock.banks(0) := io.uncacheAXI.r.data
      }
    }
    dcacheFSM.uncacheWriteWaitAXIReady.whenIsActive {
      io.uncacheAXI.aw.valid := True
    }
    dcacheFSM.uncacheWriteMem.whenIsActive {
      io.uncacheAXI.w.valid := True
      io.uncacheAXI.w.last := True
    }
  }
  //stall信号
  io.uncache.stall := io.cpu.stall

  readMiss := (!dcache.hit) & (!fifo.readHit) & (!wb.hit) & io.cpu.read //读缺失
  writeMiss := (!dcache.hit) & (!fifo.writeHit) & io.cpu.write //写缺失

  //cache读出的数据可能是
  //1.如果读miss，那么只能是来自内存的数据送回cpu
  //2.中cache: hitLine
  //3.命中Fifo中的数据
  //4.命中**正在写回**的wb.data
  io.cpu.rdata := dcache.hitLine.banks(inputAddr.wordOffset)
  when(readMiss) {
    io.cpu.rdata := recvBlock.banks(inputAddr.wordOffset)
  }.elsewhen(!dcache.hit & fifo.readHit) {
    io.cpu.rdata := fifo.queryRData(inputAddr.wordOffset << log2Up(32), 32 bits)
  }.elsewhen(!dcache.hit & wb.hit) {
    io.cpu.rdata := wb.data.banks(inputAddr.wordOffset)
  }
  //uncache读出的数据
  io.uncache.rdata := recvBlock.banks(0)

  wb.writing := !wbFSM.isActive(wbFSM.stateBoot)

  //和fifo相关的信号
  fifo.push := dcacheFSM.isActive(dcacheFSM.waitWb) & dcache.rmeta(replace.addr).valid & dcache.rmeta(replace.addr).dirty
  fifo.pushTag := dcache.rmeta(replace.addr).tag ## inputAddr.index
  fifo.pushData := dcache.rdata(replace.addr).asBits
  fifo.queryTag := inputAddr.tag ## inputAddr.index
  fifo.queryWData := 0
  fifo.queryWData(inputAddr.byteOffset << 3, 32 bits) := io.cpu.wdata //byteOffset << 3对应cpu.addr的bit偏移
  fifo.queryBE := 0
  fifo.queryBE(inputAddr.byteOffset, 4 bits) := io.cpu.byteEnable
  fifo.write := io.cpu.write & !io.cpu.stall //fifo内部会检查是否写命中
  //  fifo.write := io.cpu.write & !io.cpu.stall & fifo.writeHit

  //写入dcache ram的信号
  dcache.writeMeta.dirty := False
  dcache.writeMeta.valid := True
  dcache.writeMeta.tag := inputAddr.tag.asBits
  dcacheFSM.waitWb.whenIsActive {
    when(wb.hit) {
      dcache.tagWE(replace.addr) := True
      dcache.dataWE(replace.addr) := True
    }
  }
  dcacheFSM.stateBoot.whenIsActive {
    //当直接写cache命中时，会开启dcache的写使能
    when(dcache.writeHit) {
      dcache.tagWE := dcache.hitPerWay
      dcache.dataWE := dcache.hitPerWay
    }
  }
  //写入dache ram的数据
  //1.从内存读出来数据
  //2.从wb的data中命中的数据
  //3.似乎还应有从fifo中命中的数据? TODO
  dcache.writeData := recvBlock.asBits
  when(dcacheFSM.isActive(dcacheFSM.readMem)) {
    dcache.writeData((config.wordSize - 1) << log2Up(32), 32 bits) := io.axi.r.data
  }.elsewhen(dcacheFSM.isActive(dcacheFSM.waitWb)) {
    dcache.writeData := wb.data.asBits //在waitWb阶段也可能写cache ram，此时命中正在写回内存的数据
  }.elsewhen(dcacheFSM.isActive(dcacheFSM.stateBoot)) {
    dcache.writeData := dcache.hitLine.asBits //直接cache命中则使用cache的值
  }
  //如果是写指令，那么还需要通过byteEnable修改dcache.writeData
  when(io.cpu.write) {
    dcache.writeMeta.dirty := True
    for (i <- 0 until 4) {
      when(io.cpu.byteEnable(i)) {
        //                dcache.writeData.banks(inputAddr.wordOffset)(i * 8, 8 bits) := io.cpu.wdata(i * 8, 8 bits)
        dcache.writeData((inputAddr.byteOffset << 3) + (i * 8), 8 bits) := io.cpu.wdata(i * 8, 8 bits)
      }
    }
  }
}
