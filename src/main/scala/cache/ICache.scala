package cache

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi._
import ip.{DualPortBram, DualPortLutram, DualPortRam}

class CPUICacheInterface(config: CacheRamConfig) extends Bundle with IMasterSlave {
  val stage1 = new Bundle {
    val read = Bool //是否读
    val index = UInt(config.indexWidth bits) //从地址中截取出虚索引
    //    val cacheTags = Vec(Meta(config.tagWidth), config.wayNum) //读出来的各路tag
    //    val cacheDatas = Vec(Block(config.blockSize), config.wayNum)  //读出来的各路数据
    //    val replaceIndex = UInt(log2Up(config.wayNum) bits) //LRU读出来要替换的路编号
  }

  val stage2 = new Bundle {
    val en = Bool //如果stage1检测到异常，那么拉低该信号就不会再进行访存
    val paddr = UInt(32 bits) //用来进行tag比较
    val rdata = Bits(32 bits) //返回读出来的数据
    val stall = Bool
  }

  override def asMaster(): Unit = {
    out(stage1.read, stage1.index, stage2.paddr, stage2.en)
    //    in(stage2.rdata, stage2.stall, stage1.cacheTags, stage1.cacheDatas, stage1.replaceIndex)
    in(stage2.rdata, stage2.stall)
  }

  def <<(that: CPUICacheInterface): Unit = that >> this

  def >>(that: CPUICacheInterface): Unit = {
    //this is master
    that.stage1 := this.stage1
    that.stage2.paddr := this.stage2.paddr
    that.stage2.en := this.stage2.en
    //    this.stage1.cacheTags := that.stage1.cacheTags
    //    this.stage1.cacheDatas := that.stage1.cacheDatas
    this.stage2.rdata := that.stage2.rdata
    this.stage2.stall := that.stage2.stall
  }
}

/**
 * @note ICache采用两级流水：
 *       第一级读Ram（包括data和tag等信息）
 *       第二级若命中则更新cache并且返回数据；否则暂停流水线进行访存
 * */
class ICache(config: CacheRamConfig) extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface(config))
    val axi = master(new Axi4(Axi4Config(
      addressWidth = 32, dataWidth = 32, idWidth = 4,
      useRegion = false, useQos = false
    )))
  }

  val cacheRam = new Area {
    val tags = Array.fill(config.wayNum)(new DualPortLutram(Meta.getBitWidth(config.tagWidth)))
    val datas = Array.fill(config.wayNum)(new DualPortBram(Block.getBitWidth(config.blockSize)))
    for (i <- 0 until config.wayNum) {
      tags(i).io.portA.en := True
      tags(i).io.portB.en := True
      datas(i).io.portA.en := True
      datas(i).io.portB.en := True
    }
  }
  val plru = Array.fill(config.setSize)(new LRUManegr(config.wayNum)) //setSize是组的个数，不是“设置大小”的意思
  for(i <- 0 until config.setSize) {
    plru(i).io.update
  }

  //stage1
  // Read from Ram
  val cacheTags = Vec(Meta(config.tagWidth), config.wayNum)
  val cacheDatas = Vec(Block(config.blockSize), config.wayNum)
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portA.addr := io.cpu.stage1.index
    cacheTags(i).assignFromBits(cacheRam.tags(i).io.portA.dout)
    cacheDatas(i).assignFromBits(cacheRam.datas(i).io.portA.dout)
  }
  //Read from LRU
  val plruAddr = Reg(UInt(log2Up(config.wayNum) bits)) init(0)
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


  val wordOffset: UInt = io.cpu.addr(2 until config.offsetWidth)

  val icache = new Area {
    val dataWE = Bits(config.wayNum bits) //写cache.data的使能
    val tagWE = Bits(config.wayNum bits) //写cache.meta的使能
    /*
     * Cache地址和使能
     */
    if (!config.sim) {
      for (i <- 0 until config.wayNum) {
        cacheRam.tags(i).io.addr := inputAddr.index
        cacheRam.datas(i).io.addr := inputAddr.index
        cacheRam.tags(i).io.en := True
        cacheRam.datas(i).io.en := True
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
        rmeta(i).assignFromBits(cacheRam.tags(i).io.dout)
        rdata(i).assignFromBits(cacheRam.datas(i).io.dout)
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
        cacheRam.tags(i).io.we := tagWE(i)
        cacheRam.datas(i).io.we := dataWE(i)
        cacheRam.tags(i).io.din := writeMeta.asBits
        cacheRam.datas(i).io.din := writeData.asBits
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
    io.axi.ar.id := U"4'b0000"
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
    io.axi.aw.id := U"4'b0000"
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

object ICache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ICache(CacheRamConfig())).printPruned()
  }
}

