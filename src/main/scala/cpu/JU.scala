package cpu

import spinal.core._

object JU_OP extends SpinalEnum {
  val lz, gez, f, t, e, noe, lez, gz = newElement()
}

object JU_PC_SRC extends SpinalEnum {
  val rs, offset, index = newElement()
}

/** @note 加入分支预测后，JU的功能是在EX阶段判断是否发生跳转
  */
class JU extends Component {

  // in
  val op = in(JU_OP)
  val a  = in SInt (32 bits)
  val b  = in SInt (32 bits)

  // out
  val jump = out Bool ()

  val lz  = a(31)        // 最高位为 1 表示小于 0
  val gez = !lz
  val lez = lz | a === 0 // 最高位为 1 或等于 0
  val gz  = !lez

  jump := op.mux(
    JU_OP.lz  -> lz,
    JU_OP.gez -> gez,
    JU_OP.f   -> False,
    JU_OP.t   -> True,
    JU_OP.lez -> lez,
    JU_OP.gz  -> gz,
    JU_OP.noe -> (a =/= b),
    JU_OP.e   -> (a === b)
  )
}

object JU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new JU)
  }
}
