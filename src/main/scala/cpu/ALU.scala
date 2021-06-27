package cpu

import lib.Optional
import ip.DividerIP
import spinal.core._
import spinal.lib.fsm._

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
    val divider = new Divider(detectZero =  false)

    val unsignedDiv: Bool = io.input.op === divu
    val signedDiv: Bool = io.input.op === div
    val divideByZero = b === 0

    val quotient = UInt(32 bits) //商
    val remainder = UInt(32 bits) //余数
    val dividend = signedDiv ? a.asSInt.abs | a //如果a[31]为1则转为补码，否则不变
    val divisor = signedDiv ? b.asSInt.abs | b //如果a[31]为1则转为补码，否则不变

    divider.io.dividend.tdata := dividend
    divider.io.divisor.tdata := divisor
    divider.io.tvalid := (unsignedDiv | signedDiv) & (!divideByZero)
    quotient := divider.io.dout.tdata(32, 32 bits)
    remainder := divider.io.dout.tdata(0, 32 bits)

    io.stall := (signedDiv | unsignedDiv) & !divideByZero & divider.io.stall //是除法 & 不是除0 & 没有计算出结果的时候就stall
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
      c := temp(31 downto 0)
      d := temp(63 downto 32)
    }
    is(multu) {
      val temp = a * b
      c := temp(31 downto 0)
      d := temp(63 downto 32)
    }
    is(div) {
      c := divide.quotient.twoComplement(a(31) ^ b(31))(0, 32 bits).asUInt
      d := divide.remainder.twoComplement(a(31))(0, 32 bits).asUInt
      //      c := U(S(a) / S(b))
      //      d := U(S(a) % S(b))
    }
    is(divu) {
      c := divide.quotient
      d := divide.remainder
      //      c := a / b
      //      d := a % b
    }
    is(slt) {
      c := U(S(a) < S(b), 32 bits)
    }
    is(sltu) {
      c := U(a < b, 32 bits)
    }
  }
}

/**
 * @note Vivado的除法器是流水的，如果一直拉高`tvalid`那么会让他持续流水地计算
 *       因此使用一个状态机，仅在IDLE状态接受输入
 * */
class Divider(detectZero: Boolean = false, name: String = "divider") extends Component {
  val io = new Bundle {
    val tvalid = in Bool  // 一个输入指明除数和被除数是否准备好
    val dividend = new Bundle {
      val tdata = in UInt (32 bits)
    }
    val divisor = new Bundle {
      val tdata = in UInt (32 bits)
    }
    val dout = new Bundle {
      val tdata = out UInt (64 bits)
      val tvalid = out Bool
      val divideByZero = if (detectZero) out(Bool) else null
    }
    val stall = out Bool
  }
  val divider = new DividerIP(32, false)
  divider.io.dividend.tdata := io.dividend.tdata
  divider.io.divisor.tdata := io.divisor.tdata
  divider.io.divisor.tvalid := False
  divider.io.dividend.tvalid := False
  io.dout.assignAllByName(divider.io.dout)
  io.stall := True

  val dividerFSM = new StateMachine {
    setEntry(stateBoot)
    disableAutoStart()
    val running = new State

    stateBoot
      .whenIsNext(io.stall := False)
      .whenIsActive {
        divider.io.dividend.tvalid := io.tvalid
        divider.io.divisor.tvalid := io.tvalid
        when(io.tvalid)(goto(running))
      }

    running
      .whenIsActive {
        when(divider.io.dout.tvalid)(goto(stateBoot))
      }
  }
}

object ALU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ALU)
  }
}
