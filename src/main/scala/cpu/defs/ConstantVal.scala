package cpu.defs

import spinal.core._
import spinal.lib.bus.amba4.axi._
import Mips32InstImplicits._

object ConstantVal {
  def INST_NOP: Mips32Inst = B"00000000000000000000000000100000"
  def INIT_PC = U"hBFC00000"

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
