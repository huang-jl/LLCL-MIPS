package cpu.defs

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._

object ConstantVal {
  val INIT_PC = BigInt("BFC00000", 16)

  object ALU_OP extends SpinalEnum {
    val add, addu, sub, subu, and, or, xor, nor, sll, lu, srl, sra, mult, div,
        slt, sltu = newElement()
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
    val lz, gez, f, t, lez, gz, noe, e = newElement()
  }

  object JU_PC_SRC extends SpinalEnum {
    val rs, offset, index = newElement()
  }

  val SRAM_BUS_CONFIG =
    SramBusConfig(dataWidth = 32, addrWidth = 32, selWidth = 2)

  val AXI_BUS_CONFIG = Axi4Config(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 4,
    useRegion = false,
    useQos = false
  )
}
