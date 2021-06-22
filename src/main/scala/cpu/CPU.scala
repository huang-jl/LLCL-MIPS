package cpu

import defs.Mips32InstImplicits._
import defs.{ConstantVal, Mips32Inst, SramBus, SramBusConfig}
import lib.{Key, Optional, Updating}
import spinal.core._
import spinal.lib.{cpu => _, _}

class CPU extends Component {
  val io = new Bundle {
    val externalInterrupt = in Bits (6 bits)
    val iSramBus          = master(SramBus(SramBusConfig(32, 32, 2)))
    val dSramBus          = master(SramBus(SramBusConfig(32, 32, 2)))
    val debug             = out(DebugInterface())
  }
  val icu = new MU
  val du  = new DU
  val ju  = new JU
  val alu = new ALU
  val dcu = new MU
  val hlu = new HLU
  val rfu = new RFU
  val cp0 = new CP0

  val jumpPc = Key(Optional(UInt(32 bits)))

  val pc         = Key(UInt(32 bits))
  val bd         = Key(Bool)
  val inst       = Key(Mips32Inst())
  val exception  = Key(Optional(EXCEPTION()))
  val eret       = Key(Bool)
  val aluOp      = Key(ALU_OP())
  val aluASrc    = Key(ALU_A_SRC())
  val aluBSrc    = Key(ALU_B_SRC())
  val aluResultC = Key(UInt(32 bits))
  val aluResultD = Key(UInt(32 bits))
  val memRe      = Key(Bool)
  val memWe      = Key(Bool)
  val memBe      = Key(UInt(2 bits))
  val memEx      = Key(MU_EX())
  val memAddr    = Key(UInt(32 bits))
  val hluHiWe    = Key(Bool)
  val hluHiSrc   = Key(HLU_SRC())
  val hluLoWe    = Key(Bool)
  val hluLoSrc   = Key(HLU_SRC())
  val hluHiData  = Key(Bits(32 bits))
  val hluLoData  = Key(Bits(32 bits))
  val cp0We      = Key(Bool)
  val rfuWe      = Key(Bool)
  val rfuAddr    = Key(UInt(5 bits))
  val rfuRdSrc   = Key(RFU_RD_SRC())
  val rfuData    = Key(Bits(32 bits))
  val useRs      = Key(Bool)
  val useRt      = Key(Bool)
  val rsValue    = Key(Bits(32 bits))
  val rtValue    = Key(Bits(32 bits))

  // Later stages may depend on earlier stages, so stages are list in reversed order.

  val WB = new Stage {
    val rfuC = new StageComponent {
      io.debug.wb.rf.wen := B(4 bits, default -> rfu.io.write.valid)
      io.debug.wb.rf.wdata := rfu.io.write.data
      io.debug.wb.rf.wnum := rfu.io.write.index

      rfu.io.write.valid := input(rfuWe)
      rfu.io.write.index := input(rfuAddr)
      rfu.io.write.data := input(rfuData)
      setInputReset(rfuWe, False)
    }

    val pcDebug = new StageComponent {
      io.debug.wb.pc := input(pc)
    }
  }

  val ME = new Stage {
    val dcuC = new StageComponent {
      io.dSramBus <> dcu.io.sramBus

      dcu.io.re := input(memRe)
      dcu.io.we := input(memWe)
      dcu.io.be := input(memBe)
      dcu.io.ex := input(memEx)
      dcu.io.addr := input(memAddr)
      dcu.io.data_in := input(rtValue)
      setInputReset(memRe, False)
      setInputReset(memWe, False)

      when(dcu.io.stall) {
        stalls := True
        flushable := False
      }

      produced(rfuData) := dcu.io.data_out
      exceptionToRaise := dcu.io.exception
    }

    val hluC = new StageComponent {
      hlu.hi_we := input(hluHiWe)
      produced(hluHiData) := ((input(hluHiSrc) === HLU_SRC.rs)
        ? input(rsValue)
        | input(aluResultC).asBits)
      hlu.new_hi := produced(hluHiData)
      hlu.lo_we := input(hluLoWe)
      produced(hluLoData) := ((input(hluLoSrc) === HLU_SRC.rs)
        ? input(rsValue)
        | input(aluResultD).asBits)
      hlu.new_lo := produced(hluLoData)

      setInputReset(hluHiWe, False)
      setInputReset(hluLoWe, False)
    }

    val cp0C = new StageComponent {
      cp0.io.softwareWrite.valid := input(cp0We)
      cp0.io.softwareWrite.addr.rd := input(inst).rd
      cp0.io.softwareWrite.addr.sel := input(inst).sel
      cp0.io.softwareWrite.data := input(rtValue)
      setInputReset(cp0We, False)

      cp0.io.exceptionInput.exception := exception.next
      cp0.io.exceptionInput.eret := input(eret)
      cp0.io.exceptionInput.memAddr := input(memAddr)
      cp0.io.exceptionInput.pc := input(pc)
      cp0.io.exceptionInput.bd := input(bd)

      cp0.io.instStarting := RegNext(receiving)
      when(cp0.io.interruptOnNextInst && receiving) {
        // Causes stall to keep low
        willReset := True
      }

      cp0.io.externalInterrupt := io.externalInterrupt
      when(valid && cp0.io.jumpPc.isDefined) {
        signalsFlush := True
      }
    }

    output(rfuData) := ((input(rfuRdSrc) === RFU_RD_SRC.mu)
      ? produced(rfuData)
      | input(rfuData))
  }

