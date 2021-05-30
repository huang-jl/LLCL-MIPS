package cpu.du

import cpu.defs.ConstantVal._
import spinal.core._
import spinal.lib.OHToUInt

//TODO
//1.关于MFC0、MTC0和ERET的逻辑
//2.关于E:Bundle的逻辑
//3.申明的alu:Bundle不知道是干什么的
//4.syscall和break的逻辑


class DU extends Component {
  /// 解码指令

  // in
  val inst = in Bits (32 bits)

  // out
  val rs = out UInt (5 bits)
  val rt = out UInt (5 bits)
  val rd = out UInt (5 bits)
  val sa = out UInt (5 bits)
  val imm = out UInt (32 bits)
  val offset = out SInt (16 bits)
  val index = out UInt (26 bits)

  val alu_op = out(ALU_OP)
  val alu_a_src = out(ALU_A_SRC)
  val alu_b_src = out(ALU_B_SRC)

  val dcu_re = out Bool
  val dcu_we = out Bool
  val dcu_be = out UInt (2 bits)
  val mu_ex = out(MU_EX)

  val hlu_hi_we = out Bool
  val hlu_hi_src = out(HLU_SRC)
  val hlu_lo_we = out Bool
  val hlu_lo_src = out(HLU_SRC)

  val rfu_we = out Bool
  val rfu_rd = out UInt (5 bits)
  val rfu_rd_src = out(RFU_RD_SRC)

  val ju_op = out(JU_OP)
  val ju_pc_src = out(JU_PC_SRC)

  val use_rs = out Bool
  val use_rt = out Bool

  val alu = new Bundle {
    val e = out Bool
  }

  val E = new Bundle {
    val RI, Sys, Bp = out Bool
  }

  // wires
  val op: Bits = inst(31 downto 26)
  val fn: Bits = inst(5 downto 0)

  //一系列常量
  val OpField = new OP_FIELD_CONST
  val Func = new FUNC_CONST
  val RegImmRT = new REGIMM_RT_CONST //即REGIMM类指令对应的rt字段

  //
  rs := U(inst(25 downto 21))
  rt := U(inst(20 downto 16))
  rd := U(inst(15 downto 11))
  sa := U(inst(10 downto 6))
  imm := op(2) ? U(inst(15 downto 0), 32 bits) | U(S(inst(15 downto 0), 32 bits))
  offset := S(inst(15 downto 0))
  index := U(inst(25 downto 0))

  //下面是默认值，避免出现Latch
  val default_ = new Area {
    alu_op := ALU_OP.addu
    alu_a_src := ALU_A_SRC.rs
    alu_b_src := ALU_B_SRC.rt

    dcu_re := False
    dcu_we := False
    dcu_be := U(0)
    mu_ex := MU_EX.s

    rfu_rd := U(0) //之后只要rfu_rd不为零，就会写GPR
    rfu_we := rfu_rd =/= U(0)
    rfu_rd_src := RFU_RD_SRC.alu

    hlu_hi_we := False
    hlu_hi_src := HLU_SRC.alu
    hlu_lo_we := False
    hlu_lo_src := HLU_SRC.alu

    ju_op := JU_OP.f
    ju_pc_src := JU_PC_SRC.rs

    use_rs := False
    use_rt := False
  }

