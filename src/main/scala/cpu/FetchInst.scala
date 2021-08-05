package cpu

import cpu.defs.{ConstantVal, Mips32Inst}
import cache.{CPUICacheInterface, CacheRamConfig}
import lib.Optional
import spinal.core._
import spinal.lib._
import tlb.MMUTranslationRes

import scala.language.{existentials, postfixOps}

case class InstEntry() extends Bundle {
  val inst   = Mips32Inst()
  val pc     = UInt(32 bits)
  val branch = Bool()
  val valid  = Bool()

  def validWhen(cond: Bool): InstEntry = {
    val entry = InstEntry()
    entry.inst := inst
    entry.pc := pc
    entry.branch := branch
    entry.valid := valid & cond
    entry
  }

  def assignBranch(branch: Bool): InstEntry = {
    val entry = InstEntry()
    entry.inst := inst
    entry.pc := pc
    entry.branch := branch
    entry.valid := valid
    entry
  }
}

case class FetchException() extends Bundle {
  val code            = Optional(ExcCode())
  val refillException = Bool()

  /** 只有寄存器能调用 */
  def init(): this.type = {
    code.init(None)
    refillException.init(False)
    this
  }
}

class FetchInst(icacheConfig: CacheRamConfig) extends Component {
  class PipeS1S2 extends Bundle {
    val paddr     = UInt(32 bits)
    val exception = FetchException()
    val pc        = UInt(32 bits)
  }
  class PipeS2S3 extends Bundle {
    val exception = FetchException()
    val inst      = Vec(InstEntry(), 2)
  }
  val io = new Bundle {
    val fetch  = in Bool ()               //ID阶段是否需要取指
    val inst   = out Vec (InstEntry(), 2) //双发射一个周期取2条指令
    val jump   = slave Flow UInt(32 bits) //分支纠错
    val except = slave Flow UInt(32 bits) //异常跳转

    val vaddr     = out UInt (32 bits)                            //取指的虚地址
    val mmuRes    = in(MMUTranslationRes(ConstantVal.FINAL_MODE)) //取指的实地址
    val exception = out(FetchException())
    val ibus      = master(new CPUICacheInterface(icacheConfig))
  }

  val pcHandler = new PCHandler
  val fifo      = new InstrFifo(4, 2, 3)
  val decoder = new Area {
    val decoder = Array.fill(2)(new SimpleDecoder)
    val inst    = Vec(Mips32Inst(), 2)
    val is = new Area {
      val branch = Bits(2 bits)
    }
    for (i <- 0 until 2) {
      decoder(i).io.inst := inst(i)
      is.branch(i) := decoder(i).io.branch
    }
  }
  /*
   * 当pcHandler发起flush请求时，需要作废掉
   * 1. stage1对应pc的取指
   * 2. stage2当前取指
   * 3. 目前正在从fifo中pop出的指令
   * 4. 已经在(regS2S3中)stage3的指令
   * 5. fifo中所有的指令
   */

  pcHandler.io.jump << io.jump
  pcHandler.io.except << io.except

  val ibus = io.ibus
  /*
   * stage1: 发起对ICache的请求
   */
  val sendToS2 = Stream(new PipeS1S2)
  // 地址检查
  val check = new Area {
    val aligned          = pcHandler.io.pc(0, 2 bits) === U"00"
    val refillException  = io.mmuRes.tlbRefillException
    val invalidException = io.mmuRes.tlbInvalidException
    val exception        = FetchException()
    when(!aligned | io.mmuRes.illegal) {
      exception.code := ExcCode.loadAddrError
    }.elsewhen(refillException | invalidException) {
      exception.code := ExcCode.loadTLBError
    }.otherwise(exception.code := None)
    exception.refillException := refillException
  }

  pcHandler.io.retain := !sendToS2.ready
  // TODO 分支预测
  pcHandler.io.predict.setIdle()

  io.vaddr := pcHandler.io.pc
  ibus.stage1.read := True
  ibus.stage1.index := pcHandler.io.pc(icacheConfig.offsetWidth, icacheConfig.indexWidth bits)

