package cpu.pu

import cpu.defs.ConstantVal._
import spinal.core._

class PU extends Component {
  /// 数据流水线

  // in
  val hlu_hi_v = in Bits (32 bits)
  val hlu_lo_v = in Bits (32 bits)
  val rfu_ra_v = in Bits (32 bits)
  val rfu_rb_v = in Bits (32 bits)

  val if_pcu_pc = in UInt (32 bits)
  val if_icu_stall = in Bool
  val if_icu_data = in Bits (32 bits)

  val id_du_rs = in UInt (5 bits)
  val id_du_rt = in UInt (5 bits)
  val id_du_sa = in UInt (5 bits)
  val id_du_imm = in UInt (32 bits)
  val id_du_offset = in SInt (16 bits)
  val id_ju_jump = in Bool
  val id_alu_op = in(ALU_OP)
  val id_alu_a_src = in(ALU_A_SRC)
  val id_alu_b_src = in(ALU_B_SRC)
  val id_dcu_re = in Bool
  val id_dcu_we = in Bool
  val id_dcu_be = in UInt (2 bits)
  val id_mu_ex = in(MU_EX)
  val id_hlu_hi_we = in Bool
  val id_hlu_hi_src = in(HLU_SRC)
  val id_hlu_lo_we = in Bool
  val id_hlu_lo_src = in(HLU_SRC)
  val id_rfu_we = in Bool
  val id_rfu_rd = in UInt (5 bits)
  val id_rfu_rd_src = in(RFU_RD_SRC)
  val id_use_rs = in Bool
  val id_use_rt = in Bool

  val ex_alu_c = in Bits (32 bits)
  val ex_alu_d = in Bits (32 bits)

  val me_dcu_stall = in Bool
  val me_mu_data_out = in Bits (32 bits)

  // out
  val id_pcu_pc = out(Reg(UInt(32 bits)) init INIT_PC)
  val id_du_inst = out(Reg(Bits(32 bits)) init INST_NOP)
  val id_du_rs_v = out Bits (32 bits)
  val id_du_rt_v = out Bits (32 bits)

  val ex_alu_op = out(Reg(ALU_OP))
  val ex_alu_a = out(Reg(UInt(32 bits)))
  val ex_alu_b = out(Reg(UInt(32 bits)))

  val me_dcu_addr = out(Reg(UInt(32 bits)))
  val me_dcu_data = out(Reg(Bits(32 bits)))
  val me_dcu_re = out(RegInit(False))
  val me_dcu_we = out(RegInit(False))
  val me_dcu_be = out(Reg(UInt(2 bits)))
  val me_dcu_ex = out(Reg(MU_EX))
  val me_hlu_hi_we = out(RegInit(False))
  val me_hlu_new_hi = out(Reg(Bits(32 bits)))
  val me_hlu_lo_we = out(RegInit(False))
  val me_hlu_new_lo = out(Reg(Bits(32 bits)))

  val wb_pcu_pc = out(Reg(UInt(32 bits)))
  val wb_rfu_we = out(RegInit(False))
  val wb_rfu_rd = out(Reg(UInt(5 bits)))
  val wb_rfu_rd_v = out(Reg(Bits(32 bits)))

  val if_stall = out Bool

  //
  val id_rfu_rd_v = B(id_pcu_pc + 8)