  when(op === OpField.SPECIAL) {
    rfu_rd := rd
    rfu_rd_src := RFU_RD_SRC.alu
    when(fn === Func.sll | fn === Func.srl | fn === Func.sra) (alu_a_src := ALU_A_SRC.sa)
      .otherwise(alu_a_src := ALU_A_SRC.rs)
    alu_b_src := ALU_B_SRC.rt
    hlu_lo_we := fn === Func.div | fn === Func.divu | fn === Func.mult |
      fn === Func.multu | fn === Func.mtlo
    hlu_hi_we := fn === Func.div | fn === Func.divu | fn === Func.mult |
      fn === Func.multu | fn === Func.mthi
    switch(fn) {
      is(Func.sll)(alu_op := ALU_OP.sll)
      is(Func.srl)(alu_op := ALU_OP.srl)
      is(Func.sra)(alu_op := ALU_OP.sra)
      is(Func.sllv)(alu_op := ALU_OP.sll)
      is(Func.srlv)(alu_op := ALU_OP.srl)
      is(Func.srav)(alu_op := ALU_OP.sra)
      is(Func.jr) {
        ju_op := JU_OP.t
        ju_pc_src := JU_PC_SRC.rs
      }
      is(Func.jalr) {
        ju_op := JU_OP.t
        ju_pc_src := JU_PC_SRC.rs
        rfu_rd_src := RFU_RD_SRC.pc
      }
      is(Func.syscall) {
        //TODO
      }
      is(Func.break) {
        //TODO
      }

      is(Func.mfhi)(rfu_rd_src := RFU_RD_SRC.hi)
      is(Func.mthi)(hlu_hi_src := HLU_SRC.rs) //默认值是alu
      is(Func.mflo)(rfu_rd_src := RFU_RD_SRC.lo)
      is(Func.mtlo)(hlu_lo_src := HLU_SRC.rs) //默认值是alu

      is(Func.mult)(alu_op := ALU_OP.mult)
      is(Func.multu)(alu_op := ALU_OP.multu)
      is(Func.div) {
        alu_op := ALU_OP.div
      }
      is(Func.divu)(alu_op := ALU_OP.divu)
      is(Func.add)(alu_op := ALU_OP.add)
      is(Func.addu)(alu_op := ALU_OP.addu)
      is(Func.sub)(alu_op := ALU_OP.sub)
      is(Func.subu)(alu_op := ALU_OP.subu)
      is(Func.and)(alu_op := ALU_OP.and)
      is(Func.or)(alu_op := ALU_OP.or)
      is(Func.xor)(alu_op := ALU_OP.xor)
      is(Func.nor)(alu_op := ALU_OP.nor)
      is(Func.slt)(alu_op := ALU_OP.slt)
      is(Func.sltu)(alu_op := ALU_OP.sltu)
      default(rfu_we := False)
    }
  }.elsewhen(op === OpField.REG_IMM) {
    use_rs := True
    ju_pc_src := JU_PC_SRC.offset
    switch(inst(20 downto 16)) {
      is(RegImmRT.bltz)(ju_op := JU_OP.lz)
      is(RegImmRT.bgez)(ju_op := JU_OP.gez)
      is(RegImmRT.bltzal) {
        ju_op := JU_OP.lz
        rfu_rd_src := RFU_RD_SRC.pc
        rfu_rd := U(31)
      }
      is(RegImmRT.bgezal) {
        ju_op := JU_OP.gez
        rfu_rd_src := RFU_RD_SRC.pc
        rfu_rd := U(31)
      }
    }
  }.elsewhen(op(5 downto 2) === OpField.BRANCH) {
    use_rs := True
    use_rt := True
    ju_pc_src := JU_PC_SRC.offset
    switch(op(1 downto 0)) {
      //BEQ
      is(B"00")(ju_op := JU_OP.e)
      //BNE
      is(B"01")(ju_op := JU_OP.noe)
      //BLEZ
      is(B"10")(ju_op := JU_OP.lez)
      //BGTZ
      is(B"11")(ju_op := JU_OP.gz)
    }
  }.elsewhen(op(5 downto 3) === OpField.LOGIC_IMM) {
    rfu_rd := rt
    rfu_rd_src := RFU_RD_SRC.alu
    alu_a_src := ALU_A_SRC.rs
    alu_b_src := ALU_B_SRC.imm
    switch(op(2 downto 0)) {
      //addi
      is(B"000")(alu_op := ALU_OP.add)
      //addiu
      is(B"001")(alu_op := ALU_OP.addu)
      //slti
      is(B"010")(alu_op := ALU_OP.slt)
      //sltiu
      is(B"011")(alu_op := ALU_OP.sltu)
      //andi
      is(B"100")(alu_op := ALU_OP.and)
      //ori
      is(B"101")(alu_op := ALU_OP.or)
      //xori
      is(B"110")(alu_op := ALU_OP.xor)
      //lui
      is(B"111")(alu_op := ALU_OP.lu)
    }
  }.elsewhen(op(5 downto 3) === OpField.LOAD) {
    dcu_re := True
    dcu_we := False
    rfu_rd_src := RFU_RD_SRC.mu
    rfu_rd := rt
    mu_ex := op(2) ? MU_EX.u | MU_EX.s
    dcu_be := op(1 downto 0).asUInt
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
  }.elsewhen(op(5 downto 3) === OpField.STORE) {
    dcu_re := True
    dcu_we := True
    dcu_be := op(1 downto 0).asUInt
    //下面注释不要删，之后换cache可能有用
    //    switch(op(2 downto 0)) {
    //      //sb
    //      is(B"000")(dcu_be := 0)
    //      //sh
    //      is(B"001")(dcu_be := 1)
    //      //sw
    //      is(B"011")(dcu_be := 2)
    //    }
  }.elsewhen(op(5 downto 1) === OpField.JUMP) {
    ju_op := JU_OP.t
    ju_pc_src := JU_PC_SRC.index
    //j指令就是做上面两个事情即可
    when(op(0)) {
      //jal
      rfu_rd := U(31)
      rfu_rd_src := RFU_RD_SRC.pc
    }
  }.elsewhen(op === OpField.CP0) {
    when(inst(25)) {
      //C0:手册编码附录里，inst(25) = 1对应C0
      switch(inst(5 downto 0)) {
        //eret
        is(B"011000") {
          //TODO
        }
      }
    }
    switch(inst(25 downto 21)) {
      //mfc0
      is(B"00000") {
        //TODO
        rfu_rd := rt
        rfu_rd_src := RFU_RD_SRC.cp0
      }
      //mtc0
      is(B"00100") {
        //TODO
      }
    }
  }


