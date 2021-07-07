package cpu

import spinal.core._
import spinal.core.sim._
import spinal.lib.bus.amba4.axi.sim.AxiMemorySimConfig
import util.VivadoConf

import scala.collection.mutable

object Simulator {
  case class MemSection(
      startAddr: BigInt,
      data: Array[Byte]
  )

  case class Config(
      mem: AxiMemorySimConfig,
      initSections: Seq[MemSection],
      cpuClockPeriod: Long,
      sysClockPeriod: Long = 10
  )

  class Context(
      val soc: SimulationSoc,
      val memorySim: AxiMemorySim,
      val sysClockDomain: ClockDomain,
      val cpuClockDomain: ClockDomain
  ) {
    def mem       = memorySim.memory
    var openTrace = true
  }

  type Thread = Context => Unit
}

/** 封装的 CPU 仿真器，可配置时钟、内存等，支持添加仿真线程。 */
case class Simulator(config: Simulator.Config) {
  import Simulator._

  val threads = mutable.ArrayBuffer[Thread]()

  def addThread(thread: Thread): this.type = {
    threads += thread
    this
  }

  def run(): Unit = {
    val includeDir = VivadoConf.vivadoPath + "/data/verilog/src/xeclib"
    SimConfig.withWave
      .withConfig(SpinalConfig().includeSimulation)
      .allOptimisation
      .addSimulatorFlag("-Wno-TIMESCALEMOD")
      .addSimulatorFlag("-Wno-INITIALDLY")
      // .addIncludeDir(includeDir)
      .addSimulatorFlag(s"-I${includeDir}")
      .compile(new SimulationSoc)
      .doSimUntilVoid(this.doSim _)
  }

  def doSim(soc: SimulationSoc): Unit = {
    val sysClockDomain = ClockDomain(
      clock = soc.io.sysClock,
      reset = soc.io.reset,
      config = ClockDomainConfig(resetActiveLevel = LOW)
    )
    sysClockDomain.forkStimulus(period = config.sysClockPeriod)

    val cpuClockDomain = ClockDomain(
      clock = soc.io.cpuClock
    )
    cpuClockDomain.forkStimulus(period = config.cpuClockPeriod)

    soc.io.externalInterrupt #= 0

    val memorySim = AxiMemorySim(soc.io.axi, sysClockDomain, config.mem)
    for (MemSection(addr, data) <- config.initSections) {
      memorySim.memory.writeArray(addr.toLong, data)
    }
    memorySim.start()

    for (job <- soc.jobs) {
      fork {
        job()
      }
    }

    val threadInput = new Context(
      soc = soc,
      memorySim = memorySim,
      sysClockDomain = sysClockDomain,
      cpuClockDomain = cpuClockDomain
    )
    for (thread <- threads) {
      fork {
        thread(threadInput)
      }
    }
  }
}
