package cpu

import spinal.core._
import scala.language.postfixOps

object JU_OP extends SpinalEnum {
  val lz, gez, f, t, e, noe, lez, gz = newElement()
}

object JU_PC_SRC extends SpinalEnum {
  val rs, offset, index = newElement()
}

/** @note 加入分支预测后，JU的功能是在EX阶段判断是否发生跳转 */
class JU extends Component {

  // in
  val op = in(JU_OP)
  val a  = in SInt (32 bits)
  val b  = in SInt (32 bits)

  // out
  val jump = out Bool ()

  jump := op.mux(
    JU_OP.lz  -> (a < 0),
    JU_OP.gez -> (a >= 0),
    JU_OP.f   -> False,
    JU_OP.t   -> True,
    JU_OP.lez -> (a <= 0),
    JU_OP.gz  -> (a > 0),
    JU_OP.noe -> (a =/= b),
    JU_OP.e   -> (a === b)
  )
}

object JU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new JU)
  }
}
