package cpu

import cpu.defs.{ConstantVal, Mips32Inst}
import lib.{Optional, Updating}
import spinal.core._
import spinal.lib.{cpu => _, _}
import tlb.{TLBConfig, TLBEntry}
import Utils.IntToFixedLengthBinaryString
import cpu.CP0.Cp0Reg

import scala.collection.mutable
import scala.language.{existentials, postfixOps}

/** TLBR, TLBWI: 来源是Index寄存器
  * TLBWR: 来源是Random寄存器
  */
object TLBIndexSrc extends SpinalEnum {
  val Index, Random = newElement()
}

// 处理中断异常的部件使用
class ExceptionBus extends Bundle with IMasterSlave {
  val exception       = new EXCEPTION
  val eret            = Bool
  val vaddr           = UInt(32 bits) //虚地址
  val pc              = UInt(32 bits)
  val bd              = Bool
  val refillException = Bool


  val jumpPc = Optional(UInt(32 bits))

  override def asMaster(): Unit = {
    out(exception, eret, vaddr, pc, bd, refillException)
    in(jumpPc)
  }
}

case class Addr() extends Bundle {
  val rd  = UInt(5 bits)
  val sel = UInt(3 bits)

  def :=(that: (Int, Int)) = {
    rd := that._1
    sel := that._2
  }

  def =/=(that: (Int, Int)) = !(this === that)

  def ===(that: (Int, Int)) = rd === that._1 && sel === that._2
}

case class Read() extends Bundle with IMasterSlave {
  val addr = Addr()
  val data = Bits(32 bits)

  override def asMaster() = {
    out(addr)
    in(data)
  }
}

case class Write() extends Bundle {
  val addr = Addr()
  val data = Bits(32 bits)
}

