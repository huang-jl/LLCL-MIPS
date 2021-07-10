package cache

import cpu.defs.ConstantVal
import ip.{DualPortBRam, DualPortLutRam, BRamIPConfig}
import lib.Updating
import spinal.core._
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi._

class CPUICacheInterface(config: CacheRamConfig) extends Bundle with IMasterSlave {
  val stage1 = new Bundle {
    val read      = Bool                         //是否读
    val index     = UInt(config.indexWidth bits) //从地址中截取出虚索引
    val keepRData = Bool                         //当前周期拉高这个信号，那么后面会保持**当前输入**index所读出的值
    //    val cacheTags = Vec(Meta(config.tagWidth), config.wayNum) //读出来的各路tag
    //    val cacheDatas = Vec(Block(config.blockSize), config.wayNum)  //读出来的各路数据
    //    val replaceIndex = UInt(log2Up(config.wayNum) bits) //LRU读出来要替换的路编号
  }

  val stage2 = new Bundle {
    val en    = Bool          //如果stage1检测到异常，那么拉低该信号就不会再进行访存
    val paddr = UInt(32 bits) //用来进行tag比较
    val rdata = Bits(32 bits) //返回读出来的数据
    val stall = Bool
  }

  override def asMaster(): Unit = {
    out(stage1.read, stage1.index, stage1.keepRData, stage2.paddr, stage2.en)
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

/** @note ICache采用两级流水：
  *       第一级读Ram（包括data和tag等信息）
  *       第二级若命中则更新cache并且返回数据；否则暂停流水线进行访存
  */
class ICache(config: CacheRamConfig) extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface(config))
    val axi = master(
      new Axi4(ConstantVal.AXI_BUS_CONFIG)
    )
  }

  val cacheRam = new Area {
    val ramIPConfig = BRamIPConfig(Block.getBitWidth(config.blockSize))
    val depth: Int  = 4 * 1024 * 8 / Block.getBitWidth(config.blockSize)
    val tags = Array.fill(config.wayNum)(
      new DualPortLutRam(
        Meta.getBitWidth(config.tagWidth),
        depth * Meta.getBitWidth(config.tagWidth)
      )
    )
    val datas = Array.fill(config.wayNum)(new DualPortBRam(ramIPConfig))
  }

  val LRU = new Area {
    val manager = LRUCalculator(config.wayNum)
//    val plru = Vec.fill(config.setSize) {
//      Updating(Bits(manager.statusLength bits)) init(0)
//    }
    val plru = Vec.fill(config.setSize)(new PLRU(manager.statusLength))
    for (i <- 0 until config.setSize) {
      plru(i).prev := plru(i).next
      plru(i).next := plru(i).prev
    }
    val wayIndex = Vec(UInt(log2Up(config.wayNum) bits), config.setSize)
    for (i <- 0 until config.setSize) {
      wayIndex(i) := manager.leastRecentUsedIndex(plru(i).prev)
    }
    val replaceAddr = Reg(UInt(log2Up(config.wayNum) bits)) init (0)
  }

  val axiDefault = new Area {
    //  Default value of AXI singal
    io.axi.ar.id := U"4'b0000"
    io.axi.ar.addr := io.cpu.stage2.paddr(config.offsetWidth until 32) @@ U(
      0,
      config.offsetWidth bits
    )
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

  /** *********************************
    * ************ STAGE   1 **********
    * *********************************
    */
  //only drive portB for read
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portB.en := True
    //    cacheRam.tags(i).io.portB.we := False
    cacheRam.tags(i).io.portB.addr := io.cpu.stage1.index
    //    cacheRam.tags(i).io.portB.din.assignDontCare()

    cacheRam.datas(i).io.portB.en := True
    cacheRam.datas(i).io.portB.we := False
    cacheRam.datas(i).io.portB.addr := io.cpu.stage1.index
    cacheRam.datas(i).io.portB.din.assignDontCare()
  }
  // 读ram 和 LRU
  val keepRData  = RegNext(io.cpu.stage1.keepRData) init (False)
  val cacheTags  = Vec(Updating(Meta(config.tagWidth)), config.wayNum)
  val cacheDatas = Vec(Updating(Block(config.blockSize)), config.wayNum)
  for (i <- 0 until config.wayNum) {
    cacheTags(i).next.assignFromBits(cacheRam.tags(i).io.portB.dout)
    cacheDatas(i).next.assignFromBits(cacheRam.datas(i).io.portB.dout)
    when(keepRData) {
      cacheTags(i).next := cacheTags(i).prev
      cacheDatas(i).next := cacheDatas(i).prev
    }
  }
  //如果需要保持住当前阶段读出的值（比如EX.stall住了），那么直接保持replaceAddr
  when(!io.cpu.stage1.keepRData) {
    LRU.replaceAddr := LRU.wayIndex(io.cpu.stage1.index) //stage 2 会使用这个值，用来进行替换
  }

  /** *********************************
    * ************ STAGE   2 **********
    * *********************************
    */

  //解析Stage 2的物理地址
  val addr = new Area {
    val wordOffset: UInt = io.cpu.stage2.paddr(2, config.wordOffsetWidth bits)
    val index: UInt      = io.cpu.stage2.paddr(config.offsetWidth, config.indexWidth bits)
    val tag: Bits =
      io.cpu.stage2.paddr(config.offsetWidth + config.indexWidth, config.tagWidth bits).asBits
  }

  //判断是否前传
  //前传发生在：两个阶段索引相同，第二个阶段没有stall，并且在readMem阶段（也就是readMem即将进入stateBoot的时候）
  val forward = Reg(Bool) init (False)

  //选出可能替换的那一路的地址，同时保存替换的index，前传需要用到
  val replace = new Area {
    val wayIndex = Updating(UInt(log2Up(config.wayNum) bits)) init (0)
    wayIndex.next := LRU.replaceAddr
    for (i <- 0 until config.wayNum) {
      when(!cacheTags(i).next.valid)(wayIndex.next := i) //如果有未使用的cache line，优先替换它
    }
  }

  //only drive portA for write
  val tagWe  = B(0, config.wayNum bits)
  val dataWe = B(0, config.wayNum bits)
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portA.en := True
    cacheRam.tags(i).io.portA.we := tagWe(i)
    cacheRam.tags(i).io.portA.addr := addr.index

    cacheRam.datas(i).io.portA.en := True
    cacheRam.datas(i).io.portA.we := dataWe(i)
    cacheRam.datas(i).io.portA.addr := addr.index
  }
  //判断是否命中cache
  val hitPerWay = Bits(config.wayNum bits) //每一路是否命中
  for (i <- 0 until config.wayNum) {
    hitPerWay(i) := (cacheTags(i).next.tag === addr.tag) & cacheTags(i).next.valid
  }
  val hit: Bool = hitPerWay.orR

  //把命中的数据拿出来并返回
  val hitLine: Block = MuxOH(hitPerWay, for (i <- 0 until config.wayNum) yield cacheDatas(i).next)
  val hitTag: Meta   = MuxOH(hitPerWay, for (i <- 0 until config.wayNum) yield cacheTags(i).next)
  io.cpu.stage2.rdata := hitLine(addr.wordOffset)
  //更新LRU
  when(io.cpu.stage2.en & hit) {
    LRU.manager.updateStatus(OHToUInt(hitPerWay), LRU.plru(addr.index).next)
  }

  //不命中处理：访存，并且写Cache Ram
  //这里使用Updating是为了寄存当前周期写入的值，从而让下一周期能够前传
  val writeMeta = Updating(Meta(config.tagWidth)) init (Meta.fromBits(0, config.tagWidth))
  val writeData = Updating(Block(config.blockSize)) init (Block.fromBits(0, config.blockSize))
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portA.din := writeMeta.next.asBits
    cacheRam.datas(i).io.portA.din := writeData.next.asBits
  }

  val counter = Counter(0 until config.wordSize) //从内存读入数据的计数器
  val recvBlock =
    Reg(Block(config.blockSize)) init (Block.fromBits(B(0), config.blockSize)) //从内存中读出的一行的寄存器

  io.cpu.stage2.stall := True
  val icacheFSM = new StateMachine {
    val waitAXIReady = new State
    val readMem      = new State
    setEntry(stateBoot)
    disableAutoStart()

    stateBoot
      .whenIsNext(io.cpu.stage2.stall := False)
      .whenIsActive {
        when(io.cpu.stage2.en & !hit)(goto(waitAXIReady))
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
        recvBlock.banks(counter.value) := io.axi.r.data
      }
      when(io.axi.r.valid & io.axi.r.last) {
        tagWe(replace.wayIndex.next) := True
        dataWe(replace.wayIndex.next) := True
        // 重写Cache的时候也需要更新LRU
        when(io.cpu.stage2.en) {
          LRU.manager.updateStatus(replace.wayIndex.next, LRU.plru(addr.index).next)
        }
        //不命中则从writeData返回
        io.cpu.stage2.rdata := writeData.next(addr.wordOffset)
        goto(stateBoot)
      }
    }
  }

  //前传的时机：stage2下个上升沿写回的时候前传
  forward := io.cpu.stage1.index === addr.index & !io.cpu.stage2.stall & icacheFSM.isActive(
    icacheFSM.readMem
  )
  //前传，前传会用到上一阶段替换的index
  when(forward) {
    cacheTags(replace.wayIndex.prev).next := writeMeta.prev
    cacheDatas(replace.wayIndex.prev).next.assignFromBits(writeData.prev.asBits)
  }
  val plruForward = io.cpu.stage1.index === addr.index & !io.cpu.stage2.stall &
    io.cpu.stage2.en & (icacheFSM.isActive(icacheFSM.readMem) | hit)
  when(plruForward) {
    LRU.wayIndex(addr.index) := LRU.manager.leastRecentUsedIndex(LRU.plru(addr.index).next)
  }
  //写回cache ram的数据
  writeMeta.next.tag := addr.tag
  writeMeta.next.valid := True
  writeData.next := recvBlock
  when(icacheFSM.isActive(icacheFSM.readMem)) {
    writeData.next.banks(config.wordSize - 1) := io.axi.r.data
  }
  //如果没有enable，那么直接返回NOP
  when(!io.cpu.stage2.en)(io.cpu.stage2.rdata := ConstantVal.INST_NOP.asBits)
}

object ICache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ICache(CacheRamConfig(wayNum = 4))).printPruned()
  }
}
