package cpu

import spinal.core._
import spinal.lib._

object RFU_RD_SRC extends SpinalEnum {
  val pc, alu, hi, lo, mu = newElement()
}

case class RegRead() extends Bundle with IMasterSlave {
  val index = UInt(5 bits)
  val data = Bits(32 bits)

  def asMaster = {
    out(index)
    in(data)
  }
}

case class RegWrite() extends Bundle {
  val index = UInt(5 bits)
  val data = Bits(32 bits)
}

class RFU extends Component {
  /// 读写寄存器

  val io = new Bundle {
    // in
    val write = slave Flow(RegWrite())

    val ra = slave(RegRead())
    val rb = slave(RegRead())
  }

  // regs
  val regs = Vec(RegInit(B"32'0"), 32)

  //
  when(io.write.valid) {
    regs(io.write.index) := io.write.data
  }

  io.ra.data := regs(io.ra.index)
  io.rb.data := regs(io.rb.index)
}

object RFU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new RFU)
  }
}
