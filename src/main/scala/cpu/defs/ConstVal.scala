package cpu.defs

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._

object ConstantVal {
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
}