  // master is Stage 1
  sendToS2.valid := True
  sendToS2.paddr := io.mmuRes.paddr
  sendToS2.exception := check.exception
  sendToS2.pc := pcHandler.io.pc

  /*
   * stage2: 获得从ICache读出的指令
   */
  // S1 - S2 之间的寄存器
  val recvFromS1 = sendToS2.m2sPipe(collapsBubble = false, flush = pcHandler.io.flush.s1)

  val sendToS3 = Stream(new PipeS2S3)
  // 当S3准备好了并且S2没有stall时可以接受新的输入
  recvFromS1.ready := !ibus.stage2.stall & sendToS3.ready

  ibus.stage2.en := recvFromS1.valid & recvFromS1.exception.code.isEmpty
  ibus.stage2.paddr := recvFromS1.paddr

  // 由于AXI通信无法直接被打断，因此刷新信号需要寄存
  val delayFlush = Reg(Bool) init False
  when(!ibus.stage2.stall)(delayFlush := False)
    .otherwise(delayFlush := delayFlush | pcHandler.io.flush.s2)
  val flush = delayFlush | pcHandler.io.flush.s2

  sendToS3.valid := !ibus.stage2.stall & recvFromS1.valid
  sendToS3.exception := recvFromS1.exception
  when(!recvFromS1.valid)(sendToS3.exception.code := None)
  sendToS3.inst(0).inst.assignFromBits(ibus.stage2.rdata(0, 32 bits))
  sendToS3.inst(1).inst.assignFromBits(ibus.stage2.rdata(32, 32 bits))
  sendToS3.inst(0).pc := recvFromS1.pc
  sendToS3.inst(1).pc := recvFromS1.pc(31 downto 3) @@ U"1" @@ recvFromS1.pc(0, 2 bits)
  sendToS3.inst(0).valid := sendToS3.valid & recvFromS1.pc(2, 1 bits) === 0
  sendToS3.inst(1).valid := sendToS3.valid
  for (i <- 0 until 2) {
    sendToS3.inst(i).branch.assignDontCare()
  }

  /*
   * stage3: 压入指令队列或者byPass给下一阶段
   */
  /** 表明从RegS2S3拿到的数据是有效的 */
  // S2 - S3 之间的寄存器
  val recvFromS2 = sendToS3.m2sPipe(collapsBubble = false, flush = flush)
  // 先进行分支指令解码
  decoder.inst := Vec(recvFromS2.inst.map(entry => entry.inst))

  val validInstFromS2 = recvFromS2.valid & !pcHandler.io.flush.s3
  val byPass          = io.fetch & fifo.io.empty

  pcHandler.io.stop.valid := fifo.io.full & validInstFromS2
  pcHandler.io.stop.payload := RegNext(recvFromS1.pc)


  val s3Inst = Vec(InstEntry(), 2)
  for(i <- 0 until 2) {
    s3Inst(i).pc := recvFromS2.inst(i).pc
    s3Inst(i).inst := recvFromS2.inst(i).inst
    s3Inst(i).valid := recvFromS2.inst(i).valid & validInstFromS2
    s3Inst(i).branch := decoder.is.branch(i)
  }


