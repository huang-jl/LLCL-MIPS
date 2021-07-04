package cpu

import spinal.core.sim._
import util.Using

import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source

/** 结束仿真的线程。 */
object TerminatorThread {
  import Simulator._
  def apply(finalPc: Long): Thread = { context =>
    println(f"Simulation will terminate when PC reaches $finalPc%08x.")

    context.sysClockDomain.waitSamplingWhere(
      context.soc.io.debugInterface.wb.pc.toLong == finalPc
    )

    simSuccess()
  }
}

object PCBroadcastThread {
  import Simulator._
  def apply(period: Int): Thread = { context =>
    println(s"Broadcasting PC every $period CPU clock periods")

    def pc = context.soc.io.debugInterface.wb.pc.toLong

    while (true) {
      context.sysClockDomain.waitSampling(period)
      println(f"CPU running, PC = $pc%08x")
    }
  }
}

case class WriteBack(pc: Long, addr: Int, data: Long) {
  def toTrace = f"1 ${pc}%08x ${addr}%02x ${data}%08x\n"
}

object WriteBackProducerThread {
  import Simulator._
  def apply(onWriteBack: WriteBack => Unit): Thread = { context =>
    val debugWb = context.soc.io.debugInterface.wb

    while (true) {
      context.sysClockDomain.waitSampling()
      if (debugWb.rf.wen.toInt != 0) {
        onWriteBack(
          WriteBack(
            pc = debugWb.pc.toLong,
            addr = debugWb.rf.wnum.toInt,
            data = debugWb.rf.wdata.toLong
          )
        )
      }
    }
  }
}

/** 将写回数据写到文件中的仿真线程。 */
object WriteBackFileLoggerThread {
  import Simulator._
  def apply(file: File): Thread = { context =>
    println(s"Write back data will be logged to ${file.getAbsolutePath}...")

    Using(new BufferedWriter(new FileWriter(file))) { writer =>
      def logWriteBack(wb: WriteBack): Unit = {
        writer.write(wb.toTrace)
      }
      WriteBackProducerThread(logWriteBack)(context)
    }
  }
}

/** 将写回数据和给定数据比较的仿真线程 */
object WriteBackComparerThread {
  import Simulator._

  val traceRegex = raw"1 (\p{XDigit}{8}) (\p{XDigit}{2}) (\p{XDigit}{8})".r

  def apply(file: File, failOnError: Boolean = true): Thread = { context =>
    println(s"Write back data will be compared against ${file.getAbsolutePath}...")

    Using(Source.fromFile(file)) { source =>
      val lines      = source.getLines
      var errorCount = 0

      def compareWriteBack(wb: WriteBack): Unit = {
        if (errorCount >= 30) return

        if (!lines.hasNext) {
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
      WriteBackProducerThread(compareWriteBack)(context)
    }
  }
}
