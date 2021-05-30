package cpu

import cpu.defs.ConstantVal._
import spinal.core._

object ALU_OP extends SpinalEnum {
  val add, addu, sub, subu, and, or, xor, nor, sll, lu, srl, sra, mult, multu,
      div, divu, slt, sltu = newElement()
}

object ALU_A_SRC extends SpinalEnum {
  val rs, sa = newElement()
}

object ALU_B_SRC extends SpinalEnum {
  val rt, imm = newElement()
}

case class ALUInput() extends Bundle {
  val op = ALU_OP()
  val a = UInt(32 bits)
  val b = UInt(32 bits)
}

class ALU extends Component {
  /// 运算器

  val io = new Bundle {
    // in
    val input = in(ALUInput())

    // out
    val c = out UInt (32 bits)
    val d = out UInt (32 bits)
  }

  val a = io.input.a
  val b = io.input.b
  val c = io.c
  val d = io.d

  val p = U(S(a) * S(b))
  val pu = a * b

  //
  import ALU_OP._

  switch(io.input.op) {
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
      c := p(63 downto 32)
    }
    is(multu) {
      c := pu(63 downto 32)
    }
    is(div) {
      c := U(S(a) % S(b))
    }
    is(divu) {
      c := a % b
    }
    is(slt) {
      c := U(S(a) < S(b), 32 bits)
    }
    is(sltu) {
      c := U(a < b, 32 bits)
    }
  }

  switch(io.input.op) {
    is(mult) {
      d := p(31 downto 0)
    }
    is(multu) {
      d := pu(31 downto 0)
    }
    is(div) {
      d := U(S(a) / S(b))
    }
    is(divu) {
      d := a / b
    }
    default {
      d := 0
    }
  }
}

object ALU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ALU)
  }
}
