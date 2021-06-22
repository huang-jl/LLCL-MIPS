package cpu.defs

import spinal.core._

import cpu.Utils._

object Fields {
  val rs     = 25 downto 21
  val rt     = 20 downto 16
  val rd     = 15 downto 11
  val sa     = 10 downto 6
  val imm    = 15 downto 0
  val offset = 15 downto 0
  val index  = 25 downto 0
  val op     = 31 downto 26
  val fn     = 5 downto 0
  val co     = 25
  val sel    = 2 downto 0
}

case class Mips32Inst() extends Bundle {
  val bits = Bits(32 bits)

  def rs          = bits(Fields.rs).asUInt
  def rt          = bits(Fields.rt).asUInt
  def rd          = bits(Fields.rd).asUInt
  def sa          = bits(Fields.sa).asUInt
  def imm         = bits(Fields.imm)
  def immExtended = op(2) ? zeroExtend(imm) | signExtend(imm)
  def offset      = bits(Fields.offset).asSInt
  def index       = bits(Fields.index).asUInt
  def op          = bits(Fields.op)
  def fn          = bits(Fields.fn)
  def co          = bits(Fields.co)
  def sel         = bits(Fields.sel).asUInt
}

object Mips32InstImplicits {
  implicit def bigIntToInst(bigInt: BigInt): Mips32Inst = {
    val inst = Mips32Inst()
    inst.bits := bigInt
    inst
  }

  implicit def bitsToInst(bits: Bits): Mips32Inst = {
    val inst = Mips32Inst()
    inst.bits := bits
    inst
  }

  implicit def instToBits(inst: Mips32Inst): Bits = inst.bits

  implicit def specToMask(spec: InstructionSpec): MaskedLiteral = spec.mask
}

class InstructionSpec(val mask: MaskedLiteral = MaskedLiteral("-" * 32)) {
  assert(mask.width == 32)

  def force(range: Range, value: MaskedLiteral) = {
    val min    = range.min
    val max    = range.max
    val length = max - min + 1
    assert(value.width == length)
    assert((0 until 32) contains min)
    assert((0 until 32) contains max)

    new InstructionSpec(
      new MaskedLiteral(
        value = mask.value | (value.value << min),
        careAbout = mask.careAbout | (value.careAbout << min),
        width = mask.width
      )
    )
  }

  def force(range: Range, value: String): InstructionSpec =
    force(range, MaskedLiteral(value))

  def forceZero(range: Range) =
    force(range, MaskedLiteral("0" * range.length))

  def force(index: Int, value: MaskedLiteral): InstructionSpec =
    force(index to index, value)
  def force(index: Int, value: String): InstructionSpec =
    force(index to index, value)
  def forceZero(index: Int): InstructionSpec =
    forceZero(index to index)
}

object InstructionSpec {
  def apply(mask: MaskedLiteral = MaskedLiteral("-" * 32)) =
    new InstructionSpec(mask)

  private def IType(op: String, rt: String = "-----") =
    InstructionSpec().force(Fields.op, op).force(Fields.rt, rt)

  private def JType(op: String) =
    InstructionSpec().force(Fields.op, op)

  private def RType(op: String, fn: String, sa: String = "00000") =
    InstructionSpec()
      .force(Fields.op, op)
      .force(Fields.fn, fn)
      .force(Fields.sa, sa)

  private def special(fn: String, sa: String = "00000") =
    RType(op = "000000", fn = fn, sa = sa)

  private def regimm(rt: String) = IType(op = "000001", rt = rt)

  private def cop0(rs: String) =
    InstructionSpec()
      .force(Fields.op, "010000")
      .force(Fields.rs, rs)
      .force(10 downto 3, "00000000")

  private def cop0co(fn: String, fillZeros: Boolean = true) = {
    val spec = InstructionSpec()
      .force(Fields.op, "010000")
      .force(Fields.co, "1")
      .force(Fields.fn, fn)
    if (fillZeros) spec.force(24 downto 6, "0" * 19) else spec
  }

  val ADD   = special(fn = "100000")
  val ADDI  = IType(op = "001000")
  val ADDU  = special(fn = "100001")
  val ADDIU = IType(op = "001001")
  val SUB   = special(fn = "100010")
  val SUBU  = special(fn = "100011")
  val SLT   = special(fn = "101010")
  val SLTI  = IType(op = "001010")
  val SLTU  = special(fn = "101011")
  val SLTIU = IType(op = "001011")
  val DIV   = special(fn = "011010").forceZero(Fields.rd)
  val DIVU  = special(fn = "011011").forceZero(Fields.rd)
  val MULT  = special(fn = "011000").forceZero(Fields.rd)
  val MULTU = special(fn = "011001").forceZero(Fields.rd)

  val AND  = special(fn = "100100")
  val ANDI = IType(op = "001100")
  val LUI  = IType(op = "001111").forceZero(Fields.rs)
  val NOR  = special(fn = "100111")
  val OR   = special(fn = "100101")
  val ORI  = IType(op = "001101")
  val XOR  = special(fn = "100110")
  val XORI = IType(op = "001110")

  val SLLV = special(fn = "000100")
  val SLL  = special(fn = "000000", sa = "-----")
  val SRAV = special(fn = "000111")
  val SRA  = special(fn = "000011", sa = "-----")
  val SRLV = special(fn = "000110")
  val SRL  = special(fn = "000010", sa = "-----")

  val BEQ    = IType(op = "000100")
  val BNE    = IType(op = "000101")
  val BGEZ   = regimm(rt = "00001")
  val BGTZ   = IType(op = "000111").forceZero(Fields.rt)
  val BLEZ   = IType(op = "000110").forceZero(Fields.rt)
  val BLTZ   = regimm(rt = "00000")
  val BGEZAL = regimm(rt = "10001")
  val BLTZAL = regimm(rt = "10000")
  val J      = JType(op = "000010")
  val JAL    = JType(op = "000011")
  // sa represents "hint", which can be non-zero, for example in JR.HB
  // Since we probably won't implement JR.HB, we keep sa = 0 here.
  // Similar for JALR.
  val JR = special(fn = "001000")
    .forceZero(Fields.rt)
    .forceZero(Fields.rd)
    .forceZero(Fields.sa)
  val JALR = special(fn = "001001")
    .forceZero(Fields.rt)
    .forceZero(Fields.sa)

  val MFHI = special(fn = "010000").forceZero(Fields.rs).forceZero(Fields.rt)
  val MFLO = special(fn = "010010").forceZero(Fields.rs).forceZero(Fields.rt)
  val MTHI = special(fn = "010001").forceZero(Fields.rt).forceZero(Fields.rd)
  val MTLO = special(fn = "010011").forceZero(Fields.rt).forceZero(Fields.rd)

  val BREAK   = special(fn = "001101")
  val SYSCALL = special(fn = "001100")

  val LB  = IType(op = "100000")
  val LBU = IType(op = "100100")
  val LH  = IType(op = "100001")
  val LHU = IType(op = "100101")
  val LW  = IType(op = "100011")
  val SB  = IType(op = "101000")
  val SH  = IType(op = "101001")
  val SW  = IType(op = "101011")

  val ERET = cop0co(fn = "011000")
  val MFC0 = cop0(rs = "00000")
  val MTC0 = cop0(rs = "00100")
}
