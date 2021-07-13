package cache

import cache.LRUTest.access
import lib.Updating
import spinal.core._
import spinal.core.sim._

import scala.util.Random

class LRUTest(wayNum: Int = 8) extends Component {
  val io = new Bundle {
    val access = in UInt (log2Up(wayNum) bits)
    val update = in Bool ()
    val next   = out UInt (log2Up(wayNum) bits)
  }
  val manager    = new LRUManegr(wayNum, 1)
  val calculator = new LRUCalculator(wayNum)
  manager.io.write.en := io.update
  manager.io.write.access := io.access
  manager.io.write.addr := 0

  manager.io.read.addr := 0
  io.next := calculator.leastRecentUsedIndex(manager.io.read.data)
}

object LRUTest {
  //下面是两个手动模拟plru的测例
  def access(number: Int, status: Array[Array[Int]], width: Int): Unit = {
    val num: Array[Int] = Array.tabulate(width)(index => {
      (number >> (width - 1 - index)) & 0x1
    })
    var pre = 0
    for (i <- 0 until width) {
      status(i)(pre) = num(i)
      pre = (pre << 1) | num(i)
    }
  }

  def getNext(status: Array[Array[Int]], width: Int): Int = {
    var res = 0
    for (i <- 0 until width) {
      if (status(i)(res) == 0) {
        res = (res << 1) | 1
      } else {
        res = (res << 1) | 0
      }
    }
    res
  }

  def main(args: Array[String]): Unit = {
    val rand = new Random(2021)
    for (width <- 1 to 3) {
      val way = 1 << width
      SimConfig
        .addSimulatorFlag("-Wno-CASEINCOMPLETE")
        .withWave
        .allOptimisation
        .compile(new LRUTest(way))
        .doSim(s"LRU_Test_${width}", 2021) { dut =>
          {
            dut.clockDomain.forkStimulus(10)
            dut.clockDomain.assertReset()
            sleep(10)
            dut.clockDomain.deassertReset()
            dut.clockDomain.waitSampling()
            var status: Array[Array[Int]] = Array(Array(0), Array(0, 0), Array(0, 0, 0, 0))
            for (iter <- 0 until 20000) {
              val num = rand.nextInt(way)
              access(num, status, width)
              dut.io.access #= num
              dut.io.update #= true
              dut.clockDomain.waitSampling()
              dut.io.update #= false
              dut.clockDomain.waitFallingEdge()
              assert(dut.io.next.toInt == getNext(status, width))
              dut.clockDomain.waitSampling(rand.nextInt(3))
            }
          }
        }
    }

  }
}
