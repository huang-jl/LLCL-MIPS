package cpu

import defs.InstructionSpec._
import defs.Mips32Inst
import defs.Mips32InstImplicits._
import defs.ConstantVal
import lib.{Decoder, Key, NotConsidered, Optional}
import spinal.core._
import scala.language.postfixOps

class DU extends Component {
  /// 解码指令

  val io = new Bundle {
    // in
    val inst = in(Mips32Inst())

    // out
    val alu_op    = out(ALU_OP)
    val alu_a_src = out(ALU_A_SRC)
    val alu_b_src = out(ALU_B_SRC)

    val cp0_re = out Bool ()
    val cp0_we = out Bool ()

    val dcu_re = out Bool ()
    val dcu_we = out Bool ()
    val dcu_be = out UInt (2 bits)
    val mu_ex  = out(MU_EX)

    val hlu_hi_we  = out Bool ()
    val hlu_hi_src = out(HLU_SRC)
    val hlu_lo_we  = out Bool ()
    val hlu_lo_src = out(HLU_SRC)

    val rfu_we     = out Bool ()
    val rfu_rd     = out UInt (5 bits)
    val rfu_rd_src = out(RFU_RD_SRC)

    val ju_op     = out(JU_OP)
    val ju_pc_src = out(JU_PC_SRC)

    val use_rs = out Bool ()
    val use_rt = out Bool ()

    val exception = out(Optional(EXCEPTION()))
    val eret      = out Bool ()

    //tlb相关
    //读tlb到CP0
    val tlbr = Utils.instantiateWhen(out(Bool), ConstantVal.USE_TLB)
    //CP0写到tlb
    val tlbw = Utils.instantiateWhen(out(Bool), ConstantVal.USE_TLB)
    //probe tlb
    val tlbp = Utils.instantiateWhen(out(Bool), ConstantVal.USE_TLB)
    //tlbw index src
    val tlbIndexSrc =
      Utils.instantiateWhen(out(TLBIndexSrc()), ConstantVal.USE_TLB)

    // 仅在开启FINAL_MODE后使用的
    val invalidateDCache = Utils.instantiateWhen(out(Bool), ConstantVal.FINAL_MODE)
    val invalidateICache = Utils.instantiateWhen(out(Bool), ConstantVal.FINAL_MODE)

    val fuck = out Bool () //例如特权指令，拉高后让后面的指令都不发射
  }

  // wires
  val inst = io.inst
  val op   = inst.op
  val fn   = inst.fn

  //

  val aluOp   = Key(ALU_OP())
  val aluASrc = Key(ALU_A_SRC())
  val aluBSrc = Key(ALU_B_SRC())

  val cp0Re, cp0We = Key(Bool)

  val dcuRe, dcuWe = Key(Bool)
  val dcuBe        = Key(UInt(2 bits))
  val muEx         = Key(MU_EX())

  val hluHiWe, hluLoWe   = Key(Bool)
  val hluHiSrc, hluLoSrc = Key(HLU_SRC())

  object RFU_RD extends SpinalEnum {
    val none, rt, rd, ra = newElement()
  }

  val rfuRd    = Key(RFU_RD())
  val rfuRdSrc = Key(RFU_RD_SRC())

  val juOp    = Key(JU_OP())
  val juPcSrc = Key(JU_PC_SRC())

  val useRs, useRt = Key(Bool)

  val exception = Key(Optional(EXCEPTION()))
  val eret      = Key(Bool)

  val tlbr, tlbw, tlbp = Key(Bool)
  val tlbIndexSrc      = Key(TLBIndexSrc())
  val invalidateICache = Key(Bool)
  val invalidateDCache = Key(Bool)
  val fuck             = Key(Bool)

