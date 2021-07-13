package cpu.funcTest

import cpu._
import spinal.lib.bus.amba4.axi.sim.AxiMemorySimConfig

import java.io.File
import scala.util.{Failure, Success}

object FuncTestNormal {
  def main(args: Array[String]): Unit = {
    val instRamData = COEParse(new File("official/func_test/soft/func/obj/inst_ram.coe")) match {
      case Success(d) => d
      case Failure(exception) =>
        println(exception.getMessage)
        throw exception
    }

    val simulator = Simulator(
      Simulator.Config(
        mem = AxiMemorySimConfig(
          maxOutstandingReads = 4,
          maxOutstandingWrites = 4,
          readResponseDelay = 200,
          writeResponseDelay = 30,
          interruptProbability = 10,
          interruptMaxDelay = 20
        ),
        initSections = Seq(Simulator.MemSection(0x1fc00000, instRamData)),
        cpuClockPeriod = 10
      )
    )

    funcTest.ConfRegs().addTo(simulator)

    simulator
      .addPlugin(PCBroadcastPlugin(period = 10000))
      .addPlugin(TimeoutPlugin(timeout = 20000000))
      .addPlugin(TerminatorPlugin(0xbfc00100L))
      .addPlugin(WriteBackProducerPlugin())
      .addPlugin(
        WriteBackComparerPlugin(new File("official/func_test/golden_trace.txt"))
      )
      .onSetupSim { context =>
        context.mem.getElseAllocPage(0)
      }
      .run(new SimulationSoc, workspaceName = "FuncTestNormal")
  }
}
