package cpu

import lib.{Optional, Task}
import ip.{DividerIP, MultiplierIP}
import cpu.defs.ConstantVal
import spinal.core._
import spinal.lib.fsm._
import spinal.lib._
import scala.collection.mutable
import scala.language.postfixOps

object ALU_OP extends SpinalEnum {
  val add, addu, sub, subu, and, or, xor, nor, sll, lu, srl, sra, mult, multu, div, divu, seq, sne,
      slt, sltu, mul = newElement()
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
  val hi = Utils.instantiateWhen(UInt(32 bits), ConstantVal.FINAL_MODE)
  val lo = Utils.instantiateWhen(UInt(32 bits), ConstantVal.FINAL_MODE)
}

class ALU extends Component {
  /// 运算器

  val io = new Bundle {
    // in
    val input = in(ALUInput())

    // out
    val c     = out UInt (32 bits)
    val d     = out UInt (32 bits)
    val stall = out Bool ()

    val exception = out(Optional(ExcCode()))

    val will = new Bundle {
      val input  = in Bool ()
      val output = in Bool ()
    }
  }

  val a = io.input.a
  val b = io.input.b
  val c = io.c
  val d = io.d

  import ALU_OP._

  //无符号有符号转换
  val abs = new Area {
    val signed: Bool =
      if (ConstantVal.FINAL_MODE) Utils.equalAny(io.input.op, div, mult, mul, madd, msub)
      else Utils.equalAny(io.input.op, div, mult, mul, div)
    val a: UInt = signed ? io.input.a.asSInt.abs | io.input.a //如果a[31]为1则转为补码，否则不变
    val b: UInt = signed ? io.input.b.asSInt.abs | io.input.b //如果b[31]为1则转为补码，否则不变
  }

  //除法器统一进行无符号除法，所以要对操作数进行修正
  val divide = new Area {
    val divider = new DividerIP(32, false)

    val divideByZero = b === 0

    val running: Bool = Utils.equalAny(io.input.op, divu, div)

    val quotient  = UInt(32 bits) //商
    val remainder = UInt(32 bits) //余数

    divider.io.dividend.tdata := abs.a
    divider.io.divisor.tdata := abs.b

    val outputTask = Task(divider.io.dout.tvalid, divider.io.dout.tdata, io.will.output)

    val isNew      = RegNext(io.will.input)
    val useDivider = running & !divideByZero
    divider.io.dividend.tvalid := useDivider & isNew
    divider.io.divisor.tvalid := useDivider & isNew

    val stall: Bool = useDivider & !outputTask.has

    quotient := outputTask.value(32, 32 bits).asUInt
    remainder := outputTask.value(0, 32 bits).asUInt
  }

  val multiply = new Area {
    val stall: Bool = True
    val multiply: Bool =
      if (ConstantVal.FINAL_MODE) Utils.equalAny(io.input.op, mul, mult, multu, madd, maddu, msub, msubu)
      else Utils.equalAny(io.input.op, mult, multu, mul)
    val multiplier = new MultiplierIP()
    multiplier.io.A := abs.a
    multiplier.io.B := abs.b
    multiplier.io.CE := multiply
    // 如果有符号乘法并且a和b异号，那么得到的结果取相反数。这个过程在working中计算
    val result: UInt =
      multiplier.io.P.twoComplement(abs.signed & (a(31) ^ b(31)))(0, 64 bits).asUInt
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
    is(mul) {
      c := multiply.result(0, 32 bits)
    }
    is(mult) {
      c := multiply.result(32, 32 bits)
      d := multiply.result(0, 32 bits)
    }
    is(multu) {
      c := multiply.result(32, 32 bits)
      d := multiply.result(0, 32 bits)
    }
    is(div) {
      c := divide.remainder.twoComplement(a(31))(0, 32 bits).asUInt
      d := divide.quotient.twoComplement(a(31) ^ b(31))(0, 32 bits).asUInt
    }
    is(divu) {
      c := divide.remainder
      d := divide.quotient
    }
    is(slt) {
      c := (S(a) < S(b)).asUInt(32 bits)
    }
    is(sltu) {
      c := (a < b).asUInt(32 bits)
    }
    if (ConstantVal.FINAL_MODE) {
      val hi     = io.input.hi
      val lo     = io.input.lo
      val mulAdd = (hi @@ lo) + multiply.result
      val mulSub = (hi @@ lo) - multiply.result
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
      is(madd) {
        c := mulAdd(32, 32 bits)
        d := mulAdd(0, 32 bits)
      }
      is(maddu) {
        c := mulAdd(32, 32 bits)
        d := mulAdd(0, 32 bits)
      }
      is(msub) {
        c := mulSub(32, 32 bits)
        d := mulSub(0, 32 bits)
      }
      is(msubu) {
        c := mulSub(32, 32 bits)
        d := mulSub(0, 32 bits)
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

object ALU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ALU).printPruned()
  }

  // 先Mask得到只有最左边1保留的值
  // 然后将其视为One-Hot找到最左侧的1的位置
  // 最后用31去减
  // 如果value为0那么直接是32
//  def clz(value: UInt): UInt = {
//    assert(value.getBitsWidth == 32)
//    val res                      = U(32)
//    val onlyLeftMostBitSet: UInt = OHMasking.last(value)
//    when(value =/= 0) {
//      res := (31 - OHToUInt(onlyLeftMostBitSet)).resize(6)
//    }
//    res.resize(32)
//  }
//
//  def clo(value: UInt): UInt = {
//    clz(~value)
//  }
}
