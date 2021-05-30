package cpu.alu

import cpu.defs.ConstantVal.ALU_OP._
import cpu.defs.ConstantVal._
import spinal.core._

class ALU extends Component {
  /// 运算器

  // in
  val e = in Bool

  val op = in(ALU_OP)
  val a = in UInt (32 bits)
  val b = in UInt (32 bits)

  // out
  val c = out UInt (32 bits)
  val d = out UInt (32 bits)

  val E = new Bundle {
    val Ov = out Bool
  }

  //
  d := 0
  E.Ov := False
  switch(op) {
    is(add) {
      val temp = S(a, 33 bits) + S(b, 33 bits)
      c := U(temp(31 downto 0))
      E.Ov := e & temp(32) =/= temp(31)
    }
    is(addu) {
      c := a + b
    }
    is(sub) {
      val temp = S(a, 33 bits) - S(b, 33 bits)
      c := U(temp(31 downto 0))
      E.Ov := e & temp(32) =/= temp(31)
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
      c := b |<< a(4 downto 0)
    }
    is(lu) {
      c := b |<< 16
    }
    is(srl) {
      c := b |>> a(4 downto 0)
    }
    is(sra) {
      c := U(S(b) >> a(4 downto 0))
    }
    is(mult) {
      val temp = U(S(a) * S(b))
      c := temp(63 downto 32)
      d := temp(31 downto 0)
    }
    is(multu) {
      val temp = a * b
      c := temp(63 downto 32)
      d := temp(31 downto 0)
    }
    is(div) {
      c := U(S(a) % S(b))
      d := U(S(a) / S(b))
    }
    is(divu) {
      c := a % b
      d := a / b
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