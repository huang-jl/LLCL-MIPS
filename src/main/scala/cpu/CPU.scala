package cpu

import spinal.core._
import spinal.lib.{cpu => _, _}
import defs.Mips32InstImplicits._
import defs.{ConstantVal, Mips32Inst, SramBus, SramBusConfig}
import lib.{Key}

class CPU extends Component {
  val io = new Bundle {
    val iSramBus = master(SramBus(SramBusConfig(32, 32, 2)))
    val dSramBus = master(SramBus(SramBusConfig(32, 32, 2)))
    val debug    = out(DebugInterface())
  }
  val pcu = new PCU
  val icu = new MU
  val du  = new DU
  val ju  = new JU
  val alu = new ALU
  val dcu = new MU
  val hlu = new HLU
  val rfu = new RFU

  io.iSramBus <> icu.io.sramBus
  io.dSramBus <> dcu.io.sramBus
  io.debug.wb.rf.wen := B(4 bits, default -> rfu.io.write.valid)
  io.debug.wb.rf.wdata := rfu.io.write.data
  io.debug.wb.rf.wnum := rfu.io.write.index

  val pc         = Key(UInt(32 bits))
  val inst       = Key(Mips32Inst())
  val exception  = Key(EXCEPTION())
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
  val memData    = Key(Bits(32 bits))
  val hluHiWe    = Key(Bool)
  val hluHiSrc   = Key(HLU_SRC())
  val hluLoWe    = Key(Bool)
  val hluLoSrc   = Key(HLU_SRC())
  val hluHiData  = Key(Bits(32 bits))
  val hluLoData  = Key(Bits(32 bits))
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
    val rfuC = new StageComponent(inputKeys = Seq(rfuWe, rfuAddr, rfuData)) {
      rfu.io.write.valid := input(rfuWe)
      rfu.io.write.index := input(rfuAddr)
      rfu.io.write.data := input(rfuData)
      setInputReset(rfuWe, False)
    }

