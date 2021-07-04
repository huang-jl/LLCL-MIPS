/** A patched version of the one in SpinalHDL's library. */

package cpu

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba4.axi.sim._

import scala.collection.mutable
import scala.util.Random

case class AxiJob(
    address: Long,
    burstLength: Int,
    id: Int
)

case class AxiMemorySim(axi: Axi4, clockDomain: ClockDomain, config: AxiMemorySimConfig) {
  val memory         = SparseMemory()
  val pending_reads  = new mutable.Queue[AxiJob]
  val pending_writes = new mutable.Queue[AxiJob]

  /** Bus word width in bytes */
  val busWordWidth = axi.config.dataWidth / 8

  def start(): Unit = {
    fork {
      handleAr(axi.ar)
    }

    fork {
      handleR(axi.r)
    }

    fork {
      handleAw(axi.aw)
    }

    fork {
      handleW(axi.w, axi.b)
    }
  }

  def handleAr(ar: Stream[Axi4Ar]): Unit = {
    println("Handling AXI4 Master read cmds...")

    ar.ready #= false

    while (true) {
      ar.ready #= true
      clockDomain.waitSamplingWhere(ar.valid.toBoolean)
      ar.ready #= false

      assert(
        assertion = (ar.payload.len.toBigInt + (ar.payload.addr.toBigInt & 4095)) <= 4095,
        message = s"Read request crossing 4k boundary (addr=${ar.payload.addr.toBigInt
          .toString(16)}, len=${ar.payload.len.toLong.toHexString}"
      )

      val id = if (ar.config.useId) ar.payload.id.toInt else 0
      pending_reads += AxiJob(ar.payload.addr.toLong, ar.payload.len.toInt, id)

      //println("AXI4 read cmd: addr=0x" + ar.payload.addr.toLong.toHexString + " count=" + (ar.payload.len.toBigInt+1))

      if (pending_reads.length >= config.maxOutstandingReads)
        clockDomain.waitSamplingWhere(pending_reads.length < config.maxOutstandingReads)
    }
  }

  def handleR(r: Stream[Axi4R]): Unit = {
    println("Handling AXI4 Master read resp...")

    val random = Random

    r.valid #= false
    r.payload.last #= false

    while (true) {
      clockDomain.waitSampling(1)

      // todo: implement read issuing delay

      if (pending_reads.nonEmpty) {
        var job = pending_reads.front

        r.valid #= true
        if (r.config.useId) r.id #= job.id
        if (r.config.useResp) r.resp #= 0 // OKAY

        var i = 0
        while (i <= job.burstLength) {
          if (config.interruptProbability > random.nextInt(100)) {
            r.valid #= false
            clockDomain.waitSampling(random.nextInt(config.interruptMaxDelay + 1))
            r.valid #= true
          } else {
            if (i == job.burstLength)
              r.payload.last #= true
            r.payload.data #= memory.readBigInt(job.address + i * busWordWidth, busWordWidth)
            clockDomain.waitSamplingWhere(r.ready.toBoolean)
            i = i + 1
          }
        }

        r.valid #= false
        r.payload.last #= false

        pending_reads.dequeue()

        //println("AXI4 read rsp: addr=0x" + job.address.toLong.toHexString + " count=" + (job.burstLength+1))
      }
    }
  }

  def handleAw(aw: Stream[Axi4Aw]): Unit = {
    println("Handling AXI4 Master write cmds...")

    aw.ready #= false

    while (true) {
      aw.ready #= true
      clockDomain.waitSamplingWhere(aw.valid.toBoolean)
      aw.ready #= false

      assert(
        assertion = (aw.payload.len.toBigInt + (aw.payload.addr.toBigInt & 4095)) <= 4095,
        message = s"Write request crossing 4k boundary (addr=${aw.payload.addr.toBigInt
          .toString(16)}, len=${aw.payload.len.toLong.toHexString}"
      )

      val id = if (aw.config.useId) aw.payload.id.toInt else 0
      pending_writes += AxiJob(aw.payload.addr.toLong, aw.payload.len.toInt, id)

      //println("AXI4 write cmd: addr=0x" + aw.payload.addr.toLong.toHexString + " count=" + (aw.payload.len.toBigInt+1))

      if (pending_writes.length >= config.maxOutstandingWrites)
        clockDomain.waitSamplingWhere(pending_writes.length < config.maxOutstandingWrites)
    }
  }

  def handleW(w: Stream[Axi4W], b: Stream[Axi4B]): Unit = {
    println("Handling AXI4 Master write...")

    w.ready #= false
    b.valid #= false

    while (true) {
      clockDomain.waitSampling(10)

      if (pending_writes.nonEmpty) {
        var job   = pending_writes.front
        var count = job.burstLength

        w.ready #= true

        for (i <- 0 to job.burstLength) {
          clockDomain.waitSamplingWhere(w.valid.toBoolean)
          memory.writeBigInt(job.address + i * busWordWidth, w.payload.data.toBigInt, busWordWidth)
        }

        w.ready #= false

        clockDomain.waitSampling(config.writeResponseDelay)

        b.valid #= true
        if (b.config.useResp) b.resp #= 0 // OKAY
        if (b.config.useId) b.id #= job.id
        clockDomain.waitSamplingWhere(b.ready.toBoolean)
        b.valid #= false

        pending_writes.dequeue()

        //println("AXI4 write: addr=0x" + job.address.toLong.toHexString + " count=" + (job.burstLength+1))
      }
    }
  }
}
