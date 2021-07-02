package cpu

import spinal.core.sim._
import util.Using

import java.io.{BufferedWriter, File, FileWriter}

/** 结束仿真的线程。 */
object TerminatorThread {
  import Simulator._
  def apply(finalPc: Long): Thread = { context =>
    // 等待 CPU 开始运行
    context.sysClockDomain.waitSampling()

    // 等待写回 PC 到达给定地址
    waitUntil(context.soc.io.debugInterface.wb.pc.toLong == finalPc)
    simSuccess()
  }
}

case class WriteBack(pc: Long, addr: Int, data: Long)

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
    Using(new BufferedWriter(new FileWriter(file))) { writer =>
      def logWriteBack(wb: WriteBack): Unit = {
        writer.write(f"1 ${wb.pc}%08x ${wb.addr}%02x ${wb.data}%08x\n")
      }
      WriteBackProducerThread(logWriteBack)(context)
    }
  }
}
