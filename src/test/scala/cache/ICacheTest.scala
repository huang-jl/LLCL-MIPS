package cache

import spinal.core._
import spinal.core.sim._
import spinal.lib.slave
import spinal.lib.bus.amba4.axi.{Axi4, Axi4Config}

import scala.util.Random
import scala.util.control.Breaks.{break, breakable}
import ram._

import scala.io.Source

//class ICacheTest extends Component {
//  val io = new Bundle {
//    val cpu = slave(new CPUICacheInterface)
//  }
//
//  val cpu = new CPUICacheInterface
//  val iCache = new ICache(CacheRamConfig(32, 7, 8, true))
//  val ram = new axi_ram(AXIRamConfig(32, ICacheTest.addrWidth, idWidth = 4))
//  io.cpu >> cpu
//  cpu >> iCache.io.cpu
//  iCache.io.axi >> ram.io.axi
//}
//
//object ICacheTest {
//  val rand = new Random(2021)
//  val addrWidth = 20
//  val iterNum = 500000
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
//    val compile = SimConfig.addSimulatorFlag("-Wno-CASEINCOMPLETE").addSimulatorFlag("-Wno-TIMESCALEMOD").
//      addRtl("./rtl/axi_ram.v").withWave.allOptimisation.compile(new ICacheTest)
//    compile.doSim("ICache_small_range_random_read", 2021) { dut => {
//      dut.clockDomain.forkStimulus(10)
//      dut.clockDomain.assertReset()
//      sleep(10)
//      dut.clockDomain.deassertReset()
//      val memData = getMemInitData
//      dut.io.cpu.read #= false
//      for (index <- 0 to 10000) {
//        var waitCycle = rand.nextInt(3)
//        if (index == 0 && waitCycle == 0) waitCycle = 1
//        dut.clockDomain.waitRisingEdge(waitCycle)
//        val (_, randomAddr) = getRandomAddr(0x1000)
//        if (index % 1000 == 0) {
//          println("randomAddr = 0x%x".format(randomAddr))
//        }
//        dut.io.cpu.read #= true
//        dut.io.cpu.addr #= randomAddr
//        dut.clockDomain.waitFallingEdge()
//        while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
//        assert(dut.io.cpu.data.toLong == memData(getMapAddr(randomAddr)))
//        dut.io.cpu.read #= false
//      }
//    }
//    }
//
//    compile.doSim("ICache_large_range_random_read", 2021) { dut => {
//      dut.clockDomain.forkStimulus(10)
//      dut.clockDomain.assertReset()
//      sleep(10)
//      dut.clockDomain.deassertReset()
//      val memData = getMemInitData
//      dut.io.cpu.read #= false
//      for (index <- 0 to iterNum) {
//        if (index % (iterNum / 10) == 0) println("Sim for %d / %d times".format(index, iterNum))
//        var waitCycle = rand.nextInt(3)
//        if (index == 0 && waitCycle == 0) waitCycle = 1
//        dut.clockDomain.waitRisingEdge(waitCycle)
//        val (_, randomAddr) = getRandomAddr(0x1000)
//        dut.io.cpu.read #= true
//        dut.io.cpu.addr #= randomAddr
//        dut.clockDomain.waitFallingEdge()
//        while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
//        assert(dut.io.cpu.data.toLong == memData(getMapAddr(randomAddr)))
//        dut.io.cpu.read #= false
//      }
//    }
//    }
//  }
//}
