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
    SpinalVerilog(new DCache(CacheRamConfig(), 16))
  }
}

class DCache(config: CacheRamConfig, fifoDepth: Int = 16) extends Component {
  val writeBufferConfig = WriteBufferConfig(config.blockSize, config.tagWidth + config.indexWidth, fifoDepth)
  val io = new Bundle {
    val cpu = slave(new CPUDCacheInterface)
    val axi = master(new Axi4(Axi4Config(
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
    val tag: UInt = io.cpu.addr(config.offsetWidth + config.indexWidth, config.tagWidth bits)
    val index: UInt = io.cpu.addr(config.offsetWidth, config.indexWidth bits)
    val wordOffset: UInt = io.cpu.addr(2 until config.offsetWidth)
    //offsetWidth = 5为例，此时cache line大小为32，所以byteOffset在0-31
    val byteOffset: UInt = (wordOffset ## B(0, 2 bits)).asUInt
  }

  val cacheRam = new Area {
    val tags = Array.fill(config.wayNum)(new Mem(Bits(DMeta.getBitWidth(config.tagWidth) bits), config.setSize))
    val datas = Array.fill(config.wayNum)(new Mem(Bits(Block.getBitWidth(config.blockSize) bits), config.setSize))
    //仿真时初值
    for (i <- 0 until config.wayNum) {
      tags(i).init(for (i <- 0 until config.setSize) yield B(0))
      datas(i).init(for (i <- 0 until config.setSize) yield B(0))
    }
  }

  val dcache = new Area {
    val dataWE = Bits(config.wayNum bits) //写哪一路cache.data的使能
    val tagWE = Bits(config.wayNum bits) //写哪一路cache.meta的使能
    /*
     * Cache读端口
     */
    val rdata = Vec(Block(config.blockSize), config.wayNum)
    val rmeta = Vec(DMeta(config.tagWidth), config.wayNum)
    for (i <- 0 until config.wayNum) {
      rmeta(i).assignFromBits(cacheRam.tags(i).readAsync(inputAddr.index))
      rdata(i).assignFromBits(cacheRam.datas(i).readAsync(inputAddr.index))
    }
    /*
     * Cache写端口
     */
    val writeMeta = Meta(config.tagWidth) //要写入cache的Meta
    val writeData = Bits(config.bitSize bits) //要写入cache的Block
    //TODO 加入invalidate功能后需要修改valid的逻辑
    for (i <- 0 until config.wayNum) {
      cacheRam.tags(i).write(inputAddr.index, writeMeta.asBits, enable = tagWE(i))
      cacheRam.datas(i).write(inputAddr.index, writeData, enable = dataWE(i))
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
    addr := (fifo.popTag ## B(0, config.offsetWidth bits)).asUInt
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
    //Default Value of AXI
    //ar
    io.axi.ar.addr := (inputAddr.tag ## inputAddr.index ## B(0, config.offsetWidth bits)).asUInt
    io.axi.ar.id := 0
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
    io.axi.aw.id := 0
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
    io.axi.w.strb := B"4'b1111"
    //b
    io.axi.b.ready := True
  }

  /*
   * Logic
   */

  //这个状态机不包括写内存的逻辑
  //写内存的逻辑是单独一个状态机，直接和fifo进行交互
  val dcacheFSM = new StateMachine {
    val waitWb = new State //用来等待write_back的状态，并且会从cache中移动对应的数据到FIFO中
    val waitAXIReady = new State //等待arready信号
    val readMem = new State //读内存
    val done = new State
    setEntry(stateBoot)
    disableAutoStart()
    stateBoot
      .whenIsNext(io.cpu.stall := False)
      .whenIsActive {
        when(readMiss | writeMiss)(goto(waitWb))
      }

    //这个状态是开始读一个cache line的前置状态
    waitWb.whenIsActive {
      //当FIFO要push并且FIFO已经满了的时候，需要等待
      when(!(fifo.push & fifo.full)) {
        when(wb.hit)(goto(stateBoot)) //在写回的数据中命中了
          .otherwise(goto(waitAXIReady))
      }
    }

    waitAXIReady.whenIsActive {
      counter.recv.clear()
      io.axi.ar.valid := True
      when(io.axi.ar.ready)(goto(readMem))
    }

    readMem.whenIsActive {
      io.axi.r.ready := True
      when(io.axi.r.valid) {
        counter.recv.increment()
        recvBlock.banks(counter.recv.value) := io.axi.r.data
      }
      when(io.axi.r.valid & io.axi.r.last) {
        dcache.tagWE(replace.addr) := True
        dcache.dataWE(replace.addr) := True
        goto(done)
      }
    }

    done.whenIsActive {
      io.cpu.stall := False
      goto(stateBoot)
    }
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

  readMiss := (!dcache.hit) & (!fifo.readHit) & (!wb.hit) & io.cpu.read //读缺失
  writeMiss := (!dcache.hit) & (!fifo.writeHit) & io.cpu.write //写缺失

  io.cpu.rdata := dcache.hitLine.banks(inputAddr.wordOffset)
  when(readMiss) {
    io.cpu.rdata := recvBlock.banks(inputAddr.wordOffset)
  }.elsewhen(!dcache.hit & fifo.readHit) {
    io.cpu.rdata := fifo.queryRData(inputAddr.wordOffset << log2Up(32), 32 bits)
  }.elsewhen(!dcache.hit & wb.hit) {
    io.cpu.rdata := wb.data.banks(inputAddr.wordOffset)
  }

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
  fifo.write := io.cpu.write & !io.cpu.stall & fifo.writeHit

  //写cache ram的信号
  dcache.writeMeta.valid := True
  dcache.writeMeta.tag := inputAddr.tag.asBits
  dcacheFSM.waitWb.whenIsActive {
    when(wb.hit) {
      dcache.tagWE(replace.addr) := True
      dcache.dataWE(replace.addr) := True
    }
  }
  dcacheFSM.stateBoot.whenIsActive {
    when(dcache.writeHit) {
      dcache.tagWE := dcache.hitPerWay
      dcache.dataWE := dcache.hitPerWay
    }
  }

  dcache.writeData := recvBlock.asBits
  when(dcacheFSM.isActive(dcacheFSM.readMem)) {
    dcache.writeData((config.wordSize - 1) << log2Up(32), 32 bits) := io.axi.r.data
  }.elsewhen(dcacheFSM.isActive(dcacheFSM.waitWb)) {
    dcache.writeData := wb.data.asBits //在waitWb阶段也可能写cache ram，此时命中正在写回内存的数据
  }.elsewhen(dcacheFSM.isActive(dcacheFSM.stateBoot)) {
    dcache.writeData := dcache.hitLine.asBits //直接cache命中则使用cache的值
  }
  //如果是写cache，那么还需要通过byteEnable修改dcache.writeData
  when(io.cpu.write) {
    for (i <- 0 until 4) {
      when(io.cpu.byteEnable(i)) {
        //                dcache.writeData.banks(inputAddr.wordOffset)(i * 8, 8 bits) := io.cpu.wdata(i * 8, 8 bits)
        dcache.writeData((inputAddr.byteOffset << 3) + (i * 8), 8 bits) := io.cpu.wdata(i * 8, 8 bits)
      }
    }
  }
}
