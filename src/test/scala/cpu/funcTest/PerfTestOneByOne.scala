package cpu.funcTest

import cpu._
import spinal.lib.bus.amba4.axi.sim.AxiMemorySimConfig

import java.io.File
import scala.util.{Failure, Success}

object PerfTestOneByOne {
  def main(args: Array[String]): Unit = {
    val cpuClockPeriod = 10
    val sysClockPeriod = 10

    val tests = Seq(
      "bitcount",
      "bubble_sort",
      "coremark",
      "crc32",
      "dhrystone",
      "quick_sort",
      "select_sort",
      "sha",
      "stream_copy",
      "stringsearch"
    )

    val socCompiled = Simulator.compile(new SimulationSoc, withWave = false)

    val baseConfig = Simulator.Config(
      mem = AxiMemorySimConfig(
        maxOutstandingReads = 4,
        maxOutstandingWrites = 4,
        readResponseDelay = 250,
        writeResponseDelay = 30
      ),
      cpuClockPeriod = cpuClockPeriod,
      sysClockPeriod = sysClockPeriod
    )

    val results = for (test <- tests) yield {
      println(s"Testing $test")

      val instRamData = COEParse(
        new File(s"official/perf_test/soft/perf_func/obj/$test/axi_ram.coe")
      ) match {
        case Success(d) => d
        case Failure(exception) =>
          println(exception.getMessage)
          throw exception
      }

      val simulator = Simulator(
        baseConfig.copy(initSections = Seq(Simulator.MemSection(0x1fc00000, instRamData)))
      )

      val confRegs = funcTest.ConfRegs()
      confRegs.addTo(
        simulator,
        displayerConfig = Some(display.DisplayerConfig(throttlePeriod = Some(10000)))
      )

      simulator
        .addPlugin(TimeoutPlugin(timeout = 20000000))
        .addPlugin(TerminatorPlugin(0xbfc00100L))
        .onSetupSim { context =>
          context.mem.getElseAllocPage(0)
        }
        .run(socCompiled, name = test)

      (confRegs.led.value == 0xffff, confRegs.num.value)
    }

    val gs132Counts = Seq(0x13cf7fa, 0x7bdd47e, 0x10ce6772, 0xaa1aa5c, 0x1fc00d8, 0x719615a,
      0x6e0009a, 0x74b8b20, 0x853b00, 0x50a1bcc)

    for (((test, gs132Count), (pass, count)) <- (tests zip gs132Counts) zip results) {
      val passOrFail = if (pass) "PASS" else "FAIL"
      println(f"$test%12s: $passOrFail / $count%8x  ($gs132Count%8x)")
    }

    val ratios = (results zip gs132Counts)
      .map { case ((_, count), gs132Count) =>
        if (count != 0)
          gs132Count.toDouble / count.toDouble
        else
          0.1
      }
    val formulaScore = math.pow(ratios.foldLeft(1.0) { _ * _ }, 1.0 / tests.length)
    val loopTimes    = 10
    // 2 since SOC clock updates every cycle, while CPU clock updates every other cycle.
    val estimatedScore = formulaScore / 2 * sysClockPeriod / cpuClockPeriod / loopTimes

    val passAll = results forall { _._1 }

    // scalafmt: { maxColumn = 200 }
    println(if (passAll) "All tests passed!" else "Some test(s) failed!")
    println(f"Estimated score: ${estimatedScore}%3.3f")
    println(f"CPU clock period: ${cpuClockPeriod}%3dns")
    println(f"SOC clock period: ${sysClockPeriod}%3dns")
    println()
    println("NOTE: This program performs perf_test one by one.")
    println("NOTE: Certain problems exists within this method: ")
    println("NOTE: 1. Each test is only run once under simulation. On board, each test is run 10 times. ")
    println("NOTE: 2. Only CPU count is directly produced to confreg (SOC count result only goes through UART)")
    println("NOTE: Thus the score above is estimated with regards to these effects. ")
    // scalafmt: { maxColumn = 100 }
  }
}
