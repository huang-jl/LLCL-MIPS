package cpu

import spinal.core.sim._

import java.util.concurrent.atomic.AtomicInteger

package object confreg {
  // confreg 中寄存器寄存器可能事实上应下个周期再写入

  case class Switch(addr: Long, interleaveAddr: Option[Long] = None) extends Simulator.Plugin {
    val data = Array.fill(8)(true)

    override def setupSim(context: Simulator#Context) = {
      context.memorySim.addSingleWordMappedIO(
        addr,
        new MappedIODevice with PerWord {
          override def readWord(addr: Long) =
            data.zipWithIndex.map { case (s, i) => if (s) 1 << i else 0 }.reduce(_ | _)
        }
      )

      interleaveAddr foreach { iAddr =>
        context.memorySim.addSingleWordMappedIO(
          iAddr,
          new MappedIODevice with PerWord {
            override def readWord(addr: Long) =
              data.zipWithIndex.map { case (s, i) => if (s) 1 << (i * 2 + 1) else 0 }.reduce(_ | _)
          }
        )
      }
    }
  }

  case class IOSimu(addr: Int) extends Simulator.Plugin {
    var ioSimu = 0

    override def setupSim(context: Simulator#Context) = {
      context.memorySim.addSingleWordMappedIO(
        addr,
        new MappedIODevice with PerWord {
          override def readWord(addr: Long) = ioSimu

          override def writeWord(addr: Long, data: Int) = {
            ioSimu = ((data & 0xffff) << 16) | ((data & 0xffff0000) >>> 16)
          }
        }
      )
    }
  }

  case class OpenTrace(addr: Int) extends Simulator.Plugin {
    override def setupSim(context: Simulator#Context) = {
      context.memorySim.addSingleWordMappedIO(
        addr,
        new MappedIODevice with PerWord {
          override def readWord(addr: Long) = if (context.openTrace) 1 else 0

          override def writeWord(addr: Long, data: Int) = {
            context.openTrace = data != 0
          }
        }
      )
    }
  }

  case class Constant(addr: Int, value: Int) extends Simulator.Plugin {
    override def setupSim(context: Simulator#Context) = {
      context.memorySim.addSingleWordMappedIO(
        addr,
        new MappedIODevice with PerWord {
          override def readWord(addr: Long) = value
        }
      )
    }
  }

  case class Register(addr: Int) extends Simulator.Plugin {
    var value = 0

    override def setupSim(context: Simulator#Context) = {
      context.memorySim.addSingleWordMappedIO(
        addr,
        new MappedIODevice with PerWord {
          override def readWord(addr: Long) = value

          override def writeWord(addr: Long, data: Int) = { value = data }
        }
      )
    }
  }

  case class RepaintingRegister(addr: Int) extends Simulator.Plugin {
    var value = 0

    override def setupSim(context: Simulator#Context) = {
      context.memorySim.addSingleWordMappedIO(
        addr,
        new MappedIODevice with PerWord {
          override def readWord(addr: Long) = value

          override def writeWord(addr: Long, data: Int) = {
            value = data
            context.getPluginOption[display.DisplayerPlugin] foreach { _.triggerRepaint() }
          }
        }
      )
    }
  }

  // 可能和官方的实现有细微的不一致，如果不要求精度应该可以忽略；未严格考虑线程同步？
  case class Timer(addr: Int) extends Simulator.Plugin {
    val value = new AtomicInteger(0)

    override def setupSim(context: Simulator#Context) = {
      context.memorySim.addSingleWordMappedIO(
        addr,
        new MappedIODevice with PerWord {
          override def readWord(addr: Long) = value.get()

          override def writeWord(addr: Long, data: Int) = {
            value.set(data)
          }
        }
      )

      fork {
        while (true) {
          context.sysClockDomain.waitSampling()
          value.incrementAndGet()
        }
      }
    }
  }
}