  val decoder = new Decoder(32 bits, enableNotConsidered = true) {
    //下面是默认值，避免出现Latch
    default(aluOp) to ALU_OP.addu
    default(aluASrc) to ALU_A_SRC.rs
    default(aluBSrc) to ALU_B_SRC.rt
    default(cp0Re) to False
    default(cp0We) to False
    default(dcuRe) to False
    default(dcuWe) to False
    default(dcuBe) to U"00"
    default(muEx) to MU_EX.s
    default(rfuRd) to RFU_RD.none
    default(rfuRdSrc) to RFU_RD_SRC.alu
    default(hluHiWe) to False
    default(hluHiSrc) to HLU_SRC.alu
    default(hluLoWe) to False
    default(hluLoSrc) to HLU_SRC.alu
    default(juOp) to JU_OP.f
    default(juPcSrc) to JU_PC_SRC.rs
    default(useRs) to False
    default(useRt) to False
    default(exception) to Optional.noneOf(EXCEPTION())
    default(eret) to False
    default(tlbr) to False
    default(tlbw) to False
    default(tlbp) to False
    default(tlbIndexSrc) to TLBIndexSrc.Index
    default(invalidateICache) to False
    default(invalidateDCache) to False
    default(fuck) to False

    // Arithmetics
    val saShifts = Map(
      SLL -> ALU_OP.sll,
      SRL -> ALU_OP.srl,
      SRA -> ALU_OP.sra
    )
    val multDivs = Map(
      DIV   -> ALU_OP.div,
      DIVU  -> ALU_OP.divu,
      MULT  -> ALU_OP.mult,
      MULTU -> ALU_OP.multu
    )
    val RTypeArithmetics = Map(
      ADD  -> ALU_OP.add,
      ADDU -> ALU_OP.addu,
      SUB  -> ALU_OP.sub,
      SUBU -> ALU_OP.subu,
      SLT  -> ALU_OP.slt,
      SLTU -> ALU_OP.sltu,
      MUL  -> ALU_OP.mul
    ) ++ Map(
      AND -> ALU_OP.and,
      NOR -> ALU_OP.nor,
      OR  -> ALU_OP.or,
      XOR -> ALU_OP.xor
    ) ++ Map(
      SLLV -> ALU_OP.sll,
      SRLV -> ALU_OP.srl,
      SRAV -> ALU_OP.sra
    ) ++ saShifts ++ multDivs

    for ((inst, op) <- RTypeArithmetics) {
      on(inst) {
        set(rfuRd) to RFU_RD.rd
        set(rfuRdSrc) to RFU_RD_SRC.alu
        set(aluBSrc) to ALU_B_SRC.rt
        set(aluOp) to op
        set(useRt) to True
      }

      if (saShifts contains inst) {
        on(inst) {
          set(aluASrc) to ALU_A_SRC.sa
        }
      } else {
        on(inst) {
          set(aluASrc) to ALU_A_SRC.rs
          set(useRs) to True
        }
      }

      if (multDivs contains inst) {
        on(inst) {
          set(hluHiWe) to True
          set(hluHiSrc) to HLU_SRC.alu
          set(hluLoWe) to True
          set(hluLoSrc) to HLU_SRC.alu
        }
      }
    }

    val ITypeArithmetics = Map(
      ADDI  -> ALU_OP.add,
      ADDIU -> ALU_OP.addu,
      SLTI  -> ALU_OP.slt,
      SLTIU -> ALU_OP.sltu,
      ANDI  -> ALU_OP.and,
      ORI   -> ALU_OP.or,
      XORI  -> ALU_OP.xor
    ) ++ Map(
      LUI -> ALU_OP.lu
    )

    for ((inst, op) <- ITypeArithmetics) {
      on(inst) {
        set(rfuRd) to RFU_RD.rt
        set(rfuRdSrc) to RFU_RD_SRC.alu
        set(aluASrc) to ALU_A_SRC.rs
        set(aluBSrc) to ALU_B_SRC.imm
        set(aluOp) to op
      }

      if (inst != LUI) {
        on(inst) {
          set(useRs) to True
        }
      }
    }

    // Branches and jumps
    val rtBranches = Map(
      BEQ -> JU_OP.e,
      BNE -> JU_OP.noe
    )
    val branches = Map(
      BGEZ   -> JU_OP.gez,
      BLTZ   -> JU_OP.lz,
      BGEZAL -> JU_OP.gez,
      BLTZAL -> JU_OP.lz,
      BGTZ   -> JU_OP.gz,
      BLEZ   -> JU_OP.lez
    ) ++ rtBranches

    for ((inst, op) <- branches) {
      on(inst) {
        set(useRs) to True
        set(juOp) to op
        set(juPcSrc) to JU_PC_SRC.offset
      }

      if (rtBranches contains inst) {
        on(inst) {
          set(useRt) to True
        }
      }
    }

    val registerJumps = Set(JR, JALR)
    for (inst <- registerJumps) {
      on(inst) {
        set(useRs) to True
        set(juOp) to JU_OP.t
        set(juPcSrc) to JU_PC_SRC.rs
      }
    }
    val targetJumps = Set(J, JAL)
    for (inst <- targetJumps) {
      on(inst) {
        set(juOp) to JU_OP.t
        set(juPcSrc) to JU_PC_SRC.index
      }
    }

    val linkingJumpsAndBranches = Set(BGEZAL, BLTZAL, JAL, JALR)
    for (inst <- linkingJumpsAndBranches) {
      on(inst) {
        set(rfuRd) to RFU_RD.ra
        set(rfuRdSrc) to RFU_RD_SRC.pc
      }
    }

    // M(FT)(HI/LO)
    on(MFHI) {
      set(rfuRd) to RFU_RD.rd
      set(rfuRdSrc) to RFU_RD_SRC.hi
    }
    on(MFLO) {
      set(rfuRd) to RFU_RD.rd
      set(rfuRdSrc) to RFU_RD_SRC.lo
    }
    on(MTHI) {
      set(hluHiWe) to True
      set(hluHiSrc) to HLU_SRC.rs
      set(useRs) to True
    }
    on(MTLO) {
      set(hluLoWe) to True
      set(hluLoSrc) to HLU_SRC.rs
      set(useRs) to True
    }

    // Traps
    on(SYSCALL) {
      set(exception) to Optional.some(EXCEPTION.Sys())
    }
    on(BREAK) {
      set(exception) to Optional.some(EXCEPTION.Bp())
    }
    on(ERET) {
      set(eret) to True
    }

    // Load & Store
    val loads = Map(
      LB  -> (MU_EX.s, U"00"),
      LBU -> (MU_EX.u, U"00"),
      LH  -> (MU_EX.s, U"01"),
      LHU -> (MU_EX.u, U"01"),
      LW  -> (MU_EX.s, U"11")
    )

    for ((inst, (ex, be)) <- loads) {
      on(inst) {
        set(dcuRe) to True
        set(rfuRd) to RFU_RD.rt
        set(rfuRdSrc) to RFU_RD_SRC.mu
        set(muEx) to ex
        set(dcuBe) to be
        set(useRs) to True //手册中对应的是base而不是rs
      }
    }

    val stores = Map(
      SB -> U"00",
      SH -> U"01",
      SW -> U"11"
    )
    for ((inst, be) <- stores) {
      on(inst) {
        set(dcuWe) to True
        set(dcuBe) to be
        set(useRs) to True //手册中对应的是base而不是rs
      }
    }

    // Privileged
    on(MTC0) {
      set(cp0We) to True
      set(useRt) to True
    }
    on(MFC0) {
      set(cp0Re) to True
      set(rfuRd) to RFU_RD.rt
      set(rfuRdSrc) to RFU_RD_SRC.cp0
    }

    //TLB
    if (ConstantVal.USE_TLB) {
      on(TLBR) {
        set(tlbr) to True
        set(fuck) to True
      }
      on(TLBWI) {
        set(tlbw) to True
        set(tlbIndexSrc) to TLBIndexSrc.Index
        set(fuck) to True
      }
      on(TLBWR) {
        set(tlbw) to True
        set(tlbIndexSrc) to TLBIndexSrc.Random
        set(fuck) to True
      }
      on(TLBP) {
        set(tlbp) to True
        set(fuck) to True
      }
    }

    if (ConstantVal.FINAL_MODE) {
      on(ICacheIndexInvalidate) {
        set(invalidateICache) to True
        set(fuck) to True
      }
      on(ICacheHitInvalidate) {
        set(invalidateICache) to True
        set(fuck) to True
      }
      on(DCacheIndexInvalidate) {
        set(invalidateDCache) to True
        set(fuck) to True
      }
      on(DCacheHitInvalidate) {
        set(invalidateDCache) to True
        set(fuck) to True
      }
    }
  }

