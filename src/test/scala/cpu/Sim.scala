package cpu

import scopt.OptionParser
import spinal.lib.bus.amba4.axi.sim.AxiMemorySimConfig

import java.io.File
import scala.util.{Failure, Success}

object Sim {
  case class Config(
      finalPc: Long,
      cpuClockPeriod: Int,
      sysClockPeriod: Int,
      instRamCOE: File,
      writeBackFile: Option[File]
  )

  def main(args: Array[String]): Unit = {
    val parser = new OptionParser[Config]("sim") {
      head("LLCL-Mips simulator")

      opt[String]('f', "final-pc")
        .required()
        .validate(x =>
          if (x matches raw"\p{XDigit}{8}") success
          else failure("must be 8 digit hex number")
        )
        .action((x, c) => c.copy(finalPc = BigInt(x, radix = 16).toLong))
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

      help("help")
        .text("prints help message")
    }

    val parsed = parser.parse(
      args,
      Config(
        finalPc = 0,
        cpuClockPeriod = 20,
        sysClockPeriod = 10,
        instRamCOE = null,
        writeBackFile = None
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
    ).addThread(TerminatorThread(config.finalPc))

    config.writeBackFile match {
      case Some(file) =>
        simulator.addThread(WriteBackFileLoggerThread(file))
      case None =>
    }

    simulator.run()
  }
}
