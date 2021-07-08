package cpu

import spinal.core.sim._

import java.io.{BufferedWriter, File, FileWriter}
import scala.collection.mutable
import scala.io.Source

/** 结束仿真的插件。 */
case class TerminatorPlugin(finalPc: Long) extends Simulator.Plugin {
  override def setupSim(context: Simulator#Context) = fork {
    println(f"Simulation will terminate when PC reaches $finalPc%08x.")

    context.sysClockDomain.waitSamplingWhere(
      context.soc.io.debugInterface.wb.pc.toLong == finalPc
    )

    simSuccess()
  }
}

/** 输出 PC 的插件。 */
case class PCBroadcastPlugin(period: Int) extends Simulator.Plugin {
  override def setupSim(context: Simulator#Context) = fork {
    println(s"Broadcasting PC every $period CPU clock periods...")

    def pc = context.soc.io.debugInterface.wb.pc.toLong

    var tick: Long = 0
    while (true) {
      context.sysClockDomain.waitSampling(period)
      tick += period
      println(f"CPU running ($tick cycles), PC = $pc%08x")
    }
  }
}

case class WriteBack(pc: Long, addr: Int, data: Long) {
  def toTrace = f"1 $pc%08x $addr%02x $data%08x\n"
}

case class WriteBackProducerPlugin() extends Simulator.Plugin {
  private val callbacks = mutable.ArrayBuffer[WriteBack => Unit]()

  def addCallback(cb: WriteBack => Unit) = {
    callbacks += cb
  }

  override def setupSim(context: Simulator#Context) = fork {
    val debugWb = context.soc.io.debugInterface.wb

    while (true) {
      context.sysClockDomain.waitSampling()
      context.sysClockDomain.waitEdge(1) // 等待下沿
      if (debugWb.rf.wen.toInt != 0 && context.openTrace) {
        val wb = WriteBack(
          pc = debugWb.pc.toLong,
          addr = debugWb.rf.wnum.toInt,
          data = debugWb.rf.wdata.toLong
        )
        callbacks foreach { _(wb) }
      }
    }
  }
}

/** 将写回数据写到文件中的插件。 */
case class WriteBackFileLoggerPlugin(file: File) extends Simulator.Plugin {
  val writer = new BufferedWriter(new FileWriter(file))

  override def setupSim(context: Simulator#Context) = {
    println(s"Write back data will be logged to ${file.getAbsolutePath}...")

    context.getPlugin[WriteBackProducerPlugin].addCallback { wb =>
      writer.write(wb.toTrace)
    }
  }

  override def close() = writer.close()
}

/** 将写回数据和给定数据比较的插件。 */
case class WriteBackComparerPlugin(file: File, failOnError: Boolean = true)
    extends Simulator.Plugin {
  val traceRegex = raw"1 (\p{XDigit}{8}) (\p{XDigit}{2}) (\p{XDigit}{8})".r

  val source = Source.fromFile(file)

  val lines      = source.getLines.filter(_(0) != '0')
  var errorCount = 0

  override def setupSim(context: Simulator#Context) = {
    println(s"Write back data will be compared against ${file.getAbsolutePath}...")

    context.getPlugin[WriteBackProducerPlugin].addCallback { wb =>
      if (errorCount >= 30) {
        // Do nothing
      } else if (!lines.hasNext) {
        println("=" * 40)
        println("Extra write back detected: ")
        println(wb.toTrace)
        println("=" * 40)
        if (failOnError) simFailure("Extra write back detected.")
        errorCount += 1
      } else {
        lines.next match {
          case line @ traceRegex(pc, addr, data) =>
            if (
              BigInt(pc, 16) != wb.pc || BigInt(addr, 16) != wb.addr || BigInt(
                data,
                16
              ) != wb.data
            ) {
              println("=" * 40)
              println("Wrong write back detected: ")
              println("  Golden trace: ")
              println(line)
              println("  Simulation: ")
              println(wb.toTrace)
              println("=" * 40)
              if (failOnError) simFailure("Wrong write back detected.")
              errorCount += 1
            }
          case line =>
            println("Invalid golden trace: ")
            println(line)
            if (failOnError) simFailure(s"Invalid golden trace.")
            errorCount += 1
        }
      }
    }
  }

  override def close() = source.close()
}

case class TimeoutPlugin(timeout: Long) extends Simulator.Plugin {
  override def setupSim(context: Simulator#Context) = SimTimeout(timeout)
}
