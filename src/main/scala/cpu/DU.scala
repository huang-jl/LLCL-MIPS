package cpu

import defs.InstructionSpec._
import defs.Mips32Inst
import defs.Mips32InstImplicits._
import lib.Key
import lib.decoder.{DecoderFactory, NotConsidered}
import spinal.core._

class DU extends Component {
  /// 解码指令

  val io = new Bundle {
    // in
    val inst = in(Mips32Inst())

    // out
    val alu_op    = out(ALU_OP)
    val alu_a_src = out(ALU_A_SRC)
    val alu_b_src = out(ALU_B_SRC)

    val cp0_re = out Bool
    val cp0_we = out Bool

    val dcu_re = out Bool
    val dcu_we = out Bool
    val dcu_be = out UInt (2 bits)
    val mu_ex  = out(MU_EX)

    val hlu_hi_we  = out Bool
    val hlu_hi_src = out(HLU_SRC)
    val hlu_lo_we  = out Bool
    val hlu_lo_src = out(HLU_SRC)

    val rfu_we     = out Bool
    val rfu_rd     = out UInt (5 bits)
    val rfu_rd_src = out(RFU_RD_SRC)

    val ju_op     = out(JU_OP)
    val ju_pc_src = out(JU_PC_SRC)

    val use_rs = out Bool
    val use_rt = out Bool

    val exception = out(EXCEPTION())
  }

  // wires
  val inst = io.inst
  val op   = inst.op
  val fn   = inst.fn

  //

  val factory = new DecoderFactory(32, enableNotConsidered = true)

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

  val exception = Key(EXCEPTION())

  //下面是默认值，避免出现Latch
  factory
    .addDefault(aluOp, ALU_OP.addu)
    .addDefault(aluASrc, ALU_A_SRC.rs)
    .addDefault(aluBSrc, ALU_B_SRC.rt)
    .addDefault(cp0Re, False)
    .addDefault(cp0We, False)
    .addDefault(dcuRe, False)
    .addDefault(dcuWe, False)
    .addDefault(dcuBe, U"00")
    .addDefault(muEx, MU_EX.s)
    .addDefault(rfuRd, RFU_RD.none)
    .addDefault(rfuRdSrc, RFU_RD_SRC.alu)
    .addDefault(hluHiWe, False)
    .addDefault(hluHiSrc, HLU_SRC.alu)
    .addDefault(hluLoWe, False)
    .addDefault(hluLoSrc, HLU_SRC.alu)
    .addDefault(juOp, JU_OP.f)
    .addDefault(juPcSrc, JU_PC_SRC.rs)
    .addDefault(useRs, False)
    .addDefault(useRt, False)
    .addDefault(exception, EXCEPTION.None)

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
    SLTU -> ALU_OP.sltu
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
    factory
      .when(inst)
      .set(rfuRd, RFU_RD.rd)
      .set(rfuRdSrc, RFU_RD_SRC.alu)
      .set(aluBSrc, ALU_B_SRC.rt)
      .set(aluOp, op)
      .set(useRt, True)

    if (saShifts contains inst)
      factory
        .when(inst)
        .set(aluASrc, ALU_A_SRC.sa)
    else
      factory
        .when(inst)
        .set(aluASrc, ALU_A_SRC.rs)
        .set(useRs, True)

    if (multDivs contains inst)
      factory
        .when(inst)
        .set(hluHiWe, True)
        .set(hluHiSrc, HLU_SRC.alu)
        .set(hluLoWe, True)
        .set(hluLoSrc, HLU_SRC.alu)
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
    factory
      .when(inst)
      .set(rfuRd, RFU_RD.rt)
      .set(rfuRdSrc, RFU_RD_SRC.alu)
      .set(aluASrc, ALU_A_SRC.rs)
      .set(aluBSrc, ALU_B_SRC.imm)
      .set(aluOp, op)