object CP0 {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CP0)
  }

  object Type extends Enumeration {
    val SoftWrite, HardWrite, NoWrite = Value
  }

  def TLBIndexWidth: Int = log2Up(ConstantVal.TLBEntryNum)

  class RegisterBase(num: Int, sel: Int) extends Bundle {
    case class RegValue(name: String, range: Range, ty: Type.Value) {}

    val map: mutable.Map[RegValue, Bits] = mutable.Map()
    val addr                             = Addr()
    addr := (num, sel)

    def addField(name: String, range: Range, ty: CP0.Type.Value, initial: Option[Bits]): Bits = {
      for ((field, _) <- map) {
        assert(range.max < field.range.min || range.min > field.range.max)
        assert(field.name != name)
      }
      initial match {
        case Some(v) =>
          assert(v.getBitsWidth == range.length)
          ty match {
            case Type.SoftWrite =>
              map += RegValue(name, range, ty) -> (Reg(Bits(range.length bits)) init v)
            case Type.HardWrite =>
              map += RegValue(name, range, ty) -> (Reg(Bits(range.length bits)) init v)
            case Type.NoWrite => map += RegValue(name, range, ty) -> B(v, range.length bits)
          }
        case None =>
          ty match {
            case Type.SoftWrite =>
              map += RegValue(name, range, ty) -> Reg(Bits(range.length bits))
            case Type.HardWrite =>
              map += RegValue(name, range, ty) -> Reg(Bits(range.length bits))
            case Type.NoWrite => map += RegValue(name, range, ty) -> Bits(range.length bits)
          }
      }
      this.apply(name).setCompositeName(this, name)
      this.apply(name)
    }

    /** 普通的域 */
    def field(name: String, range: Range, initial: Bits): Bits = {
      addField(name, range, Type.SoftWrite, Some(initial))
    }
    def field(name: String, range: Range): Bits = {
      addField(name, range, Type.SoftWrite, None)
    }
    def field(name: String, idx: Int, initial: Bits): Bits = {
      addField(name, idx to idx, Type.SoftWrite, Some(initial))
    }
    def field(name: String, idx: Int): Bits = {
      addField(name, idx to idx, Type.SoftWrite, None)
    }

    /** 只有硬件可写的域 */
    def hardField(name: String, range: Range, initial: Bits): Bits = {
      addField(name, range, Type.HardWrite, Some(initial))
    }
    def hardField(name: String, range: Range): Bits = {
      addField(name, range, Type.HardWrite, None)
    }
    def hardField(name: String, idx: Int, initial: Bits): Bits = {
      addField(name, idx to idx, Type.HardWrite, Some(initial))
    }
    def hardField(name: String, idx: Int): Bits = {
      addField(name, idx to idx, Type.HardWrite, None)
    }

    /** 不可写的域 */
    def staticField(name: String, range: Range, initial: Bits): Bits = {
      addField(name, range, Type.NoWrite, Some(initial))
    }
    def staticField(name: String, range: Range): Bits = {
      addField(name, range, Type.NoWrite, None)
    }
    def staticField(name: String, idx: Int, initial: Bits): Bits = {
      addField(name, idx to idx, Type.NoWrite, Some(initial))
    }
    def staticField(name: String, idx: Int): Bits = {
      addField(name, idx to idx, Type.NoWrite, None)
    }

    def softWrite(next: Bits): Unit = {
      for ((meta, value) <- map) {
        if (meta.ty == Type.SoftWrite) {
          value := next(meta.range)
        }
      }
    }

    def hardWrite(next: Bits): Unit = {
      for ((meta, value) <- map) {
        if (meta.ty != Type.NoWrite) {
          value := next(meta.range)
        }
      }
    }

    def apply(fieldName: String): Bits = {
      for ((meta, value) <- map) {
        if (meta.name == fieldName) return value
      }
      assert(false, s"Not find ${fieldName}")
      B(0)
    }

    def value: Bits = {
      val res = map.toList.sortBy(_._1.range.min).foldRight(B(0, 0 bits)) { case (x, y) =>
        y ## B(0, 31 - x._1.range.max - y.getBitsWidth bits) ## x._2
      }
      res ## B(0, 32 - res.getBitsWidth bits)
    }
  }

  class BadVaddr extends RegisterBase(8, 0) {
    val badVaddr = hardField("BadVaddr", 31 downto 0)
  }
  class Count extends RegisterBase(9, 0) {
    val count = field("Count", 31 downto 0, B"32'h0")
  }
  class Comparae extends RegisterBase(11, 0) {
    val compare = field("Compare", 31 downto 0, B"32'hffff_ffff")
  }
  class Status extends RegisterBase(12, 0) {
    val cu0 = field("CU0", 28, B"0")
    val bev = field("Bev", 22, B"1")
    val im  = field("IM", 15 downto 8, B"8'hff")
    val um  = field("UM", 4, B"0")
    val erl = field("ERL", 2, B"0") // TODO 手册上要求初始化为1，但是许多地方包括nontrivials也初始化为0
    val exl = field("EXL", 1, B"0")
    val ie  = field("IE", 0, B"0")
  }
  class Cause extends RegisterBase(13, 0) {
    val bd      = hardField("BD", 31)
    val ti      = staticField("TI", 30, B"0")
    val ce      = hardField("CE", 29 downto 28, B"00")  //TODO 如果有其他的COP需要修改
    val iv      = field("IV", 23, B"0")
    val ipHW    = staticField("IP_HW", 15 downto 10).setAsReg() //跨时钟域的问题，需要寄存下来
    val ipSW    = field("IP_SW", 9 downto 8, B"00")
    val excCode = hardField("ExcCode", 6 downto 2)
  }
  class EPC extends RegisterBase(14, 0) {
    val epc = field("EPC", 31 downto 0)
  }
  class EBase extends RegisterBase(15, 1) {
    val _             = staticField("31", 31, B"1")
    val exceptionBase = field("ExceptionBase", 29 downto 12, B"18'h0")
    val cpuNum        = staticField("CPUNum", 9 downto 0, B"10'h0")
  }
  class Config0 extends RegisterBase(16, 0) {
    val M  = staticField("M", 31, B"1")
    val MT = staticField("MT", 9 downto 7, B"001")
    val K0 = field("K0", 2 downto 0, B"011")
  }
  class Config1 extends RegisterBase(16, 1) {
    val mmuSize = staticField("MMUSize", 30 downto 25, B(ConstantVal.TLBEntryNum, 6 bits))
    val IS      = staticField("IS", 24 downto 22, B(log2Up(ConstantVal.IcacheSetsPerWay) - 6, 3 bits))
    val IL      = staticField("IL", 21 downto 19, B(log2Up(ConstantVal.IcacheLineSize) - 1, 3 bits))
    val IA      = staticField("IA", 18 downto 16, B(ConstantVal.IcacheWayNum - 1, 3 bits))
    val DS      = staticField("DS", 15 downto 13, B(log2Up(ConstantVal.DcacheSetsPerWay) - 6, 3 bits))
    val DL      = staticField("DL", 12 downto 10, B(log2Up(ConstantVal.DcacheLineSize) - 1, 3 bits))
    val DA      = staticField("DA", 9 downto 7, B(ConstantVal.DcacheWayNum - 1, 3 bits))
  }
  class ErrorEPC extends RegisterBase(30, 0) {
    val errorEPC = field("ErrorEPC", 31 downto 0, B"32'h0")
  }
  class EntryLo0 extends RegisterBase(2, 0) {
    val pfn = field("PFN", (ConstantVal.PABITS - 7) downto 6)
    val C   = field("C", 5 downto 3)
    val D   = field("D", 2)
    val V   = field("V", 1)
    val G   = field("G", 0)
  }
  class EntryLo1 extends RegisterBase(3, 0) {
    val pfn = field("PFN", (ConstantVal.PABITS - 7) downto 6)
    val C   = field("C", 5 downto 3)
    val D   = field("D", 2)
    val V   = field("V", 1)
    val G   = field("G", 0)
  }
  class Index extends RegisterBase(0, 0) {
    val P     = hardField("P", 31, B"0")
    val index = field("Index", 0 until TLBIndexWidth)
  }
  class EntryHi extends RegisterBase(10, 0) {
    val vpn2 = field("VPN2", 31 downto 13)
    val asid = field("ASID", 7 downto 0)
  }
  class PageMask extends RegisterBase(5, 0) {
    val mask = staticField("Mask", 28 downto 13, B(0, TLBConfig.maskWidth bits))
  }
  class Random extends RegisterBase(1, 0) {
    val random =
      hardField("Random", 0 until TLBIndexWidth, B(ConstantVal.TLBEntryNum - 1, TLBIndexWidth bits))
  }
  class Wired extends RegisterBase(6, 0) {
    val wired = field("Wired", 0 until TLBIndexWidth, B(0, TLBIndexWidth bits))
  }
  class Context extends RegisterBase(4, 0) {
    val pteBase = field("PTEBase", 31 downto 23)
    val badVPN2 = hardField("BadVPN2", 22 downto 4)
  }

  class Cp0Reg extends Bundle {
    val badVaddr = new BadVaddr
    val count    = new Count
    val compare  = new Comparae
    val status   = new Status
    val cause    = new Cause
    val epc      = new EPC
    val eBase    = new EBase
    val config0  = new Config0
    val config1  = new Config1
    val errorEPC = new ErrorEPC

    val entryLo0 = new EntryLo0
    val entryLo1 = new EntryLo1
    val index    = new Index
    val entryHi  = new EntryHi
    val pageMask = new PageMask
    val random   = new Random
    val wired    = new Wired
    val context  = new Context
  }
}

