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

  io.cpu >> cpu
  cpu >> dCache.io.cpu
  dCache.io.axi >> ram.io.axi
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

    compile.doSim("DCached_read_after_write", 2021) { dut =>

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
        // write must wait for at least one cycle
        do ((dut.clockDomain.waitSampling())) while (dut.io.cpu.stall.toBoolean)
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

    compile.doSim("small_range_random_read_write", 2021) { dut =>
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
        val randomRamAddr = rand.nextInt(0x1000) & 0xfffc
        val cpuAddr = (0xbfc00000L + randomRamAddr) & ((0x1L << 32) - 1)
//        var waitCycle = rand.nextInt(3)
//        if (i == 0 && waitCycle == 0) waitCycle = 1
        if (op == 0) {
          if(i==0)
            dut.clockDomain.waitRisingEdge()
          dut.io.cpu.read #= true
          dut.io.cpu.write #= false
          dut.io.cpu.addr #= cpuAddr
          dut.clockDomain.waitFallingEdge() //read can get from the same cycle
          while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
          assert(dut.io.cpu.rdata.toLong == memData(randomRamAddr))
        } else if (op == 1) {
          val randData = rand.nextLong() & 0xffffffffL
          memData += (randomRamAddr -> randData)
          if(i==0)
            dut.clockDomain.waitRisingEdge()
          dut.io.cpu.addr #= cpuAddr
          dut.io.cpu.read #= false
          dut.io.cpu.write #= true
          dut.io.cpu.wdata #= randData
          dut.clockDomain.waitFallingEdge()
          if (dut.dCache.comparison.hit.toBoolean) dut.clockDomain.waitSampling()
          else {
            // do (dut.clockDomain.waitSampling()) while (dut.io.cpu.stall.toBoolean)
            while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
          }
        }
        dut.io.cpu.read #= false
        dut.io.cpu.write #= false
      }
    }

    compile.doSim("small_range_byteEnable", 2021) { dut =>
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.assertReset()
      sleep(10)
      dut.clockDomain.deassertReset()

      var memData = getMemInitData
      dut.io.cpu.read #= false
      dut.io.cpu.write #= false
      for (i <- 0 to 30000) {
        val op = rand.nextInt(2) //0: read, 1: write
        var randomRamAddr = rand.nextInt(0x600)
        var waitCycle = rand.nextInt(3)
        if (i == 0 && waitCycle == 0) waitCycle = 1
        if (op == 0) {
          val cpuAddr = (0xbfc00000L + (randomRamAddr & 0xfffc)) & ((0x1L << 32) - 1)
          dut.clockDomain.waitRisingEdge(waitCycle)
          dut.io.cpu.read #= true
          dut.io.cpu.write #= false
          dut.io.cpu.addr #= cpuAddr
          dut.io.cpu.byteEnable #= 2
          dut.clockDomain.waitFallingEdge() //read can get from the same cycle
          while (dut.io.cpu.stall.toBoolean) (dut.clockDomain.waitSampling())
          //          println("read addr = 0x%x, cpu data = 0x%x, answer = 0x%x".format(cpuAddr, dut.io.cpu.rdata.toLong, memData(randomRamAddr & 0xfffc)))
          assert(dut.io.cpu.rdata.toLong == memData(randomRamAddr & 0xfffc))
        } else if (op == 1) {
          val byteEnable = rand.nextInt(3)
          val randData = rand.nextLong() & 0xffffffffL
          val rawBytes = getBytes(memData(randomRamAddr & 0xfffc))
          val randBytes = getBytes(randData)
          var newData = 0L
          if (byteEnable == 0) {
            rawBytes(randomRamAddr & 0x3) = randBytes(randomRamAddr & 0x3)
            newData = toNum(rawBytes)
          } else if (byteEnable == 1) {
            randomRamAddr = randomRamAddr & 0xfffe
            rawBytes(randomRamAddr & 0x3) = randBytes(randomRamAddr & 0x3)
            rawBytes((randomRamAddr & 0x3) + 1) = randBytes((randomRamAddr & 0x3) + 1)
            newData = toNum(rawBytes)
          } else if (byteEnable == 2) {
            randomRamAddr = randomRamAddr & 0xfffc
            newData = randData
          } else {
            throw new Exception
          }
          val cpuAddr = (0xbfc00000L + randomRamAddr) & ((0x1L << 32) - 1)
          //          println("write addr = 0x%x, byteEnable = %d, raw data = 0x%x, random data = 0x%x, write data = 0x%x".format(cpuAddr,
          //            byteEnable, memData(randomRamAddr & 0xfffc), randData, newData))
          memData += ((randomRamAddr & 0xfffc) -> newData)
          dut.clockDomain.waitRisingEdge(waitCycle)
          dut.io.cpu.addr #= cpuAddr
          dut.io.cpu.read #= false
          dut.io.cpu.write #= true
          dut.io.cpu.wdata #= randData
          dut.io.cpu.byteEnable #= byteEnable
          // write must wait for at least one cycle
          do (dut.clockDomain.waitSampling()) while (dut.io.cpu.stall.toBoolean)
        }
        dut.io.cpu.read #= false
        dut.io.cpu.write #= false
      }
    }

  }
}
