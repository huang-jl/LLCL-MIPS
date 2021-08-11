package cpu

import lib.Optional
import ip.MultiplierIP
import cpu.defs.ConstantVal
import spinal.core._
import spinal.lib.fsm._
import spinal.lib._
import scala.collection.mutable
import scala.language.postfixOps


object ALU_OP extends SpinalEnum {
  val add, addu, sub, subu, and, or, xor, nor, sll, lu, srl, sra, mult, multu, div, divu, seq, sne, slt, sltu, mul = newElement()
  val madd, maddu, msub, msubu, clo, clz, sge, sgeu =
    Utils.instantiateWhen(newElement(), ConstantVal.FINAL_MODE)
}

object ALU_A_SRC extends SpinalEnum {
  val rs, sa = newElement()
}

object ALU_B_SRC extends SpinalEnum {
  val rt, imm = newElement()
}

case class ALUInput() extends Bundle {
  val op = ALU_OP()
  val a  = UInt(32 bits)
  val b  = UInt(32 bits)
}

class ALU extends Component {
  /// 运算器
  val io = new Bundle {
    // in
    val input = in(ALUInput())

    // out
    val c = out UInt (32 bits)
    val d = out UInt (32 bits)

    val exception = out(Optional(ExcCode()))
  }

  val a = io.input.a
  val b = io.input.b
  val c = io.c
  val d = io.d

  import ALU_OP._

  c.assignDontCare()
  d.assignDontCare()
  io.exception := None