  val delaySlot = new Area {
    val waiting = new Bundle {
      val next = Bool
      val reg = RegNext(next) init False
      next := reg
    }
    // 1.（清除） 如果之前已经需要延迟槽:
    //     a) 如果队列为空，那么S2一旦送来有效指令则消除了延迟槽
    //     b) 如果队列非空，那么直接pop，也消除了延迟槽
    // 2. （产生）如果之前不需要延迟槽
    //    当ID阶段**需要指令**时，开始判断是否需要等待延迟槽
    //     a) 如果队列为空，那么此时会byPass：S2送过来的第二条有效指令是否是分支
    //     b) 如果队列非空，如果队列只有1条有效指令，则看data(0)；如果有两条有效指令，则看data(1)
    when(waiting.reg) {
      when(fifo.io.empty)(waiting.next := !validInstFromS2)
        .otherwise(waiting.next := False)
    }.elsewhen(io.fetch) {
      when(fifo.io.empty)(waiting.next := decoder.is.branch(1) & validInstFromS2)
        .otherwise {
          waiting.next := fifo.io.pop.data(1).valid ? fifo.io.pop.data(1).branch | fifo.io.pop.data(0).branch
        }
    }
  }
  val branch = new Area {
    val inst = Reg(InstEntry())
    val busy = new Bundle {
      val next = Bool
      val reg = RegNext(next)
      next := reg
    }
    when(io.fetch) {
      when(busy.reg) {
        when(fifo.io.empty)(busy.next := delaySlot.waiting.next)
          .otherwise(busy.next := False)
      }.otherwise {
        when(fifo.io.empty)(busy.next := decoder.is.branch(1) & validInstFromS2)
          .otherwise {
            busy.next := fifo.io.pop.data(1).valid ? fifo.io.pop.data(1).branch | fifo.io.pop.data(0).branch
          }
      }
    }
  }
  when(!branch.busy.reg) {
    when(fifo.io.empty) (branch.inst := s3Inst(1))
      .otherwise {
        branch.inst := fifo.io.pop.data(1).valid ? fifo.io.pop.data(1) | fifo.io.pop.data(0)
      }
  }

  // 0是发送给第一条流水线
  when(branch.busy.reg) {
    io.inst(0) := branch.inst.validWhen(!branch.busy.next)
  }.otherwise(io.inst(0) := fifo.io.empty ? s3Inst(0) | fifo.io.pop.data(0))
  when(branch.busy.next & !fifo.io.empty & !fifo.io.pop.data(1).valid)(io.inst(0).valid := False)
  // 1是发射给第二条流水线
  when(branch.busy.reg)(io.inst(1) := byPass ? s3Inst(0) | fifo.io.pop.data(0))
    .otherwise(io.inst(1) := fifo.io.empty ? s3Inst(1) | fifo.io.pop.data(1))
  when(branch.busy.next)(io.inst(1).valid := False)

  when(pcHandler.io.flush.s3) {
    for (i <- 0 until 2) io.inst(i).valid := False
    delaySlot.waiting.next := False
    branch.busy.next := False
  }

  // FIFO能够PUSH的必要条件是S2传来有效指令
  // 1. 正在等待延迟槽，那么另一条指令进入
  // 2. 队列没有满并且不是byPass
  fifo.io.push.en := (delaySlot.waiting.reg | (!fifo.io.full  & !byPass)) & validInstFromS2
  // push num
  when(byPass & delaySlot.waiting.reg) (fifo.io.push.num := 1)
    .otherwise(fifo.io.push.num := s3Inst.map(entry => entry.valid.asUInt).reduce((x, y) => x +^ y))
  // push data
  when((byPass & delaySlot.waiting.reg) | !s3Inst(0).valid) {
    // 当byPass并且此时需要等延迟槽的时候，只塞入一条指令
    fifo.io.push.data(0) := s3Inst(1)
  }.otherwise {
    fifo.io.push.data(0) := s3Inst(0)
  }
  fifo.io.push.data(1) := s3Inst(1)
  // FIFO POP的条件
  // 1. 队列不是空
  // 2. 后续阶段需要指令供给
  fifo.io.pop.en := io.fetch & !fifo.io.empty
  fifo.io.pop.num := branch.busy.reg ? U(1) | U(2)
  // FIFO FLUSH
  fifo.io.flush := pcHandler.io.flush.s3

  recvFromS2.ready := !fifo.io.full | delaySlot.waiting.reg
  io.exception := recvFromS2.exception
  when(!recvFromS2.valid)(io.exception.code := None)
}

class PCHandler extends Component {
  val io = new Bundle {
    val retain  = in Bool ()               //保持当前的PC值
    val stop    = slave Flow UInt(32 bits) //当队列满了之后需要重新取
    val jump    = slave Flow UInt(32 bits) //分支纠错
    val except  = slave Flow UInt(32 bits) //发生异常
    val predict = slave Flow UInt(32 bits) //分支预测
    val pc      = out UInt (32 bits)
    val flush   = new Bundle {
      val s1 = out Bool()
      val s2 = out Bool()
      val s3 = out Bool()
    }
  }

