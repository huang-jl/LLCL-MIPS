package cpu

import cpu.defs.ConstantVal._
import spinal.core._

class PCU extends Component {
  /// 控制 PC

  // in
  val stall = in Bool
  val we = in Bool
  val new_pc = in UInt (32 bits)

  // out
  val pc = out(RegInit(INIT_PC))

  val E = new Bundle {
    val AdEIL = out Bool
  }

  //
  pc := stall ? pc | (we ? new_pc | pc + 4)

  E.AdEIL := pc(0) | pc(1)
}

object PCU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new PCU)
  }
}