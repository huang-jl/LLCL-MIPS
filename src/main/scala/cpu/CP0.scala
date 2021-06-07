package cpu

import defs.Mips32Inst
import spinal.core._
import spinal.lib.{cpu => _, _}

import scala.collection.mutable._

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
      fields: ArrayBuffer[Field.Description] = ArrayBuffer()
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
            case Some(i) => bits := B(i)
            case None => bits := 0
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

    val value = {
      val sorted = description.fields.sortBy(_.range.min)
      if (sorted.isEmpty) {
        B(0, 32 bits)
      } else {
        sorted
          .foldRight(B(0, 0 bits)) { case (field, bits) =>
            this.field(field.name) ## B(
              0,
              (31 - bits.getBitsWidth - field.range.max) bits
            ) ## bits
          }
          .resize(32)
      }
    }

    def softwareWrite(data: Bits) = {
      for (field <- description.fields) {
        if (field.fieldType == Field.Type.ReadWrite) {
          this.field(field.name) := data(field.range)
        }
      }
    }
  }

  object Register {
    def apply(description: RegisterDescription) = new Register(description)

    val allDescriptions = HashMap[(Int, Int), RegisterDescription]()

    def describeRegister(name: String, number: Int, sel: Int) = {
      val description = RegisterDescription(name)
      allDescriptions((number, sel)) = description
      description
    }

    val BadVAddrDesc = describeRegister("BadVAddr", 8, 0)
      .readOnlyField("BadVAddr", 31 downto 0)

    val CountDesc = describeRegister("Count", 9, 0)
      .field("Count", 31 downto 0)

    val StatusDesc = describeRegister("Status", 12, 0)
      .field("Bev", 22, true)
      .field("IM", 15 downto 8)
      .field("EXL", 1, false)
      .field("IE", 0, false)

    val CauseDesc = describeRegister("Cause", 13, 0)
      .readOnlyField("BD", 31)
      .readOnlyField("TI", 30)
      .readOnlyField("IP_HW", 15 downto 10)
      .field("IP_SW", 9 downto 8)
      .readOnlyField("ExcCode", 6 downto 2)

    val EPCDesc = describeRegister("EPC", 14, 0)
      .readOnlyField("EPC", 31 downto 0)
  }
}

case class CP0Addr() extends Bundle {
  val rd  = UInt(5 bits)
  val sel = UInt(3 bits)

  def :=(that: (Int, Int)) = {
    rd := that._1
    sel := that._2
  }

  def ===(that: (Int, Int)) = rd === that._1 && sel === that._2
  def =/=(that: (Int, Int)) = !(this === that)
}

object CP0AddrImplicits {
  implicit class InstHasCP0Addr(inst: Mips32Inst) {
    def addr = signalCache(inst, "addr") {
      val addr = CP0Addr()
      addr.rd := inst.rd
      addr.sel := inst.sel
      addr
    }
  }

  implicit class BitsHasCP0Addr(bits: Bits) {
    import defs.Mips32InstImplicits._
    def addr = new InstHasCP0Addr(bits).addr
  }
}

case class CP0Read() extends Bundle with IMasterSlave {
  val addr = CP0Addr()
  val data = Bits(32 bits)

  override def asMaster() = {
    out(addr)
    in(data)
  }
}

case class CP0Write() extends Bundle {
  val addr = CP0Addr()
  val data = Bits(32 bits)
}

class CP0 extends Component {
  import CP0._

  val io = new Bundle {
    val softwareWrite = slave Flow (CP0Write())
    val read          = slave(CP0Read())
  }

  io.read.data := 0

  for ((addr, description) <- Register.allDescriptions) {
    val cp0Reg = Register(description)
    when(io.read.addr === addr) {
      io.read.data := cp0Reg.value
    }
    when(io.softwareWrite.addr === addr) {
      cp0Reg.softwareWrite(io.softwareWrite.data)
    }
  }
}

case class CP0ExecUnitInput() extends Bundle {
  val addr      = CP0Addr()
  val old_value = Bits(32 bits)
  val new_value = Bits(32 bits)
}

class CP0ExecUnit extends Component {
  import CP0._

  val io = new Bundle {
    val input = in(CP0ExecUnitInput())

    val to_write = out Bits (32 bits)
  }

  io.to_write := io.input.old_value
  for ((addr, description) <- Register.allDescriptions) {
    when(io.input.addr === addr) {
      val softwareWriteMask = description.softwareWriteMask
      io.to_write := (softwareWriteMask & io.input.new_value) |
        (~softwareWriteMask & io.input.old_value)
    }
  }
}
