package cpu

package object confreg {

  /** 处理单个地址的单元 */
  trait Element {
    def read: Int
    def write(data: Int): Unit = {}
  }

  /** 处理一系列地址的模块 */
  trait Module {
    def elements(context: Simulator#Context): Map[Int, Element]
  }

  abstract class ConfRegPlugin(addrBase: Long) extends Simulator.Plugin {
    def modules: Seq[Module]

    override def setupSim(context: Simulator#Context): Unit = {
      val startAddr = addrBase & 0xffff0000
      val endAddr   = startAddr | 0xffff

      val elementsList = modules.flatMap(_.elements(context))
      val elements     = elementsList.toMap

      assert(
        elementsList.size == elements.size,
        "ConfRef Modules contains elements on the same address"
      )

      val device = new MappedIODevice with PerWord {
        override def readWord(addr: Long) =
          elements.get(addr.toInt & 0xffff).map { _.read }.getOrElse(0xcececece)

        override def writeWord(addr: Long, data: Int) =
          elements.get(addr.toInt & 0xffff).foreach { _.write(data) }
      }

      context.memorySim.addMappedIO(startAddr to endAddr, device)
    }
  }

  case class Switch(addr: Int, interleaveAddr: Option[Int] = None) extends Module {
    val data = Array.fill(8)(true)

    override def elements(context: Simulator#Context) = Map(
      addr -> new Element {
        override def read =
          data.zipWithIndex.map { case (s, i) => if (s) 1 << i else 0 }.reduce(_ | _)
      }
    ) ++ interleaveAddr.map(
      _ ->
        new Element {
          override def read =
            data.zipWithIndex
              .map { case (s, i) => if (s) 1 << (i * 2 + 1) else 0 }
              .reduce(_ | _)
        }
    )
  }

  case class IOSimu(addr: Int) extends Module {
    var ioSimu = 0

    override def elements(context: Simulator#Context) = Map(
      addr -> new Element {
        override def read = ioSimu

        override def write(data: Int) = {
          ioSimu = ((data & 0xffff) << 16) | ((data & 0xffff0000) >>> 16)
        }
      }
    )
  }

  case class OpenTrace(addr: Int) extends Module {
    override def elements(context: Simulator#Context) = Map(
      addr -> new Element {
        override def read = if (context.openTrace) 1 else 0

        override def write(data: Int) = {
          context.openTrace = data != 0
        }
      }
    )
  }

  case class FuncTestConfRegPlugin() extends ConfRegPlugin(0x1faf0000) {
    override def modules = Seq(
      Switch(0xf020, Some(0xf02c)),
      IOSimu(0xffec),
      OpenTrace(0xfff8)
    )
  }
}