    if (inst != LUI)
      factory
        .when(inst)
        .set(useRs, True)
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
    factory
      .when(inst)
      .set(useRs, True)
      .set(juOp, op)
      .set(juPcSrc, JU_PC_SRC.offset)

    if (rtBranches contains inst)
      factory
        .when(inst)
        .set(useRt, True)
  }

  val registerJumps = Set(JR, JALR)
  for (inst <- registerJumps) {
    factory
      .when(inst)
      .set(useRs, True)
      .set(juOp, JU_OP.t)
      .set(juPcSrc, JU_PC_SRC.rs)
  }
  val targetJumps = Set(J, JAL)
  for (inst <- targetJumps) {
    factory
      .when(inst)
      .set(juOp, JU_OP.t)
      .set(juPcSrc, JU_PC_SRC.index)
  }

  val linkingJumpsAndBranches = Set(BGEZAL, BLTZAL, JAL, JALR)
  for (inst <- linkingJumpsAndBranches) {
    factory
      .when(inst)
      .set(rfuRd, RFU_RD.ra)
      .set(rfuRdSrc, RFU_RD_SRC.pc)
  }

  // M(FT)(HI/LO)
  factory
    .when(MFHI)
    .set(rfuRd, RFU_RD.rd)
    .set(rfuRdSrc, RFU_RD_SRC.hi)

  factory
    .when(MFLO)
    .set(rfuRd, RFU_RD.rd)
    .set(rfuRdSrc, RFU_RD_SRC.lo)

  factory
    .when(MTHI)
    .set(hluHiWe, True)
    .set(hluHiSrc, HLU_SRC.rs)
    .set(useRs, True)

  factory
    .when(MTLO)
    .set(hluLoWe, True)
    .set(hluLoSrc, HLU_SRC.rs)
    .set(useRs, True)

  // Traps
  factory
    .when(SYSCALL)
    .set(exception, EXCEPTION.Sys)

  factory
    .when(BREAK)
    .set(exception, EXCEPTION.Bp)

  factory
    .when(ERET)
    .set(exception, EXCEPTION.Eret)

  // Load & Store
  val loads = Map(
    LB  -> (MU_EX.s, U"00"),
    LBU -> (MU_EX.u, U"00"),
    LH  -> (MU_EX.s, U"01"),
    LHU -> (MU_EX.u, U"01"),
    LW  -> (MU_EX.s, U"11") // TODO: 更换 Cache 可能要改成 10
  )

  for ((inst, (ex, be)) <- loads) {
    factory
      .when(inst)
      .set(dcuRe, True)
      .set(rfuRd, RFU_RD.rt)
      .set(rfuRdSrc, RFU_RD_SRC.mu)
      .set(muEx, ex)
      .set(dcuBe, be)
      .set(useRs, True) //手册中对应的是base而不是rs
  }

  val stores = Map(
    SB -> U"00",
    SH -> U"01",
    SW -> U"11" // TODO: 更换 Cache 可能要改成 10
  )
  for ((inst, be) <- stores) {
    factory
      .when(inst)
      .set(dcuWe, True)
      .set(dcuBe, be)
      .set(useRs, True) //手册中对应的是base而不是rs
  }

  // Privileged
  factory
    .when(MTC0)
    .set(cp0Re, True)
    .set(cp0We, True)
    .set(useRt, True)

  factory
    .when(MFC0)
    .set(cp0Re, True)
    .set(rfuRd, RFU_RD.rt)
    .set(rfuRdSrc, RFU_RD_SRC.cp0)

  // TODO

  val decoder = factory.createDecoder(io.inst)

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

  /*
  when(op === OP_FIELD_CONST.SPECIAL) {
    io.rfu_rd := inst.rd
    io.rfu_rd_src := RFU_RD_SRC.alu
    when(fn === FUNC_CONST.sll | fn === FUNC_CONST.srl | fn === FUNC_CONST.sra)(
      io.alu_a_src := ALU_A_SRC.sa
    )
      .otherwise(io.alu_a_src := ALU_A_SRC.rs)
    io.alu_b_src := ALU_B_SRC.rt
    io.hlu_lo_we := fn === FUNC_CONST.div | fn === FUNC_CONST.divu | fn === FUNC_CONST.mult |
      fn === FUNC_CONST.multu | fn === FUNC_CONST.mtlo
    io.hlu_hi_we := fn === FUNC_CONST.div | fn === FUNC_CONST.divu | fn === FUNC_CONST.mult |
      fn === FUNC_CONST.multu | fn === FUNC_CONST.mthi
    switch(fn) {
      is(FUNC_CONST.sll)(io.alu_op := ALU_OP.sll)
      is(FUNC_CONST.srl)(io.alu_op := ALU_OP.srl)
      is(FUNC_CONST.sra)(io.alu_op := ALU_OP.sra)
      is(FUNC_CONST.sllv)(io.alu_op := ALU_OP.sll)
      is(FUNC_CONST.srlv)(io.alu_op := ALU_OP.srl)
      is(FUNC_CONST.srav)(io.alu_op := ALU_OP.sra)
      is(FUNC_CONST.jr) {
        io.ju_op := JU_OP.t
        io.ju_pc_src := JU_PC_SRC.rs
      }
      is(FUNC_CONST.jalr) {
        io.ju_op := JU_OP.t
        io.ju_pc_src := JU_PC_SRC.rs
      }
      is(FUNC_CONST.syscall) {
        //TODO
      }
      is(FUNC_CONST.break) {
        //TODO
      }

      is(FUNC_CONST.mfhi)(io.rfu_rd_src := RFU_RD_SRC.hi)
      is(FUNC_CONST.mthi)(io.hlu_hi_src := HLU_SRC.rs) //默认值是alu
      is(FUNC_CONST.mflo)(io.rfu_rd_src := RFU_RD_SRC.lo)
      is(FUNC_CONST.mtlo)(io.hlu_lo_src := HLU_SRC.rs) //默认值是alu

      is(FUNC_CONST.mult)(io.alu_op := ALU_OP.mult)
      is(FUNC_CONST.multu)(io.alu_op := ALU_OP.multu)
      is(FUNC_CONST.div) {
        io.alu_op := ALU_OP.div
      }
      is(FUNC_CONST.divu)(io.alu_op := ALU_OP.divu)
      is(FUNC_CONST.add)(io.alu_op := ALU_OP.add)
      is(FUNC_CONST.addu)(io.alu_op := ALU_OP.addu)
      is(FUNC_CONST.sub)(io.alu_op := ALU_OP.sub)
      is(FUNC_CONST.subu)(io.alu_op := ALU_OP.subu)
      is(FUNC_CONST.and)(io.alu_op := ALU_OP.and)
      is(FUNC_CONST.or)(io.alu_op := ALU_OP.or)
      is(FUNC_CONST.xor)(io.alu_op := ALU_OP.xor)
      is(FUNC_CONST.nor)(io.alu_op := ALU_OP.nor)
      is(FUNC_CONST.slt)(io.alu_op := ALU_OP.slt)
      is(FUNC_CONST.sltu)(io.alu_op := ALU_OP.sltu)
      default(io.rfu_we := False)
    }
  }.elsewhen(op === OP_FIELD_CONST.REG_IMM) {
    io.use_rs := True
    io.ju_pc_src := JU_PC_SRC.offset
    switch(inst.rt.asBits) {
      is(REGIMM_RT_CONST.bltz)(io.ju_op := JU_OP.lz)
      is(REGIMM_RT_CONST.bgez)(io.ju_op := JU_OP.gez)
      is(REGIMM_RT_CONST.bltzal) {
        io.ju_op := JU_OP.lz
        io.rfu_rd_src := RFU_RD_SRC.pc
        io.rfu_rd := U(31)
      }
      is(REGIMM_RT_CONST.bgezal) {
        io.ju_op := JU_OP.gez
        io.rfu_rd_src := RFU_RD_SRC.pc
        io.rfu_rd := U(31)
      }
    }
  }.elsewhen(op(5 downto 2) === OP_FIELD_CONST.BRANCH) {
    io.use_rs := True
    io.use_rt := True
    io.ju_pc_src := JU_PC_SRC.offset
    switch(op(1 downto 0)) {
      //BEQ
      is(B"00")(io.ju_op := JU_OP.e)
      //BNE
      is(B"01")(io.ju_op := JU_OP.noe)
      //BLEZ
      is(B"10")(io.ju_op := JU_OP.lez)
      //BGTZ
      is(B"11")(io.ju_op := JU_OP.gz)
    }
  }.elsewhen(op(5 downto 3) === OP_FIELD_CONST.LOGIC_IMM) {
    io.rfu_rd := inst.rt
    io.rfu_rd_src := RFU_RD_SRC.alu
    io.alu_a_src := ALU_A_SRC.rs
    io.alu_b_src := ALU_B_SRC.imm
    switch(op(2 downto 0)) {
      //addi
      is(B"000")(io.alu_op := ALU_OP.add)
      //addiu
      is(B"001")(io.alu_op := ALU_OP.addu)
      //slti
      is(B"010")(io.alu_op := ALU_OP.slt)
      //sltiu
      is(B"011")(io.alu_op := ALU_OP.sltu)
      //andi
      is(B"100")(io.alu_op := ALU_OP.and)
      //ori
      is(B"101")(io.alu_op := ALU_OP.or)
      //xori
      is(B"110")(io.alu_op := ALU_OP.xor)
      //lui
      is(B"111")(io.alu_op := ALU_OP.lu)
    }
  }.elsewhen(op(5 downto 3) === OP_FIELD_CONST.LOAD) {
    io.dcu_re := True
    io.dcu_we := False
    io.rfu_rd_src := RFU_RD_SRC.mu
    io.rfu_rd := inst.rt
    io.mu_ex := op(2) ? MU_EX.u | MU_EX.s
    io.dcu_be := op(1 downto 0).asUInt
    //下面注释不要删，之后换cache可能有用
    //    switch(op(2 downto 0)) {
    //      //lb
    //      is(B"000")(dcu_be := 0)
    //      //lh
    //      is(B"001")(dcu_be := 1)
    //      //lw
    //      is(B"011")(dcu_be := 2)
    //      //lbu
    //      is(B"100")(dcu_be := 0)
    //      //lhu
    //      is(B"101")(dcu_be := 1)
    //    }
  }.elsewhen(op(5 downto 3) === OP_FIELD_CONST.STORE) {
    io.dcu_re := True
    io.dcu_we := True
    io.dcu_be := op(1 downto 0).asUInt
    //下面注释不要删，之后换cache可能有用
    //    switch(op(2 downto 0)) {
    //      //sb
    //      is(B"000")(dcu_be := 0)
    //      //sh
    //      is(B"001")(dcu_be := 1)
    //      //sw
    //      is(B"011")(dcu_be := 2)
    //    }
  }.elsewhen(op(5 downto 1) === OP_FIELD_CONST.JUMP) {
    io.ju_op := JU_OP.t
    io.ju_pc_src := JU_PC_SRC.index
    //j指令就是做上面两个事情即可
    when(op(0)) {
      //jal
      io.rfu_rd := U(31)
      io.rfu_rd_src := RFU_RD_SRC.pc
    }
  }.elsewhen(op === OP_FIELD_CONST.CP0) {
    when(inst.co) {
      //CO:手册编码附录里，inst(25) = 1对应CO
      switch(inst.fn) {
        //eret
        is(B"011000") {
          //TODO
        }
      }
    }
    switch(inst.rs.asBits) {
      //mfc0
      is(B"00000") {
        //TODO
        io.rfu_rd := inst.rt
        io.rfu_rd_src := RFU_RD_SRC.cp0
      }
      //mtc0
      is(B"00100") {
        //TODO
      }
    }
  }
   */
}

object DU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DU)
  }
}
