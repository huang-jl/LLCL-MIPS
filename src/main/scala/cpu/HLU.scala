package cpu

import spinal.core._

object HLU_SRC extends SpinalEnum {
  val rs, alu = newElement()
}

class HLU extends Component {
  val hi_we  = in Bool ()
  val new_hi = in Bits (32 bits)
  val hi_v   = out Bits (32 bits)

  val lo_we  = in Bool ()
  val new_lo = in Bits (32 bits)
  val lo_v   = out Bits (32 bits)

  hi_v := RegNextWhen(new_hi, hi_we) init 0
  lo_v := RegNextWhen(new_lo, lo_we) init 0
}

object HLU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new HLU)
  }
}
