package cache

import spinal.core._
import spinal.core.sim._

import scala.util.Random
import scala.util.control.Breaks.{break, breakable}

object ICacheTest {
  val rand = new Random(2021)
  val config = ICacheConfig()

  def intToLong(v: Int): Long = {
    v.toLong & ((0x1L << 32) - 1)
  }

  def genRandomAddrGreaterThanOneLine: Long = {
    val base = rand.nextInt(0x200) + config.lineSize
    ((base >> 2) << 2)
  }

  def genRandomAddrAlignCacheLine(index: Int): Long = {
    var base = 0x20001000L
    base = base + (rand.nextLong() % 0x80000000)
    val offset = config.wordIndexWidth + config.byteIndexWidth
    base = (base >> (offset + config.lineOffset)) << config.lineOffset
    base = base + index
    (base << offset) & ((0x1L << 32) - 1)
  }

  def main(args: Array[String]): Unit = {
    val compile = SimConfig.withWave.allOptimisation.compile(new ICache(config))
    compile.doSim("testCacheA", 2021) { dut =>
      dut.clockDomain.forkStimulus(10)

      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()
      dut.io.cpu.addr #= 0xbfc00000L
      dut.clockDomain.waitSampling()

      var golden = Map[Long, Long]()
      for (i <- 0 to 100) {
        breakable {
          // Simulate CPU input
          dut.clockDomain.waitRisingEdge()
          val randomAddr = dut.io.cpu.addr.toLong + genRandomAddrGreaterThanOneLine
//          println(s"start with random addr = 0x%x".format(randomAddr))
          dut.io.cpu.addr #= randomAddr
          dut.io.cpu.read #= true
          dut.clockDomain.waitFallingEdge()
          if (!dut.io.cpu.stall.toBoolean) {
            println("No stall with addr = 0x%x, data out = 0x%x, golden = 0x%x".format(randomAddr, dut.io.cpu.data.toLong, golden(randomAddr)))
            assert(dut.io.cpu.data.toLong == golden(randomAddr))
            break()
          }
          //Wait AXI address handshake
          while(!dut.io.axi.ar.valid.toBoolean) {
            dut.clockDomain.waitSampling()
          }
          val alignedAddr = dut.io.axi.ar.addr.toLong
          val length = dut.io.axi.ar.len.toInt
          val id = dut.io.axi.ar.id.toInt
          //Random wait for some cycle to assert ar.ready
          for (_ <- 0 until rand.nextInt(5)) {
            dut.clockDomain.waitSampling()
          }
          dut.io.axi.ar.ready #= true

          //Align to cache line size
          assert(alignedAddr == (randomAddr >> config.lineOffset) << config.lineOffset)
          //Transfer data [Incr Burst mode]
          for (x <- 0 to length) {
            val randomData = intToLong((rand.nextLong() % 0xff000000L + 0x01000100L).toInt)
            golden += (alignedAddr + x * 4 -> randomData)
            //put read payload on bus
            dut.io.axi.r.payload.id #= id
            dut.io.axi.r.payload.data #= randomData
            dut.io.axi.r.payload.resp #= 0
            dut.io.axi.r.valid #= false
            dut.io.axi.r.last #= false
            //random wait for valid data
            for(_ <- 0 until rand.nextInt(5)) {
              dut.clockDomain.waitSampling()
            }
            dut.io.axi.r.payload.last #= (x == length)
            dut.io.axi.r.valid #= true
            //Payload.data must keep for one cycle
            do (dut.clockDomain.waitSampling()) while (!dut.io.axi.r.ready.toBoolean)
          }
          while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
          dut.io.axi.ar.ready #= false
          //check for cache
          val lineIndex = (randomAddr >> config.lineOffset) & ((1L << config.lineIndexWidth) - 1)
          assert(randomAddr == dut.io.cpu.addr.toLong)
          assert(lineIndex == dut.comparison.lineIndex.toLong)

          val res = dut.cacheData.lines.vec(lineIndex.toInt).datas.vec.zipWithIndex.forall { case (word, index) =>
//            println("index = %d, aligned addr = 0x%x, word = 0x%x, golden = 0x%x".format(index, alignedAddr + 4 * index, word.toLong, golden(alignedAddr + 4 * index)))
            word.toLong == golden(alignedAddr + 4 * index)
          }
          assert(res)
          //check for cpu
          assert(dut.io.cpu.data.toLong == golden(randomAddr))
        }
      }
    }

    compile.doSim("iCacheTestB", 2021) { dut =>
      val rand = new Random(2021)
      dut.clockDomain.forkStimulus(10)

      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()
      //Random Init cache
      var golden = Map[Long, Long]()
      for (index <- 0 until config.lineNum) {
        val randomAddr = genRandomAddrAlignCacheLine(index)
        val lineOffsetStart = 31 - config.tagWidth
        val lineOffsetEnd = lineOffsetStart - config.lineIndexWidth + 1
        val lineIndex = (randomAddr >> lineOffsetEnd) & ((1L << config.lineIndexWidth) - 1)
        val addrTag = (randomAddr >> (32 - config.tagWidth)) & ((0x1L << config.tagWidth) - 1)
        assert(lineIndex == index)
        dut.cacheData.lines.vec(lineIndex.toInt).tag #= addrTag
        dut.cacheData.lines.vec(lineIndex.toInt).valid #= true
        for (wordIndex <- 0 until config.wordPerLine) {
          val randomData = rand.nextLong() & ((0x1L << 32) - 1)
//          println(s"addr = 0x%x, line tag = $lineIndex, word index = $wordIndex, data = 0x%x".format(randomAddr + 4 * wordIndex, randomData))
          dut.cacheData.lines.vec(lineIndex.toInt).datas.vec(wordIndex) #= randomData
          golden += (randomAddr + 4 * wordIndex -> randomData)
        }
      }

      dut.io.cpu.addr #= 0xbfc00000L
      dut.io.cpu.read #= false
      //Access Cache
      for ((addr, value) <- golden) {
        dut.clockDomain.waitRisingEdge()
        dut.io.cpu.addr #= addr
        dut.io.cpu.read #= true
        dut.clockDomain.waitFallingEdge()
        assert(!dut.io.cpu.stall.toBoolean && dut.comparison.hit.toBoolean)
//        println("addr = 0x%x, cpu data = 0x%x, golend data = 0x%x".format(addr, dut.io.cpu.data.toLong, value))
        assert(dut.io.cpu.data.toLong == value)
      }
    }
  }
}
