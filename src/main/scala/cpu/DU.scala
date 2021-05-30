package cpu

import spinal.core._

import defs.ConstantVal._
import Utils._

case class Mips32Inst() extends Bundle {
  val bits = Bits(32 bits)

  def rs = bits(25 downto 21).asUInt // rt is also used for COP0
  def rt = bits(20 downto 16).asUInt // rt is also used for REGIMM
  def rd = bits(15 downto 11).asUInt
  def sa = bits(10 downto 6).asUInt
  def imm = bits(15 downto 0)
  def immExtended = op(2) ? zeroExtend(imm) | signExtend(imm)
  def offset = bits(15 downto 0).asSInt
  def index = bits(25 downto 0).asUInt
  def co = bits(25)
  def op = bits(31 downto 26)
  def fn = bits(5 downto 0)
}

object Mips32InstImplicits {
  implicit def bigIntToInst(bigInt: BigInt): Mips32Inst = {
    val inst = Mips32Inst()
    inst.bits := bigInt
    inst
  }

  implicit def bitsToInst(bits: Bits): Mips32Inst = {
    val inst = Mips32Inst()
    inst.bits := bits
    inst
  }
}

object OP_FIELD_CONST {

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

object FUNC_CONST {
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

object REGIMM_RT_CONST { //REGIMM
  val bltz = B"00000"
  val bgez = B"00001"
  val bltzal = B"10000"
  val bgezal = B"10001"
}

class DU extends Component {
  /// 解码指令

  val io = new Bundle {
    // in
    val inst = in(Mips32Inst())

    // out
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
  }

  // wires
  val inst = io.inst
  val op = inst.op
  val fn = inst.fn

  //

  //下面是默认值，避免出现Latch
  val defaults = new Area {
    io.alu_op := ALU_OP.addu
    io.alu_a_src := ALU_A_SRC.rs
    io.alu_b_src := ALU_B_SRC.rt

    io.dcu_re := False
    io.dcu_we := False
    io.dcu_be := 0
    io.mu_ex := MU_EX.s

    io.rfu_rd := 0 //之后只要rfu_rd不为零，就会写GPR
    io.rfu_we := io.rfu_rd =/= 0
    io.rfu_rd_src := RFU_RD_SRC.alu

    io.hlu_hi_we := False
    io.hlu_hi_src := HLU_SRC.alu
    io.hlu_lo_we := False
    io.hlu_lo_src := HLU_SRC.alu

    io.ju_op := JU_OP.f
    io.ju_pc_src := JU_PC_SRC.rs

    io.use_rs := False
    io.use_rt := False
  }

  when(op === OP_FIELD_CONST.SPECIAL) {
    io.rfu_rd := inst.rd
    io.rfu_rd_src := RFU_RD_SRC.alu
    io.alu_a_src := ALU_A_SRC.rs
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

}

object DU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DU)
  }
}
