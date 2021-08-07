package cpu

import cpu.defs.{ConstantVal, Mips32Inst}
import cpu.defs.Config._
import cache.{CPUICacheInterface, CacheRamConfig}
import lib.Optional
import spinal.core._
import spinal.lib._
import tlb.MMUTranslationRes

import scala.language.{existentials, postfixOps}

object BranchType extends SpinalEnum {
  val RELATIVE, OTHER = newElement()
}

case class PredictInfo() extends Bundle {
  val taken      = Bool()   //是否预测会发生跳转
  val btbHit     = Bool()
  val btbHitLine = Bits(BTB.NUM_WAYS bits)
  val btbP       = Bits(BTB.NUM_WAYS - 1 bits)
  val bhtV       = Bits(BHT.DATA_WIDTH bits)
  val phtV       = UInt(2 bits)
}

case class BranchInfo() extends Bundle {
  val ty            = BranchType()
  val is            = Bool()
  def isDjump: Bool = is & ty === BranchType.RELATIVE
}

/** @note 队列中保存的信息 */
class InstEntry extends Bundle {
  val inst    = Mips32Inst()
  val pc      = UInt(32 bits)
  val branch  = BranchInfo()
  val predict = PredictInfo()
  val valid   = Bool()

  def validWhen(cond: Bool): InstEntry = {
    val entry = new InstEntry
    entry.inst := inst
    entry.pc := pc
    entry.branch := branch
    entry.predict := predict
    entry.valid := valid & cond
    entry
  }
}
object InstEntry {
  def apply(): InstEntry = new InstEntry
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

case class IssueEntry() extends InstEntry {
  val target = UInt(32 bits)

  def :=(that: InstEntry): Unit = this.assignSomeByName(that)
}

class FetchInst(icacheConfig: CacheRamConfig) extends Component {
  private implicit class InstWithBranchImplicit(inst: Mips32Inst) {
    def target(pc: UInt): UInt = {
      val res = UInt(32 bits)
      when(inst.op(1, 2 bits) === B"01") {
        res := (pc(31 downto 2) + 1)(29 downto 26) @@ inst.index @@ U"00"
      }.otherwise {
        res := U(S(pc(31 downto 2)) + inst.offset + 1) @@ U"00"
      }
      res
    }

    def branchTy: SpinalEnumCraft[BranchType.type] = {
      val ty = BranchType()
      ty := (inst.op(1, 2 bits) === B"01") ? BranchType.OTHER | BranchType.RELATIVE
      ty
    }
  }
  class PipeS1S2 extends Bundle {
    val paddr     = UInt(32 bits)
    val exception = FetchException()
    val pc        = UInt(32 bits)
  }
  class PipeS2S3 extends Bundle {
    val exception = FetchException()
    val entry      = Vec(InstEntry(), 2)
  }
  val io = new Bundle {
    val fetch  = in Bool ()               //ID阶段是否需要取指
    val issue  = out Vec (IssueEntry(), 2)
    val jump   = slave Flow UInt(32 bits) //分支纠错
    val except = slave Flow UInt(32 bits) //异常跳转

    val bp = in(new Bundle {
      val write1 = new Bundle {
        val pc         = Address()
        val isDJump1   = Bool()
        val jumpPC1    = UInt(32 bits)
        val isDJump2   = Bool()
        val jumpPC2    = UInt(32 bits)
        val btbHit     = Bool()
        val btbHitLine = Bits(BTB.NUM_WAYS bits)
        val btbP       = Bits(BTB.NUM_WAYS - 1 bits)
        val bhtV       = Bits(BHT.DATA_WIDTH bits)
        val phtV       = UInt(2 bits)
      }
      val write2 = new Bundle {
        val assignJump = Bool()
        val will = new Bundle {
          val input  = Bool()
        }
      }
    })

    val vaddr     = out UInt (32 bits)                            //取指的虚地址
    val mmuRes    = in(MMUTranslationRes(ConstantVal.FINAL_MODE)) //取指的实地址
    val exception = out(FetchException())
    val ibus      = master(new CPUICacheInterface(icacheConfig))
  }

