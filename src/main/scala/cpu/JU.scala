package cpu

import cpu.defs.ConstantVal._
import spinal.core._

object JU_OP extends SpinalEnum {
  val lz, gez, f, t, e, noe, lez, gz = newElement()
}

object JU_PC_SRC extends SpinalEnum {
  val rs, offset, index1, index2 = newElement()
}

class JU extends Component {
  /// 处理跳转

  // in
  val op = in(JU_OP)
  val pc_src = in(JU_PC_SRC)
  val a = in SInt (32 bits)
  val b = in SInt (32 bits)

  val pc = in UInt (32 bits)
  val offset = in SInt (16 bits)
  val index = in UInt (26 bits)

  // out
  val jump = out Bool
  val jump_pc = out UInt (32 bits)

  //
  import JU_OP._
  import JU_PC_SRC.{offset => _, _}

  jump := op.mux(
    lz -> (a < 0),
    gez -> (a >= 0),
    f -> False,
    t -> True,
    lez -> (a <= 0),
    gz -> (a > 0),
    noe -> (a =/= b),
    e -> (a === b)
  )
  jump_pc := pc_src.mux(
    rs -> U(a),
    JU_PC_SRC.offset -> U(S(pc) + S(offset ## B"00")),
    default -> U(pc(31 downto 28) ## index ## B"00")
  )
}

object JU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new JU)
  }
}
