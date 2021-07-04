package cpu

import scopt.OptionParser
import spinal.lib.bus.amba4.axi.sim.AxiMemorySimConfig
import spinal.core.sim._
import java.io.File
import scala.util.{Failure, Success}

object Sim {
  case class Config(
      finalPc: Option[Long],
      cpuClockPeriod: Int,
      sysClockPeriod: Int,
      instRamCOE: File,
      writeBackFile: Option[File],
      timeout: Option[Long]
  )

  def main(args: Array[String]): Unit = {
    val parser = new OptionParser[Config]("sim") {
      head("LLCL-Mips simulator")

      opt[String]('f', "final-pc")
        .validate(x =>
          if (x matches raw"\p{XDigit}{8}") success
          else failure("must be 8 digit hex number")
        )
        .action((x, c) => c.copy(finalPc = Some(BigInt(x, radix = 16).toLong)))
        .valueName("<pc>")
        .text("PC where the simulation terminates (format: bfc00000)")

      opt[Int]('c', "cpu-clock")
        .action((x, c) => c.copy(cpuClockPeriod = x))
        .valueName("<length>")
        .text("period of CPU clock in simulation, defaults to 20")

      opt[Int]('s', "sys-clock")
        .action((x, c) => c.copy(sysClockPeriod = x))
        .valueName("<length>")
        .text("period of soc clock in simulation, defaults to 10")

      opt[File]('i', "inst-ram-coe")
        .required()
        .valueName("<file-name>")
        .action((x, c) => c.copy(instRamCOE = x))
        .text(".coe file to initialize inst ram")

      opt[File]('w', "write-back")
        .action((x, c) => c.copy(writeBackFile = Some(x)))
        .valueName("<file-name>")
        .text("file to print write back data")

      opt[Long]('t', "timeout")
        .action((x, c) => c.copy(timeout = Some(x)))
        .valueName("<units>")
        .text("terminate the simulation after such units of time")

      help("help")
        .text("prints help message")
    }

    val parsed = parser.parse(
      args,
      Config(
        finalPc = None,
        cpuClockPeriod = 20,
        sysClockPeriod = 10,
        instRamCOE = null,
        writeBackFile = None,
        timeout = None
      )
    )

    if (parsed.isEmpty) sys.exit(1)
    val config = parsed.get

    val instRamData = COEParse(config.instRamCOE) match {
      case Success(d) => d
      case Failure(exception) =>
        println(exception.getMessage)
        sys.exit(1)
    }

    val simulator = Simulator(
      Simulator.Config(
        mem = AxiMemorySimConfig(
          maxOutstandingReads = 4,
          maxOutstandingWrites = 4,
          readResponseDelay = 25,
          writeResponseDelay = 3,
          interruptProbability = 0,
          interruptMaxDelay = 10
        ),
        initSections = Seq(Simulator.MemSection(0x1fc00000, instRamData)),
        cpuClockPeriod = config.cpuClockPeriod,
        sysClockPeriod = config.sysClockPeriod
      )
    )

    for (finalPc <- config.finalPc) {
      simulator.addThread(TerminatorThread(finalPc))
    }

    for (file <- config.writeBackFile) {
      simulator.addThread(WriteBackFileLoggerThread(file))
    }

    for (timeout <- config.timeout) {
      simulator.addThread(_ => SimTimeout(timeout))
    }

    simulator.run()
  }
}
