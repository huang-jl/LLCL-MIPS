package cpu

import cpu.defs.ConstantVal
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi.Axi4

/** 对 MyCPUTop 的简单封装，使得其适用于仿真 */
class SimulationSoc extends Component {
  val io = new Bundle {
    val sysClock = in Bool
    val cpuClock = in Bool
    val reset    = in Bool

    val externalInterrupt = in Bits (6 bits)

    val axi = master(Axi4(ConstantVal.AXI_BUS_CONFIG))

    val debugInterface = out(DebugInterface())
  }

  val cpu = new MyCPUTop
  cpu.io.ext_int := io.externalInterrupt
  cpu.io.aclk := io.cpuClock
  cpu.io.aresetn := io.reset
  cpu.io.axi >> io.axi
  io.debugInterface := cpu.io.debug
}