/** 从CP0中读和TLB相关值的接口 */
class TLBInterface extends Bundle with IMasterSlave {
  val TLBIndexWidth: Int = log2Up(ConstantVal.TLBEntryNum)

  /** index is used for TLBWI and TLBWR, write to MMU */
  val index     = UInt(TLBIndexWidth bits) //读出的index寄存器值
  val random    = UInt(TLBIndexWidth bits) //读出的random寄存器值
  val tlbwEntry = new TLBEntry             //从CP0读出要写入tlb的内容

  /** following is used for TLBR, write to CP0 */
  val tlbr      = Bool //是否是TLBR而修改CP0
  val tlbrEntry = new TLBEntry

  /** following is used for tlbp to write Index Reg */
  val tlbp       = Bool
  val probeIndex = Bits(32 bits)

  /** following is used for random register to update (round robin) */
  val tlbwr = Bool

  override def asMaster(): Unit = {
    in(index, random, tlbwEntry)
    out(tlbrEntry, tlbr, tlbp, tlbwr, probeIndex)
  }
}

/** 从CP0返回当前机器状态的信息 */
class MachineStateBus extends Bundle with IMasterSlave {
  val userMode = Bool
  val cu0      = Bool
  val ERL      = Bool
  val K0       = Bits(3 bits)

  override def asMaster(): Unit = {
    in(userMode, cu0, ERL, K0)
  }
}

class CP0 extends Component {

  import CP0._

  val io = new Bundle {
    val softwareWrite = slave Flow (Write())
    val read          = slave(Read())

    val tlbBus       = Utils.instantiateWhen(slave(new TLBInterface), ConstantVal.FINAL_MODE)
    val machineState = slave(new MachineStateBus)

    val externalInterrupt = in Bits (5 bits)
    val exceptionBus      = slave(new ExceptionBus)
  }