    val pcDebug = new StageComponent(inputKeys = Seq(pc)) {
      io.debug.wb.pc := input(pc)
    }
  }

  val ME = new Stage {
    val dcuC = new StageComponent(
      inputKeys = Seq(memRe, memWe, memBe, memEx, memAddr, memData),
      producedKeys = Seq(rfuData, exception),
      stalls = dcu.io.stall
    ) {
      dcu.io.re := input(memRe)
      dcu.io.we := input(memWe)
      dcu.io.be := input(memBe)
      dcu.io.ex := input(memEx)
      dcu.io.addr := input(memAddr)
      dcu.io.data_in := input(memData)
      setInputReset(memRe, False)
      setInputReset(memWe, False)

      produced(exception) := dcu.io.exception
      produced(rfuData) := dcu.io.data_out
    }

    val hluC = new StageComponent(
      inputKeys = Seq(
        hluHiWe,
        hluHiSrc,
        hluLoWe,
        hluLoSrc,
        rsValue,
        aluResultC,
        aluResultD
      ),
      producedKeys = Seq(hluHiData, hluLoData)
    ) {
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

    ensureInputKey(rfuRdSrc)
    ensureInputKey(rfuData)
    output(rfuData) := ((input(rfuRdSrc) === RFU_RD_SRC.mu)
      ? produced(rfuData)
      | input(rfuData))
  }

  val EX = new Stage {
    ensureInputKey(rsValue)
    ensureInputKey(rtValue)
    ensureInputKey(inst)

    ME.ensureInputKey(rfuWe)
    ME.ensureInputKey(rfuAddr)
    when(
      ME.input(rfuWe) &&
        ME.input(rfuAddr) === input(inst).rs
    ) {
      input(rsValue) := ME.produced(rfuData)
    }
    when(
      ME.input(rfuWe) &&
        ME.input(rfuAddr) === input(inst).rt
    ) {
      input(rtValue) := ME.produced(rfuData)
    }

    val aluC = new StageComponent(
      inputKeys = Seq(aluOp, aluASrc, aluBSrc, rsValue, rtValue, inst),
      producedKeys = Seq(aluResultC, aluResultD, exception)
    ) {
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
      produced(exception) := alu.io.exception
    }

    val aguC = new StageComponent(
      inputKeys = Seq(inst, rsValue),
      producedKeys = Seq(memAddr)
    ) {
      produced(memAddr) := U(S(input(rsValue)) + input(inst).offset)
    }

    ensureInputKey(rfuRdSrc)
    ensureInputKey(rfuData)
    ensureProducedKey(rfuData)
    produced(rfuData) := input(rfuRdSrc) mux (
      RFU_RD_SRC.alu -> produced(aluResultC).asBits,
      RFU_RD_SRC.hi -> (ME.input(hluHiWe) ? ME.produced(
        hluHiData
      ) | hlu.hi_v),
      RFU_RD_SRC.lo -> (ME.input(hluLoWe) ? ME.produced(
        hluLoData
      ) | hlu.lo_v),
      default -> input(rfuData)
    )
  }

  val ID = new Stage {
    val duC = new StageComponent(
      inputKeys = Seq(inst),
      producedKeys = Seq(
        aluOp,
        aluASrc,
        aluBSrc,
        memRe,
        memWe,
        memBe,
        memEx,
        hluHiWe,
        hluHiSrc,
        hluLoWe,
        hluLoSrc,
        rfuWe,
        rfuAddr,
        rfuRdSrc,
        useRs,
        useRt,
        exception
      )
    ) {
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
      produced(rfuWe) := du.io.rfu_we
      produced(rfuAddr) := du.io.rfu_rd
      produced(rfuRdSrc) := du.io.rfu_rd_src
      produced(useRs) := du.io.use_rs
      produced(useRt) := du.io.use_rt
      produced(exception) := du.io.exception
    }

    val rfuRead = new StageComponent(
      inputKeys = Seq(inst),
      producedKeys = Seq(rsValue, rtValue)
    ) {
      rfu.io.ra.index := input(inst).rs
      rfu.io.rb.index := input(inst).rt
      produced(rsValue) := rfu.io.ra.data
      produced(rtValue) := rfu.io.rb.data
    }

    val juC = new StageComponent(
      inputKeys = Seq(inst)
    ) {
      ju.op := du.io.ju_op
      ju.a := output(rsValue).asSInt
      ju.b := output(rtValue).asSInt
      ju.pc_src := du.io.ju_pc_src
      ju.pc := pcu.pc
      ju.offset := input(inst).offset
      ju.index := input(inst).index
    }

    EX.ensureInputKey(rfuWe)
    EX.ensureInputKey(rfuAddr)
    when(EX.input(rfuWe) && EX.input(rfuAddr) === input(inst).rs) {
      output(rsValue) := EX.produced(rfuData)
    } elsewhen (ME.input(rfuWe) &&
      ME.input(rfuAddr) === input(inst).rs) {
      output(rsValue) := ME.produced(rfuData)
    } elsewhen (WB.input(rfuWe) && WB.input(rfuAddr) === input(
      inst
    ).rs) {
      output(rsValue) := WB.input(rfuData)
    } otherwise {
      output(rsValue) := produced(rsValue)
    }

    when(EX.input(rfuWe) && EX.input(rfuAddr) === input(inst).rt) {
      output(rtValue) := EX.produced(rfuData)
    } elsewhen (ME
      .input(rfuWe) && ME.input(rfuAddr) === input(inst).rt) {
      output(rtValue) := ME.produced(rfuData)
    } elsewhen (WB.input(rfuWe) && WB.input(rfuAddr) === input(
      inst
    ).rt) {
      output(rtValue) := WB.input(rfuData)
    } otherwise {
      output(rtValue) := produced(rsValue)
    }

    ensureProducedKey(rfuData)
    ensureInputKey(pc)
    produced(rfuData) := produced(rfuRdSrc) mux (
      default -> B(input(pc) + 8)
    )

    ensureProducedKey(memData)
    produced(memData) := output(rtValue)

    EX.ensureInputKey(rfuWe)
    EX.ensureInputKey(rfuRdSrc)
    EX.ensureInputKey(rfuAddr)
    when(
      (EX.input(rfuWe) &&
        EX.input(rfuRdSrc) === RFU_RD_SRC.mu &&
        (EX.input(rfuAddr) === input(inst).rs && produced(useRs) ||
          EX.input(rfuAddr) === input(inst).rt && produced(useRt)))
    ) {
      stallsBySelf := True
    }
  }

  val IF = new Stage {
    val icuC = new StageComponent(
      producedKeys = Seq(pc, inst, exception),
      stalls = icu.io.stall
    ) {
      icu.io.addr := pcu.pc
      icu.io.re := True
      icu.io.we := False
      icu.io.be := U"11"
      icu.io.data_in.assignDontCare()
      icu.io.ex.assignDontCare()

      produced(pc) := pcu.pc
      produced(inst) := icu.io.data_out
    }

    pcu.we := ju.jump
    pcu.new_pc := ju.jump_pc
    pcu.stall := stalls
  }

  val stages = Seq(IF, ID, EX, ME, WB)
  for ((prev, next) <- (stages zip stages.tail).reverse) {
    prev connect next
  }
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}
