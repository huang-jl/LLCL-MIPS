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
  val  cpu = new CPUDCacheInterface
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
    compile.doSim("DCacheRead", 2021) { dut => {
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()

      val initMemData = getMemInitData
      for(i <- 0 to 200){
        val randomRamAddr = rand.nextInt(0xffff) & 0xfffc
        val cpuAddr = (0xbfc00000L + randomRamAddr) & ((0x1L << 32) - 1)
        dut.clockDomain.waitRisingEdge(rand.nextInt(3) + 1)
        dut.io.cpu.addr #= cpuAddr
        dut.io.cpu.read #= true
        dut.io.cpu.write #= false
        dut.clockDomain.waitFallingEdge()
        while(dut.io.cpu.stall.toBoolean)(dut.clockDomain.waitSampling())
        assert(dut.io.cpu.rdata.toLong == initMemData(randomRamAddr))
        dut.io.cpu.read #= false
      }
    }
    }

    compile.doSim("DCachedWrite", 2021) {dut => {

      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()

      val initMemData = getMemInitData
      var writeData = Map[Long, Long]()
      for(i <- 0 to 200){
        val randomRamAddr = rand.nextInt(0xffff)
        val cpuAddr = (0xbfc00000L + randomRamAddr) & ((0x1L << 32) - 1)
        val randData = rand.nextLong() & 0xffffffff
        writeData += (cpuAddr -> randData)
        dut.clockDomain.waitRisingEdge(rand.nextInt(3) + 1)
        dut.io.cpu.addr #= cpuAddr
        dut.io.cpu.read #= false
        dut.io.cpu.write #= true
        dut.io.cpu.wdata #= randData
        dut.clockDomain.waitFallingEdge()
        while(dut.io.cpu.stall.toBoolean)(dut.clockDomain.waitSampling())
        dut.io.cpu.read #= false
        dut.io.cpu.write #= false
        dut.clockDomain.waitRisingEdge(rand.nextInt(3))
        dut.io.cpu.read #= true
        dut.io.cpu.write #= false
        dut.io.cpu.addr #= cpuAddr
        dut.clockDomain.waitFallingEdge()
        assert(!dut.io.cpu.stall.toBoolean && (dut.io.cpu.rdata.toLong == writeData(cpuAddr)))
      }
    }}
  }
}
