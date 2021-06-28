package cpu

import defs.Mips32Inst
import defs.ConstantVal
import tlb.{TLBEntry, TLBConfig}
import spinal.core._
import spinal.lib.{cpu => _, _}
import lib.{Optional, Updating}

import scala.collection.mutable


/**
 * TLBR, TLBWI: 来源是Index寄存器
 * TLBWR: 来源是Random寄存器
 * */
object TLBIndexSrc extends SpinalEnum {
  val Index, Random = newElement()
}

/**
 * 从CP0中读和TLB相关值的接口
 * */
class TLBInterface extends Bundle with IMasterSlave {
  val TLBIndexWidth: Int = log2Up(ConstantVal.TLBEntryNum)

  /** index is used for TLBWI and TLBWR, write to MMU */
  val index = UInt(TLBIndexWidth bits) //读出的index寄存器值
  val random = UInt(TLBIndexWidth bits) //读出的random寄存器值
  val tlbwEntry = new TLBEntry //从CP0读出要写入tlb的内容

  /** following is used for TLBR, write to CP0 */
  val tlbr = Bool //是否是TLBR而修改CP0
  val tlbrEntry = new TLBEntry

  /** following is used for tlbp to write Index Reg */
  val tlbp = Bool
  val probeIndex = Bits(32 bits)

  override def asMaster(): Unit = {
    in(index, random, tlbwEntry)
    out(tlbrEntry, tlbr, tlbp, probeIndex)
  }
}


object CP0 {
  object Field {
    object Type extends Enumeration {
      val ReadWrite, ReadOnly, Hardwired = Value
    }

    case class Description(
                            name: String,
                            fieldType: Type.Value,
                            range: Range,
                            init: Option[String]
                          ) {
      assert(0 <= range.min && range.max < 32)
      assert(range.step == 1 || range.step == -1)

      for (s <- init) {
        assert(s.length == range.length)
        assert(s.forall("01" contains _))
      }
    }
  }

  case class RegisterDescription(
                                  name: String,
                                  addr: (Int, Int),
                                  fields: mutable.ArrayBuffer[Field.Description] = mutable.ArrayBuffer()
                                ) {
    private def addField(field: Field.Description) = {
      assert(fields.forall({ other =>
        other.name != field.name && (other.range.max < field.range.min || field.range.max < other.range.min)
      }))
      fields += field
      this
    }

    private def regField(
                          name: String,
                          fieldType: Field.Type.Value,
                          range: Range,
                          init: Option[String]
                        ) = addField(Field.Description(name, fieldType, range, init))

    def field(name: String, range: Range, init: String) =
      regField(name, Field.Type.ReadWrite, range, Some(init))

    def field(name: String, range: Range) =
      regField(name, Field.Type.ReadWrite, range, None)

    def readOnlyField(name: String, range: Range, init: String) =
      regField(name, Field.Type.ReadOnly, range, Some(init))

    def readOnlyField(name: String, range: Range) =
      regField(name, Field.Type.ReadOnly, range, None)

    def hardwiredField(name: String, range: Range, init: String) =
      addField(Field.Description(name, Field.Type.Hardwired, range, Some(init)))

    def hardwiredField(name: String, range: Range) =
      addField(Field.Description(name, Field.Type.Hardwired, range, None))

    def field(name: String, index: Int, init: Boolean): RegisterDescription =
      field(name, index to index, if (init) "1" else "0")

    def field(name: String, index: Int): RegisterDescription =
      field(name, index to index)

    def readOnlyField(
                       name: String,
                       index: Int,
                       init: Boolean
                     ): RegisterDescription =
      readOnlyField(name, index to index, if (init) "1" else "0")

    def readOnlyField(name: String, index: Int): RegisterDescription =
      readOnlyField(name, index to index)

    def hardwiredField(
                        name: String,
                        index: Int,
                        init: Boolean
                      ): RegisterDescription =
      hardwiredField(name, index to index, if (init) "1" else "0")

    def hardwiredField(name: String, index: Int): RegisterDescription =
      hardwiredField(name, index to index)

    def softwareWriteMask = B(
      (0 until 32)
        .map(index =>
          if (
            fields.exists(field =>
              (field.fieldType == Field.Type.ReadWrite) && (field.range contains index)
            )
          ) '1'
          else '0'
        )
        .mkString
    )
  }

  class Register(val description: RegisterDescription) extends Bundle {
    //    setWeakName(description.name)
    setName(description.name)

    val field = Map(
      (for (field <- description.fields) yield (field.name -> {
        val length = field.range.length
        if (field.fieldType == Field.Type.Hardwired) {
          val bits = Bits(length bits).setCompositeName(this, field.name)
          field.init match {
            case Some(i) =>
              bits := B(i)
              bits.allowOverride
            case None =>
          }
          bits
        } else {
          val bits = Reg(Bits(length bits)).setCompositeName(this, field.name)
          field.init match {
            case Some(i) => bits init B(i)
            case None => bits init 0
          }
          bits
        }
      })): _*
    )