  decoder.input := io.inst

  io.alu_op := decoder.output(aluOp)
  io.alu_a_src := decoder.output(aluASrc)
  io.alu_b_src := decoder.output(aluBSrc)

  io.cp0_re := decoder.output(cp0Re)
  io.cp0_we := decoder.output(cp0We)

  io.dcu_re := decoder.output(dcuRe)
  io.dcu_we := decoder.output(dcuWe)
  io.dcu_be := decoder.output(dcuBe)
  io.mu_ex := decoder.output(muEx)

  io.hlu_hi_we := decoder.output(hluHiWe)
  io.hlu_hi_src := decoder.output(hluHiSrc)
  io.hlu_lo_we := decoder.output(hluLoWe)
  io.hlu_lo_src := decoder.output(hluLoSrc)

  io.rfu_rd := decoder.output(rfuRd) mux (
    RFU_RD.none -> U"00000",
    RFU_RD.rt   -> io.inst.rt,
    RFU_RD.rd   -> io.inst.rd,
    RFU_RD.ra   -> U"11111"
  )
  io.rfu_we := io.rfu_rd =/= 0
  io.rfu_rd_src := decoder.output(rfuRdSrc)

  io.ju_op := decoder.output(juOp)
  io.ju_pc_src := decoder.output(juPcSrc)

  io.use_rs := decoder.output(useRs)
  io.use_rt := decoder.output(useRt)

  when(decoder.output(NotConsidered)) {
    io.exception := EXCEPTION.RI
  } otherwise {
    io.exception := decoder.output(exception)
  }
  io.eret := decoder.output(eret)
  io.fuck := decoder.output(fuck)

  if (ConstantVal.USE_TLB) {
    io.tlbr := decoder.output(tlbr)
    io.tlbw := decoder.output(tlbw)
    io.tlbp := decoder.output(tlbp)
    io.tlbIndexSrc := decoder.output(tlbIndexSrc)
  }

  if (ConstantVal.FINAL_MODE) {
    io.invalidateICache := decoder.output(invalidateICache)
    io.invalidateDCache := decoder.output(invalidateDCache)
  }
}

object DU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DU)
  }

}
