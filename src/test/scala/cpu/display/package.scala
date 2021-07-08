package cpu

import spinal.core.sim._

package object display {
  trait DisplayerPlugin extends Simulator.Plugin {
    private var needRepaint = false

    val sysCycles: Int

    def triggerRepaint() = { needRepaint = true }

    def paint(): Unit

    override def setupSim(context: Simulator#Context) = fork {
      while (true) {
        context.sysClockDomain.waitSampling(sysCycles)
        context.sysClockDomain.waitEdge(1)

        if (needRepaint) {
          paint()
          needRepaint = false
        }
      }
    }
  }
}
