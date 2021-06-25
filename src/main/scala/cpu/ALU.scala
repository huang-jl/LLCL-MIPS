package cpu

import lib.Optional
import ip.Divider
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
    val stall = out Bool

    val exception = out(Optional(EXCEPTION()))
  }

  val a = io.input.a
  val b = io.input.b
  val c = io.c
  val d = io.d

  import ALU_OP._

  //除法器统一进行无符号除法，所以要对操作数进行修正
  val divide = new Area {
    val divider = new Divider(32, false)

    val unsignedDiv: Bool = io.input.op === divu
    val signedDiv: Bool = io.input.op === div
    val divideByZero = b === 0

    val quotient = UInt(32 bits) //商
    val remainder = UInt(32 bits) //余数
    val dividend = signedDiv ? a.asSInt.abs | a //如果a[31]为1则转为补码，否则不变
    val divisor = signedDiv ? b.asSInt.abs | b //如果a[31]为1则转为补码，否则不变

    divider.io.dividend.tdata := dividend
    divider.io.dividend.tvalid := (unsignedDiv | signedDiv) & (!divideByZero)
    divider.io.divisor.tdata := divisor
    divider.io.divisor.tvalid := (unsignedDiv | signedDiv) & (!divideByZero)
    quotient := divider.io.dout.tdata(32, 32 bits)
    remainder := divider.io.dout.tdata(0, 32 bits)

    io.stall := (signedDiv | unsignedDiv) & !divideByZero & !divider.io.dout.tvalid //是除法 & 不是除0 & 没有计算出结果的时候就stall
  }


  d := 0
  io.exception := None
  switch(io.input.op) {
    is(add) {
      val temp = S(a, 33 bits) + S(b, 33 bits)
      c := U(temp(31 downto 0))
      when(temp(32) =/= temp(31)) {
        io.exception := EXCEPTION.Ov
      }
    }
    is(addu) {
      c := a + b
    }
    is(sub) {
      val temp = S(a, 33 bits) - S(b, 33 bits)
      c := U(temp(31 downto 0))
      when(temp(32) =/= temp(31)) {
        io.exception := EXCEPTION.Ov
      }
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
      c := divide.remainder.twoComplement(a(31))(0, 32 bits).asUInt
      d := divide.quotient.twoComplement(a(31) ^ b(31))(0, 32 bits).asUInt
      //      c := U(S(a) % S(b))
      //      d := U(S(a) / S(b))
    }
    is(divu) {
      c := divide.remainder
      d := divide.quotient
      //      c := a % b
      //      d := a / b
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
