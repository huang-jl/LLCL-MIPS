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

//  val lz = a(31)  //最高位为1表示小于0
//  val gez = !lz
//  val gz = !a(31) & a =/= 0 //最高位为0且不等于1
//  val lez = !gz

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