  val EX = new Stage {
    when(
      ME.valid && ME.currentInput(rfuWe) &&
        ME.currentInput(rfuAddr) === input(inst).rs
    ) {
      input(rsValue) := ME.currentOutput(rfuData)
      when(ME.stalls) {
        stalls := True
      }
    }
    when(
      ME.valid && ME.currentInput(rfuWe) &&
        ME.currentInput(rfuAddr) === input(inst).rt
    ) {
      input(rtValue) := ME.currentOutput(rfuData)
      when(ME.stalls) {
        stalls := True
      }
    }

    val aluC = new StageComponent {
      alu.io.input.op := input(aluOp)
      alu.io.input.a := input(aluASrc).mux(
        ALU_A_SRC.rs -> U(input(rsValue)),
        ALU_A_SRC.sa -> input(inst).sa.resize(32)
      )
      alu.io.input.b := input(aluBSrc).mux(
        ALU_B_SRC.rt  -> U(input(rtValue)),
        ALU_B_SRC.imm -> input(inst).immExtended.asUInt
      )

      produced(aluResultC) := alu.io.c
      produced(aluResultD) := alu.io.d
      exceptionToRaise := alu.io.exception
    }

    val aguC = new StageComponent {
      produced(memAddr) := U(S(input(rsValue)) + input(inst).offset)
    }

    produced(rfuData) := input(rfuRdSrc) mux (
      RFU_RD_SRC.alu -> produced(aluResultC).asBits,
      RFU_RD_SRC.hi -> ((ME.valid && ME.currentInput(hluHiWe))
        ? ME.currentOutput(hluHiData)
        | hlu.hi_v),
      RFU_RD_SRC.lo -> ((ME.valid && ME.currentInput(hluLoWe))
        ? ME.currentOutput(hluLoData)
        | hlu.lo_v),
      default -> input(rfuData)
    )
  }