  io.flush.s1 := io.jump.valid | io.stop.valid | io.except.valid
  io.flush.s2 := io.jump.valid | io.stop.valid | io.except.valid
  io.flush.s3 := io.jump.valid | io.except.valid

  val pc = new Bundle {
    val next = UInt(32 bits)
    val reg  = RegNext(next) init ConstantVal.INIT_PC
  }
  io.pc := io.predict.valid ? io.predict.payload | pc.reg
  pc.next := (io.pc(31 downto 3) + 1) @@ U"000" //默认情况下pc + 8
  when(io.retain)(pc.next := pc.reg)
  when(io.stop.valid)(pc.next := io.stop.payload)
  when(io.predict.valid)(pc.next := io.predict.payload)
  when(io.jump.valid)(pc.next := io.jump.payload)
  when(io.except.valid)(pc.next := io.except.payload)
}

/** @param way 由多少个Fifo拼成
  * @param pushWay 最多可压入的数量
  * @param depth Fifo的深度
  * @note 针对32位的多发射的指令队列，内部采用多个单端口Fifo交叠实现
  * @note 正因为内部交叠实现，因此各个fifo之间有效数据的数量
  * @note 固定为双发射，因此每个周期最多pop 2条指令出来
  */
class InstrFifo(way: Int, pushWay: Int, depth: Int) extends Component {
  require((way & (way - 1)) == 0)
  def wayIndexWidth: Int     = log2Up(way)
  def pushWayIndexWidth: Int = log2Up(pushWay)
  def popWay: Int            = 2
  def popWayIndexWidth: Int  = log2Up(2)
  val io = new Bundle {
    val push = new Bundle {
      val data = in Vec (InstEntry(), pushWay)
      val en   = in Bool ()
      val num  = in UInt (log2Up(pushWay + 1) bits)
    }
    val pop = new Bundle {
      val data = out Vec (InstEntry(), popWay)
      val en   = in Bool ()
      val num  = in UInt (log2Up(popWay + 1) bits)
    }
    val flush = in Bool ()
    val empty = out Bool () //当所有fifo都空为1
    val full  = out Bool () //当有一路满了就停止push的操作
  }
  val counter = SimpleCounter(way * depth + 1)
  val fifo = new Area {
    val fifos = Array.fill(way)(new SimpleFifo(InstEntry(), 32, depth))
    val push = new Bundle {
      val data = Vec(InstEntry(), way)
      val en   = Bits(way bits)
    }
    val pop = new Bundle {
      val data = Vec(InstEntry(), way)
      val en   = Bits(way bits)
    }
    val empty = Bits(way bits)
    val full  = Bits(way bits)
    for (i <- 0 until way) {
      fifos(i).io.push.en := push.en(i)
      fifos(i).io.push.data := push.data(i)
      fifos(i).io.pop.en := pop.en(i)
      pop.data(i) := fifos(i).io.pop.data
      empty(i) := fifos(i).io.empty
      full(i) := fifos(i).io.full
      //flush
      fifos(i).io.flush := io.flush
    }
  }

//  io.empty := fifo.empty.andR
//  io.full := CountOne(fifo.full) > way - pushWay
  io.empty := counter.value === 0
  io.full := counter.value > (way * depth - pushWay)

  val ptr = new Bundle {
    val push = SimpleCounter(way)
    val pop  = SimpleCounter(way)
  }

  /** 因为采用交叠的方式，需要选择pop和push的具体位置 */
  val index = new Bundle {

    /** index.pop(idx)表示第idx个pop的entry来自于哪一个fifo */
    val pop = Vec(UInt(log2Up(way) bits), popWay)
    for (i <- 0 until popWay) pop(i) := ptr.pop.value + i
  }
  val distance = new Bundle {
    val push = Vec(UInt(log2Up(way) bits), way)
    val pop  = Vec(UInt(log2Up(way) bits), way)
    for (i <- 0 until way) {
      push(i) := i - ptr.push.value
      pop(i) := i - ptr.pop.value
    }
  }

