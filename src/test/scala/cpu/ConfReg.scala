package cpu

import spinal.lib.{cpu => _}

package object confreg {
  import Simulator._

  object FuncTestConfReg {
    val SwitchAddr           = 0xf020
    val SwitchInterleaveAddr = 0xf02c
  }

  case class FuncTestConfReg() extends MappedIODevice with PerWord {
    import FuncTestConfReg._

    val switch = Array.fill(8)(true)

    override def readWord(addr: Long) = {
      (addr & 0xffff) match {
        case SwitchAddr =>
          switch.zipWithIndex
            .map { case (s, i) => if (s) 1 << i else 0 }
            .reduce(_ | _)
        case SwitchInterleaveAddr =>
          switch.zipWithIndex
            .map { case (s, i) => if (s) 1 << (i * 2 + 1) else 0 }
            .reduce(_ | _)
        case _ => 0xcececece
      }
    }

    override def writeWord(addr: Long, data: Int) = {
      (addr & 0xffff) match {
        case _ =>
      }
    }
  }

  def funcTestThread: Thread = { context =>
    context.memorySim.addMappedIO(
      0x1faf0000L to 0x1fafffffL,
      FuncTestConfReg()
    )
  }
}