  //  alu_op.assignFromBits(B(OHToUInt((is.sltu | is.sltiu) ## (is.slt | is.slti) ## is.divu ## is.div ## is.multu ## is.mult ## (is.sra | is.srav) ## (is.srl | is.srlv) ## is.lui ## (is.sll | is.sllv) ## is.nor ## is.xor ## is.or ## is.and ## is.subu ## is.sub ## (is.addu | is.addiu) ## (is.add | is.addi))))
  //  alu_a_src.assignFromBits(B(is.sll | is.srl | is.sra))
  //  alu_b_src.assignFromBits(B(is.special))
  //
  //  dcu_re := is.lb | is.lh | is.lw | is.lbu | is.lhu
  //  dcu_we := is.sb | is.sh | is.sw
  //  dcu_be := U(op(1 downto 0))
  //  mu_ex.assignFromBits(B(op(2)))
  //
  //  hlu_hi_we := is.mult | is.multu | is.div | is.divu | is.mthi
  //  hlu_hi_src.assignFromBits(B(is.mthi))
  //  hlu_lo_we := is.mult | is.multu | is.div | is.divu | is.mtlo
  //  hlu_hi_src.assignFromBits(B(is.mtlo))
  //
  //  val rfu_we = out Bool
  //  val rfu_rd = out UInt (5 bits)
  //  val rfu_rd_src = out(RFU_RD_SRC)
  //  rfu_we := is.special | is.;
  //  rfu_rd :=

  //  E.Sys := is.syscall
  //  E.Bp := is.break
}

object DU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DU)
  }
}

class OP_FIELD_CONST extends Area {

  val SPECIAL = B"000000"
  val REG_IMM = B"000001"
  val CP0 = B"010000"
  /** beq, bne, blez, bgtz */
  val BRANCH = B"0001"
  /** andi, lui, ori, xori */
  val LOGIC_IMM = B"001"
  /** lb, lbu, lh, lhu, lw */
  val LOAD = B"100"
  /** sb, sh, sw */
  val STORE = B"101"
  /** jal and j */
  val JUMP = B"00001"
}

class FUNC_CONST extends Area {
  //SPECIAL
  val sll = B"000000"
  val srl = B"000010"
  val sra = B"000011"
  val sllv = B"000100"
  val srlv = B"000110"
  val srav = B"000111"
  val jr = B"001000"
  val jalr = B"001001"
  val syscall = B"001100"
  val break = B"001101"
  val mfhi = B"010000"
  val mthi = B"010001"
  val mflo = B"010010"
  val mtlo = B"010011"
  val mult = B"011000"
  val multu = B"011001"
  val div = B"011010"
  val divu = B"011011"
  val add = B"100000"
  val addu = B"100001"
  val sub = B"100010"
  val subu = B"100011"
  val and = B"100100"
  val or = B"100101"
  val xor = B"100110"
  val nor = B"100111"
  val slt = B"101010"
  val sltu = B"101011"
}

class REGIMM_RT_CONST extends Area { //REGIMM
  val bltz = B"00000"
  val bgez = B"00001"
  val bltzal = B"10000"
  val bgezal = B"10001"
}