    val next = Map(
      (for (field <- description.fields) yield (field.name -> {
        val length = field.range.length
        if (field.fieldType == Field.Type.Hardwired) {
          this.field(field.name)
        } else {
          val bits =
            Bits(length bits).setCompositeName(this, field.name + "_next")
          bits := this.field(field.name)
          this.field(field.name) := bits
          bits.allowOverride
        }
      })): _*
    )

    val value = {
      val sorted = description.fields.sortBy(_.range.min)

      val concatenated = sorted
        .foldRight(B(0, 0 bits)) {
          case (field, bits) => {
            bits ##
              B(0, (31 - widthOf(bits) - field.range.max) bits) ##
              this.field(field.name)
          }
        }
      concatenated ## B(0, (32 - widthOf(concatenated)) bits)
    }

    def softwareWrite(data: Bits) = {
      for (field <- description.fields) {
        if (field.fieldType == Field.Type.ReadWrite) {
          this.next(field.name) := data(field.range)
        }
      }
    }

    /**
     * @param data 要写的各个域的值，按照起始bit的降序（从高到低）排列
     * @note 这个方法用于硬件自己写，但是只能写Hardwired和ReadWrite
     * */
    def write(data: Seq[Bits]): Unit = {
      for ((field, index) <- description.fields.sortBy(x => x.range.max).reverse.zipWithIndex) {
        this.next(field.name) := data(index)
      }
    }

    /**
     * @param data 要写的各个域的值，按照起始bit的降序（从高到低）排列
     * @note 这个方法用于硬件自己写，但是只能写Readonly和ReadWrite
     * */
    def write(data: Bits): Unit = {
      for (field <- description.fields) {
        if (field.fieldType != Field.Type.Hardwired) {
          this.next(field.name) := data(field.range)
        }
      }
    }

    def apply(name: String) = field.apply(name)
  }

  object Register {
    def apply(description: RegisterDescription) = new Register(description)

    val allDescriptions = mutable.HashMap[(Int, Int), RegisterDescription]()

    def describeRegister(name: String, number: Int, sel: Int) = {
      val description = RegisterDescription(name, (number, sel))
      allDescriptions((number, sel)) = description
      description
    }

    describeRegister("BadVAddr", 8, 0)
      .readOnlyField("BadVAddr", 31 downto 0)

    describeRegister("Count", 9, 0)
      .field("Count", 31 downto 0)

    describeRegister("Status", 12, 0)
      .field("Bev", 22, true)
      .field("IM", 15 downto 8)
      .field("EXL", 1, false)
      .field("IE", 0, false)

    describeRegister("Cause", 13, 0)
      .readOnlyField("BD", 31)
      .readOnlyField("TI", 30)
      .hardwiredField("IP_HW", 15 downto 10)
      .field("IP_SW", 9 downto 8)
      .readOnlyField("ExcCode", 6 downto 2)

    describeRegister("EPC", 14, 0)
      .field("EPC", 31 downto 0)

    //CP0 Reg for TLB-based MMU
    if (ConstantVal.USE_TLB) {
      val TLBIndexWidth: Int = log2Up(ConstantVal.TLBEntryNum)
      describeRegister("EntryLo0", number = 2, sel = 0)
        //31 downto (PABITS - 6) is hardwire to 0
        .field("PFN", (ConstantVal.PABITS - 7) downto 6)
        .field("C", 5 downto 3)
        .field("D", 2)
        .field("V", 1)
        .field("G", 0)

      describeRegister("EntryLo1", number = 3, sel = 0)
        //31 downto (PABITS - 6) is hardwire to 0
        .field("PFN", (ConstantVal.PABITS - 7) downto 6)
        .field("C", 5 downto 3)
        .field("D", 2)
        .field("V", 1)
        .field("G", 0)

      describeRegister("Index", number = 0, sel = 0)
        .readOnlyField("P", 31, init = false)
        //log2Up(TLBConfig.tlbEntryNum) to 30 is hardwired to 0
        .field("Index", 0 until TLBIndexWidth, "0" * TLBIndexWidth)

      describeRegister("EntryHi", number = 10, sel = 0)
        .field("VPN2", 31 downto 13, "0" * TLBConfig.vpn2Width)
        //In Release1 VPN2X(12 downto 11) and (10 downto 8) is hardwired to 0
        .field("ASID", 7 downto 0, "0" * TLBConfig.asidWidth)

      describeRegister("PageMask", number = 5, sel = 0)
        //(31 downto 29) is hardwired to 0
        //1 means do not participate in TLB match, current only support 4KiB page
        .hardwiredField("Mask", 28 downto 13, "0" * TLBConfig.maskWidth)
      //In Release1 MaskX(12 downto 11) is hardwired to 0

      //TODO Random be set with (TLBEntryNum - 1)  when Reset Exception and write wired register
      //Random Range : [Wired, TLBEntryNum - 1]
      describeRegister("Random", number = 1, sel = 0)
        .readOnlyField("Random", 0 until TLBIndexWidth, "1" * TLBIndexWidth)

      //TODO Wired is set to 0 when Reset Exception
      describeRegister("Wired", number = 6, sel = 0)
        .field("Wired", 0 until TLBIndexWidth, "0" * TLBIndexWidth)
    }
  }