  /** pop输出的逻辑 */
  for (i <- 0 until popWay) {
    io.pop.data(i) := fifo.pop.data(index.pop(i)).validWhen(!fifo.empty(index.pop(i)))
  }
  for (i <- 0 until way) {
    fifo.push.data(i) := io.push.data(distance.push(i).resize(pushWayIndexWidth))
    fifo.push.en(i) := distance.push(i) < io.push.num & io.push.en
    fifo.pop.en(i) := distance.pop(i) < io.pop.num & io.pop.en
  }

  /** 更新ptr */
  val popNum = (counter.value > 1) ? io.pop.num | U(1) // 真正pop出来的数量
  when(io.flush) {
    ptr.push.clear()
    ptr.pop.clear()
    counter.clear()
  }.otherwise {
    when(io.push.en) {
      counter.incr(io.push.num)
      ptr.push.incr(io.push.num)
    }
    when(io.pop.en) {
      counter.decr(popNum)
      ptr.pop.incr(popNum)
    }
    when(io.push.en & io.pop.en) {
      counter.value := counter.value - popNum + io.push.num
    }
  }
}

/** 简单的译码器，判断一条指令是否是分支指令 */
class SimpleDecoder extends Component {
  val io = new Bundle {
    val inst   = in(Mips32Inst())
    val branch = out Bool ()
  }
  io.branch := False
  import defs.InstructionSpec._
  import defs.Mips32InstImplicits._
  for (inst <- Seq(BEQ, BNE, BGEZ, BGTZ, BLEZ, BLTZ, BGEZAL, BLTZAL, J, JAL, JR, JALR)) {
    when(io.inst === inst)(io.branch := True)
  }
}

/** 简单的队列实现
  * @param width 队列中数据宽度
  * @param depth 队列中深度
  * @note 从Spinal.lib.SteamFifo中简化而来
  */
class SimpleFifo[T <: Data](gen: => T, width: Int, depth: Int) extends Component {
  require(depth > 1 && width > 0)
  val io = new Bundle {
    val flush = in Bool ()
    val full  = out Bool ()
    val empty = out Bool ()
    val push = new Bundle {
      val en   = in Bool ()
      val data = in(gen)
    }
    val pop = new Bundle {
      val en   = in Bool ()
      val data = out(gen)
    }
  }

  val ram             = Mem(gen, depth)
  val pushPtr         = Counter(depth)
  val popPtr          = Counter(depth)
  val ptrMatch        = pushPtr === popPtr
  val risingOccupancy = RegInit(False) //表示上一次容量变化是否是增加
  when(io.push.en =/= io.pop.en)(risingOccupancy := io.push.en)
  io.full := risingOccupancy & ptrMatch
  io.empty := !risingOccupancy & ptrMatch
  io.pop.data := ram(popPtr.value)

  when(io.push.en & (!io.full | io.pop.en)) {
    ram(pushPtr.value) := io.push.data
    pushPtr.increment()
  }
  when(io.pop.en & !io.empty) {
    popPtr.increment()
  }
  when(io.flush) {
    pushPtr.clear()
    popPtr.clear()
    risingOccupancy := False
  }
}

/** @param upper 计数器的上界，计数范围在[0, upper - 1]
  * @note 只适用于2的幂对齐的计数器，例如upper = 3或者upper = 7
  */
case class SimpleCounter(upper: Int) extends Bundle {
  val counter               = Reg(UInt(log2Up(upper) bits)) init 0
  def clear(): Unit         = counter := 0
  def incr(num: UInt): Unit = counter := counter + num
  def decr(num: UInt): Unit = counter := counter - num
  def value: UInt           = counter
}

object FetchInst {
  def main(args: Array[String]): Unit = {
    val icacheConfig =
      CacheRamConfig(blockSize = ConstantVal.IcacheLineSize, wayNum = ConstantVal.IcacheWayNum)
    SpinalVerilog(new FetchInst(icacheConfig))
  }
}
