package cache

import spinal.core._
import spinal.core.sim._
import spinal.lib.slave
import spinal.lib.bus.amba4.axi.{Axi4, Axi4Config}

import scala.util.Random
import scala.util.control.Breaks.{break, breakable}
import ram._

import scala.io.Source

class ICacheTest extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface)
  }

  val cpu = new CPUICacheInterface
  val iCache = new ICache(ICacheConfig(lineSize = 128, cacheSize = 128 * 32))
  val ram = new axi_ram(AXIRamConfig(dataWidth = 32, addrWidth = 16, idWidth = 4, pipelineOutput = 0))
  io.cpu >> cpu
  cpu >> iCache.io.cpu
  iCache.io.axi >> ram.io.axi
  ram.io.axi.b.ready := False
  ram.io.axi.w.valid := False
  ram.io.axi.aw.valid := False
  ram.io.axi.w.data := 0
  ram.io.axi.w.last := False
  ram.io.axi.w.strb := 0
  ram.io.axi.aw.id := 0
  ram.io.axi.aw.addr := 0
  ram.io.axi.aw.len := 0
  ram.io.axi.aw.size := 0
  ram.io.axi.aw.prot := 0
  ram.io.axi.aw.cache := 0
  ram.io.axi.aw.lock := 0
  ram.io.axi.aw.burst := 0
}

object ICacheTest {
  val rand = new Random(2021)

  def getMemInitData: Map[Int, Long] = {
    val source = Source.fromFile("./script/data.txt")
    val lineIterator = source.getLines().zipWithIndex
    var res = Map[Int, Long]()
    for ((line, addr) <- lineIterator) {
      res += (addr * 4 -> java.lang.Long.parseLong(line, 16))
    }
    res
  }

  def main(args: Array[String]): Unit = {
    val compile = SimConfig.addSimulatorFlag("-Wno-CASEINCOMPLETE").
      addRtl("./rtl/axi_ram.v").withWave.allOptimisation.compile(new ICacheTest)
    compile.doSim("ICache_small_range_random_read", 2021) { dut => {
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()
      val memData = getMemInitData
      dut.io.cpu.read #= false
      for (index <- 0 to 10000) {
        var waitCycle = rand.nextInt(3)
        if (index == 0 && waitCycle == 0) waitCycle = 1
        dut.clockDomain.waitRisingEdge(waitCycle)
        val randomMemAddr = rand.nextInt(0x200) & 0xfffc
        val randomAddr = (0xbfc00000L + randomMemAddr) & ((0x1L << 32) - 1)
        dut.io.cpu.read #= true
        dut.io.cpu.addr #= randomAddr
        dut.clockDomain.waitFallingEdge()
        while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
        assert(dut.io.cpu.data.toLong == memData(randomMemAddr))
        dut.io.cpu.read #= false
      }
    }
    }

    compile.doSim("ICache_large_range_random_read", 2021) { dut => {
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()
      val memData = getMemInitData
      dut.io.cpu.read #= false
      for (index <- 0 to 10000) {
        var waitCycle = rand.nextInt(3)
        if (index == 0 && waitCycle == 0) waitCycle = 1
        dut.clockDomain.waitRisingEdge(waitCycle)
        val randomMemAddr = rand.nextInt(0xffff) & 0xfffc
        val randomAddr = (0xbfc00000L + randomMemAddr) & ((0x1L << 32) - 1)
        dut.io.cpu.read #= true
        dut.io.cpu.addr #= randomAddr
        dut.clockDomain.waitFallingEdge()
        while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
        assert(dut.io.cpu.data.toLong == memData(randomMemAddr))
        dut.io.cpu.read #= false
      }
    }
    }
  }
}