  case class Addr() extends Bundle {
    val rd = UInt(5 bits)
    val sel = UInt(3 bits)

    def :=(that: (Int, Int)) = {
      rd := that._1
      sel := that._2
    }

    def ===(that: (Int, Int)) = rd === that._1 && sel === that._2

    def =/=(that: (Int, Int)) = !(this === that)
  }

  object AddrImplicits {
    implicit class InstHasAddr(inst: Mips32Inst) {
      def addr = signalCache(inst, "addr") {
        val addr = Addr()
        addr.rd := inst.rd
        addr.sel := inst.sel
        addr
      }
    }

    implicit class BitsHasAddr(bits: Bits) {

      import defs.Mips32InstImplicits._

      def addr = new InstHasAddr(bits).addr
    }
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

  case class ExceptionInput() extends Bundle {
    val exception = Optional(EXCEPTION())
    val eret = Bool
    val memAddr = UInt(32 bits) //虚地址
    val pc = UInt(32 bits)
    val bd = Bool
    val instFetch = Bool  //是否是取址时的异常
  }

  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CP0)
  }
}

class CP0 extends Component {

  import CP0._

  val io = new Bundle {
    val softwareWrite = slave Flow (Write())
    val read = slave(Read())

    val tlbBus = if (ConstantVal.USE_TLB) slave(new TLBInterface) else null //tlbBus

    val externalInterrupt = in Bits (6 bits)
    val exceptionInput = in(ExceptionInput())
    val jumpPc = out(Optional(UInt(32 bits)))

    val instStarting = in Bool
    val interruptOnNextInst = out Bool
  }

  // Register constructions, read and write.

  io.read.data := 0

  val regs = mutable.Map[String, Register]()
  for ((addr, description) <- Register.allDescriptions) {
    val reg = Register(description)
    regs(description.name) = reg

    when(io.read.addr === addr) {
      io.read.data := reg.value
    }
    when(io.softwareWrite.valid && io.softwareWrite.addr === addr) {
      reg.softwareWrite(io.softwareWrite.data)
    }
  }

  // TLB related logic and register
  if (ConstantVal.USE_TLB) {
    val TLBRelated = new Area {
      val entryHi = regs("EntryHi")
      val entryLo0 = regs("EntryLo0")
      val entryLo1 = regs("EntryLo1")
      val index = regs("Index")
      val random = regs("Random")
      //reset random when write Wired Reg
      when(io.softwareWrite.valid & io.softwareWrite.addr === (6, 0)) {
        random.next("Random") := B(ConstantVal.TLBEntryNum - 1, log2Up(ConstantVal.TLBEntryNum) bits)
      }
      //tlbBus signal
      io.tlbBus.index := index("Index").asUInt
      io.tlbBus.random := random("Random").asUInt
      //tlbp
      when(io.tlbBus.tlbp) {
        index.write(io.tlbBus.probeIndex)
      }
      //read from cp0
      io.tlbBus.tlbwEntry.vpn2 := entryHi("VPN2") //TLB模块会自己做Mask处理
      io.tlbBus.tlbwEntry.asid := entryHi("ASID")
      io.tlbBus.tlbwEntry.G := (entryLo0("G") & entryLo1("G")).asBool
      io.tlbBus.tlbwEntry.pfn1 := entryLo1("PFN")
      io.tlbBus.tlbwEntry.C1 := entryLo1("C")
      io.tlbBus.tlbwEntry.D1 := entryLo1("D").asBool
      io.tlbBus.tlbwEntry.V1 := entryLo1("V").asBool
      io.tlbBus.tlbwEntry.pfn0 := entryLo0("PFN")
      io.tlbBus.tlbwEntry.C0 := entryLo0("C")
      io.tlbBus.tlbwEntry.D0 := entryLo0("D").asBool
      io.tlbBus.tlbwEntry.V0 := entryLo0("V").asBool
      //read from cp0
      io.tlbBus.tlbwEntry.mask := regs("PageMask")("Mask")
      //write to cp0 for TLBR
      when(io.tlbBus.tlbr) {
        // TODO Becasue current only support 4KiB page, so cannot modify PageMask's Mask Field
        // pageMask.write(Array(io.tlbBus.tlbrEntry.mask))
        val vpnMask = ~io.tlbBus.tlbrEntry.mask.resize(TLBConfig.vpn2Width)
        val pfnMask = ~io.tlbBus.tlbrEntry.mask.resize(TLBConfig.pfnWidth)
        entryHi.write(Array(io.tlbBus.tlbrEntry.vpn2 & vpnMask, io.tlbBus.tlbrEntry.asid))
        entryLo0.write(Array(io.tlbBus.tlbrEntry.pfn0 & pfnMask, io.tlbBus.tlbrEntry.C0,
          io.tlbBus.tlbrEntry.D0.asBits, io.tlbBus.tlbrEntry.V0.asBits, io.tlbBus.tlbrEntry.G.asBits))
        entryLo1.write(Array(io.tlbBus.tlbrEntry.pfn1 & pfnMask, io.tlbBus.tlbrEntry.C1,
          io.tlbBus.tlbrEntry.D1.asBits, io.tlbBus.tlbrEntry.V1.asBits, io.tlbBus.tlbrEntry.G.asBits))
      }
    }
  }

