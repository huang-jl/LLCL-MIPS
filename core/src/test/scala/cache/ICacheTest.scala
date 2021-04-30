package cache

import spinal.core._
import spinal.core.sim._
import cache.ICache

import scala.util.Random
import scala.util.control.Breaks.{break, breakable}

object ICacheTest {
  def intToLong(v: Int): Long = {
    v.toLong & ((0x1L << 32) - 1)
  }

  def main(args: Array[String]): Unit = {
    val config = ICacheConfig()
    SimConfig.withWave.allOptimisation.compile(new ICache(config)).doSim("testCache", 2021) { dut =>
      val rand = new Random(2021)
      dut.clockDomain.forkStimulus(10)

      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()
      dut.io.cpu.raddr #= 0xbfc00000L
      dut.clockDomain.waitSampling()

      var golden = Map[Long, Long]()
      for (i <- 0 to 100) {
        breakable {
          // Simulate CPU input
          dut.clockDomain.waitRisingEdge()
          var randomAddr = dut.io.cpu.raddr.toLong + ((rand.nextInt(0x100) >> 2) << 2)
          println(s"start with random addr = 0x%x".format(randomAddr))
          dut.io.cpu.raddr #= randomAddr
          dut.io.cpu.read #= true
          dut.io.axi.ar.ready #= true
          dut.clockDomain.waitFallingEdge()
          if (!dut.io.cpu.stall.toBoolean) {
            println("No stall with addr = %x, data out = %x, golden = %x".format(randomAddr, dut.io.cpu.rdata.toLong, golden(randomAddr)))
            assert(dut.io.cpu.rdata.toLong == golden(randomAddr))
            break()
          }

          //Wait AXI address handshake
          while(!dut.io.axi.ar.ready.toBoolean)(dut.clockDomain.waitSampling())
          val length = dut.io.axi.ar.len.toInt
          val id = dut.io.axi.ar.id.toInt

          //Align to cache line size
          val alignedAddr = (randomAddr >> log2Up(config.lineWidth / 8)) << log2Up(config.lineWidth / 8)
          //Transfer data [Incr Burst mode]
          for (x <- 0 until length) {
            val randomData = intToLong((rand.nextLong() % 0xff00000L + 0x01000100L).toInt)
            golden += (alignedAddr + x * 4 -> randomData)
            //put read payload on bus
            dut.io.axi.r.payload.id #= id
            dut.io.axi.r.payload.data #= randomData
            dut.io.axi.r.payload.resp #= 0
            dut.io.axi.r.payload.last #= (x == length - 1)
            dut.io.axi.r.valid #= true
            //Payload.data must keep for one cycle
            do (dut.clockDomain.waitSampling()) while (!dut.io.axi.r.ready.toBoolean)
          }
          while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
          //check for cache
          val lineOffsetStart = 31 - config.tagWidth
          val lineOffsetEnd = lineOffsetStart - config.lineOffsetWidth + 1
          val lineTag = (randomAddr >> lineOffsetEnd) & ((1L << config.lineOffsetWidth) - 1)
          assert(randomAddr == dut.io.cpu.raddr.toLong)
          assert(lineTag == dut.tags.lineTag.toLong)
          //          print("randomAddr = %x, cpuAddr = %x, ".format(randomAddr, dut.io.cpu.raddr.toLong))
          //          println("line tag = %x, line tag from cpu = %x".format(lineTag, dut.tags.lineTag.toLong))
          val res = dut.lines.vec(lineTag.toInt).datas.vec.zipWithIndex.forall { case (word, index) =>
//            println(s"index = %d, Word from cache = %x, golden = %x".format(index, word.toLong, golden(alignedAddr + 4 * index)))
            word.toLong == golden(alignedAddr + 4 * index)
          }
          assert(res)
          //check for cpu
//          println("addr = %x, CPU-Interface data = %x, wordTag = %d, golden data = %x".format(randomAddr, dut.io.cpu.rdata.toLong, dut.tags.wordTag.toInt, golden(randomAddr)))
          assert(dut.io.cpu.rdata.toLong == golden(randomAddr))
        }
      }
    }
  }
}
