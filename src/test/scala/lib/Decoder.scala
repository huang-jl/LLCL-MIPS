package lib.decoder

import scala.util.Random
import spinal.core._
import spinal.core.sim._

import lib.Key

object Fruits extends SpinalEnum {
  val Apple, Pear, Orange = newElement()
}

object DecoderTest {
  class Dut extends Component {
    val io = new Bundle {
      val input = in Bits (4 bits)
      val v1 = out Bool
      val v2 = out UInt (2 bits)
      val v3 = out(Fruits)
    }

    val factory = new DecoderFactory(4)

    val k1 = Key(Bool)
    val k2 = Key(UInt(2 bits))
    val k3 = Key(Fruits())

    factory.addDefault(k3, Fruits.Orange)
    factory.addDefault(k1, True)
    factory.when(M"01--").set(k1, False).set(k2, U(3, 2 bits))
    factory.when(M"1--0").set(k2, U(2, 2 bits)).set(k3, Fruits.Pear)
    factory.when(M"0---").set(k3, Fruits.Apple)
    factory.when(M"-0-1").set(k2, U(1, 2 bits))

    val decoder = factory.createDecoder(io.input)

    io.v1 := decoder.output(k1)
    io.v2 := decoder.output(k2)
    io.v3 := decoder.output(k3)
  }

  def main(args: Array[String]) = {
    SimConfig.compile(new Dut).doSim { dut =>
      for (i <- 0 until 16) {
        dut.io.input #= i
        sleep(1)
        val v1 = dut.io.v1.toBoolean
        val v2 = dut.io.v2.toInt
        val v3 = dut.io.v3.toEnum
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
