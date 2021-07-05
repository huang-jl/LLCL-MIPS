/** A patched version of the one in SpinalHDL's library. */

package cpu

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba4.axi.sim._
import util.binary._

import scala.collection.immutable.NumericRange
import scala.collection.mutable
import scala.util.Random

trait MappedIODevice {
  def read(addr: Long, length: Int): Array[Byte]
  def write(addr: Long, data: Array[Byte]): Unit
}

trait PerByte {
  this: MappedIODevice =>

  def readByte(addr: Long): Byte
  def writeByte(addr: Long, data: Byte): Unit

  override def read(addr: Long, length: Int) =
    ((addr until (addr + length)) map readByte).toArray

  override def write(addr: Long, data: Array[Byte]) =
    data.zipWithIndex foreach { case (b, i) => writeByte(addr + i, b) }
}

trait PerWord {
  this: MappedIODevice =>

  // addr will be aligned by 4
  def readWord(addr: Long): Int
  def writeWord(addr: Long, data: Int): Unit

  override def read(addr: Long, length: Int) = {
    val startPaddingLength = (addr % 4).toInt
    val endPaddingLength   = 3 - ((addr + length + 3) % 4).toInt

    if (startPaddingLength != 0 || endPaddingLength != 0)
      println("Unaligned read to PerWord MappedIODevice")

    val start = addr - startPaddingLength
    val end   = addr + length + endPaddingLength

    val words = (start until end by 4) map readWord
    val bytes = for (w <- words; i <- 0 until 4) yield (w >> (i * 8)).toByte
    bytes.drop(startPaddingLength).take(length).toArray
  }

  override def write(addr: Long, data: Array[Byte]) = {
    val length             = data.length
    val startPaddingLength = (addr % 4).toInt
    val endPaddingLength   = 3 - ((addr + length + 3) % 4).toInt

    val bytes = if (startPaddingLength != 0 || endPaddingLength != 0) {
      println("Unaligned read to PerWord MappedIODevice")

      val startPadding = Array.fill(startPaddingLength)(0.toByte)
      Random.nextBytes(startPadding)
      val endPadding = Array.fill(endPaddingLength)(0.toByte)
      Random.nextBytes(endPadding)

      startPadding ++ data ++ endPadding
    } else {
      data
    }
    assert(bytes.length % 4 == 0)

    val start = addr - startPaddingLength
    for (i <- data.indices by 4) {
      writeWord(start + i, (0 until 4) map { j => bytes(i + j).toInt << (j * 8) } reduce { _ | _ })
    }
  }
}

