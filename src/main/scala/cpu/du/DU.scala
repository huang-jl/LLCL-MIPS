package cpu.du

import cpu.defs.ConstantVal._
import spinal.core._
import spinal.lib.OHToUInt

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

  val alu = new Bundle {
    val e = out Bool
  }

  val E = new Bundle {
    val RI, Sys, Bp = out Bool
  }

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

  val is = new Bundle {
    val special = op === B"000000"
    val regimm = op === B"000001"
    val j = op === B"000010"
    val jal = op === B"000011"
    val beq = op === B"000100"
    val bne = op === B"000101"
    val blez = op === B"000110"
    val bgtz = op === B"000111"

    val addi = op === B"001000"
    val addiu = op === B"001001"
    val slti = op === B"001010"
    val sltiu = op === B"001011"
    val andi = op === B"001100"
    val ori = op === B"001101"
    val xori = op === B"001110"
    val lui = op === B"001111"

    val cop0 = op === B"010000"

    val lb = op === B"100000"
    val lh = op === B"100001"
    val lw = op === B"100011"
    val lbu = op === B"100100"
    val lhu = op === B"100101"

    val sb = op === B"101000"
    val sh = op === B"101001"
    val sw = op === B"101011"

    val sll = special & fn === B"000000"
    val srl = special & fn === B"000010"
    val sra = special & fn === B"000011"
    val sllv = special & fn === B"000100"
    val srlv = special & fn === B"000110"
    val srav = special & fn === B"000111"

    val jr = special & fn === B"001000"
    val jalr = special & fn === B"001001"
    val syscall = special & fn === B"001100"
    val break = special & fn === B"001101"

    val mfhi = special & fn === B"010000"
    val mthi = special & fn === B"010001"
    val mflo = special & fn === B"010010"
    val mtlo = special & fn === B"010011"

    val mult = special & fn === B"011000"
    val multu = special & fn === B"011001"
    val div = special & fn === B"011010"
    val divu = special & fn === B"011011"

    val add = special & fn === B"100000"
    val addu = special & fn === B"100001"
    val sub = special & fn === B"100010"
    val subu = special & fn === B"100011"
    val and = special & fn === B"100100"
    val or = special & fn === B"100101"
    val xor = special & fn === B"100110"
    val nor = special & fn === B"100111"

    val slt = special & fn === B"101010"
    val sltu = special & fn === B"101011"

    val bltz = regimm & rt === B"00000"
    val bgez = regimm & rt === B"00001"
    val bltzal = regimm & rt === B"10000"
    val bgezal = regimm & rt === B"10001"

    val mfc0 = cop0 & rs === B"00000"
    val mtc0 = cop0 & rs === B"00100"
    val c0 = cop0 & rs(4)

    val eret = c0 & fn === B"011000"
  }

  alu_op.assignFromBits(B(OHToUInt((is.sltu | is.sltiu) ## (is.slt | is.slti) ## is.divu ## is.div ## is.multu ## is.mult ## (is.sra | is.srav) ## (is.srl | is.srlv) ## is.lui ## (is.sll | is.sllv) ## is.nor ## is.xor ## is.or ## is.and ## is.subu ## is.sub ## (is.addu | is.addiu) ## (is.add | is.addi))))
  alu_a_src.assignFromBits(B(is.sll | is.srl | is.sra))
  alu_b_src.assignFromBits(B(is.special))

  dcu_re := is.lb | is.lh | is.lw | is.lbu | is.lhu
  dcu_we := is.sb | is.sh | is.sw
  dcu_be := U(op(1 downto 0))
  mu_ex.assignFromBits(B(op(2)))

  hlu_hi_we := is.mult | is.multu | is.div | is.divu | is.mthi
  hlu_hi_src.assignFromBits(B(is.mthi))
  hlu_lo_we := is.mult | is.multu | is.div | is.divu | is.mtlo
  hlu_hi_src.assignFromBits(B(is.mtlo))

//  val rfu_we = out Bool
//  val rfu_rd = out UInt (5 bits)
//  val rfu_rd_src = out(RFU_RD_SRC)
  rfu_we := is.special | ;
  rfu_rd :=

  E.Sys := is.syscall
  E.Bp := is.break
}

object DU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DU)
  }
}