  val ex_pcu_pc = Reg(UInt(32 bits))
  val ex_du_rs = Reg(UInt(5 bits))
  val ex_du_rt = Reg(UInt(5 bits))
  val ex_du_offset = Reg(SInt(16 bits))
  val ex_id_du_rs_v = Reg(Bits(32 bits))
  val ex_du_rs_v = ex_id_du_rs_v
  val ex_id_du_rt_v = Reg(Bits(32 bits))
  val ex_du_rt_v = ex_id_du_rt_v
  val ex_dcu_addr = U(S(ex_du_rs_v) + ex_du_offset)
  val ex_dcu_data = ex_du_rt_v
  val ex_dcu_re = RegInit(False)
  val ex_dcu_we = RegInit(False)
  val ex_dcu_be = Reg(UInt(2 bits))
  val ex_mu_ex = Reg(MU_EX)
  val ex_hlu_hi_we = RegInit(False)
  val ex_hlu_hi_src = Reg(HLU_SRC)
  val ex_hlu_hi_v = me_hlu_hi_we ? me_hlu_new_hi | hlu_hi_v
  val ex_hlu_lo_we = RegInit(False)
  val ex_hlu_lo_src = Reg(HLU_SRC)
  val ex_hlu_lo_v = me_hlu_lo_we ? me_hlu_new_lo | hlu_lo_v
  val ex_rfu_we = RegInit(False)
  val ex_rfu_rd = Reg(UInt(5 bits))
  val ex_rfu_rd_src = Reg(RFU_RD_SRC)
  val ex_id_rfu_rd_v = Reg(Bits(32 bits))
  val ex_rfu_rd_v = ex_rfu_rd_src.mux(RFU_RD_SRC.alu -> ex_alu_c, RFU_RD_SRC.hi -> ex_hlu_hi_v, RFU_RD_SRC.lo -> ex_hlu_lo_v, default -> ex_id_rfu_rd_v)

  val me_pcu_pc = Reg(UInt(32 bits))
  val me_rfu_we = RegInit(False)
  val me_rfu_rd = Reg(UInt(5 bits))
  val me_rfu_rd_src = Reg(RFU_RD_SRC)
  val me_ex_rfu_rd_v = Reg(Bits(32 bits))
  val me_rfu_rd_v = (me_rfu_rd_src === RFU_RD_SRC.mu) ? me_mu_data_out | me_ex_rfu_rd_v

  val me_stall = me_dcu_stall
  val ex_stall = me_stall
  val id_stall = ex_stall | ex_rfu_we & B(ex_rfu_rd_src)(2) & (ex_rfu_rd === id_du_rs & id_use_rs | ex_rfu_rd === id_du_rt & id_use_rt) | if_icu_stall & id_ju_jump
  if_stall := id_stall | if_icu_stall

  val IF = new Bundle {
    val pcu = new Bundle {
      val E = new Bundle {
        val AdEIL = in Bool
      }
    }

    val E = Bits(NUM_EXCEPTIONS bits)
  }

  val ID = new Bundle {
    val du = new Bundle {
      val E = new Bundle {
        val RI, Sys, Bp = in Bool
      }
    }

    val IF_E = Reg(Bits(NUM_EXCEPTIONS bits))
    val E = Bits(NUM_EXCEPTIONS bits)
  }

  val EX = new Bundle {
    val alu = new Bundle {
      val E = new Bundle {
        val Ov = in Bool
      }
    }

    val ID_E = Reg(Bits(NUM_EXCEPTIONS bits))
    val E = Bits(NUM_EXCEPTIONS bits)
  }

  val ME = new Bundle {
    val EX_E = Reg(Bits(NUM_EXCEPTIONS bits))
    val E = Bits(NUM_EXCEPTIONS bits)
  }

  when(!id_stall) {
    when(if_stall) {
      id_du_inst := INST_NOP

      ID.IF_E := 0
    } otherwise {
      id_pcu_pc := if_pcu_pc
      id_du_inst := if_icu_data

      ID.IF_E := IF.E
    }
  }

  when(!ex_stall) {
    when(id_stall) {
      ex_dcu_re := False
      ex_dcu_we := False
      ex_rfu_we := False
      ex_hlu_hi_we := False
      ex_hlu_lo_we := False

      EX.ID_E := 0
    } otherwise {
      ex_pcu_pc := id_pcu_pc
      ex_alu_op := id_alu_op
      ex_alu_a := id_alu_a_src.mux(ALU_A_SRC.rs -> U(id_du_rs_v), ALU_A_SRC.sa -> id_du_sa.resize(32))
      ex_alu_b := id_alu_b_src.mux(ALU_B_SRC.rt -> U(id_du_rt_v), ALU_B_SRC.imm -> id_du_imm)
      ex_du_rs := id_du_rs
      ex_du_rt := id_du_rt
      ex_du_offset := id_du_offset
      ex_id_du_rs_v := id_du_rs_v
      ex_id_du_rt_v := id_du_rt_v
      ex_dcu_re := id_dcu_re
      ex_dcu_we := id_dcu_we
      ex_dcu_be := id_dcu_be
      ex_mu_ex := id_mu_ex
      ex_hlu_hi_we := id_hlu_hi_we
      ex_hlu_hi_src := id_hlu_hi_src
      ex_hlu_lo_we := id_hlu_lo_we
      ex_hlu_lo_src := id_hlu_lo_src
      ex_rfu_we := id_rfu_we
      ex_rfu_rd := id_rfu_rd
      ex_rfu_rd_src := id_rfu_rd_src
      ex_id_rfu_rd_v := id_rfu_rd_v

      EX.ID_E := ID.E
    }
  }

