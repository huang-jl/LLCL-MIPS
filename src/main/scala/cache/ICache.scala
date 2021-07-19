package cache

import cpu.defs.ConstantVal
import ip.{SimpleDualPortBram, BRamIPConfig}
import spinal.core._
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi._
import scala.language.postfixOps

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

  val invalidate = new Bundle {
    val en   = Bool
    val addr = UInt(32 bits)
  }

  override def asMaster(): Unit = {
    out(stage1.read, stage1.index, stage1.keepRData, stage2.paddr, stage2.en, invalidate)
    in(stage2.rdata, stage2.stall)
  }
}

/** @note ICache采用两级流水：
  *       第一级读Ram（包括data和tag等信息）
  *       第二级若命中则更新cache并且返回数据；否则暂停流水线进行访存
  */
class ICache(config: CacheRamConfig) extends Component {
  private implicit class ParseAddr(addr: UInt) {
    assert(addr.getBitsWidth == 32)
    def index: UInt = addr(config.offsetWidth, config.indexWidth bits)

    def cacheTag: Bits = addr(config.offsetWidth + config.indexWidth, config.tagWidth bits).asBits

    def wordOffset: UInt = addr(2, config.wordOffsetWidth bits)
  }

  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface(config))
    val axi = master(
      new Axi4(ConstantVal.AXI_BUS_CONFIG)
    )
  }
  //解析Stage 2的物理地址
  val stage2 = new Area {
    val wordOffset: UInt = io.cpu.stage2.paddr.wordOffset
    val index: UInt      = io.cpu.stage2.paddr.index
    val tag: Bits = io.cpu.stage2.paddr.cacheTag
  }

  val cacheRam = new Area {
    val dataRamConfig    = BRamIPConfig(Block.getBitWidth(config.blockSize))
    val depth: Int       = 4 * 1024 * 8 / Block.getBitWidth(config.blockSize)
    val tagBitWidth: Int = Meta.getBitWidth(config.tagWidth)
    val tagRamConfig     = BRamIPConfig(tagBitWidth, depth * tagBitWidth)
    val tags             = Array.fill(config.wayNum)(new SimpleDualPortBram(tagRamConfig))
    val datas            = Array.fill(config.wayNum)(new SimpleDualPortBram(dataRamConfig))
  }

  val LRU = new Area {
    val mem        = new LRUManegr(config.wayNum, config.setSize)
    val calculator = new LRUCalculator(config.wayNum)
    // LRU读端口
    mem.io.read.addr := io.cpu.stage1.index
    //如果需要保持住当前阶段读出的值（比如EX.stall住了），那么直接保持replaceAddr
    val rindex = RegNextWhen(
      calculator.leastRecentUsedIndex(mem.io.read.data),
      !io.cpu.stage1.keepRData
    ) init 0
    // LRU写端口
    val access = U(0, log2Up(config.wayNum) bits)
    val we     = False //默认为False
    mem.io.write.access := access
    mem.io.write.en := we
    mem.io.write.addr := stage2.index
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
//  val keepRData  = RegNext(io.cpu.stage1.keepRData) init (False)
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portB.en := !io.cpu.stage1.keepRData
    cacheRam.tags(i).io.portB.addr := io.cpu.stage1.index

    cacheRam.datas(i).io.portB.en := !io.cpu.stage1.keepRData
    cacheRam.datas(i).io.portB.addr := io.cpu.stage1.index
  }
  // 读ram 和 LRU
  val cacheTags  = Vec(Meta(config.tagWidth), config.wayNum)
  val cacheDatas = Vec(Block(config.blockSize), config.wayNum)
  for (i <- 0 until config.wayNum) {
    cacheTags(i).assignFromBits(cacheRam.tags(i).io.portB.dout)
    cacheDatas(i).assignFromBits(cacheRam.datas(i).io.portB.dout)
  }

  /** *********************************
    * ************ STAGE   2 **********
    * *********************************
    */

  val replace = new Area {
    val wayIndex = UInt(log2Up(config.wayNum) bits)
    wayIndex := LRU.rindex
    for (i <- 0 until config.wayNum) {
      when(!cacheTags(i).valid)(wayIndex := i) //如果有未使用的cache line，优先替换它
    }
  }

  //only drive portA for write
  val tagWe  = B(0, config.wayNum bits)
  val dataWe = B(0, config.wayNum bits)
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portA.en := True
    cacheRam.tags(i).io.portA.we := tagWe(i)
    cacheRam.tags(i).io.portA.addr := stage2.index

    cacheRam.datas(i).io.portA.en := True
    cacheRam.datas(i).io.portA.we := dataWe(i)
    cacheRam.datas(i).io.portA.addr := stage2.index
  }
  //提前选择，减少fanout
  val targetData = Vec(Bits(32 bits), config.wayNum)
  for(i <- 0 until config.wayNum) {
    targetData(i) := cacheDatas(i)(stage2.wordOffset)
  }
  //判断是否命中cache，并选出对应的数据
  val hitIndex:UInt = U(0, log2Up(config.wayNum) bits)
  val hit:Bool = (cacheTags(0).tag === stage2.tag) & cacheTags(0).valid
  io.cpu.stage2.rdata := targetData(0)
  for (i <- 1 until config.wayNum) {
    when((cacheTags(i).tag === stage2.tag) & cacheTags(i).valid) {
      hit := True
      hitIndex := i
      io.cpu.stage2.rdata := targetData(i)
    }
  }

  //把命中的数据拿出来并返回
//  val hitLine: Block = MuxOH(hitPerWay, for (i <- 0 until config.wayNum) yield cacheDatas(i))
//  val hitTag: Meta   = MuxOH(hitPerWay, for (i <- 0 until config.wayNum) yield cacheTags(i))
//  io.cpu.stage2.rdata := hitLine(stage2.wordOffset)
  //更新LRU
  when(io.cpu.stage2.en & hit) {
    LRU.we := True
    LRU.access := hitIndex
  }

  //不命中处理：访存，并且写Cache Ram
  //这里使用Updating是为了寄存当前周期写入的值，从而让下一周期能够前传
  val writeMeta = Meta(config.tagWidth)
  val writeData = Block(config.blockSize)
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portA.din := writeMeta.asBits
    cacheRam.datas(i).io.portA.din := writeData.asBits
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
        tagWe(replace.wayIndex) := True
        dataWe(replace.wayIndex) := True
        // 重写Cache的时候也需要更新LRU
        when(io.cpu.stage2.en) {
          LRU.we := True
          LRU.access := replace.wayIndex
        }
        //不命中则从writeData返回
        io.cpu.stage2.rdata := writeData(stage2.wordOffset)
        goto(stateBoot)
      }
    }
  }

  //写回cache ram的数据
  writeMeta.tag := stage2.tag
  writeMeta.valid := !io.cpu.invalidate.en
  writeData := recvBlock
  when(icacheFSM.isActive(icacheFSM.readMem)) {
    writeData.banks(config.wordSize - 1) := io.axi.r.data
  }
  //如果是invalidate指令，那么直接刷掉那一行（只需要刷tag）
  when(io.cpu.invalidate.en) {
    for (i <- 0 until config.wayNum) {
      tagWe(i) := True
      cacheRam.tags(i).io.portA.addr := io.cpu.invalidate.addr.index
    }
  }
  //如果没有enable，那么直接返回NOP
  when(!io.cpu.stage2.en)(io.cpu.stage2.rdata := ConstantVal.INST_NOP.asBits)
}

object ICache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ICache(CacheRamConfig(wayNum = 4))).printPruned()
  }
}