  switch(io.input.op) {
    is(add) {
      val temp = S(a, 33 bits) + S(b, 33 bits)
      c := U(temp(31 downto 0))
      when(temp(32) =/= temp(31)) {
        io.exception := ExcCode.overflow
      }
    }
    is(addu) {
      c := a + b
    }
    is(sub) {
      val temp = S(a, 33 bits) - S(b, 33 bits)
      c := U(temp(31 downto 0))
      when(temp(32) =/= temp(31)) {
        io.exception := ExcCode.overflow
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
    is(slt) {
      c := (S(a) < S(b)).asUInt(32 bits)
    }
    is(sltu) {
      c := (a < b).asUInt(32 bits)
    }
    if (ConstantVal.FINAL_MODE) {
      is(clo) {
        c := cloImpl(a, clo)
//        c := ALU.clo(a)
      }
      is(clz) {
        c := cloImpl(~a, clz)
//        c := ALU.clz(a)
      }
      is(seq) {
        c := (a === b).asUInt(32 bits)
      }
      is(sne) {
        c := (a =/= b).asUInt(32 bits)
      }
      is(sge) {
        c := (S(a) >= S(b)).asUInt(32 bits)
      }
      is(sgeu) {
        c := (a >= b).asUInt(32 bits)
      }
    }
    def cloImpl(a: BitVector, nameProvider: Nameable) =
      new Composite(nameProvider) {
        val layers = mutable.ArrayBuffer[Vec[Bits]](
          Vec(a.asBools map { _.asBits })
        )
        for (i <- 1 to log2Up(32)) {
          val groupLength = 1 << i
          val groupCount  = 32 / groupLength
          layers += Vec(for (j <- 0 until groupCount) yield {
            val hiHalf = layers.last(2 * j + 1)
            val loHalf = layers.last(j * 2)
            hiHalf.msb mux (
              False -> B"0" ## hiHalf,
              True -> (loHalf.msb mux (
                False -> B"01" ## loHalf((loHalf.getBitsWidth - 2) downto 0),
                True  -> B(1 << i)
              ))
            )
          })
        }
        val result = layers.last(0).asUInt.resize(32 bits)
      }.result
  }
}

/** @note 复杂运算单元，例如乘除法 */
class ComplexALU extends Component {
  val io = new Bundle {
    val input = in(ALUInput())
    val hi = ConstantVal.FINAL_MODE generate in UInt(32 bits)
    val lo = ConstantVal.FINAL_MODE generate in UInt(32 bits)
    val flush = in Bool ()

    // out
    val c     = out UInt (32 bits)
    val d     = out UInt (32 bits)
    val stall = out Bool ()
  }

  import ALU_OP._

  //无符号有符号转换
  val abs = new Area {
    val signed: Bool = Utils.equalAny(io.input.op, div, mult, mul)
    val a: UInt      = signed ? io.input.a.asSInt.abs | io.input.a //如果a[31]为1则转为补码，否则不变
    val b: UInt      = signed ? io.input.b.asSInt.abs | io.input.b //如果b[31]为1则转为补码，否则不变
  }

  //除法器统一进行无符号除法，所以要对操作数进行修正
  val divide = new Area {
    val divider = new DivUnit

    val useDivider = Utils.equalAny(io.input.op, div, divu)

    divider.io.flush := io.flush
    divider.io.input.dividend := io.input.a
    divider.io.input.divisor := io.input.b
    divider.io.input.op := io.input.op
    divider.io.input.valid := useDivider
    val stall = useDivider & !divider.io.output.valid
    val quotient = divider.io.output.quotient
    val remainder = divider.io.output.remainder
  }

  val multiply = new Area {
    val stall: Bool    = True
    val multiply: Bool = Utils.equalAny(io.input.op, mult, multu, mul)
    val multiplier     = new MultiplierIP()
    multiplier.io.A := abs.a
    multiplier.io.B := abs.b
    multiplier.io.CE := multiply
    multiplier.io.SCLR := io.flush
    // 如果有符号乘法并且a和b异号，那么得到的结果取相反数。这个过程在working中计算
    val result: UInt =
      multiplier.io.P
        .twoComplement(abs.signed & (io.input.a(31) ^ io.input.b(31)))(0, 64 bits)
        .asUInt
    val counter = Reg(UInt(log2Up(ConstantVal.Multiply_Latency - 1) + 1 bits)) init 0

    new StateMachine {
      setEntry(stateBoot)
      disableAutoStart()
      val working = new State

      stateBoot
        .whenIsNext(stall := False)
        .whenIsActive {
          when(multiply) {
            counter := 0
            goto(working)
          }
        }

      working.whenIsActive {
        when(counter === ConstantVal.Multiply_Latency - 1) {
          goto(stateBoot)
        }.otherwise(counter := counter + 1)
      }
    }
  }
  io.stall := divide.stall | (multiply.multiply & multiply.stall)
  io.c.assignDontCare()
  io.d.assignDontCare()
  switch(io.input.op) {
    is(mul) {
      io.c := multiply.result(0, 32 bits)
    }
    is(mult) {
      io.c := multiply.result(32, 32 bits)
      io.d := multiply.result(0, 32 bits)
    }
    is(multu) {
      io.c := multiply.result(32, 32 bits)
      io.d := multiply.result(0, 32 bits)
    }
    is(div) {
      io.c := divide.remainder
      io.d := divide.quotient
    }
    is(divu) {
      io.c := divide.remainder
      io.d := divide.quotient
    }
    if (ConstantVal.FINAL_MODE) {
      val hi     = io.hi
      val lo     = io.lo
      val mulAdd = (hi @@ lo) + multiply.result
      val mulSub = (hi @@ lo) - multiply.result
      is(madd) {
        io.c := mulAdd(32, 32 bits)
        io.d := mulAdd(0, 32 bits)
      }
      is(maddu) {
        io.c := mulAdd(32, 32 bits)
        io.d := mulAdd(0, 32 bits)
      }
      is(msub) {
        io.c := mulSub(32, 32 bits)
        io.d := mulSub(0, 32 bits)
      }
      is(msubu) {
        io.c := mulSub(32, 32 bits)
        io.d := mulSub(0, 32 bits)
      }
    }
  }
}

case class DividerInput() extends Bundle {
  val op       = ALU_OP()
  val dividend = UInt(32 bits)
  val divisor  = UInt(32 bits)
}

case class DividerOutput() extends Bundle {
  val quotient  = UInt(32 bits)
  val remainder = UInt(32 bits)
}

class NormalDivUnit extends Component {
  val io = new Bundle {
    val input  = slave Flow DividerInput()
    val output = master Flow DividerOutput()
    val flush  = in Bool ()
  }

  val divider = new math.MixedDivider(32, 32, true)

  divider.io.flush := io.flush

  divider.io.cmd.valid := io.input.valid
  divider.io.rsp.ready := True

  divider.io.cmd.numerator := io.input.dividend.asBits
  divider.io.cmd.denominator := io.input.divisor.asBits
  divider.io.cmd.signed := io.input.op === ALU_OP.div

  io.output.quotient := divider.io.rsp.quotient.asUInt
  io.output.remainder := divider.io.rsp.remainder.asUInt
  io.output.valid := divider.io.rsp.valid

}

class DivPow2Unit extends Component {
  val io = new Bundle {
    val input  = slave Flow DividerInput()
    val output = master Flow DividerOutput()
  }

  val dividend = io.input.dividend
  val divisor  = io.input.divisor

  var whenStatement = when(False) {
    io.output.payload.assignDontCare()
  }
  for (i <- Seq(4, 3, 1)) {
    whenStatement = whenStatement elsewhen (divisor(i)) {
      io.output.quotient := dividend(i until 32).resize(32 bits)
      io.output.remainder := dividend(0 until i).resize(32 bits)
    }
  }
  whenStatement otherwise {
    io.output.payload.assignDontCare()
  }
}

case class Div10Interstage1() extends Bundle {
  import ComplexALU._

  val quotient = new Bundle {
    val lo_mul_lo = UInt((aLoWidth + bLoWidth) bits)
    val lo_mul_hi = UInt((aLoWidth + bHiWidth) bits)
    val hi_mul_lo = UInt((aHiWidth + bLoWidth) bits)
    val hi_mul_hi = UInt((aHiWidth + bHiWidth) bits)
  }

  val remainder = new Bundle {
    val lo_mul_lo = UInt((aLoWidth + bLoWidth) bits)
    val lo_mul_hi = UInt((32 - bLoWidth) bits)
    val hi_mul_lo = UInt((32 - bHiWidth) bits)
    val sum       = UInt(32 bits)
  }
}

case class Div10Interstage2() extends Bundle {
  import ComplexALU._

  val quotient = new Bundle {
    val lo_sum = UInt((65 - aHiWidth) bits)
    val hi_sum = UInt((64 - aLoWidth) bits)
  }

  val remainder = new Bundle {
    val index = UInt(4 bits)
  }
}

/** Magic! */
class Div10Unit extends Component {
  val io = new Bundle {
    val input  = slave Flow DividerInput()
    val output = master Flow DividerOutput()
  }

  import ComplexALU._

  val stage1 = new Area {
    val input  = Flow(DividerInput())
    val output = Flow(Div10Interstage1())

    val b   = input.dividend
    val bLo = S"0" @@ b(0 until bLoWidth).asSInt
    val bHi = S"0" @@ b(bLoWidth until 32).asSInt

    val q = new Area {
      val a = U(0xcccccccdL, 32 bits)

      val aLo = S"0" @@ a(0 until aLoWidth).asSInt
      val aHi = S"0" @@ a(aLoWidth until 32).asSInt

      output.quotient.lo_mul_lo := (aLo * bLo).asUInt.resized
      output.quotient.lo_mul_hi := (aLo * bHi).asUInt.resized
      output.quotient.hi_mul_lo := (aHi * bLo).asUInt.resized
      output.quotient.hi_mul_hi := (aHi * bHi).asUInt.resized
    }

    val r = new Area {
      val a = U(0x19999999L, 32 bits)

      val aLo = S"0" @@ a(0 until aLoWidth).asSInt
      val aHi = S"0" @@ a(aLoWidth until 32).asSInt

      output.remainder.lo_mul_lo := (aLo * bLo).asUInt.resized
      output.remainder.lo_mul_hi := (aLo * bHi).asUInt.resized
      output.remainder.hi_mul_lo := (aHi * bLo).asUInt.resized

      output.remainder.sum := b(0 until 31) +^ b(0 until 29)
    }
  }

  val stage2 = new Area {
    val input  = Flow(Div10Interstage1())
    val output = Flow(Div10Interstage2())

    val q = new Area {
      val lo_mul_lo = input.quotient.lo_mul_lo
      val lo_mul_hi = input.quotient.lo_mul_hi @@ U(0, bLoWidth bits)
      val hi_mul_lo = input.quotient.hi_mul_lo
      val hi_mul_hi = input.quotient.hi_mul_hi @@ U(0, bLoWidth bits)

      output.quotient.lo_sum := lo_mul_lo +^ lo_mul_hi
      output.quotient.hi_sum := hi_mul_lo + hi_mul_hi
    }

    val r = new Area {
      val lo_mul_lo = input.remainder.lo_mul_lo
      val lo_mul_hi = input.remainder.lo_mul_hi @@ U(0, bLoWidth bits)
      val hi_mul_lo = input.remainder.hi_mul_lo @@ U(0, aLoWidth bits)
      val sum       = input.remainder.sum

      output.remainder.index :=
        (lo_mul_lo + lo_mul_hi + hi_mul_lo + sum)(28 until 32)
    }
  }

  val stage3 = new Area {
    val input  = Flow(Div10Interstage2())
    val output = Flow(DividerOutput())

    val q = new Area {
      val lo_sum = input.quotient.lo_sum
      val hi_sum = input.quotient.hi_sum @@ U(0, aLoWidth bits)

      output.quotient := (lo_sum + hi_sum)(35 until 64).resize(32 bits)
    }

    val r = new Area {
      val tableData = Seq(0, 1, 2, 2, 3, 3, 4, 5, 5, 6, 7, 7, 8, 8, 9, 0)
      val table     = Vec(tableData map { x => U(x, 4 bits) })

      output.remainder := table(input.remainder.index).resize(32 bits)
    }
  }

  io.input >> stage1.input
  stage1.output >-> stage2.input
  stage2.output >-> stage3.input
  stage3.output >> io.output
}


class DivUnit extends Component {
  val io = new Bundle {
    val input  = slave Flow DividerInput()
    val output = master Flow DividerOutput()
    val flush = in Bool ()
  }
  val pow2   = new DivPow2Unit
  val div10   = new Div10Unit
  val normal = new NormalDivUnit

  io.output.setIdle()
  normal.io.flush := io.flush

  val fsm = new StateMachine {
    val decision   = new State
    val waitPow2   = new State
    val wait10   = new StateDelay(2)
    val waitNormal = new StateDelay(32)

    disableAutoStart()
    setEntry(stateBoot)

    def finishJob(): Unit = {
      io.output.valid := True
      goto(stateBoot)
    }

    stateBoot.whenIsActive {
      when(io.input.valid)(goto(decision))
    }
    decision.whenIsActive {
      goto(waitNormal)
      when(io.input.op === ALU_OP.divu) {
        switch(io.input.divisor) {
          is(2, 8, 16)(goto(waitPow2))
          is(10)(goto(wait10))
        }
      }
    }
    waitPow2.whenIsActive {
      finishJob()
      normal.io.flush := True
      io.output.payload := pow2.io.output.payload
    }
    waitNormal.whenCompleted{
      finishJob()
      io.output.payload := normal.io.output.payload
    }
    wait10.whenCompleted {
      finishJob()
      normal.io.flush := True
      io.output.payload := div10.io.output.payload
    }
    // flush
    always(when(io.flush)(goto(stateBoot)))
  }

  val input = io.input.takeWhen(fsm.isActive(fsm.stateBoot))
  input >> pow2.io.input
  input >> normal.io.input
  input >> div10.io.input
}

object ComplexALU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ComplexALU)
  }

  val aLoWidth = 24
  val bLoWidth = 17
  def aHiWidth = 32 - aLoWidth
  def bHiWidth = 32 - bLoWidth

  // DSP48E1 有符号乘法器大小
  require(aLoWidth < 25)
  require(bLoWidth < 18)

  // 如果不需要高 32 位，避免有必要将 a 和 b 高位相乘
  require(aLoWidth + bLoWidth >= 32)
}

object ALU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ALU).printPruned()
  }
}
