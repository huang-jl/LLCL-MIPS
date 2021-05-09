package cpu.rfu

import cpu.{CpuAXIInterface, DebugInterface}
import spinal.core._

class RFU extends Component {
  // in
  val we = in Bool
  val rd = in UInt (5 bits)
  val rd_v = in Bits (32 bits)

  val ra = in UInt (5 bits)
  val rb = in UInt (5 bits)

  // out
  val ra_v = out Bits (32 bits)
  val rb_v = out Bits (32 bits)
  val debug = out(new DebugInterface)

  // regs
  val regs = Vec(RegInit(B"32'0"), 32)

  //
  when(we) {
    regs(rd) := rd_v
  }

  ra_v := regs(ra)
  rb_v := regs(rb)

  debug.wb.rf.wen := we.asSInt.resize(4).asBits
  debug.wb.rf.wdata := rd_v
  debug.wb.rf.wnum := rd.asBits
  debug.wb.pc := 0
}

object RFU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new RFU)
  }
}