  val ID = new Stage {
    val duC = new StageComponent {
      du.io.inst := input(inst)
      setInputReset(inst, ConstantVal.INST_NOP)

      produced(aluOp) := du.io.alu_op
      produced(aluASrc) := du.io.alu_a_src
      produced(aluBSrc) := du.io.alu_b_src
      produced(memRe) := du.io.dcu_re
      produced(memWe) := du.io.dcu_we
      produced(memBe) := du.io.dcu_be
      produced(memEx) := du.io.mu_ex
      produced(hluHiWe) := du.io.hlu_hi_we
      produced(hluHiSrc) := du.io.hlu_hi_src
      produced(hluLoWe) := du.io.hlu_lo_we
      produced(hluLoSrc) := du.io.hlu_lo_src
      produced(cp0We) := du.io.cp0_we
      produced(rfuWe) := du.io.rfu_we
      produced(rfuAddr) := du.io.rfu_rd
      produced(rfuRdSrc) := du.io.rfu_rd_src
      produced(useRs) := du.io.use_rs
      produced(useRt) := du.io.use_rt
      produced(eret) := du.io.eret
      exceptionToRaise := du.io.exception
    }

    val rfuRead = new StageComponent {
      rfu.io.ra.index := input(inst).rs
      rfu.io.rb.index := input(inst).rt
      produced(rsValue) := rfu.io.ra.data
      produced(rtValue) := rfu.io.rb.data
    }

    val cp0Read = new StageComponent {
      cp0.io.read.addr.rd := input(inst).rd
      cp0.io.read.addr.sel := input(inst).sel
      produced(rfuData) := cp0.io.read.data
    }

    val juC = new StageComponent {
      ju.op := du.io.ju_op
      ju.a := output(rsValue).asSInt
      ju.b := output(rtValue).asSInt
      ju.pc_src := du.io.ju_pc_src
      ju.pc := input(pc) + 4
      ju.offset := input(inst).offset
      ju.index := input(inst).index

      when(ju.jump) {
        produced(jumpPc) := ju.jump_pc
      } otherwise {
        produced(jumpPc) := None
      }
    }

    val bdValue = Reg(Bool) init False
    when(finishing) {
      bdValue := du.io.ju_op =/= JU_OP.f
    }
    when(wantsFlush) {
      bdValue := False
    }
    produced(bd) := bdValue

    when(
      EX.valid && EX.currentInput(rfuWe) &&
        EX.currentInput(rfuAddr) === input(inst).rs
    ) {
      output(rsValue) := EX.currentOutput(rfuData)
    } elsewhen (ME.valid && ME.currentInput(rfuWe) &&
      ME.currentInput(rfuAddr) === input(inst).rs) {
      output(rsValue) := ME.currentOutput(rfuData)
    } elsewhen (WB.valid && WB.currentInput(rfuWe) &&
      WB.currentInput(rfuAddr) === input(inst).rs) {
      output(rsValue) := WB.currentInput(rfuData)
    } otherwise {
      output(rsValue) := produced(rsValue)
    }

    when(
      EX.valid && EX.currentInput(rfuWe) &&
        EX.currentInput(rfuAddr) === input(inst).rt
    ) {
      output(rtValue) := EX.currentOutput(rfuData)
    } elsewhen (ME.valid && ME.currentInput(rfuWe) &&
      ME.currentInput(rfuAddr) === input(inst).rt) {
      output(rtValue) := ME.currentOutput(rfuData)
    } elsewhen (WB.valid && WB.currentInput(rfuWe) &&
      WB.currentInput(rfuAddr) === input(inst).rt) {
      output(rtValue) := WB.currentInput(rfuData)
    } otherwise {
      output(rtValue) := produced(rtValue)
    }

    produced(rfuData) := produced(rfuRdSrc) mux (
      RFU_RD_SRC.cp0 -> cp0Read.produced(rfuData),
      default        -> B(input(pc) + 8)
    )

    when(
      EX.valid && EX.currentInput(rfuWe) &&
        EX.currentInput(rfuRdSrc) === RFU_RD_SRC.mu &&
        (EX.currentInput(rfuAddr) === input(inst).rs && produced(useRs) ||
          EX.currentInput(rfuAddr) === input(inst).rt && produced(useRt))
    ) {
      stalls := True
    }

    when(
      du.io.ju_op =/= JU_OP.f && ME.valid && ME.stalls &&
        ME.currentInput(rfuWe) && ME.currentInput(rfuRdSrc) === RFU_RD_SRC.mu &&
        (ME.currentInput(rfuAddr) === input(inst).rs && produced(useRs) ||
          ME.currentInput(rfuAddr) === input(inst).rt && produced(useRt))
    ) {
      stalls := True
    }
  }

  val IF = new Stage {
    val icuC = new StageComponent {
      io.iSramBus <> icu.io.sramBus

      icu.io.addr := input(pc)
      icu.io.re := True
      icu.io.we := False
      icu.io.be := U"11"
      icu.io.data_in.assignDontCare()
      icu.io.ex.assignDontCare()

      when(icu.io.stall) {
        stalls := True
        flushable := False
      }

      produced(inst) := icu.io.data_out
      exceptionToRaise := icu.io.exception
    }
  }

  // Pseudo-stage for producing PCU.
  val PC = new Stage {
    val pcValue = Updating(UInt(32 bits)) init ConstantVal.INIT_PC

    val wasSending = RegNext(sending) init False
    when(wasSending) { // Starting a new instruction
      pcValue.next := IF.currentInput(pc) + 4
    }
    when(ID.valid) {
      ID.currentOutput(jumpPc).whenIsDefined { jumpPc =>
        pcValue.next := jumpPc
      }
    }
    when(ME.valid) {
      cp0.io.jumpPc.whenIsDefined { pc =>
        pcValue.next := pc
      }
    }

    produced(pc) := pcValue.next
  }

  val stages = Seq(PC, IF, ID, EX, ME, WB)
  for ((prev, next) <- (stages zip stages.tail).reverse) {
    prev connect next
  }

  PC.signalsFlush.allowOverride
  PC.signalsFlush := False
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}