  // Register constructions, read and write.
  val regs = new Cp0Reg

  io.read.data := 0
  for (reg <- regs.elements.map(x => x._2.asInstanceOf[RegisterBase])) {
    when(io.read.addr === reg.addr) {
      io.read.data := reg.value
    }
    when(io.softwareWrite.valid && io.softwareWrite.addr === reg.addr) {
      reg.softWrite(io.softwareWrite.data)
    }
  }
  //reset random when write Wired Reg
  when(io.softwareWrite.valid & io.softwareWrite.addr === (6, 0)) {
    regs.random.random := B(ConstantVal.TLBEntryNum - 1, log2Up(ConstantVal.TLBEntryNum) bits)
  }

  val increaseCount = Reg(Bool) init False
  increaseCount := !increaseCount
  val count = regs.count.count
  when(increaseCount) {
    count := (count.asUInt + 1).asBits
  }

  regs.cause.ipHW(0, 5 bits) := io.externalInterrupt
  if (ConstantVal.TimeInterruptEnable) {
    regs.cause.ipHW(5) := count === regs.compare.compare
  } else {
    regs.cause.ipHW(5) := False
  }
  val tlbHandler       = if(ConstantVal.FINAL_MODE) new TLBHandler(regs, io.tlbBus) else null
  val exceptionHandler = new ExceptionHandler(regs, io.exceptionBus)
  //其他的信息
  io.machineState.ERL := regs.status.erl.asBool
  io.machineState.K0 := regs.config0.K0
  // 目前仅实现了Kernel和User Mode
  io.machineState.userMode := regs.status.um === 1 & regs.status.erl === 0 & regs.status.exl === 0
  io.machineState.cu0 := regs.status.cu0.asBool
}

