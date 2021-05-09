package cpu.defs

import spinal.core._

object ConstantVal {
  val INIT_PC = U"32'hbfc0_0000"
  val INST_NOP = BigInt("00000000000000000000000000100000", 2)

  object ALU_OP extends SpinalEnum {
    val add, addu, sub, subu, and, or, xor, nor, sll, lu, srl, sra, mult, div, slt, sltu = newElement()
  }

  object ALU_A_SRC extends SpinalEnum {
    val rs, sa = newElement()
  }

  object ALU_B_SRC extends SpinalEnum {
    val rt, imm = newElement()
  }

  object MU_EX extends SpinalEnum {
    val s, u = newElement()
  }

  object RFU_RD_SRC extends SpinalEnum {
    val alu, pc, mu = newElement()
  }

  object JU_OP extends SpinalEnum {
    val lz, gez, f, t, e, noe, lez, gz = newElement()
  }

  object JU_PC_SRC extends SpinalEnum {
    val rs, offset, index1, index2 = newElement()
  }
}
