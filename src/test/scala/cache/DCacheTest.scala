package cache

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import scala.io.Source
import scala.util.Random

import ram._


//class DCacheTest extends Component {
//  val io = new Bundle {
//    val cpu = slave(new CPUDCacheInterface)
//    val uncache = slave(new CPUDCacheInterface)
//  }
//  val cpu = new CPUDCacheInterface
//  val dCache = new DCache(CacheRamConfig(32, 4), 8)
//  val ram = new axi_ram(AXIRamConfig(32, DCacheTest.addrWidth, 4))
//  val uncacheRam = new axi_ram(AXIRamConfig(32, DCacheTest.addrWidth, 4))
//
//  //  io.cpu >> cpu
//  //  cpu >> dCache.io.cpu
//  io.cpu >> dCache.io.cpu
//  io.uncache >> dCache.io.uncache
//  dCache.io.axi >> ram.io.axi
//  dCache.io.uncacheAXI >> uncacheRam.io.axi
//}
//
//
//object DCacheTest {
//  val rand = new Random(2021)
//  val addrWidth: Int = 20
//  val printBase: Int = 5000
//
//  def getMapAddr(addr: Long): Long = {
//    addr & ((0x1 << addrWidth) - 1)
//  }
//
//  def getMemInitData: Map[Long, Long] = {
//    val source = Source.fromFile("./script/data.txt")
//    val lineIterator = source.getLines().zipWithIndex
//    var res = Map[Long, Long]()
//    for ((line, addr) <- lineIterator) {
//      res += ((addr * 4).toLong -> java.lang.Long.parseLong(line, 16))
//    }
//    res
//  }
//
//  def getBytes(num: Long): Array[Long] = {
//    val byte1 = num & 0xff
//    val byte2 = (num >> 8) & 0xff
//    val byte3 = (num >> 16) & 0xff
//    val byte4 = (num >> 24) & 0xff
//    Array(byte1, byte2, byte3, byte4)
//  }
//
//  def toNum(l: Array[Long]): Long = {
//    (l(3) << 24) | (l(2) << 16) | (l(1) << 8) | l(0)
//  }
//
//  //range: 后16位变动的范围
//  def getRandomAddr(range: Int): (Int, Long) = {
//    val baseRange = 1 << (addrWidth - 16)
//    val randomRamAddr = rand.nextInt(range) & 0xfffc
//    var cpuBase = (rand.nextInt(baseRange) & 0xffff) + 0xbfc0
//    cpuBase = Math.min(cpuBase, 0xffff)
//    val cpuAddr = ((cpuBase.toLong << 16) | randomRamAddr)
//    //    println("base = 0x%x, rand = 0x%x, cpu addr = 0x%x".format(cpuBase, randomRamAddr, cpuAddr))
//    (randomRamAddr, cpuAddr)
//  }
//
//  def main(args: Array[String]): Unit = {
//    val compile = SimConfig.addSimulatorFlag("-Wno-CASEINCOMPLETE").addSimulatorFlag("-Wno-TIMESCALEMOD")
//      .addRtl("./rtl/axi_ram.v").withWave.allOptimisation.compile(new DCacheTest)
//    compile.doSim("DCache_read_only", 2021) { dut => {
//      dut.clockDomain.forkStimulus(10)
//      dut.clockDomain.assertReset()
//      sleep(10)
//      dut.clockDomain.deassertReset()
//
//      val initMemData = getMemInitData
//      dut.io.cpu.byteEnable #= 0xf
//      dut.io.cpu.read #= false
//      dut.io.cpu.write #= false
//      dut.io.uncache.addr #= 0
//      dut.io.uncache.read #= false
//      dut.io.uncache.write #= false
//      dut.io.uncache.byteEnable #= 0xf
//      dut.io.uncache.wdata #= 0
//      for (i <- 0 to 2000) {
//        val (randomRamAddr, cpuAddr) = getRandomAddr(0xffff)
//        var waitCycle = rand.nextInt(3)
//        if (i == 0 && waitCycle == 0) waitCycle = 1
//        dut.clockDomain.waitRisingEdge(waitCycle)
//        dut.io.cpu.addr #= cpuAddr
//        dut.io.cpu.read #= true
//        dut.io.cpu.write #= false
//        dut.clockDomain.waitFallingEdge() //read can get from the same cycle
//        while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
//        //        println("addr = 0x%x, data from cpu = 0x%x, answer = 0x%x".format(randomRamAddr, dut.io.cpu.rdata.toLong, initMemData(randomRamAddr)))
//        assert(dut.io.cpu.rdata.toLong == initMemData(getMapAddr(cpuAddr)))
//        dut.io.cpu.read #= false
//      }
//    }
//    }
//
//    compile.doSim("DCached_read_after_write", 2021) { dut =>
//
//      dut.clockDomain.forkStimulus(10)
//      dut.clockDomain.assertReset()
//      sleep(10)
//      dut.clockDomain.deassertReset()
//
//      dut.io.cpu.byteEnable #= 0xf
//      dut.io.cpu.read #= false
//      dut.io.cpu.write #= false
//      dut.io.uncache.addr #= 0
//      dut.io.uncache.read #= false
//      dut.io.uncache.write #= false
//      dut.io.uncache.byteEnable #= 0xf
//      dut.io.uncache.wdata #= 0
//      val origin = getMemInitData
//      for (i <- 0 to 2000) {
//        val (_, cpuAddr) = getRandomAddr(0xffff)
//        val randData = rand.nextLong() & 0xffffffffL
//        var waitCycle = rand.nextInt(3)
//        if (i == 0 && waitCycle == 0) waitCycle = 1
//        dut.clockDomain.waitRisingEdge(waitCycle)
//        dut.io.cpu.addr #= cpuAddr
//        dut.io.cpu.read #= false
//        dut.io.cpu.write #= true
//        dut.io.cpu.wdata #= randData
//        // write must wait for at least one cycle
//        do ((dut.clockDomain.waitSampling())) while (dut.io.cpu.stall.toBoolean)
//        dut.io.cpu.read #= false
//        dut.io.cpu.write #= false
//        dut.clockDomain.waitRisingEdge(rand.nextInt(3) + 1)
//        dut.io.cpu.read #= true
//        dut.io.cpu.write #= false
//        dut.io.cpu.addr #= cpuAddr
//        dut.clockDomain.waitFallingEdge()
//        //        println("CPU data = 0x%x, answer = 0x%x, origin = 0x%x".format(dut.io.cpu.rdata.toLong, randData, origin(randomRamAddr)))
//        assert(!dut.io.cpu.stall.toBoolean && dut.io.cpu.rdata.toLong == randData)
//      }
//    }
//
//    compile.doSim("large_range_random_read_write", 2021) { dut =>
//      dut.clockDomain.forkStimulus(10)
//      dut.clockDomain.assertReset()
//      sleep(10)
//      dut.clockDomain.deassertReset()
//
//      var memData = getMemInitData
//      dut.io.cpu.byteEnable #= 0xf
//      dut.io.cpu.read #= false
//      dut.io.cpu.write #= false
//      dut.io.uncache.addr #= 0
//      dut.io.uncache.read #= false
//      dut.io.uncache.write #= false
//      dut.io.uncache.byteEnable #= 0xf
//      dut.io.uncache.wdata #= 0
//      for (i <- 0 to 30000) {
//        val op = rand.nextInt(2) //0: read, 1: write
//        val (randomRamAddr, cpuAddr) = getRandomAddr(0x1000)
//        val answer = memData.getOrElse(cpuAddr, memData(getMapAddr(cpuAddr)))
//        //        val cpuAddr = (0xbfc00000L + randomRamAddr) & ((0x1L << 32) - 1)
//        if (op == 0) {
//          if (i == 0)
//            dut.clockDomain.waitRisingEdge()
//          dut.io.cpu.read #= true
//          dut.io.cpu.write #= false
//          dut.io.cpu.addr #= cpuAddr
//          dut.clockDomain.waitFallingEdge() //read can get from the same cycle
//          while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
//          if (i % printBase == 0) {
//            println("read addr = 0x%x, cpu data = 0x%x, answer = 0x%x".format(cpuAddr, dut.io.cpu.rdata.toLong, answer))
//          }
//          assert(dut.io.cpu.rdata.toLong == answer)
//        } else if (op == 1) {
//          val randData = rand.nextLong() & 0xffffffffL
//          if (i % printBase == 0) {
//            println(s"write addr = 0x%x, raw data = 0x%x, write data = 0x%x".format(cpuAddr,
//              answer, randData))
//          }
//          memData += (cpuAddr -> randData)
//          if (i == 0)
//            dut.clockDomain.waitRisingEdge()
//          dut.io.cpu.addr #= cpuAddr
//          dut.io.cpu.read #= false
//          dut.io.cpu.write #= true
//          dut.io.cpu.wdata #= randData
//          dut.clockDomain.waitFallingEdge()
//          do (dut.clockDomain.waitSampling()) while (dut.io.cpu.stall.toBoolean)
//        }
//        dut.io.cpu.read #= false
//        dut.io.cpu.write #= false
//      }
//    }
//
//    val iterNum = 1000000
//    val noWave = SimConfig.addSimulatorFlag("-Wno-CASEINCOMPLETE").addSimulatorFlag("-Wno-TIMESCALEMOD")
//      .addRtl("./rtl/axi_ram.v").allOptimisation.compile(new DCacheTest)
//    noWave.doSim("super_large_byteEnable", 2021) { dut =>
//      dut.clockDomain.forkStimulus(10)
//      dut.clockDomain.assertReset()
//      sleep(10)
//      dut.clockDomain.deassertReset()
//
//      var memData = getMemInitData
//      dut.io.cpu.read #= false
//      dut.io.cpu.write #= false
//      dut.io.uncache.addr #= 0
//      dut.io.uncache.read #= false
//      dut.io.uncache.write #= false
//      dut.io.uncache.byteEnable #= 0xf
//      dut.io.uncache.wdata #= 0
//      for (i <- 0 to iterNum) {
//        if (i % (iterNum / 10) == 0) println("Sim for %d / %d times".format(i, iterNum))
//        val op = rand.nextInt(2) //0: read, 1: write
//        val (randomRamAddr, cpuAddr) = getRandomAddr(0xffff)
//        val answer = memData.getOrElse(cpuAddr, memData(getMapAddr(cpuAddr)))
//        var waitCycle = rand.nextInt(3)
//        if (i == 0 && waitCycle == 0) waitCycle = 1
//        if (op == 0) {
//          dut.clockDomain.waitRisingEdge(waitCycle)
//          dut.io.cpu.read #= true
//          dut.io.cpu.write #= false
//          dut.io.cpu.addr #= cpuAddr
//          dut.io.cpu.byteEnable #= 0xf
//          dut.clockDomain.waitFallingEdge() //read can get from the same cycle
//          while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
////          if (i % printBase == 0) {
////            println("read addr = 0x%x, cpu data = 0x%x, answer = 0x%x".format(cpuAddr, dut.io.cpu.rdata.toLong, answer))
////          }
//          assert(dut.io.cpu.rdata.toLong == answer)
//        } else if (op == 1) {
//          val byteEnable = rand.nextInt(0xf) + 1
//          val randData = rand.nextLong() & 0xffffffffL
//          val rawBytes = getBytes(answer) //0->最低byte, 1->次低byte
//          val randBytes = getBytes(randData)
//          for (b <- 0 until 4) {
//            if ((byteEnable & (1 << b)) > 0) {
//              rawBytes(b) = randBytes(b)
//            }
//          }
//          val newData = toNum(rawBytes)
//          memData += (cpuAddr -> newData)
////          if (i % printBase == 0) {
////            println(s"write addr = 0x%x, byteEnable = ${byteEnable.toBinaryString}, raw data = 0x%x, random data = 0x%x, write data = 0x%x".format(cpuAddr,
////              answer, randData, newData))
////          }
//          dut.clockDomain.waitRisingEdge(waitCycle)
//          dut.io.cpu.addr #= cpuAddr
//          dut.io.cpu.read #= false
//          dut.io.cpu.write #= true
//          dut.io.cpu.wdata #= randData
//          dut.io.cpu.byteEnable #= byteEnable
//          // write must wait for at least one cycle
//          do (dut.clockDomain.waitSampling()) while (dut.io.cpu.stall.toBoolean)
//        }
//        dut.io.cpu.read #= false
//        dut.io.cpu.write #= false
//      }
//    }
//
//  }
//}