case class AxiMemorySim(axi: Axi4, clockDomain: ClockDomain, config: AxiMemorySimConfig) {
  val memory        = SparseMemory()
  val pendingReads  = new mutable.Queue[(AxiJob, MappedIODevice)]
  val pendingWrites = new mutable.Queue[(AxiJob, MappedIODevice)]

  private val mappedIODevices = mutable.ArrayBuffer[(NumericRange[Long], MappedIODevice)]()

  /** Reading and writing to memory, as a pseudo-device. */
  private val memoryDevice = new MappedIODevice {
    override def read(addr: Long, length: Int) = {
      memory.readArray(addr, length)
    }

    override def write(addr: Long, data: Array[Byte]): Unit = {
      memory.writeArray(addr, data)
    }
  }

  /** Bus word width in bytes */
  val busWordWidth = axi.config.dataWidth / 8

  case class AxiJob(
      address: Long,
      burstLength: Int,
      size: Int,
      id: Int
  ) {
    def numberBytes    = 1 << size
    def alignedAddress = address / numberBytes * numberBytes

    def beat(i: Int) = AxiBeat(
      startAddr = (alignedAddress + i * numberBytes) max address,
      endAddr = alignedAddress + (i + 1) * numberBytes
    )

    /** Not inclusive */
    def endAddress = alignedAddress + (burstLength + 1) * size

    def addrRange = address until endAddress
  }

  /** endAddr not included */
  case class AxiBeat(startAddr: Long, endAddr: Long) {
    def length = (endAddr - startAddr).toInt
    def bytesRange =
      (startAddr % busWordWidth).toInt until ((endAddr - 1) % busWordWidth + 1).toInt
  }

  def addMappedIO(addrRange: NumericRange[Long], device: MappedIODevice) = {
    assert(
      assertion = mappedIODevices.forall { case (range, _) =>
        range.max < addrRange.min || addrRange.max < range.min
      },
      message = "Mapped IO range intersects"
    )

    mappedIODevices += ((addrRange, device))
  }

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

  private def findIODevice(job: AxiJob): Option[MappedIODevice] = {
    for ((range, device) <- mappedIODevices) {
      if ((range contains job.address) && (range contains job.endAddress)) {
        return Some(device)
      } else if ((job.addrRange contains range.min) || (job.addrRange contains range.max)) {
        return None
      }
    }
    return Some(memoryDevice)
  }

  def handleAr(ar: Stream[Axi4Ar]): Unit = {
    println("Handling AXI4 Master read cmds...")

    ar.ready #= false

    while (true) {
      ar.ready #= true
      clockDomain.waitSamplingWhere(ar.valid.toBoolean)
      ar.ready #= false

      val job = AxiJob(
        address = ar.payload.addr.toLong,
        burstLength = ar.payload.len.toInt,
        size = if (axi.config.useSize) ar.payload.size.toInt else log2Up(busWordWidth),
        id = if (axi.config.useId) ar.payload.id.toInt else 0
      )

      assert(
        assertion = ((job.burstLength << job.size) + (job.alignedAddress & 4095)) <= 4095,
        message =
          f"Read request crossing 4k boundary (addr=${job.address}%x, len=${job.burstLength}%x, size=${job.size})"
      )

      val device = findIODevice(job).getOrElse {
        simFailure(
          f"Read request cross different devices (addr=${job.address}%x, len=${job.burstLength}%x, size=${job.size})"
        )
      }

      delayed(config.readResponseDelay) {
        pendingReads += ((job, device))
      }

      //println("AXI4 read cmd: addr=0x" + ar.payload.addr.toLong.toHexString + " count=" + (ar.payload.len.toBigInt+1))

      if (pendingReads.length >= config.maxOutstandingReads)
        clockDomain.waitSamplingWhere(pendingReads.length < config.maxOutstandingReads)
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

      if (pendingReads.nonEmpty) {
        val (job, device) = pendingReads.front

        r.valid #= true
        if (axi.config.useId) r.id #= job.id
        if (axi.config.useResp) r.resp #= 0 // OKAY

        var i = 0
        while (i <= job.burstLength) {
          if (config.interruptProbability > random.nextInt(100)) {
            r.valid #= false
            clockDomain.waitSampling(random.nextInt(config.interruptMaxDelay + 1))
            r.valid #= true
          } else {
            if (i == job.burstLength)
              r.payload.last #= true

            val beat = job.beat(i)

            val readData = device.read(beat.startAddr, beat.length)

            // Pad random bytes around readData
            val validRange = beat.bytesRange
            val padded     = Array.fill(busWordWidth)(0.toByte)
            random.nextBytes(padded)
            for (i <- validRange) {
              padded(i) = readData(i - validRange.min)
            }

            r.payload.data #= padded

            clockDomain.waitSamplingWhere(r.ready.toBoolean)
            i = i + 1
          }
        }

        r.valid #= false
        r.payload.last #= false

        pendingReads.dequeue()

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

      val job = AxiJob(
        address = aw.payload.addr.toLong,
        burstLength = aw.payload.len.toInt,
        size = if (axi.config.useSize) aw.payload.size.toInt else log2Up(busWordWidth),
        id = if (axi.config.useId) aw.payload.id.toInt else 0
      )

      assert(
        assertion = ((job.burstLength << job.size) + (job.alignedAddress & 4095)) <= 4095,
        message =
          f"Write request crossing 4k boundary (addr=${job.address}%x, len=${job.burstLength}%x, size=${job.size})"
      )

      val device = findIODevice(job).getOrElse {
        simFailure(
          f"Write request cross different devices (addr=${job.address}%x, len=${job.burstLength}%x, size=${job.size})"
        )
      }

      pendingWrites += ((job, device))

      //println("AXI4 write cmd: addr=0x" + aw.payload.addr.toLong.toHexString + " count=" + (aw.payload.len.toBigInt+1))

      if (pendingWrites.length >= config.maxOutstandingWrites)
        clockDomain.waitSamplingWhere(pendingWrites.length < config.maxOutstandingWrites)
    }
  }

  def handleW(w: Stream[Axi4W], b: Stream[Axi4B]): Unit = {
    println("Handling AXI4 Master write...")

    w.ready #= false
    b.valid #= false

    while (true) {
      clockDomain.waitSampling(10)

      if (pendingWrites.nonEmpty) {
        val (job, device) = pendingWrites.front

        w.ready #= true

        for (i <- 0 to job.burstLength) {
          clockDomain.waitSamplingWhere(w.valid.toBoolean)

          val beat = job.beat(i)

          /** Data written contains invalid bytes around valid ones */
          val padded      = w.payload.data.toBigInt.toBinary(busWordWidth)
          val validRange  = beat.bytesRange
          val writtenData = padded.slice(validRange.min, validRange.max + 1)

          device.write(beat.startAddr, writtenData)
        }

        w.ready #= false

        clockDomain.waitSampling(config.writeResponseDelay)

        b.valid #= true
        if (axi.config.useResp) b.resp #= 0 // OKAY
        if (axi.config.useId) b.id #= job.id
        clockDomain.waitSamplingWhere(b.ready.toBoolean)
        b.valid #= false

        pendingWrites.dequeue()

        //println("AXI4 write: addr=0x" + job.address.toLong.toHexString + " count=" + (job.burstLength+1))
      }
    }
  }
}
