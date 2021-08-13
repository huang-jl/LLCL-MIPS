package cache

import cpu.defs.ConstantVal
import ip._
import spinal.core._
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi._
import scala.language.postfixOps

class CPUICacheInterface(config: CacheRamConfig) extends Bundle with IMasterSlave {
  val stage1 = new Bundle {
    val read      = Bool                         //是否读
    val index     = UInt(config.indexWidth bits) //从地址中截取出虚索引
//    val keepRData = Bool                         //当前周期拉高这个信号，那么后面会保持**当前输入**index所读出的值
    //    val cacheTags = Vec(Meta(config.tagWidth), config.wayNum) //读出来的各路tag
    //    val cacheDatas = Vec(Block(config.blockSize), config.wayNum)  //读出来的各路数据
    //    val replaceIndex = UInt(log2Up(config.wayNum) bits) //LRU读出来要替换的路编号
  }

  val stage2 = new Bundle {
    val en    = Bool          //如果stage1检测到异常，那么拉低该信号就不会再进行访存
    val paddr = UInt(32 bits) //用来进行tag比较
    val rdata = Bits(64 bits) //返回读出来的数据
    val stall = Bool
  }

  val invalidate = new Bundle {
    val en   = Bool.default(False)
    val addr = UInt(32 bits).default(0)
  }

  override def asMaster(): Unit = {
    out(stage1.read, stage1.index, stage2.paddr, stage2.en, invalidate)
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

    def bankOffset: UInt = addr(log2Up(config.bankSize), config.bankOffsetWidth bits)
  }

  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface(config))
    val axi = master(
      new Axi4(ConstantVal.AXI_BUS_CONFIG)
    )
  }
  //解析Stage 2的物理地址
  val stage2 = new Area {
    val bankOffset: UInt = io.cpu.stage2.paddr.bankOffset
    val index: UInt      = io.cpu.stage2.paddr.index
    val tag: Bits        = io.cpu.stage2.paddr.cacheTag
  }

  val cacheRam = new Area {
    val dataRamConfig = BRamIPConfig(
      Block.getBitWidth(config.blockSize),
    )
    val depth: Int       = dataRamConfig.depth
    val tagBitWidth: Int = Meta.getBitWidth(config.tagWidth)
    val tagRamConfig = BRamIPConfig(tagBitWidth, depth * tagBitWidth)
    val tags  = Array.fill(config.wayNum)(new SimpleDualPortBram(tagRamConfig))
    val datas = Array.fill(config.wayNum)(new SimpleDualPortBram(dataRamConfig))
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
    io.axi.ar.len := config.wordNum - 1
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
  // 读ram
  val read = new Bundle {
    val tags = Vec(Meta(config.tagWidth), config.wayNum)
    val datas = Vec(Block(config.blockSize, config.bankSize), config.wayNum)
    val addr = UInt(config.indexWidth bits)
    addr := io.cpu.stage1.index
    for (i <- 0 until config.wayNum) {
      tags(i).assignFromBits(cacheRam.tags(i).io.portB.dout)
      datas(i).assignFromBits(cacheRam.datas(i).io.portB.dout)
    }
  }
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portB.en := True
    cacheRam.tags(i).io.portB.addr := read.addr
//    cacheRam.tags(i).io.portB.we := False
//    cacheRam.tags(i).io.portB.din.assignDontCare()

    cacheRam.datas(i).io.portB.en := True
    cacheRam.datas(i).io.portB.addr := read.addr
//    cacheRam.datas(i).io.portB.we := False
//    cacheRam.datas(i).io.portB.din.assignDontCare()
  }

  /** *********************************
    * ************ STAGE   2 **********
    * *********************************
    */

  val LRU = new Area {
    val mem = new LRUManegr(config.wayNum, config.setNum)
    // LRU写端口
    val access = UInt(log2Up(config.wayNum) bits)
    val we     = False //默认为False
    mem.write.access := access
    mem.write.en := we
    mem.write.addr := stage2.index
  }

  // LRU不必担心同时读写一个地址的问题，因为真正使用LRU计算替换地址
  // 是在readMem的末尾阶段，此时不可能写LRU
  val replace = new Area {
    val wayIndex = LRU.mem.latestWayIndex(stage2.index)
    for (i <- 0 until config.wayNum) {
      when(!read.tags(i).valid)(wayIndex := i) //如果有未使用的cache line，优先替换它
    }
  }

  //判断是否命中cache，并得到要返回的值
  io.cpu.stage2.rdata := ConstantVal.INST_NOP.asBits ## ConstantVal.INST_NOP.asBits
  val hit = False
  val hitIndex  = U(0, log2Up(config.wayNum) bits)
  for (i <- 0 until config.wayNum) {
    when((read.tags(i).tag === stage2.tag) & read.tags(i).valid) {
      hit := True
      hitIndex := i
      io.cpu.stage2.rdata := read.datas(i)(stage2.bankOffset)
    }
  }

  //更新LRU
  LRU.access := hitIndex
  when(io.cpu.stage2.en & hit) (LRU.we := True)

  //only drive portA for write
  val write = new Bundle {
    val en = B(0, config.wayNum bits)
    val meta = Meta(config.tagWidth)
    val data = Block(config.blockSize)
    val addr = UInt(config.indexWidth bits)
    addr := stage2.index
  }
  for (i <- 0 until config.wayNum) {
    cacheRam.tags(i).io.portA.en := True
    cacheRam.tags(i).io.portA.we := write.en(i)
    cacheRam.tags(i).io.portA.addr := write.addr
    cacheRam.tags(i).io.portA.din := write.meta.asBits

    cacheRam.datas(i).io.portA.en := True
    cacheRam.datas(i).io.portA.we := write.en(i)
    cacheRam.datas(i).io.portA.addr := write.addr
    cacheRam.datas(i).io.portA.din := write.data.asBits
  }

  //不命中处理：访存，并且写Cache Ram
  val counter = Counter(0 until config.wordNum) //从内存读入数据的计数器
  val recvBlock = Reg(Block(config.blockSize)) //从内存中读出的一行的寄存器

  val icacheFSM = new StateMachine {
    val waitAXIReady = new State
    val readMem      = new State
    val refill       = new State
    val finish       = new State
    setEntry(stateBoot)
    disableAutoStart()

    stateBoot
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
        write.en(replace.wayIndex) := True
        // 重写Cache的时候也需要更新LRU
        when(io.cpu.stage2.en) {
          LRU.we := True
          LRU.access := replace.wayIndex
        }
        //不命中则从writeData返回
        goto(refill)
      }
    }

    refill.whenIsActive {
      read.addr := stage2.index
      goto(finish)
    }
    finish.whenIsActive(goto(stateBoot))
  }
  io.cpu.stage2.stall := !(icacheFSM.isActive(icacheFSM.finish) |
    icacheFSM.isActive(icacheFSM.stateBoot) & (!io.cpu.stage2.en | hit))

  //写回cache ram的数据
  write.meta.tag := stage2.tag
  write.meta.valid := !io.cpu.invalidate.en
  for(i <- 0 until config.wordNum - 1) {
    write.data(i) := recvBlock(i)
  }
  write.data(config.wordNum - 1) := io.axi.r.data
  //如果是invalidate指令，那么直接刷掉那一行
  when(io.cpu.invalidate.en) {
    write.addr := io.cpu.invalidate.addr.index
    write.en.setAll()
  }
}

object ICache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ICache(CacheRamConfig(wayNum = 4, bankSize = 8))).printPruned()
  }
}