  when(!me_stall) {
    //    when(ex_stall) {
    //      me_dcu_re := False
    //      me_dcu_we := False
    //      me_rfu_we := False
    //    } otherwise {
    me_pcu_pc := ex_pcu_pc
    me_dcu_addr := ex_dcu_addr
    me_dcu_data := ex_dcu_data
    me_dcu_re := ex_dcu_re
    me_dcu_we := ex_dcu_we
    me_dcu_be := ex_dcu_be
    me_dcu_ex := ex_mu_ex
    me_hlu_hi_we := ex_hlu_hi_we
    me_hlu_new_hi := (ex_hlu_hi_src === HLU_SRC.rs) ? ex_du_rs_v | ex_alu_c
    me_hlu_lo_we := ex_hlu_lo_we
    me_hlu_new_lo := (ex_hlu_lo_src === HLU_SRC.rs) ? ex_du_rs_v | ex_alu_d
    me_rfu_we := ex_rfu_we
    me_rfu_rd := ex_rfu_rd
    me_rfu_rd_src := ex_rfu_rd_src
    me_ex_rfu_rd_v := ex_rfu_rd_v

    ME.EX_E := EX.E
    //    }
  }

  when(me_stall) {
    wb_rfu_we := False
  } otherwise {
    wb_pcu_pc := me_pcu_pc
    wb_rfu_we := me_rfu_we
    wb_rfu_rd := me_rfu_rd
    wb_rfu_rd_v := me_rfu_rd_v
  }

  when(me_rfu_we & me_rfu_rd === ex_du_rs) {
    ex_du_rs_v := me_rfu_rd_v
  }
  when(me_rfu_we & me_rfu_rd === ex_du_rt) {
    ex_du_rt_v := me_rfu_rd_v
  }


  when(ex_rfu_we & ex_rfu_rd === id_du_rs) {
    id_du_rs_v := ex_rfu_rd_v
  } elsewhen (me_rfu_we & me_rfu_rd === id_du_rs) {
    id_du_rs_v := me_rfu_rd_v
  } elsewhen (wb_rfu_we & wb_rfu_rd === id_du_rs) {
    id_du_rs_v := wb_rfu_rd_v
  } otherwise {
    id_du_rs_v := rfu_ra_v
  }
  when(ex_rfu_we & ex_rfu_rd === id_du_rt) {
    id_du_rt_v := ex_rfu_rd_v
  } elsewhen (me_rfu_we & me_rfu_rd === id_du_rt) {
    id_du_rt_v := me_rfu_rd_v
  } elsewhen (wb_rfu_we & wb_rfu_rd === id_du_rt) {
    id_du_rt_v := wb_rfu_rd_v
  } otherwise {
    id_du_rt_v := rfu_rb_v
  }

  IF.E := 0
  IF.E(U(B(EXCEPTION.AdEIL))) := IF.pcu.E.AdEIL

  ID.E := ID.IF_E
  ID.E(U(B(EXCEPTION.RI))) := ID.du.E.RI
  ID.E(U(B(EXCEPTION.Sys))) := ID.du.E.Sys
  ID.E(U(B(EXCEPTION.Bp))) := ID.du.E.Bp

  EX.E := EX.ID_E
  EX.E(U(B(EXCEPTION.Ov))) := EX.alu.E.Ov

  ME.E := ME.EX_E
  ME.E(U(B(EXCEPTION.AdEDL))) := False
  ME.E(U(B(EXCEPTION.AdEDS))) := False
}

object PU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new PU)
  }
}