  regs("Cause")("IP_HW") := io.externalInterrupt

  val epc = regs("EPC")("EPC")
  val exl = regs("Status")("EXL")

  io.jumpPc := None

  // Exception
  io.exceptionInput.exception.whenIsDefined { exc =>
    io.jumpPc := U"hBFC00380"
    when(!exl.asBool) {
      exl := True.asBits
      when(io.exceptionInput.bd) {
        epc := (io.exceptionInput.pc - 4).asBits
      } otherwise {
        epc := io.exceptionInput.pc.asBits
      }
      regs("Cause")("BD") := io.exceptionInput.bd.asBits
      regs("Cause")("ExcCode") := exc.asBits.resized

      when(Utils.equalAny(exc, EXCEPTION.AdES, EXCEPTION.AdEL, EXCEPTION.TLBL, EXCEPTION.TLBS)) {
        regs("BadVAddr")("BadVAddr") :=
          (io.exceptionInput.instFetch
            ? io.exceptionInput.pc
            | io.exceptionInput.memAddr).asBits

      }
      if(ConstantVal.USE_TLB){
        when(Utils.equalAny(exc, EXCEPTION.TLBS, EXCEPTION.TLBL)) {
          regs("EntryHi")("VPN2") :=
            (io.exceptionInput.instFetch
              ? io.exceptionInput.pc
              | io.exceptionInput.memAddr)(31 downto 13).asBits
        }
      }
//      when(exc === EXCEPTION.AdEL || exc === EXCEPTION.AdES) {
//        val isFromInstruction =
//          io.exceptionInput.pc(1) || io.exceptionInput.pc(0)
//        regs("BadVAddr")("BadVAddr") :=
//          (isFromInstruction
//            ? io.exceptionInput.pc
//            | io.exceptionInput.memAddr).asBits
//      }
    }
  }

  // ERET
  when(io.exceptionInput.eret) {
    io.jumpPc := epc.asUInt
    exl := False.asBits
  }

  // Interrupt
  val interrupts = regs("Cause")("IP_HW") ## regs("Cause").next("IP_SW")
  val interruptOnNextInst = Updating(Bool) init False
  io.interruptOnNextInst := interruptOnNextInst.next

  when(interrupts.orR && !exl.asBool) {
    interruptOnNextInst.next := True
  }
  when(interruptOnNextInst.prev && io.instStarting) {
    interruptOnNextInst.next := False
    io.jumpPc := U"hBFC00380"
    exl := True.asBits
    when(io.exceptionInput.bd) {
      epc := (io.exceptionInput.pc - 4).asBits
    } otherwise {
      epc := io.exceptionInput.pc.asBits
    }
    regs("Cause")("BD") := io.exceptionInput.bd.asBits
    regs("Cause")("ExcCode") := EXCEPTION.Int.asBits.resized
  }
}

// case class CP0ExecUnitInput() extends Bundle {
//   val addr      = CP0.Addr()
//   val old_value = Bits(32 bits)
//   val new_value = Bits(32 bits)
// }

// class CP0ExecUnit extends Component {
//   import CP0._

//   val io = new Bundle {
//     val input = in(CP0ExecUnitInput())

//     val to_write = out Bits (32 bits)
//   }

//   io.to_write := io.input.old_value
//   for ((addr, description) <- Register.allDescriptions) {
//     when(io.input.addr === addr) {
//       val softwareWriteMask = description.softwareWriteMask
//       io.to_write := (softwareWriteMask & io.input.new_value) |
//         (~softwareWriteMask & io.input.old_value)
//     }
//   }
// }
