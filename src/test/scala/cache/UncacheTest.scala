package cache

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import scala.io.Source
import scala.util.Random

import ram._

class UncacheTest extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUDCacheInterface)
    val uncache = slave(new CPUDCacheInterface)
  }
  val cpu = new CPUDCacheInterface
  val dCache = new DCache(CacheRamConfig(32, 7, 4), 8)
  val ram = new axi_ram(AXIRamConfig(32, UncacheTest.addrWidth, 4))
  val uncacheRam = new axi_ram(AXIRamConfig(32, UncacheTest.addrWidth, 4))

  //  io.cpu >> cpu
  //  cpu >> dCache.io.cpu
  io.cpu >> dCache.io.cpu
  io.uncache >> dCache.io.uncache
  dCache.io.axi >> ram.io.axi
  dCache.io.uncacheAXI >> uncacheRam.io.axi
}

//Use DCacheTest component
object UncacheTest {
  val rand = new Random(2021)
  val addrWidth: Int = 20
  val printBase: Int = 5000

  def getMapAddr(addr: Long): Long = {
    addr & ((0x1 << addrWidth) - 1)
  }

  def getMemInitData: Map[Long, Long] = {
    val source = Source.fromFile("./script/data.txt")
    val lineIterator = source.getLines().zipWithIndex
    var res = Map[Long, Long]()
    for ((line, addr) <- lineIterator) {
      res += ((addr * 4).toLong -> java.lang.Long.parseLong(line, 16))
    }
    res
  }

  def getBytes(num: Long): Array[Long] = {
    val byte1 = num & 0xff
    val byte2 = (num >> 8) & 0xff
    val byte3 = (num >> 16) & 0xff
    val byte4 = (num >> 24) & 0xff
    Array(byte1, byte2, byte3, byte4)
  }

  def toNum(l: Array[Long]): Long = {
    (l(3) << 24) | (l(2) << 16) | (l(1) << 8) | l(0)
  }

  //range: 后16位变动的范围
  def getRandomAddr(range: Int): (Int, Long) = {
    val baseRange = 1 << (addrWidth - 16)
    val randomRamAddr = rand.nextInt(range) & 0xfffc
    var cpuBase = (rand.nextInt(baseRange) & 0xffff) + 0xbfc0
    cpuBase = Math.min(cpuBase, 0xffff)
    val cpuAddr = ((cpuBase.toLong << 16) | randomRamAddr)
    //    println("base = 0x%x, rand = 0x%x, cpu addr = 0x%x".format(cpuBase, randomRamAddr, cpuAddr))
    (randomRamAddr, cpuAddr)
  }

  def initDUT(dut: UncacheTest): Unit = {
    dut.io.cpu.byteEnable #= 0xf
    dut.io.cpu.read #= false
    dut.io.cpu.write #= false
    dut.io.uncache.byteEnable #= 0xf
    dut.io.uncache.read #= false
    dut.io.uncache.write #= false
  }

  /**
   * 返回(byteEnable, randomData, newData)
   */
  def getByteEnableRandomData(raw: Long): (Int, Long, Long) = {
    val byteEnable = rand.nextInt(0xf) + 1
    val randData = rand.nextLong() & 0xffffffffL
    val rawBytes = getBytes(raw)
    val randBytes = getBytes(randData)
    for (b <- 0 until 4) {
      if ((byteEnable & (1 << b)) > 0) {
        rawBytes(b) = randBytes(b)
      }
    }
    val newData = toNum(rawBytes)
    (byteEnable, randData, newData)
  }

