package cpu

import cpu.defs.ConstantVal
import ip.sim.JobCollector
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.lib.bus.amba4.axi._

/** 对 MyCPUTop 的简单封装，使得其适用于仿真 */
class SimulationSoc extends Component with JobCollector {
  val io = new Bundle {
    val sysClock = in Bool
    val cpuClock = in Bool
    val reset    = in Bool

    val externalInterrupt = in Bits (6 bits)

    val axi = master(Axi4(ConstantVal.AXI_BUS_CONFIG))

    val debugInterface = out(DebugInterface())
  }

  val sysClockDomain = ClockDomain(
    clock = io.sysClock,
    reset = io.reset,
    config = ClockDomainConfig(resetActiveLevel = LOW)
  )

  val cpuClockDomain = ClockDomain(
    clock = io.cpuClock,
    reset = io.reset,
    config = ClockDomainConfig(resetActiveLevel = LOW)
  )

  val cpu = new MyCPUTop
  cpu.io.ext_int := io.externalInterrupt
  cpu.io.aclk := io.cpuClock
  cpu.io.aresetn := io.reset
  io.debugInterface := cpu.io.debug

  val clockConverter = new Axi4CC(
    axiConfig = ConstantVal.AXI_BUS_CONFIG,
    inputCd = cpuClockDomain,
    outputCd = sysClockDomain,
    arFifoSize = 4,
    rFifoSize = 4,
    awFifoSize = 4,
    wFifoSize = 4,
    bFifoSize = 4
  )
  cpu.io.axi >> clockConverter.io.input
  clockConverter.io.output >> io.axi

  cpu.io.aclk.pull().simPublic()
  cpu.io.aresetn.pull().simPublic()
}
