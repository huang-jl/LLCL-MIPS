package cpu.ju

import cpu.defs.ConstantVal.JU_OP._
import cpu.defs.ConstantVal.JU_PC_SRC._
import cpu.defs.ConstantVal._
import spinal.core._

class JU extends Component {
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
  jump := op.mux(lz -> (a < 0), gez -> (a >= 0), f -> False, t -> True, lez -> (a <= 0), gz -> (a > 0), noe -> (a =/= b), e -> (a === b))
  jump_pc := pc_src.mux(rs -> a.asUInt, JU_PC_SRC.offset -> (pc.asSInt + (offset ## B"00").asSInt).asUInt, default -> (pc(31 downto 28) ## index ## B"00").asUInt)
}

object JU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new JU)
  }
}
