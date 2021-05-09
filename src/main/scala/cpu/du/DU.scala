package cpu.du

import cpu.defs.ConstantVal._
import spinal.core._

class DU extends Component {
  /// 解码指令

  // in
  val inst = in Bits (32 bits)

  // out
  val rs = out UInt (5 bits)
  val rt = out UInt (5 bits)
  val rd = out UInt (5 bits)
  val sa = out UInt (5 bits)
  val imm = out UInt (32 bits)
  val offset = out SInt (16 bits)
  val index = out UInt (26 bits)

  val alu_op = out(ALU_OP)
  val alu_a_src = out(ALU_A_SRC)
  val alu_b_src = out(ALU_B_SRC)

  val dcu_re = out Bool
  val dcu_we = out Bool
  val dcu_be = out UInt (2 bits)
  val mu_ex = out(MU_EX)

  val rfu_we = out Bool
  val rfu_rd = out UInt (5 bits)
  val rfu_rd_src = out(RFU_RD_SRC)

  val ju_op = out(JU_OP)
  val ju_pc_src = out(JU_PC_SRC)

  val use_rs = out Bool
  val use_rt = out Bool

  // wires
  val op = inst(31 downto 26)
  val fn = inst(5 downto 0)

  //
  rs := U(inst(25 downto 21))
  rt := U(inst(20 downto 16))
  rd := U(inst(15 downto 11))
  sa := U(inst(10 downto 6))
  imm := op(2) ? U(inst(15 downto 0), 32 bits) | U(S(inst(15 downto 0), 32 bits))
  offset := S(inst(15 downto 0))
  index := U(inst(25 downto 0))

  alu_op.assignFromBits(op(3) ? op(2 downto 0).mux(B"010" -> B"1110", B"011" -> B"1111", B"111" -> B"1001", default -> B"0" ## op(2 downto 0)) | (fn(5) ## fn(3)).mux(B"00" -> B"10" ## fn(1 downto 0), B"01" -> B"110" ## fn(1), B"10" -> B"0" ## fn(2 downto 0), B"11" -> B"11" ## fn(1 downto 0)))
  alu_a_src.assignFromBits(B(op(3) ## fn(5 downto 4) ## fn(2) === B"0000"))
  alu_b_src.assignFromBits(B(op(3)))

  dcu_re := op(5) ## op(3) === B"10"
  dcu_we := op(5) ## op(3) === B"11"
  dcu_be := U(op(1 downto 0))
  mu_ex.assignFromBits(B(op(2)))

  rfu_we := rfu_rd =/= 0 & (op(5) ^ op(3) | op(5) ## op(2 downto 0) === B"0011" | (op(5) ## op(3) ## op(2 downto 0) === B"00000" & (!fn(3) | fn(4) | fn(5) | fn(0))) | (op(5) ## op(3) ## op(2 downto 0) === B"00001" & rt(4)))
  rfu_rd := (op(5) ## op(3) === B"00") ? (op(0) ? U"11111" | rd) | rt
  rfu_rd_src.assignFromBits(op(5) ## (op(5) ## op(3) === B"00" & (op(0) | fn(5 downto 3) === B"001")))

  ju_op.assignFromBits((op(5) ## op(3) === B"00") ? op(2 downto 0).mux(B"000" -> ((fn(5 downto 3) === B"001") ? B"011" | B"010"), B"001" -> B"00" ## rt(0), B"010" -> B"011", default -> op(2 downto 0)) | B"010")
  ju_pc_src.assignFromBits(op(2) ? B"01" | op(1 downto 0))

  use_rs := op(5) ## op(3) === B"01" | op(5) ## op(2) === B"01" | op(5) ## op(1 downto 0) === B"001" | op(5) ## op(3 downto 0) === B"00000" & (fn(2) | fn(3) | fn(5))
  use_rt := op(5) ## op(3) ## op(2 downto 1) === B"0010" | op(5) ## op(3) ## op(2 downto 0) === B"00000" & (!fn(3) | fn(4) | fn(5))
}

object DU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DU)
  }
}