  val pcHandler = new PCHandler
  val fifo      = new InstrFifo(4, 2, 2)
  val decoder = new Area {
    val decoder = Array.fill(2)(new SimpleDecoder)
    val inst    = Vec(Mips32Inst(), 2)
    val pc      = Vec(UInt(32 bits), 2)
    val target  = Vec(UInt(32 bits), 2)
    val is = new Area {
      val branch = Bits(2 bits)
    }
    for (i <- 0 until 2) {
      decoder(i).io.inst := inst(i)
      is.branch(i) := decoder(i).io.branch
    }
  }
  val bp = new BranchPredictor
  bp.write1.io.assignAllByName(io.bp.write1)
  bp.write2.io.assignAllByName(io.bp.write2)
  bp.write2.will.input := io.bp.write2.will.input
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
  val S1 = new Area {
    val sendToS2 = Stream(new PipeS1S2)
    // 地址检查
    val check = new Area {
      val aligned          = pcHandler.io.pc.current(0, 2 bits) === U"00"
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

    sendToS2.valid := True
    sendToS2.paddr := io.mmuRes.paddr
    sendToS2.exception := check.exception
    sendToS2.pc := pcHandler.io.pc.current
  }
  // S2没有准备好就retain pc
  pcHandler.io.retain := !S1.sendToS2.ready
  // 分支预测
  bp.read1.io.pc := pcHandler.io.pc.current
  bp.read1.io.nextPC := pcHandler.io.pc.next

  bp.read1.will.input := S1.sendToS2.fire

  io.vaddr := pcHandler.io.pc.current
  ibus.stage1.read := True
  ibus.stage1.index := pcHandler.io.pc
    .current(icacheConfig.offsetWidth, icacheConfig.indexWidth bits)

  /*
   * stage2: 获得从ICache读出的指令
   */
  val S2 = new Area {
    val recvFromS1 = S1.sendToS2.m2sPipe(collapsBubble = false, flush = pcHandler.io.flush.s1)
    val sendToS3   = Stream(new PipeS2S3)
    // 当S3准备好了并且S2没有stall时可以接受新的输入
    recvFromS1.ready := !ibus.stage2.stall & sendToS3.ready

    sendToS3.valid := !ibus.stage2.stall & recvFromS1.valid
    sendToS3.exception := recvFromS1.exception
    when(!recvFromS1.valid)(sendToS3.exception.code := None)
    sendToS3.entry(0).pc := recvFromS1.pc
    sendToS3.entry(1).pc := recvFromS1.pc(31 downto 3) @@ U"1" @@ recvFromS1.pc(0, 2 bits)
    for (i <- 0 until 2) {
      sendToS3.entry(i).inst.assignFromBits(ibus.stage2.rdata(i * 32, 32 bits))
      sendToS3.entry(i).predict.assignSomeByName(bp.read2.io)
      sendToS3.entry(i).branch.is.assignDontCare()
      sendToS3.entry(i).branch.ty.assignDontCare()
      sendToS3.entry(i).predict.taken := bp.read2.io.assignJump(i)
    }

    // 由于AXI通信无法直接被打断，因此刷新信号需要寄存
    val delayFlush = Reg(Bool) init False
    when(!ibus.stage2.stall)(delayFlush := False)
      .otherwise(delayFlush := delayFlush | pcHandler.io.flush.s2)
    val flush = delayFlush | pcHandler.io.flush.s2

    // S2已经检测到分支跳转了但是需要等延迟槽
    val waiting = new Bundle {
      val delaySlot = Bool() clearWhen flush
      val jumpPc    = UInt(32 bits)
    }.setAsReg()

    sendToS3.entry(0).valid := sendToS3.valid & recvFromS1.pc(2, 1 bits) === 0
    sendToS3.entry(1).valid := sendToS3.valid & !waiting.delaySlot
  }

  ibus.stage2.en := S2.recvFromS1.valid & S2.recvFromS1.exception.code.isEmpty
  ibus.stage2.paddr := S2.recvFromS1.paddr

  // 分支预测
  val predict = new Area {
    val taken = bp.read2.io.assignJump
    bp.read2.will.input := S1.sendToS2.fire
  }
  pcHandler.io.predict.setIdle() //默认值
  when(predict.taken(0) & S2.recvFromS1.fire) {
    // 第一条指令是跳转，可以立即更新pc
    pcHandler.io.predict.push(bp.read2.io.jumpPC)
  }
  when(predict.taken(1) & S2.recvFromS1.fire) {
    // 第二条指令是跳转，需要等延迟槽
    S2.waiting.delaySlot := True
    S2.waiting.jumpPc := bp.read2.io.jumpPC
  }
  when(S2.waiting.delaySlot & S2.recvFromS1.fire) {
    pcHandler.io.predict.push(S2.waiting.jumpPc)
  }
  when(S2.waiting.delaySlot & S2.sendToS3.fire)(S2.waiting.delaySlot.clear())

  /*
   * stage3: 压入指令队列或者byPass给下一阶段
   */
  val S3 = new Area {
    val recvFromS2 = S2.sendToS3.m2sPipe(collapsBubble = false, flush = S2.flush)

    val validInstFromS2 = recvFromS2.valid & !pcHandler.io.flush.s3

    val entry = Vec(InstEntry(), 2)
    for (i <- 0 until 2) {
      entry(i).pc := recvFromS2.entry(i).pc
      entry(i).inst := recvFromS2.entry(i).inst
      entry(i).valid := recvFromS2.entry(i).valid & validInstFromS2
      entry(i).predict := recvFromS2.entry(i).predict
      entry(i).branch.is := decoder.is.branch(i)
      entry(i).branch.ty := recvFromS2.entry(i).inst.branchTy
    }

  }
  // 先进行分支指令解码
  decoder.inst := Vec(S3.recvFromS2.entry.map(entry => entry.inst))
  decoder.pc := Vec(S3.recvFromS2.entry.map(entry => entry.pc))

  pcHandler.io.stop.valid := fifo.io.full & S3.validInstFromS2
  pcHandler.io.stop.payload := RegNext(S2.recvFromS1.pc)

  val delaySlot = new Area {
    val waiting = new Bundle {
      val next = Bool
      val reg  = RegNext(next) init False
      next := reg
    }
    // 对于延迟槽的处理，我们需要两个信号，一个是delaySlot.waiting
    // 另一个是branch.busy
    // delaySlot.waiting是处理S2取进的指令逻辑：当正在等待延迟槽时
    // 队列能够接受新的指令（因为始终留了位置给延迟槽），一旦延迟槽被取回，
    // delaySlot.waiting就清空，此时队列满了则不再接受S2的指令
    // branch.busy是处理ID阶段拿走的逻辑：一旦分支指令处于准备发射阶段
    // 给到ID阶段的第一条指令一定是暂存的分支指令
    when(waiting.reg) {
      when(fifo.io.empty)(waiting.next := !S3.validInstFromS2)
        .otherwise(waiting.next := False)
    }.elsewhen(io.fetch) {
      when(fifo.io.empty)(waiting.next := decoder.is.branch(1) & S3.validInstFromS2)
        .otherwise {
          waiting.next := fifo.io.pop.data(1).valid ? fifo.io.pop.data(1).branch.is |
            fifo.io.pop.data(0).branch.is
        }
    }
  }
  val branch = new Area {
    val inst = Reg(InstEntry())
    val busy = new Bundle {
      val next = Bool
      val reg  = RegNext(next)
      next := reg
    }
    when(io.fetch) {
      when(busy.reg) {
        when(fifo.io.empty)(busy.next := delaySlot.waiting.next)
          .otherwise(busy.next := False)
      }.otherwise {
        when(fifo.io.empty)(busy.next := decoder.is.branch(1) & S3.validInstFromS2)
          .otherwise {
            busy.next := fifo.io.pop.data(1).valid ? fifo.io.pop.data(1).branch.is |
              fifo.io.pop.data(0).branch.is
          }
      }
    }
  }
  when(!branch.busy.reg) {
    when(fifo.io.empty)(branch.inst := S3.entry(1))
      .otherwise {
        branch.inst := fifo.io.pop.data(1).valid ? fifo.io.pop.data(1) | fifo.io.pop.data(0)
      }
  }

  val byPass = io.fetch & fifo.io.empty
  // 0是发送给第一条流水线
  when(branch.busy.reg) {
    io.issue(0) := branch.inst.validWhen(!branch.busy.next)
  }.otherwise(io.issue(0) := fifo.io.empty ? S3.entry(0) | fifo.io.pop.data(0))
  when(branch.busy.next & !fifo.io.empty & !fifo.io.pop.data(1).valid) {
    io.issue(0).valid := False
  }
  // 1是发射给第二条流水线
  when(branch.busy.reg)(io.issue(1) := byPass ? S3.entry(0) | fifo.io.pop.data(0))
    .otherwise(io.issue(1) := fifo.io.empty ? S3.entry(1) | fifo.io.pop.data(1))
  when(branch.busy.next)(io.issue(1).valid := False)

  for (i <- 0 until 2) {
    io.issue(i).target := io.issue(i).inst.target(io.issue(i).pc)
  }

  when(pcHandler.io.flush.s3) {
    for (i <- 0 until 2) io.issue(i).valid := False
    delaySlot.waiting.next := False
    branch.busy.next := False
  }

  // FIFO能够PUSH的必要条件是S2传来有效指令
  // 1. 正在等待延迟槽，那么另一条指令进入
  // 2. 队列没有满并且不是byPass
  fifo.io.push.en := (delaySlot.waiting.reg | (!fifo.io.full & !byPass)) & S3.validInstFromS2
  // push num
  when(byPass & delaySlot.waiting.reg)(fifo.io.push.num := 1)
    .otherwise {
      fifo.io.push.num := S3.entry.map(entry => entry.valid.asUInt).reduce((x, y) => x +^ y)
    }
  // push data
  when((byPass & delaySlot.waiting.reg) | !S3.entry(0).valid) {
    // 当byPass并且此时需要等延迟槽的时候，只塞入一条指令
    fifo.io.push.data(0) := S3.entry(1)
  }.otherwise {
    fifo.io.push.data(0) := S3.entry(0)
  }
  fifo.io.push.data(1) := S3.entry(1)
  // FIFO POP的条件
  // 1. 队列不是空
  // 2. 后续阶段需要指令供给
  fifo.io.pop.en := io.fetch & !fifo.io.empty
  fifo.io.pop.num := branch.busy.reg ? U(1) | U(2)
  // FIFO FLUSH
  fifo.io.flush := pcHandler.io.flush.s3

  S3.recvFromS2.ready := !fifo.io.full | delaySlot.waiting.reg
  io.exception := S3.recvFromS2.exception
  when(!S3.recvFromS2.valid)(io.exception.code := None)
}

class PCHandler extends Component {
  val io = new Bundle {
    val retain  = in Bool ()               //保持当前的PC值
    val stop    = slave Flow UInt(32 bits) //当队列满了之后需要重新取
    val jump    = slave Flow UInt(32 bits) //分支纠错
    val except  = slave Flow UInt(32 bits) //发生异常
    val predict = slave Flow UInt(32 bits) //分支预测
    val pc = new Bundle {
      val current = out UInt (32 bits)
      val next    = out UInt (32 bits)
    }
    val flush = new Bundle {
      val s1 = out Bool ()
      val s2 = out Bool ()
      val s3 = out Bool ()
    }
  }