  def main(args: Array[String]): Unit = {
    val compile = SimConfig.addSimulatorFlag("-Wno-CASEINCOMPLETE").addSimulatorFlag("-Wno-TIMESCALEMOD")
      .addRtl("./rtl/axi_ram.v").withWave.allOptimisation.compile(new UncacheTest)
    //    compile.doSim("uncache_read_only", 2021) { dut => {
    //      dut.clockDomain.forkStimulus(10)
    //      dut.clockDomain.assertReset()
    //      sleep(10)
    //      dut.clockDomain.deassertReset()
    //
    //      val initMemData = getMemInitData
    //      initDUT(dut)
    //      for (i <- 0 to 2000) {
    //        val (_, cpuAddr) = getRandomAddr(0xffff)
    //        var waitCycle = rand.nextInt(3)
    //        if (i == 0 && waitCycle == 0) waitCycle = 1
    //        dut.clockDomain.waitRisingEdge(waitCycle)
    //        dut.io.uncache.addr #= cpuAddr
    //        dut.io.uncache.read #= true
    //        dut.io.uncache.write #= false
    //        dut.io.uncache.byteEnable #= rand.nextInt(0xf) + 1
    //        dut.clockDomain.waitFallingEdge() //read can get from the same cycle
    //        while (dut.io.uncache.stall.toBoolean) (dut.clockDomain.waitSampling())
    //        assert(dut.io.uncache.rdata.toLong == initMemData(getMapAddr(cpuAddr)))
    //        dut.io.uncache.read #= false
    //      }
    //    }
    //    }
    //
    //    compile.doSim("Uncached_read_after_write", 2021) { dut =>
    //
    //      dut.clockDomain.forkStimulus(10)
    //      dut.clockDomain.assertReset()
    //      sleep(10)
    //      dut.clockDomain.deassertReset()
    //
    //      initDUT(dut)
    //      for (i <- 0 to 2000) {
    //        val (_, cpuAddr) = getRandomAddr(0xffff)
    //        val randData = rand.nextLong() & 0xffffffffL
    //        var waitCycle = rand.nextInt(3)
    //        if (i == 0 && waitCycle == 0) waitCycle = 1
    //        dut.clockDomain.waitRisingEdge(waitCycle)
    //        dut.io.uncache.addr #= cpuAddr
    //        dut.io.uncache.read #= false
    //        dut.io.uncache.write #= true
    //        dut.io.uncache.wdata #= randData
    //        // write must wait for at least one cycle
    //        dut.clockDomain.waitFallingEdge()
    //        while (dut.io.uncache.stall.toBoolean) (dut.clockDomain.waitSampling())
    //        dut.io.uncache.read #= false
    //        dut.io.uncache.write #= false
    //        dut.clockDomain.waitRisingEdge(rand.nextInt(3) + 1)
    //        dut.io.uncache.read #= true
    //        dut.io.uncache.write #= false
    //        dut.io.uncache.addr #= cpuAddr
    //        dut.clockDomain.waitFallingEdge()
    //        while (dut.io.uncache.stall.toBoolean) (dut.clockDomain.waitSampling())
    //        assert(dut.io.uncache.rdata.toLong == randData)
    //        dut.io.uncache.read #= false
    //        dut.io.uncache.write #= false
    //      }
    //    }
    //
    //    compile.doSim("uncache_random_read_write", 2021) { dut =>
    //      dut.clockDomain.forkStimulus(10)
    //      dut.clockDomain.assertReset()
    //      sleep(10)
    //      dut.clockDomain.deassertReset()
    //
    //      var memData = getMemInitData
    //      initDUT(dut)
    //      for (i <- 0 to 30000) {
    //        val op = rand.nextInt(2) //0: read, 1: write
    //        val (_, cpuAddr) = getRandomAddr(0xffff)
    //        if (op == 0) {
    //          if (i == 0)
    //            dut.clockDomain.waitRisingEdge()
    //          dut.io.uncache.read #= true
    //          dut.io.uncache.write #= false
    //          dut.io.uncache.addr #= cpuAddr
    //          dut.clockDomain.waitFallingEdge() //read can get from the same cycle
    //          while (dut.io.uncache.stall.toBoolean) (dut.clockDomain.waitSampling())
    //          val answer = memData.getOrElse(cpuAddr, memData(getMapAddr(cpuAddr)))
    //          assert(dut.io.uncache.rdata.toLong == answer)
    //        } else if (op == 1) {
    //          val randData = rand.nextLong() & 0xffffffffL
    //          memData += (cpuAddr -> randData)
    //          if (i == 0)
    //            dut.clockDomain.waitRisingEdge()
    //          dut.io.uncache.addr #= cpuAddr
    //          dut.io.uncache.read #= false
    //          dut.io.uncache.write #= true
    //          dut.io.uncache.wdata #= randData
    //          // write must wait for at least one cycle
    //          dut.clockDomain.waitFallingEdge() //read can get from the same cycle
    //          while (dut.io.uncache.stall.toBoolean) (dut.clockDomain.waitSampling())
    //        }
    //        dut.io.uncache.read #= false
    //        dut.io.uncache.write #= false
    //      }
    //    }
    //
    //    compile.doSim("uncache_byteEnable", 2021) { dut =>
    //      dut.clockDomain.forkStimulus(10)
    //      dut.clockDomain.assertReset()
    //      sleep(10)
    //      dut.clockDomain.deassertReset()
    //
    //      var memData = getMemInitData
    //      initDUT(dut)
    //      for (i <- 0 to 30000) {
    //        val op = rand.nextInt(2) //0: read, 1: write
    //        val (_, cpuAddr) = getRandomAddr(0xffff)
    //        val answer = memData.getOrElse(cpuAddr, memData(getMapAddr(cpuAddr)))
    //        var waitCycle = rand.nextInt(3)
    //        if (i == 0 && waitCycle == 0) waitCycle = 1
    //        if (op == 0) {
    //          dut.clockDomain.waitRisingEdge(waitCycle)
    //          dut.io.uncache.read #= true
    //          dut.io.uncache.write #= false
    //          dut.io.uncache.addr #= cpuAddr
    //          dut.io.uncache.byteEnable #= 0xf
    //          dut.clockDomain.waitFallingEdge() //read can get from the same cycle
    //          while (dut.io.uncache.stall.toBoolean) (dut.clockDomain.waitSampling())
    //          //          println("read addr = 0x%x, cpu data = 0x%x, answer = 0x%x".format(cpuAddr, dut.io.cpu.rdata.toLong, memData(randomRamAddr & 0xfffc)))
    //          assert(dut.io.uncache.rdata.toLong == answer)
    //        } else if (op == 1) {
    //          val (byteEnable, randData, newData) = getByteEnableRandomData(answer)
    //          memData += (cpuAddr -> newData)
    //          //          println("write addr = 0x%x, byteEnable = %d, raw data = 0x%x, random data = 0x%x, write data = 0x%x".format(cpuAddr,
    //          //            byteEnable, memData(randomRamAddr & 0xfffc), randData, newData))
    //          dut.clockDomain.waitRisingEdge(waitCycle)
    //          dut.io.uncache.addr #= cpuAddr
    //          dut.io.uncache.read #= false
    //          dut.io.uncache.write #= true
    //          dut.io.uncache.wdata #= randData
    //          dut.io.uncache.byteEnable #= byteEnable
    //          // write must wait for at least one cycle
    //          do (dut.clockDomain.waitSampling()) while (dut.io.uncache.stall.toBoolean)
    //        }
    //        dut.io.uncache.read #= false
    //        dut.io.uncache.write #= false
    //      }
    //    }

    //一百万次随机操作
    val iterNum = 1000000
    val noWave = SimConfig.addSimulatorFlag("-Wno-CASEINCOMPLETE").addSimulatorFlag("-Wno-TIMESCALEMOD")
      .addRtl("./rtl/axi_ram.v").allOptimisation.compile(new UncacheTest)
    noWave.doSim("super_large_cache_and_uncache_random", 2021) { dut => {
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()

      var cacheData = getMemInitData
      var uncacheData = getMemInitData
      initDUT(dut)
      for (i <- 0 until iterNum) {
        if (i % (iterNum / 10) == 0) println("Sim for %d / %d times".format(i, iterNum))
        val op = rand.nextInt(4) //0:cache read, 1:cache write, 2:uncache read, 3:uncache write
        val (_, cpuAddr) = getRandomAddr(0xffff)
        val cacheAns = cacheData.getOrElse(cpuAddr, cacheData(getMapAddr(cpuAddr)))
        val uncacheAns = uncacheData.getOrElse(cpuAddr, uncacheData(getMapAddr(cpuAddr)))
        if (i == 0) dut.clockDomain.waitSampling()
        op match {
          case 0 =>
            dut.io.cpu.read #= true
            dut.io.cpu.write #= false
            dut.io.cpu.addr #= cpuAddr
            dut.io.cpu.byteEnable #= 0xf
          case 1 =>
            val (byteEnable, randData, newData) = getByteEnableRandomData(cacheAns)
            cacheData += (cpuAddr -> newData)
            //              println(s"Cache: addr = 0x%x, byteEnable=${byteEnable.toBinaryString}, raw data = 0x%x, rand data = 0x%x, write data = 0x%x"
            //                .format(cpuAddr, cacheAns, randData, newData))
            dut.io.cpu.read #= false
            dut.io.cpu.write #= true
            dut.io.cpu.addr #= cpuAddr
            dut.io.cpu.byteEnable #= byteEnable
            dut.io.cpu.wdata #= randData
          case 2 =>
            dut.io.uncache.read #= true
            dut.io.uncache.write #= false
            dut.io.uncache.addr #= cpuAddr
            dut.io.uncache.byteEnable #= 0xf
          case 3 =>
            val (byteEnable, randData, newData) = getByteEnableRandomData(uncacheAns)
            uncacheData += (cpuAddr -> newData)
            //              println(s"Uncache: addr = 0x%x, byteEnable=${byteEnable.toBinaryString}, raw data = 0x%x, rand data = 0x%x, write data = 0x%x"
            //                .format(cpuAddr, uncacheAns, randData, newData))
            dut.io.uncache.read #= false
            dut.io.uncache.write #= true
            dut.io.uncache.addr #= cpuAddr
            dut.io.uncache.byteEnable #= byteEnable
            dut.io.uncache.wdata #= randData
        }
        op match {
          case 0 =>
            dut.clockDomain.waitFallingEdge()
            while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
          case 1 =>
            do (dut.clockDomain.waitSampling()) while (dut.io.cpu.stall.toBoolean)
          case 2 =>
            dut.clockDomain.waitFallingEdge()
            while (dut.io.uncache.stall.toBoolean) (dut.clockDomain.waitSampling())
          case 3 =>
            do (dut.clockDomain.waitSampling()) while (dut.io.uncache.stall.toBoolean)
        }
        op match {
          case 0 =>
            //            print("Cache: addr = 0x%x, from cpu = 0x%x, answer=  0x%x".format(cpuAddr, dut.io.cpu.rdata.toLong, cacheAns))
            //            if (cacheData.contains(cpuAddr)) {
            //              println(", answer from 0x%x, = 0x%x".format(cpuAddr, cacheData(cpuAddr)))
            //            } else {
            //              println(", answer from 0x%x, = 0x%x".format(getMapAddr(cpuAddr), cacheData(getMapAddr(cpuAddr))))
            //            }
            assert(dut.io.cpu.rdata.toLong == cacheAns)
          case 2 =>
            //            println("Uncache: addr = 0x%x, from cpu = 0x%x, answer=  0x%x".format(cpuAddr, dut.io.uncache.rdata.toLong, uncacheAns))
            assert(dut.io.uncache.rdata.toLong == uncacheAns)
          case _ =>
        }
        dut.io.cpu.read #= false
        dut.io.cpu.write #= false
        dut.io.uncache.read #= false
        dut.io.uncache.write #= false
        dut.clockDomain.waitSampling(rand.nextInt(4)) //随机等待几个周期
      }
    }
    }
  }
}
