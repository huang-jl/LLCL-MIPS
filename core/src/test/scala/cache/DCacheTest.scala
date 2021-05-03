package cache

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import scala.io.Source
import scala.util.Random

import ram._

class DCacheTest extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUDCacheInterface)
  }
  val cpu = new CPUDCacheInterface
  val dCache = new DCache(DCacheConfig())
  val ram = new axi_ram(AXIRamConfig(32, 16, 4))
  cpu >> dCache.io.cpu
  io.cpu >> cpu
  dCache.io.cachedAxi >> ram.io.axi
}

object DCacheTest {
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
    val compile = SimConfig.addSimulatorFlag("-Wno-CASEINCOMPLETE").addRtl("./rtl/axi_ram.v").withWave.allOptimisation.compile(new DCacheTest)
    compile.doSim("DCache_read_only", 2021) { dut => {
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()

      val initMemData = getMemInitData
      dut.io.cpu.byteEnable #= 2
      dut.io.cpu.read #= false
      dut.io.cpu.write #= false
      for (i <- 0 to 200) {
        val randomRamAddr = rand.nextInt(0xffff) & 0xfffc
        val cpuAddr = (0xbfc00000L + randomRamAddr) & ((0x1L << 32) - 1)
        var waitCycle = rand.nextInt(3)
        if (i == 0 && waitCycle == 0) waitCycle = 1
        dut.clockDomain.waitRisingEdge(waitCycle)
        dut.io.cpu.addr #= cpuAddr
        dut.io.cpu.read #= true
        dut.io.cpu.write #= false
        dut.clockDomain.waitFallingEdge() //read can get from the same cycle
        while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
        assert(dut.io.cpu.rdata.toLong == initMemData(randomRamAddr))
        dut.io.cpu.read #= false
      }
    }
    }

    compile.doSim("DCached_read_after_write", 2021) { dut => {

      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()

      dut.io.cpu.byteEnable #= 2
      dut.io.cpu.read #= false
      dut.io.cpu.write #= false
      for (i <- 0 to 200) {
        val randomRamAddr = rand.nextInt(0xffff) & 0xfffc
        val cpuAddr = (0xbfc00000L + randomRamAddr) & ((0x1L << 32) - 1)
        val randData = rand.nextLong() & 0xffffffffL
        var waitCycle = rand.nextInt(3)
        if (i == 0 && waitCycle == 0) waitCycle = 1
        dut.clockDomain.waitRisingEdge(waitCycle)
        dut.io.cpu.addr #= cpuAddr
        dut.io.cpu.read #= false
        dut.io.cpu.write #= true
        dut.io.cpu.wdata #= randData
        dut.clockDomain.waitRisingEdge() // write must wait for at least one cycle
        while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
        dut.io.cpu.read #= false
        dut.io.cpu.write #= false
        dut.clockDomain.waitRisingEdge(rand.nextInt(3) + 1)
        dut.io.cpu.read #= true
        dut.io.cpu.write #= false
        dut.io.cpu.addr #= cpuAddr
        dut.clockDomain.waitFallingEdge()
        assert(!dut.io.cpu.stall.toBoolean && dut.io.cpu.rdata.toLong == randData)
      }
    }
    }

    compile.doSim("Random_read_write", 2021) { dut =>
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()

      var memData = getMemInitData
      dut.io.cpu.byteEnable #= 2
      dut.io.cpu.read #= false
      dut.io.cpu.write #= false
      for (i <- 0 to 10000) {
        val op = rand.nextInt(2) //0: read, 1: write
        val randomRamAddr = rand.nextInt(0xffff) & 0xfffc
        val cpuAddr = (0xbfc00000L + randomRamAddr) & ((0x1L << 32) - 1)
        var waitCycle = rand.nextInt(3)
        if (i == 0 && waitCycle == 0) waitCycle = 1
        if (op == 0) {
          dut.clockDomain.waitRisingEdge(waitCycle)
          dut.io.cpu.read #= true
          dut.io.cpu.write #= false
          dut.io.cpu.addr #= cpuAddr
          dut.clockDomain.waitFallingEdge() //read can get from the same cycle
          while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
          assert(dut.io.cpu.rdata.toLong == memData(randomRamAddr))
        } else if (op == 1) {
          val randData = rand.nextLong() & 0xffffffffL
          memData += (randomRamAddr -> randData)
          dut.clockDomain.waitRisingEdge(waitCycle)
          dut.io.cpu.addr #= cpuAddr
          dut.io.cpu.read #= false
          dut.io.cpu.write #= true
          dut.io.cpu.wdata #= randData
          // write must wait for at least one cycle
          do (dut.clockDomain.waitSampling()) while (dut.io.cpu.stall.toBoolean)
        }
        dut.io.cpu.read #= false
        dut.io.cpu.write #= false
      }
    }
  }
}
