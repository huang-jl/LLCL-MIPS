package cache

import spinal.core.sim._
import scala.util.Random

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
      SimConfig.addSimulatorFlag("-Wno-CASEINCOMPLETE").withWave.allOptimisation.compile(new LRUManegr(way))
        .doSim("LRU_Test_8_way", 2021) { dut => {
          dut.clockDomain.forkStimulus(10)
          dut.clockDomain.assertReset()
          sleep(10)
          dut.clockDomain.deassertReset()
          dut.clockDomain.waitSampling()
          var status: Array[Array[Int]] = Array(Array(0), Array(0, 0), Array(0, 0, 0, 0))
          for (iter <- 0 until 200) {
            val num = rand.nextInt(way)
            access(num, status, width)
            dut.io.access #= 1 << num
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
