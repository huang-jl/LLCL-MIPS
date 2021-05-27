package cpu.defs

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._

object ConstantVal {
  val INST_NOP = BigInt("00000000000000000000000000100000", 2)
  val INIT_PC = BigInt("BFC00000", 16)

  val SRAM_BUS_CONFIG =
    SramBusConfig(dataWidth = 32, addrWidth = 32, selWidth = 2)
  val AXI_BUS_CONFIG = Axi4Config(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 4,
    useRegion = false,
    useQos = false
  )

  object ALU_OP extends SpinalEnum {
    val add, addu, sub, subu, and, or, xor, nor, sll, lu, srl, sra, mult, multu, div, divu, slt, sltu = newElement()
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

  object HLU_SRC extends SpinalEnum {
    val rs, alu = newElement()
  }

  object RFU_RD_SRC extends SpinalEnum {
    val pc, alu, hi, lo, mu = newElement()
  }

  object JU_OP extends SpinalEnum {
    val lz, gez, f, t, e, noe, lez, gz = newElement()
  }

  object JU_PC_SRC extends SpinalEnum {
    val rs, offset, index1, index2 = newElement()
  }
}
