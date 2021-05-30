package cpu

import spinal.core._

object HLU_SRC extends SpinalEnum {
  val rs, alu = newElement()
}

class HLU extends Component {
  val hi_we = in Bool
  val new_hi = in Bits (32 bits)
  val hi_v = out(RegNextWhen(new_hi, hi_we))

  val lo_we = in Bool
  val new_lo = in Bits (32 bits)
  val lo_v = out(RegNextWhen(new_lo, lo_we))
}

object HLU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new HLU)
  }
}