class ExceptionHandler(val regs: Cp0Reg, val exceptBus: ExceptionBus) extends Area {
  val jumpPc    = exceptBus.jumpPc
  val epc       = regs.epc.epc
  val errorEpc  = regs.errorEPC.errorEPC
  val exl       = regs.status.exl
  val erl       = regs.status.erl
  val excCode   = exceptBus.exception.excCode
  val instFetch = exceptBus.exception.instFetch
  // Exception
  excCode.whenIsDefined { exc =>
    when(!exl.asBool) {
      exl := True.asBits
      when(exceptBus.bd) {
        epc := (exceptBus.pc - 4).asBits
        errorEpc := (exceptBus.pc - 4).asBits
      } otherwise {
        epc := exceptBus.pc.asBits
        errorEpc := exceptBus.pc.asBits
      }
      regs.cause.bd := exceptBus.bd.asBits
      regs.cause.excCode := exc.asBits.resized

      when(ExcCode.addrRelated(exc) | ExcCode.tlbRelated(exc)) {
        regs.badVaddr.badVaddr := (instFetch
          ? exceptBus.pc
          | exceptBus.vaddr).asBits
      }
      when(ExcCode.tlbRelated(exc)) {
        regs.entryHi.vpn2 := (instFetch
          ? exceptBus.pc
          | exceptBus.vaddr)(31 downto 13).asBits

        regs.context.badVPN2 := (instFetch
          ? exceptBus.pc
          | exceptBus.vaddr)(31 downto 13).asBits
      }
    }
  }
  // Interrupt
  val interrupts = (regs.cause.ipHW ## regs.cause.ipSW) & regs.status.im
  //Status.EXL = 1 | Status.ERL = 1 会禁止所有中断
  val intTaken   = interrupts.orR && !exl.asBool & !erl.asBool & regs.status.ie.asBool

  when(intTaken) {
    exl := True.asBits
    when(exceptBus.bd) {
      epc := (exceptBus.pc - 4).asBits
      errorEpc := (exceptBus.pc - 4).asBits
    } otherwise {
      epc := exceptBus.pc.asBits
      errorEpc := exceptBus.pc.asBits
    }
    regs.cause.bd := exceptBus.bd.asBits
    regs.cause.excCode := ExcCode.Int
  }

  // 计算发生异常和中断时的跳转地址
  jumpPc := None
  // TODO 实现Cache Error后需要增加Base的可能
  val vectorBaseAddr: UInt = regs.status.bev.asBool ? U"32'hBFC0_0200" |
    U"2'b10" @@ regs.eBase.exceptionBase.asUInt @@ U"12'b0"
  //当异常或中断发生时，默认是偏移180
  val offset = U"10'h180"
  excCode.whenIsDefined { exc =>
    // TLB Refill并且Status.EXL为0时offset为0x0
    when(
      Utils.equalAny(exc, ExcCode.loadTLBError, ExcCode.storeTLBError) &
        exceptBus.exception.tlbRefill & !exl.asBool
    ) {
      offset := U"10'h0"
    }
    jumpPc := vectorBaseAddr + offset
  }

  // 中断
  when(intTaken) {
    // Cause.IV用来配置Interrupt Vector
    // Cause.IV = 0时offset为默认的0x180
    // Casue.IV = 1时offset为0x200
    when(regs.cause.iv.asBool) {
      offset := U"10'h200"
    }
    jumpPc := vectorBaseAddr + offset
  }

  // ERET
  when(exceptBus.eret) {
    when(erl.asBool) {
      jumpPc := errorEpc.asUInt
      erl := False.asBits
    }.otherwise {
      jumpPc := epc.asUInt
      exl := False.asBits
    }
  }
}

class TLBHandler(val regs: Cp0Reg, val tlbBus: TLBInterface) extends Area {
  // TLB related logic and register
  val entryHi  = regs.entryHi
  val entryLo0 = regs.entryLo0
  val entryLo1 = regs.entryLo1
  val index    = regs.index
  val random   = regs.random
  when(tlbBus.tlbwr) {
    random.random := B(random.random.asUInt + 1)
  }
  //tlbBus signal
  tlbBus.index := index("Index").asUInt
  tlbBus.random := random("Random").asUInt
  //tlbp
  when(tlbBus.tlbp) {
    index.hardWrite(tlbBus.probeIndex)
  }
  //read from cp0
  tlbBus.tlbwEntry.vpn2 := entryHi.vpn2 //TLB模块会自己做Mask处理
  tlbBus.tlbwEntry.asid := entryHi.asid
  tlbBus.tlbwEntry.G := (entryLo0.G & entryLo1.G).asBool
  tlbBus.tlbwEntry.pfn1 := entryLo1.pfn
  tlbBus.tlbwEntry.C1 := entryLo1.C
  tlbBus.tlbwEntry.D1 := entryLo1.D.asBool
  tlbBus.tlbwEntry.V1 := entryLo1.V.asBool
  tlbBus.tlbwEntry.pfn0 := entryLo0.pfn
  tlbBus.tlbwEntry.C0 := entryLo0.C
  tlbBus.tlbwEntry.D0 := entryLo0.D.asBool
  tlbBus.tlbwEntry.V0 := entryLo0.V.asBool
  tlbBus.tlbwEntry.mask := regs.pageMask.mask
  //write to cp0 for TLBR
  when(tlbBus.tlbr) {
    // TODO Becasue current only support 4KiB page, so cannot modify PageMask's Mask Field
    val vpnMask = ~tlbBus.tlbrEntry.mask.resize(TLBConfig.vpn2Width)
    val pfnMask = ~tlbBus.tlbrEntry.mask.resize(TLBConfig.pfnWidth)
    entryHi.vpn2 := tlbBus.tlbrEntry.vpn2 & vpnMask
    entryHi.asid := tlbBus.tlbrEntry.asid

    entryLo0.pfn := tlbBus.tlbrEntry.pfn0 & pfnMask
    entryLo0.C := tlbBus.tlbrEntry.C0
    entryLo0.D := tlbBus.tlbrEntry.D0.asBits
    entryLo0.V := tlbBus.tlbrEntry.V0.asBits
    entryLo0.G := tlbBus.tlbrEntry.G.asBits

    entryLo1.pfn := tlbBus.tlbrEntry.pfn1 & pfnMask
    entryLo1.C := tlbBus.tlbrEntry.C1
    entryLo1.D := tlbBus.tlbrEntry.D1.asBits
    entryLo1.V := tlbBus.tlbrEntry.V1.asBits
    entryLo1.G := tlbBus.tlbrEntry.G.asBits
  }
}


//object AddrImplicits {
//  implicit class InstHasAddr(inst: Mips32Inst) {
//    def addr = signalCache(inst, "addr") {
//      val addr = Addr()
//      addr.rd := inst.rd
//      addr.sel := inst.sel
//      addr
//    }
//  }
//
//  implicit class BitsHasAddr(bits: Bits) {
//
//    import defs.Mips32InstImplicits._
//
//    def addr = new InstHasAddr(bits).addr
//  }
//}
