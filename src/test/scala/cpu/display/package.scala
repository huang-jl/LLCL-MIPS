package cpu

import spinal.core.sim._

package object display {
  case class DisplayerConfig(
      throttlePeriod: Option[Int]
  )

  abstract class DisplayerPlugin(config: DisplayerConfig) extends Simulator.Plugin {
    private var needRepaint = true

    def triggerRepaint() = { needRepaint = true }

    def paint(): Unit

    override def setupSim(context: Simulator#Context) = fork {
      onSimEnd {
        paint()
      }

      while (true) {
        waitUntil(needRepaint)
        needRepaint = false
        paint()

        for (p <- config.throttlePeriod) {
          context.sysClockDomain.waitSampling(p)
        }
      }
    }
  }
}
