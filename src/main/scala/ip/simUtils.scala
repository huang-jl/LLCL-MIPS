package ip

import spinal.core._
import spinal.core.sim._

import scala.collection.mutable

package object simUtils {
  def isInSim = GenerationFlags.simulation.isEnabled

  def pullToTopLevel[T <: Data](data: T): T = Data.doPull(data, GlobalData.get.toplevel)

  def addJob(job: () => Unit): Unit = {
    GlobalData.get.toplevel.asInstanceOf[JobCollector].jobs += job
  }

  trait JobCollector {
    val jobs = mutable.ArrayBuffer[() => Unit]()
  }

  case class DelayedPipeline(latency: Int) {
    private val idleJob       = mutable.ArrayBuffer[() => Unit]()
    private val resetJob      = mutable.ArrayBuffer[() => Unit]()
    private val everyTickJobs = mutable.ArrayBuffer[((=> Unit) => Unit) => Unit]()
    private val clockDomain   = ClockDomain.current

    def whenIdle(idleJob: => Unit) = {
      this.idleJob += { () => idleJob }
      this
    }

    def whenReset(resetJob: => Unit) = {
      this.resetJob += { () => resetJob }
      this
    }

    def everyTick(everyTickJob: ((=> Unit) => Unit) => Unit) = {
      everyTickJobs += everyTickJob
      this
    }

    def toJob(): Unit = {
      var tick: Long = 0

      val pendingJobs = mutable.Queue[(() => Unit, Long)]()

      idleJob.foreach { _() }
      while (true) {
        clockDomain.waitActiveEdge()

        if (clockDomain.isResetAsserted) {
          resetJob.foreach { _() }
          pendingJobs.clear() // May need to be customizable
        }

        if (clockDomain.isSamplingEnable) {
          def schedule(scheduled: => Unit) = pendingJobs.enqueue((() => scheduled, tick + latency))
          everyTickJobs.foreach { _(schedule) }
        }

        // 顺序问题：如果 latency = 0，必须先做 everyTickJob，再检查 pendingJobs。

        if (pendingJobs.isEmpty || pendingJobs.front._2 != tick) {
          idleJob.foreach { _() }
        } else {
          while (pendingJobs.nonEmpty && pendingJobs.front._2 == tick) {
            pendingJobs.dequeue()._1()
          }
        }

        tick += 1
      }
    }
  }
}
