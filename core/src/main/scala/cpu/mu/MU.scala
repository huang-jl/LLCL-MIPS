package cpu.mu

import cpu.defs.ConstantVal._
import spinal.core._

class MU extends Component {
  // in
  val addr = in UInt (32 bits)
  val data_in = in Bits (32 bits)
  val re = in Bool
  val we = in Bool
  val be = in UInt (2 bits)
  val ex = in(MU_EX)

  // out
  val data_out = out Bits (32 bits)

  //
  data_out := 0
}

object MU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MU)
  }
}