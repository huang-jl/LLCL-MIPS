package cache

import spinal.core._
import spinal.core.sim.SimDataPimper
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi._

class CPUICacheInterface extends Bundle with IMasterSlave {
  val read = Bool
  val addr = UInt(32 bits)
  val data = Bits(32 bits)
  val stall = Bool

  override def asMaster(): Unit = {
    out(read, addr)
    in(data, stall)
  }

  def <<(that: CPUICacheInterface): Unit = that >> this

  def >>(that: CPUICacheInterface): Unit = {
    //this is master
    that.read := this.read
    that.addr := this.addr
    this.data := that.data
    this.stall := that.stall
  }
}

object ICache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ICache(CacheRamConfig())).printPruned()
  }
}

class ICache(config: CacheRamConfig) extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface)
    val axi = master(new Axi4(Axi4Config(
      addressWidth = 32, dataWidth = 32, idWidth = 4,
      useRegion = false, useQos = false
    )))
  }

  /*
   * Signal and Area Definition
   */

  val inputAddr = new Area {
    val tag: UInt = io.cpu.addr(config.offsetWidth + config.indexWidth, config.tagWidth bits)
    val index: UInt = io.cpu.addr(config.offsetWidth, config.indexWidth bits)
    val wordOffset: UInt = io.cpu.addr(2 until config.offsetWidth)
  }
  val cacheRam = new Area {
    //    val tags = Array.fill(config.wayNum)(Mem(Bits(Meta.getBitWidth(config.tagWidth) bits), config.setSize))
    //    val datas = Array.fill(config.wayNum)(Mem(Bits(Block.getBitWidth(config.blockSize) bits), config.setSize))
    val tags = if (!config.sim) Array.fill(config.wayNum)(new SinglePortBRAM(config.indexWidth,
      Meta.getBitWidth(config.tagWidth), "single_port_imeta_ram")) else null
    val datas = if (!config.sim) Array.fill(config.wayNum)(new SinglePortBRAM(config.indexWidth,
      Block.getBitWidth(config.blockSize), "single_port_data_ram")) else null

    val simTags = if (config.sim) Array.fill(config.wayNum)(Mem(Bits(Meta.getBitWidth(config.tagWidth) bits), config.setSize)) else null
    val simDatas = if (config.sim) Array.fill(config.wayNum)(Mem(Bits(Block.getBitWidth(config.blockSize) bits), config.setSize)) else null
    //仿真时初值
    if (config.sim) {
      for (i <- 0 until config.wayNum) {
        simTags(i).init(for (i <- 0 until config.setSize) yield B(0))
        simDatas(i).init(for (i <- 0 until config.setSize) yield B(0))
      }
    }
  }

  val icache = new Area {
    val dataWE = Bits(config.wayNum bits) //写cache.data的使能
    val tagWE = Bits(config.wayNum bits) //写cache.meta的使能
    /*
     * Cache地址
     */
    if (!config.sim) {
      for (i <- 0 until config.wayNum) {
        cacheRam.tags(i).io.addra := inputAddr.index
        cacheRam.datas(i).io.addra := inputAddr.index
      }
    }
    /*
     * Cache读端口
     */
    val rdata = Vec(Block(config.blockSize), config.wayNum)
    val rmeta = Vec(Meta(config.tagWidth), config.wayNum)
    for (i <- 0 until config.wayNum) {
      if (config.sim) {
        rmeta(i).assignFromBits(cacheRam.simTags(i).readAsync(inputAddr.index))
        rdata(i).assignFromBits(cacheRam.simDatas(i).readAsync(inputAddr.index))
      } else {
        rmeta(i).assignFromBits(cacheRam.tags(i).io.douta)
        rdata(i).assignFromBits(cacheRam.datas(i).io.douta)
      }
    }
    /*
     * Cache写端口
     */
    val writeMeta = Meta(config.tagWidth) //要写入cache的Meta
    val writeData = Block(config.blockSize) //要写入cache的Block
    //TODO 加入invalidate功能后需要修改tags.valid的逻辑
    for (i <- 0 until config.wayNum) {
      if (config.sim) {
        cacheRam.simTags(i).write(inputAddr.index, writeMeta.asBits, enable = tagWE(i))
        cacheRam.simDatas(i).write(inputAddr.index, writeData.asBits, enable = dataWE(i))
      } else {
        cacheRam.tags(i).io.wea := tagWE(i)
        cacheRam.datas(i).io.wea := dataWE(i)
        cacheRam.tags(i).io.dina := writeMeta.asBits
        cacheRam.datas(i).io.dina := writeData.asBits
      }
    }

    val hitPerWay: Bits = Bits(config.wayNum bits) //每一路是否命中
    val hit: Bool = hitPerWay.orR & io.cpu.read
    //把hitPerWay中为1的那一路对应数据拿出来
    val hitLine: Block = MuxOH(hitPerWay, for (i <- 0 until config.wayNum) yield rdata(i))
    for (i <- 0 until config.wayNum) {
      hitPerWay(i) := (rmeta(i).tag.asUInt === inputAddr.tag) & rmeta(i).valid
    }
  }

  val plru = Array.fill(config.setSize)(new LRUManegr(config.wayNum)) //setSize是组的个数，不是“设置大小”的意思
  val plruAddr = Vec(UInt(log2Up(config.wayNum) bits), config.setSize)
  for (i <- 0 until config.setSize) {
    plruAddr(i) := plru(i).io.next
    plru(i).io.update := (!io.cpu.stall) & (inputAddr.index === i)
    plru(i).io.access := icache.hitPerWay
  }

  //要替换的那一路的地址
  val replace = new Area {
    val addr = UInt(log2Up(config.wayNum) bits)
    addr := plruAddr(inputAddr.index)
    for (i <- 0 until config.wayNum) {
      when(!icache.rmeta(i).valid)(addr := i) //如果有未使用的cache line，优先替换它
    }
  }

  val counter = Counter(0 to config.wordSize) //从内存读入数据的计数器
  val recvBlock = Reg(Block(config.blockSize)) init (Block.fromBits(B(0), config.blockSize)) //从内存中读出的一行的寄存器

  /** *********************************
   * ********* default value **********
   * ********************************* */
  val default = new Area {
    io.cpu.stall := True //默认stall
    io.cpu.data := icache.hitLine.banks(inputAddr.wordOffset)

    icache.tagWE := 0 //默认禁止所有的cache写
    icache.dataWE := 0 //默认禁止所有的cache写

    //  Default value of AXI singal
    io.axi.ar.id := 0
    io.axi.ar.addr := (inputAddr.tag ## inputAddr.index ## B(0, config.offsetWidth bits)).asUInt
    io.axi.ar.lock := 0
    io.axi.ar.cache := 0
    io.axi.ar.prot := 0
    io.axi.ar.len := config.wordSize - 1
    io.axi.ar.size := U"3'b010" //2^2 = 4Bytes
    io.axi.ar.burst := B"2'b01" //INCR
    io.axi.ar.valid := False
    //r
    io.axi.r.ready := False
    //Write is always disabled for ICache
    //aw
    io.axi.aw.addr := 0
    io.axi.aw.id := 0
    io.axi.aw.lock := 0
    io.axi.aw.cache := 0
    io.axi.aw.prot := 0
    io.axi.aw.len := 0
    io.axi.aw.size := U"3'b010" //2^2 = 4Bytes
    io.axi.aw.burst := B"2'b01" //INCR
    io.axi.aw.valid := False
    //w
    io.axi.w.valid := False
    io.axi.w.last := False
    io.axi.w.strb := B"4'b1111"
    io.axi.w.data := 0
    //b
    io.axi.b.ready := False
  }

  val icacheFSM = new StateMachine {
    val waitAXIReady = new State
    val readMem = new State
    val done = new State
    setEntry(stateBoot)
    disableAutoStart()

    stateBoot
      .whenIsNext(io.cpu.stall := False) //当下一个状态仍然是boot的时候，icache不会stall
      .whenIsActive {
        counter.clear()
        when(io.cpu.read & !icache.hit)(goto(waitAXIReady))
      }

    waitAXIReady.whenIsActive {
      io.axi.ar.valid := True
      counter.clear()
      when(io.axi.ar.ready)(goto(readMem))
    }

    readMem.whenIsActive {
      io.axi.r.ready := True
      when(io.axi.r.valid) {
        counter.increment()
        recvBlock.banks(counter.value.resize(3)) := io.axi.r.data
      }
      when(io.axi.r.valid & io.axi.r.last) {
        icache.tagWE(replace.addr) := True
        icache.dataWE(replace.addr) := True
        goto(done)
      }
    }

    done.whenIsActive {
      goto(stateBoot)
    }
  }

  //写回cache ram的数据
  icache.writeMeta.tag := inputAddr.tag.asBits
  icache.writeMeta.valid := True
  icache.writeData := recvBlock
  when(icacheFSM.isActive(icacheFSM.readMem)) {
    icache.writeData.banks(config.wordSize - 1) := io.axi.r.data
  }

}