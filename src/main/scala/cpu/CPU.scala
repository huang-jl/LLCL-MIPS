package cpu

import defs.Mips32InstImplicits._
import defs.{ConstantVal, Mips32Inst, SramBus, SramBusConfig}
import lib.{Key, Optional}
import spinal.core._
import spinal.lib.{cpu => _, _}

class CPU extends Component {
  val io = new Bundle {
    val iSramBus = master(SramBus(SramBusConfig(32, 32, 2)))
    val dSramBus = master(SramBus(SramBusConfig(32, 32, 2)))
    val debug    = out(DebugInterface())
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
      }

      produced(rfuData) := dcu.io.data_out
      exceptionBySelf := dcu.io.exception
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
    }

    output(rfuData) := ((input(rfuRdSrc) === RFU_RD_SRC.mu)
      ? produced(rfuData)
      | input(rfuData))

    when(firing && exceptionBySelf.isDefined || exceptionByPrev.isDefined) {
      flushBySelf := True
    }
  }

  val EX = new Stage {
    when(
      ME.firing && ME.input(rfuWe) &&
        ME.input(rfuAddr) === input(inst).rs
    ) {
      input(rsValue) := ME.output(rfuData)
    }
    when(
      ME.firing && ME.input(rfuWe) &&
        ME.input(rfuAddr) === input(inst).rt
    ) {
      input(rtValue) := ME.output(rfuData)
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
      exceptionBySelf := alu.io.exception
    }

    val aguC = new StageComponent {
      produced(memAddr) := U(S(input(rsValue)) + input(inst).offset)
    }

    produced(rfuData) := input(rfuRdSrc) mux (
      RFU_RD_SRC.alu -> produced(aluResultC).asBits,
      RFU_RD_SRC.hi -> ((ME.firing && ME.input(hluHiWe))
        ? ME.produced(hluHiData)
        | hlu.hi_v),
      RFU_RD_SRC.lo -> ((ME.firing && ME.input(hluLoWe))
        ? ME.produced(hluLoData)
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
      exceptionBySelf := du.io.exception
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

    when(EX.firing && EX.input(rfuWe) && EX.input(rfuAddr) === input(inst).rs) {
      output(rsValue) := EX.produced(rfuData)
    } elsewhen (ME.firing && ME.input(rfuWe) &&
      ME.input(rfuAddr) === input(inst).rs) {
      output(rsValue) := ME.produced(rfuData)
    } elsewhen (WB.firing && WB.input(rfuWe) &&
      WB.input(rfuAddr) === input(inst).rs) {
      output(rsValue) := WB.input(rfuData)
    } otherwise {
      output(rsValue) := produced(rsValue)
    }

    when(EX.firing && EX.input(rfuWe) && EX.input(rfuAddr) === input(inst).rt) {
      output(rtValue) := EX.produced(rfuData)
    } elsewhen (ME.firing && ME.input(rfuWe) &&
      ME.input(rfuAddr) === input(inst).rt) {
      output(rtValue) := ME.produced(rfuData)
    } elsewhen (WB.firing && WB.input(rfuWe) &&
      WB.input(rfuAddr) === input(inst).rt) {
      output(rtValue) := WB.input(rfuData)
    } otherwise {
      output(rtValue) := produced(rtValue)
    }

    produced(rfuData) := produced(rfuRdSrc) mux (
      cp0     -> cp0Read.produced(rfuData),
      default -> B(input(pc) + 8)
    )

    when(
      (EX.firing && EX.input(rfuWe) &&
        EX.input(rfuRdSrc) === RFU_RD_SRC.mu &&
        (EX.input(rfuAddr) === input(inst).rs && produced(useRs) ||
          EX.input(rfuAddr) === input(inst).rt && produced(useRt)))
    ) {
      stallsBySelf := True
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
      }

      produced(inst) := icu.io.data_out
    }
  }

  // Pseudo-stage for producing PCU.
  val PC = new Stage {
    val pcValue     = UInt(32 bits)
    val pcValuePrev = RegNext(pcValue) init ConstantVal.INIT_PC
    pcValue := pcValuePrev

    val bdValue     = Bool
    val bdValuePrev = RegNext(bdValue) init False
    bdValue := bdValuePrev
    val bdNextInst = Reg(Bool) init False

    val IFWasStalling = RegNext(IF.firing && IF.stalls)
    when(IF.firing && !IFWasStalling) { // Starting a new instruction
      pcValue := IF.input(pc) + 4
      bdValue := bdNextInst
      bdNextInst := False
    }
    when(ID.firing) {
      ID.output(jumpPc).whenIsDefined { jumpPc =>
        pcValue := jumpPc
        bdNextInst := True
      }
    }
    when(ME.firing) {
      (ME.exceptionByPrev orElse ME.exceptionBySelf).whenIsDefined {
        exception =>
          pcValue := U"hBFC00380"
          bdValue := False
          bdNextInst := False
      }
    }

    produced(pc) := pcValue
    produced(bd) := bdValue
  }

  val stages = Seq(PC, IF, ID, EX, ME, WB)
  for ((prev, next) <- (stages zip stages.tail).reverse) {
    prev connect next
  }

  PC.flush.allowOverride
  PC.flush := False
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}
