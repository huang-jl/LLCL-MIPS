package cpu

import spinal.core._
import scala.language.postfixOps

object CompareOp extends SpinalEnum {
  /** NZ = not zero, EZ = equal zero */
  val NZ, EZ = newElement()
}

/** 比较器，用于MOVN和MOVZ */
class Comparator extends Component {
  val io = new Bundle {
    val operand = in Bits (32 bits)
    val op      = in(CompareOp)
    val mov     = out Bool()
  }

  io.mov := io.op.mux(
    CompareOp.NZ ->  (io.operand =/= 0),
    CompareOp.EZ -> (io.operand === 0)
  )
}
