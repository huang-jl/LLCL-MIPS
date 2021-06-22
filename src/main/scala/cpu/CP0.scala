package cpu

import defs.Mips32Inst
import spinal.core._
import spinal.lib.{cpu => _, _}
import lib.{Optional, Updating}

import scala.collection.mutable

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
    setWeakName(description.name)

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
            case None    => bits init 0
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
  }

  case class Addr() extends Bundle {
    val rd  = UInt(5 bits)
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
    val eret      = Bool
    val memAddr   = UInt(32 bits)
    val pc        = UInt(32 bits)
    val bd        = Bool
  }
}

class CP0 extends Component {
  import CP0._

  val io = new Bundle {
    val softwareWrite = slave Flow (Write())
    val read          = slave(Read())

    val externalInterrupt = in Bits (6 bits)
    val exceptionInput    = in(ExceptionInput())
    val jumpPc            = out(Optional(UInt(32 bits)))

    val instStarting        = in Bool
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

      when(exc === EXCEPTION.AdEL || exc === EXCEPTION.AdES) {
        val isFromInstruction =
          io.exceptionInput.pc(1) || io.exceptionInput.pc(0)
        regs("BadVAddr")("BadVAddr") :=
          (isFromInstruction
            ? io.exceptionInput.pc
            | io.exceptionInput.memAddr).asBits
      }
    }
  }

  // ERET
  when(io.exceptionInput.eret) {
    io.jumpPc := epc.asUInt
    exl := False.asBits
  }

  // Interrupt
  val interrupts          = regs("Cause")("IP_HW") ## regs("Cause").next("IP_SW")
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
