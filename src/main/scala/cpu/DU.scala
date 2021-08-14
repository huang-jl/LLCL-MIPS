package cpu

import defs.InstructionSpec._
import defs.{ConstantVal, InstructionSpec, Mips32Inst}
import defs.Mips32InstImplicits._
import lib.{Decoder, Key, NotConsidered, Optional}
import spinal.core._

import scala.language.postfixOps

class DU extends Component {
  /// 解码指令

  val io = new Bundle {
    // in
    val inst = in(Mips32Inst())

    // out
    val alu_op     = out(ALU_OP)
    val alu_a_src  = out(ALU_A_SRC)
    val alu_b_src  = out(ALU_B_SRC)
    val complex_op = out Bool ()

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

    val exception = out(Optional(ExcCode()))
    val eret      = out Bool ()

    val canUseMem1ALU = out Bool ()
  }

  // wires
  val inst = io.inst
  val op   = inst.op
  val fn   = inst.fn

  //

  val aluOp     = Key(ALU_OP())
  val aluASrc   = Key(ALU_A_SRC())
  val aluBSrc   = Key(ALU_B_SRC())
  val complexOp = Key(Bool())

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

  val exception = Key(Optional(ExcCode()))
  val eret      = Key(Bool)
  val fuck      = Key(Bool)
  val privilege = Key(Bool) //是否是COP0特权指令

  val canUseMem1ALU    = Key(Bool())

  val decoder = new Decoder(32 bits, enableNotConsidered = true) {
    //下面是默认值，避免出现Latch
    default(aluOp) to ALU_OP.addu
    default(aluASrc) to ALU_A_SRC.rs
    default(aluBSrc) to ALU_B_SRC.rt
    default(complexOp) to False
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
    default(exception) to Optional.noneOf(ExcCode())
    default(eret) to False
    default(fuck) to False
    default(canUseMem1ALU) to False

    /* 可以延迟处理的运算 */
    for (inst <- Seq(ADDU, ADDIU, SUBU, SLT, SLTI, SLTU, SLTIU, AND, ANDI, LUI, NOR, OR, ORI, XOR, XORI, SLLV, SLL, SRAV, SRA, SRLV, SRL)) {
      on(inst) { set(canUseMem1ALU) to True }
    }
    /**/

    // Arithmetics
    val saShifts = Map( // useRt rfuWE
      SLL -> ALU_OP.sll,
      SRL -> ALU_OP.srl,
      SRA -> ALU_OP.sra
    )
    val multDivs = Map( // useRs useRt hiWE loWE
      DIV   -> ALU_OP.div,
      DIVU  -> ALU_OP.divu,
      MULT  -> ALU_OP.mult,
      MULTU -> ALU_OP.multu
    )
    val RTypeArithmetics = Map( // useRs useRt rfuWE
      ADD  -> ALU_OP.add,
      ADDU -> ALU_OP.addu,
      SUB  -> ALU_OP.sub,
      SUBU -> ALU_OP.subu,
      SLT  -> ALU_OP.slt,
      SLTU -> ALU_OP.sltu,
      MUL  -> ALU_OP.mul
    ) ++ Map( // useRs useRt rfuWE
      AND -> ALU_OP.and,
      NOR -> ALU_OP.nor,
      OR  -> ALU_OP.or,
      XOR -> ALU_OP.xor
    ) ++ Map( // useRs useRt rfuWE
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

    val ITypeArithmetics = Map( // useRs rfuWE
      ADDI  -> ALU_OP.add,
      ADDIU -> ALU_OP.addu,
      SLTI  -> ALU_OP.slt,
      SLTIU -> ALU_OP.sltu,
      ANDI  -> ALU_OP.and,
      ORI   -> ALU_OP.or,
      XORI  -> ALU_OP.xor
    ) ++ Map( // rfuWE
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
    val rtBranches = Map( // useRs useRt
      BEQ -> JU_OP.e,
      BNE -> JU_OP.noe
    )
    val branches = Map( // useRs
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

    val registerJumps = Set(JR, JALR) // useRs
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

    val linkingJumpsAndBranches = Set(BGEZAL, BLTZAL, JAL, JALR) // rfuWE
    for (inst <- linkingJumpsAndBranches) {
      on(inst) {
        set(rfuRd) to RFU_RD.ra
        set(rfuRdSrc) to RFU_RD_SRC.pc
      }
    }

    // M(FT)(HI/LO)
    on(MFHI) { // rfuWE
      set(rfuRd) to RFU_RD.rd
      set(rfuRdSrc) to RFU_RD_SRC.hi
    }
    on(MFLO) { // rfuWE
      set(rfuRd) to RFU_RD.rd
      set(rfuRdSrc) to RFU_RD_SRC.lo
    }
    on(MTHI) { // useRs hiWE
      set(hluHiWe) to True
      set(hluHiSrc) to HLU_SRC.rs
      set(useRs) to True
    }
    on(MTLO) { // useRs loWE
      set(hluLoWe) to True
      set(hluLoSrc) to HLU_SRC.rs
      set(useRs) to True
    }

    // Traps
    on(SYSCALL) { // TODO stall id
      set(exception) to Optional.some(ExcCode.syscall())
    }
    on(BREAK) { // TODO stall id
      set(exception) to Optional.some(ExcCode.break())
    }
    on(ERET) { // TODO stall id
      set(eret) to True
    }

    // Load & Store
    val loads = Map( // useRs memRE rfuWE
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

    val stores = Map( // useRs useRt memWE
      SB -> U"00",
      SH -> U"01",
      SW -> U"11"
    )
    for ((inst, be) <- stores) {
      on(inst) {
        set(dcuWe) to True
        set(dcuBe) to be
        set(useRs) to True //手册中对应的是base而不是rs
        set(useRt) to True
      }
    }

    // Privileged
    on(MTC0) { // cp0WE modifyCP0
      set(cp0We) to True
      set(useRt) to True
    }
    on(MFC0) { // cp0RE rfuWE
      set(cp0Re) to True
      set(rfuRd) to RFU_RD.rt
      set(rfuRdSrc) to RFU_RD_SRC.cp0
    }
    // Complex Op
    val complexOperation = Seq(MUL, MULT, MULTU, DIV, DIVU)
    for (inst <- complexOperation) on(inst) { set(complexOp) to True }

  }

  decoder.input := io.inst

  io.alu_op := decoder.output(aluOp)
  io.alu_a_src := decoder.output(aluASrc)
  io.alu_b_src := decoder.output(aluBSrc)
  io.complex_op := decoder.output(complexOp)

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
    io.exception := ExcCode.reservedInstruction
  } otherwise {
    io.exception := decoder.output(exception)
  }
  io.eret := decoder.output(eret)

  io.canUseMem1ALU := decoder.output(canUseMem1ALU)

}

object DU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DU)
  }

}
