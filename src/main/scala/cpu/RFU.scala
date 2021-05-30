package cpu

import spinal.core._

class RFU extends Component {
  /// 读写寄存器

  // in
  val we = in Bool
  val rd = in UInt (5 bits)
  val rd_v = in Bits (32 bits)

  val ra = in UInt (5 bits)
  val rb = in UInt (5 bits)

  // out
  val ra_v = out Bits (32 bits)
  val rb_v = out Bits (32 bits)

  // regs
  val regs = Vec(RegInit(B"32'0"), 32)

  //
  when(we) {
    regs(rd) := rd_v
  }

  ra_v := regs(ra)
  rb_v := regs(rb)
}

object RFU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new RFU)
  }
}