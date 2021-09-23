package lib

import spinal.core._
import spinal.core.sim._

object Fruits extends SpinalEnum {
  val Apple, Pear, Orange = newElement()
}

object DecoderTest {
  def main(args: Array[String]) = {
    val k1 = Key(Bool).setName("k1")
    val k2 = Key(UInt(2 bits)).setName("k2")
    val k3 = Key(Fruits()).setName("k3")

    SimConfig
      .compile(new Decoder(4 bits) {
        default(k3) to Fruits.Orange
        default(k1) to True

        on(M"01--") {
          set(k1) to False
          set(k2) to U(3, 2 bits)
        }

        on(M"1--0") {
          set(k2) to U(2, 2 bits)
          set(k3) to Fruits.Pear
        }

        on(M"0---") {
          set(k3) to Fruits.Apple
        }
        on(M"-0-1") {
          set(k2) to U(1, 2 bits)
        }
      })
      .doSim { dut =>
        for (i <- 0 until 16) {
          dut.input #= i
          sleep(1)
          val v1 = dut.output(k1).toBoolean
          val v2 = dut.output(k2).toInt
          val v3 = dut.output(k3).toEnum
          assert(v1 == ((i & 12) != 4))
          if ((i & 8) == 0) {
            assert(v3 == Fruits.Apple)
          } else if ((i & 1) == 0) {
            assert(v3 == Fruits.Pear)
          } else {
            assert(v3 == Fruits.Orange)
          }
          if (!v1) {
            assert(v2 == 3)
          } else if (v3 == Fruits.Pear) {
            assert(v2 == 2)
          } else if ((i & 5) == 1) {
            assert(v2 == 1)
          } else {
            assert(v2 == 0)
          }
        }
      }

    ()
  }
}
