package cpu

import spinal.core._

import defs.ConstantVal._
import Utils._

case class Mips32Inst() extends Bundle {
  val bits = Bits(32 bits)

  def rs = bits(25 downto 21).asUInt
  def rt = bits(20 downto 16).asUInt
  def rd = bits(15 downto 11).asUInt
  def sa = bits(10 downto 6).asUInt
  def imm = bits(15 downto 0)
  def immExtended = op(2) ? zeroExtend(imm) | signExtend(imm)
  def offset = bits(15 downto 0).asSInt
  def index = bits(25 downto 0).asUInt
  def op = bits(31 downto 26)
  def fn = bits(5 downto 0)
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
}

class DU extends Component {
  /// 解码指令

  val io = new Bundle {
    // in
    val inst = in(Mips32Inst())

    // out
    val alu_op = out(ALU_OP)
    val alu_a_src = out(ALU_A_SRC)
    val alu_b_src = out(ALU_B_SRC)

    val dcu_re = out Bool
    val dcu_we = out Bool
    val dcu_be = out UInt (2 bits)
    val mu_ex = out(MU_EX)

    val hlu_hi_we = out Bool
    val hlu_hi_src = out(HLU_SRC)
    val hlu_lo_we = out Bool
    val hlu_lo_src = out(HLU_SRC)

    val rfu_we = out Bool
    val rfu_rd = out UInt (5 bits)
    val rfu_rd_src = out(RFU_RD_SRC)

    val ju_op = out(JU_OP)
    val ju_pc_src = out(JU_PC_SRC)

    val use_rs = out Bool
    val use_rt = out Bool
  }

  // wires
  val inst = io.inst
  val op = inst.op
  val fn = inst.fn

  //
  io.alu_op.assignFromBits(
    op(3) ? op(2 downto 0).mux(
      B"010" -> B"10000",
      B"011" -> B"10001",
      B"111" -> B"01001",
      default -> B"00" ## op(2 downto 0)
    ) | (fn(5) ## fn(3)).mux(
      B"00" -> B"010" ## fn(1 downto 0),
      B"01" -> B"011" ## fn(1 downto 0),
      B"10" -> B"00" ## fn(2 downto 0),
      B"11" -> B"1000" ## fn(0)
    )
  )
  io.alu_a_src.assignFromBits(B(op(3) ## fn(5 downto 4) ## fn(2) === B"0000"))
  io.alu_b_src.assignFromBits(B(op(3)))

  io.dcu_re := op(5) ## op(3) === B"10"
  io.dcu_we := op(5) ## op(3) === B"11"
  io.dcu_be := U(op(1 downto 0))
  io.mu_ex.assignFromBits(B(op(2)))

  io.rfu_we := io.rfu_rd =/= 0 & (op(5) ^ op(3) | !op(3) & !op(2) & (op(1) === op(
    0
  ) | !op(1) & inst.rt(4)))
  io.rfu_rd := (op(5) ^ op(3)) ? inst.rt | (op(0) ? U(31) | inst.rd)
  io.rfu_rd_src.assignFromBits(
    (op(5) ## op(3) ## op(0) === B"000") ? ((fn(
      4 downto 3
    ) === B"10") ? (B"01" ## fn(1)) | B"00" ## (fn(5) ## fn(3) =/= B"01")) | op(
      5 downto 3
    )
  )

  io.ju_op.assignFromBits(
    (op(5) ## op(3) === B"00") ? op(2 downto 0).mux(
      B"000" -> ((fn(5 downto 3) === B"001") ? B"011" | B"010"),
      B"001" -> B"00" ## inst.rt(0),
      B"010" -> B"011",
      default -> op(2 downto 0)
    ) | B"010"
  )
  io.ju_pc_src.assignFromBits(op(2) ? B"01" | op(1 downto 0))

  io.use_rs := op(5) ## op(3) === B"01" | op(5) ## op(2) === B"01" | op(5) ## op(
    1 downto 0
  ) === B"001" | op(5) ## op(1) === B"00" & (fn(2) | fn(3) | fn(5))
  io.use_rt := op(5) ## op(3) ## op(2 downto 1) === B"0010" | op(5) ## op(3) ## op(
    1 downto 0
  ) === B"0000" & (fn(4) === fn(5) | fn(5))

  io.hlu_hi_we := op(5) ## op(3 downto 0) === B"00000" & (fn(
    4 downto 3
  ) === B"11" | fn(4) ## fn(1 downto 0) === B"101")
  io.hlu_hi_src.assignFromBits(B(fn(3)))
  io.hlu_lo_we := op(5) ## op(3 downto 0) === B"00000" & (fn(
    4 downto 3
  ) === B"11" | fn(4) ## fn(1 downto 0) === B"111")
  io.hlu_lo_src.assignFromBits(B(fn(3)))
}

object DU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DU)
  }
}
