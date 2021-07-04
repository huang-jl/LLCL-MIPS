package cpu

import spinal.core._
import defs.ConstantVal._

object JU_OP extends SpinalEnum {
  val lz, gez, f, t, e, noe, lez, gz = newElement()
}

object JU_PC_SRC extends SpinalEnum {
  val rs, offset, index = newElement()
}

class JU extends Component {
  /// 处理跳转

  // in
  val op = in(JU_OP)
//  val pc_src = in(JU_PC_SRC)
  val a = in SInt (32 bits)
  val b = in SInt (32 bits)

//  val pc = in UInt (32 bits)
//  val offset = in SInt (16 bits)
//  val index = in UInt (26 bits)

  // out
  val jump = out Bool
//  val jump_pc = out UInt (32 bits)

  val lz = a(31)  //最高位为1表示小于0
  val gez = !lz
  val gz = !a(31) & a =/= 0 //最高位为0且不等于1
  val lez = !gz

  jump := op.mux(
    JU_OP.lz -> lz,
    JU_OP.gez -> gez,
    JU_OP.f -> False,
    JU_OP.t -> True,
    JU_OP.lez -> lez,
    JU_OP.gz -> gz,
    JU_OP.noe -> (a =/= b),
    JU_OP.e -> (a === b)
  )
//  jump_pc := pc_src.mux(
//    JU_PC_SRC.rs -> U(a),
//    JU_PC_SRC.offset -> U(S(pc) + S(offset ## B"00")),
//    default -> U(pc(31 downto 28) ## index ## B"00")
//  )
}

object JU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new JU)
  }
}