  io.flush.s1 := io.jump.valid | io.stop.valid | io.except.valid
  io.flush.s2 := io.jump.valid | io.stop.valid | io.except.valid
  io.flush.s3 := io.jump.valid | io.except.valid

  val pc = new Bundle {
    val next = UInt(32 bits)
    val reg  = RegNext(next) init ConstantVal.INIT_PC
  }
  io.pc.current := io.predict.valid ? io.predict.payload | pc.reg
  io.pc.next := pc.next

  pc.next := (io.pc.current(31 downto 3) + 1) @@ U"000" //默认情况下pc + 8
  when(io.retain)(pc.next := pc.reg)
  when(io.stop.valid)(pc.next := io.stop.payload)
//  when(io.predict.valid)(pc.next := io.predict.payload)
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
    val fifos = Array.fill(way)(new SimpleFifo(InstEntry(), depth))
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

  // 因为采用交叠的方式，需要选择pop和push的具体位置
  val index = new Bundle {
    // index.pop(idx)表示第idx个pop的entry来自于哪一个fifo
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
  for (inst <- Seq(BEQ, BNE, BGEZ, BLTZ, BGEZAL, BLTZAL, BGTZ, BLEZ, J, JAL, JR, JALR)) {
    when(inst === io.inst) {
      io.branch := True
    }
  }
}

/** 简单的队列实现
  * @param depth 队列中深度
  * @note 从Spinal.lib.SteamFifo中简化而来
  */
class SimpleFifo[T <: Data](gen: => T, depth: Int) extends Component {
  require(depth > 1)
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
  * @note 如果数值可能溢出，则适用于2的幂对齐的计数器，例如upper = 4或者upper = 8
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
