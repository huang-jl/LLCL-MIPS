package cpu

import spinal.core._
import spinal.lib._

object RFU_RD_SRC extends SpinalEnum {
  val pc, alu, hi, lo, mu, cp0 = newElement()
}

case class RegWrite() extends Bundle {
  val index = UInt(5 bits)
  val data  = Bits(32 bits)
}

case class RegRead() extends Bundle with IMasterSlave {
  val index = UInt(5 bits)
  val data  = Bits(32 bits)

  def asMaster = {
    out(index)
    in(data)
  }
}

class RFU(numW: Int = 1, numR: Int = 2) extends Component {
  /// 读写寄存器

  val io = new Bundle {
    val w = Vec(slave(Flow(RegWrite())), numW)
    val r = Vec(slave(RegRead()), numR)
  }

//  val mem = Vec(Reg(Bits(32 bits)), 32)
val mem = Vec(RegInit(B"32'h0"), 32)
//  mem(0).init(0)

  for (i <- 0 until numW) {
    when(io.w(i).valid) { mem(io.w(i).index) := io.w(i).data }
  }

  for (i <- 0 until numR) {
    io.r(i).data := mem(io.r(i).index)
    for (j <- 0 until numW) {
      when(io.w(j).valid & io.w(j).index === io.r(i).index) {
        io.r(i).data := io.w(j).data
      }
    }
  }
}

object RFU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new RFU)
  }
}
