package cpu.alu

import cpu.defs.ConstantVal.ALU_OP._
import cpu.defs.ConstantVal._
import spinal.core._

class ALU extends Component {
  /// 运算器

  // in
  val op = in(ALU_OP)
  val a = in UInt (32 bits)
  val b = in UInt (32 bits)

  // out
  val c = out UInt (32 bits)

  //
  switch(op) {
    is(add) {
      c := a + b
    }
    is(addu) {
      c := a + b
    }
    is(sub) {
      c := a - b
    }
    is(subu) {
      c := a - b
    }
    is(and) {
      c := a & b
    }
    is(or) {
      c := a | b
    }
    is(xor) {
      c := a ^ b
    }
    is(nor) {
      c := ~(a | b)
    }
    is(sll) {
      c := b |<< a
    }
    is(lu) {
      c := b |<< 16
    }
    is(srl) {
      c := b |>> a
    }
    is(sra) {
      c := b >> a
    }
    is(mult) {
      c := 0
    }
    is(div) {
      c := 0
    }
    is(slt) {
      c := U(S(a) < S(b), 32 bits)
    }
    is(sltu) {
      c := U(a < b, 32 bits)
    }
  }
}

object ALU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ALU)
  }
}