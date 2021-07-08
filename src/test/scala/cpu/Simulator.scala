package cpu

import spinal.core._
import spinal.core.sim._
import spinal.lib.bus.amba4.axi.sim.AxiMemorySimConfig
import util.VivadoConf

import scala.collection.mutable
import scala.reflect.{ClassTag, classTag}

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

  trait Plugin {
    def setupSim(context: Simulator#Context): Unit
    def close(): Unit = {}
  }
}

/** 封装的 CPU 仿真器，可配置时钟、内存等，支持添加插件。 */
case class Simulator(config: Simulator.Config) {
  import Simulator._

  val plugins = mutable.ArrayBuffer[Plugin]()

  def addPlugin(plugin: Plugin): this.type = {
    plugins += plugin
    this
  }

  private def getPluginSeq[T <: Plugin: ClassTag]: Seq[T] = {
    val clazz = classTag[T].runtimeClass.asInstanceOf[Class[T]]

    plugins.filter(p => clazz.isAssignableFrom(p.getClass)).map(_.asInstanceOf[T])
  }

  /** 获取某种特定类型的插件，仅在恰有一个插件满足条件时成功 */
  def getPluginOption[T <: Plugin: ClassTag]: Option[T] = {
    getPluginSeq[T] match {
      case Seq(only) => Some(only)
      case _         => None
    }
  }

  /** 获取某种特定类型的插件，仅在恰有一个插件满足条件时成功 */
  def getPlugin[T <: Plugin: ClassTag]: T = {
    getPluginSeq[T] match {
      case Seq() =>
        throw new AssertionError(s"Plugin of type ${classTag[T].runtimeClass.getName} not loaded.")
      case Seq(only) =>
        only
      case _ =>
        throw new AssertionError(
          s"Reference to plugin of type ${classTag[T].runtimeClass.getName} is ambiguous"
        )
    }
  }

  def onSetupSim(cb: Simulator#Context => Unit) = addPlugin(new Plugin {
    override def setupSim(context: Simulator#Context) = cb(context)
  })

  /** 插件所需要的环境 */
  class Context(
      val soc: SimulationSoc,
      val memorySim: AxiMemorySim,
      val sysClockDomain: ClockDomain,
      val cpuClockDomain: ClockDomain
  ) {
    def mem       = memorySim.memory
    var openTrace = true

    def getPluginOption[T <: Plugin: ClassTag]: Option[T] = Simulator.this.getPluginOption
    def getPlugin[T <: Plugin: ClassTag]: T               = Simulator.this.getPlugin
  }

  def run(dut: => SimulationSoc): Unit = {
    val includeDir = VivadoConf.vivadoPath + "/data/verilog/src/xeclib"

    try {
      SimConfig.withWave
        .withConfig(SpinalConfig().includeSimulation)
        .allOptimisation
        .addSimulatorFlag("-Wno-TIMESCALEMOD")
        .addSimulatorFlag("-Wno-INITIALDLY")
        // .addIncludeDir(includeDir)
        .addSimulatorFlag(s"-I$includeDir")
        .addSimulatorFlag("--timescale-override 1ns/1ns")
        .compile(dut)
        .doSimUntilVoid(doSim _)
    } finally {
      for (plugin <- plugins) {
        plugin.close()
      }
    }
  }

  private def doSim(soc: SimulationSoc): Unit = {
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

    val pluginThreadContext = new Context(
      soc = soc,
      memorySim = memorySim,
      sysClockDomain = sysClockDomain,
      cpuClockDomain = cpuClockDomain
    )
    for (plugin <- plugins) {
      fork {
        plugin.setupSim(pluginThreadContext)
      }
    }
  }
}
