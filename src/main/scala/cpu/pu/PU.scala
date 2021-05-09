package cpu.pu

import cpu.defs.ConstantVal._
import spinal.core._

class PU extends Component {
  // in
  val mu_data_out = in Bits (32 bits)
  val rfu_ra_v = in Bits (32 bits)
  val rfu_rb_v = in Bits (32 bits)

  val if_pcu_pc = in UInt (32 bits)

  val id_du_rs = in UInt (5 bits)
  val id_du_rt = in UInt (5 bits)
  val id_du_sa = in UInt (5 bits)
  val id_du_imm = in UInt (16 bits)
  val id_du_offset = in SInt (16 bits)
  val id_alu_op = in(ALU_OP)
  val id_alu_a_src = in(ALU_A_SRC)
  val id_alu_b_src = in(ALU_B_SRC)
  val id_mu_re = in Bool
  val id_mu_we = in Bool
  val id_mu_be = in UInt (2 bits)
  val id_mu_ex = in(MU_EX)
  val id_rfu_we = in Bool
  val id_rfu_rd = in UInt (5 bits)
  val id_rfu_rd_src = in(RFU_RD_SRC)
  val id_use_rs = in Bool
  val id_use_rt = in Bool

  val ex_alu_c = in Bits (32 bits)

  // out
  val du_rs_v = out Bits (32 bits)
  val du_rt_v = out Bits (32 bits)

  val mu_addr = out UInt (32 bits)
  val mu_data_in = out Bits (32 bits)
  val mu_re = out Bool
  val mu_we = out Bool
  val mu_be = out UInt (2 bits)
  val mu_ex = out(MU_EX)

  val id_pcu_pc = out(Reg(UInt(32 bits)))
  val id_du_inst = out(Reg(Bits(32 bits)))

  val ex_alu_op = out(Reg(ALU_OP))
  val ex_alu_a = out(Reg(UInt(32 bits)))
  val ex_alu_b = out(Reg(UInt(32 bits)))

  val wb_rfu_we = out(Reg(Bool))
  val wb_rfu_rd = out(Reg(UInt(5 bits)))
  val wb_rfu_rd_v = out(Reg(Bits(32 bits)))

  val if_stall = out Bool
  val id_stall = out Bool

  //
  val id_rfu_rd_v = (id_pcu_pc + 8).asBits

  val ex_du_rs = RegNext(id_du_rs)
  val ex_du_rt = RegNext(id_du_rt)
  val ex_du_offset = RegNext(id_du_offset)
  val ex_id_du_rs_v = RegNext(du_rs_v)
  val ex_du_rs_v = ex_id_du_rs_v
  val ex_id_du_rt_v = RegNext(du_rt_v)
  val ex_du_rt_v = ex_id_du_rt_v
  val ex_mu_addr = (ex_du_rs_v.asSInt + ex_du_offset).asUInt
  val ex_mu_data = ex_du_rt_v
  val ex_mu_re = Reg(Bool)
  val ex_mu_we = Reg(Bool)
  val ex_mu_be = RegNext(id_mu_be)
  val ex_mu_ex = RegNext(id_mu_ex)
  val ex_rfu_we = Reg(Bool)
  val ex_rfu_rd = RegNext(id_rfu_rd)
  val ex_rfu_rd_src = RegNext(id_rfu_rd_src)
  val ex_id_rfu_rd_v = RegNext(id_rfu_rd_v)
  val ex_rfu_rd_v = (ex_rfu_rd_src === RFU_RD_SRC.alu) ? ex_alu_c | ex_id_rfu_rd_v

  val me_mu_addr = RegNext(ex_mu_addr)
  val me_mu_data = RegNext(ex_mu_data)
  val me_mu_re = RegNext(ex_mu_re)
  val me_mu_we = RegNext(ex_mu_we)
  val me_mu_be = RegNext(ex_mu_be)
  val me_mu_ex = RegNext(ex_mu_ex)
  val me_rfu_we = RegNext(ex_rfu_we)
  val me_rfu_rd = RegNext(ex_rfu_rd)
  val me_rfu_rd_src = RegNext(ex_rfu_rd_src)
  val me_ex_rfu_rd_v = RegNext(ex_rfu_rd_v)
  val me_rfu_rd_v = (me_rfu_rd_src === RFU_RD_SRC.mu) ? mu_data_out | me_ex_rfu_rd_v

  when(id_stall) {
    ex_mu_re := False
    ex_mu_we := False
    ex_rfu_we := False
  } otherwise {
    id_pcu_pc := if_pcu_pc
    id_du_inst := mu_data_out

    ex_mu_re := id_mu_re
    ex_mu_we := id_mu_we
    ex_rfu_we := id_rfu_we
  }
  when(me_rfu_we & me_rfu_rd === ex_du_rs) {
    ex_du_rs_v := me_rfu_rd_v
  }
  when(me_rfu_we & me_rfu_rd === ex_du_rt) {
    ex_du_rt_v := me_rfu_rd_v
  }
  ex_alu_op := id_alu_op
  ex_alu_a := id_alu_a_src.mux(ALU_A_SRC.rs -> du_rs_v.asUInt, ALU_A_SRC.sa -> id_du_sa.resize(32))
  ex_alu_b := id_alu_b_src.mux(ALU_B_SRC.rt -> du_rt_v.asUInt, ALU_B_SRC.imm -> id_du_imm.resize(32))

  wb_rfu_we := me_rfu_we
  wb_rfu_rd := me_rfu_rd
  wb_rfu_rd_v := me_rfu_rd_v

  when(ex_rfu_we & ex_rfu_rd === id_du_rs) {
    du_rs_v := ex_rfu_rd_v
  } elsewhen (me_rfu_we & me_rfu_rd === id_du_rs) {
    du_rs_v := me_rfu_rd_v
  } elsewhen (wb_rfu_we & wb_rfu_rd === id_du_rs) {
    du_rs_v := wb_rfu_rd_v
  } otherwise {
    du_rs_v := rfu_ra_v
  }
  when(ex_rfu_we & ex_rfu_rd === id_du_rt) {
    du_rt_v := ex_rfu_rd_v
  } elsewhen (me_rfu_we & me_rfu_rd === id_du_rt) {
    du_rt_v := me_rfu_rd_v
  } elsewhen (wb_rfu_we & wb_rfu_rd === id_du_rt) {
    du_rt_v := wb_rfu_rd_v
  } otherwise {
    du_rt_v := rfu_rb_v
  }
  mu_data_in := me_mu_data
  mu_ex := me_mu_ex
  when(me_mu_re | me_mu_we) {
    mu_addr := me_mu_addr
    mu_re := me_mu_re
    mu_we := me_mu_we
    mu_be := me_mu_be
  } otherwise {
    mu_addr := if_pcu_pc
    mu_re := True
    mu_we := False
    mu_be := 2
  }

  id_stall := ex_rfu_we & ex_rfu_rd_src === RFU_RD_SRC.mu & (ex_rfu_rd === id_du_rs & id_use_rs | ex_rfu_rd === id_du_rt & id_use_rt)
  if_stall := id_stall | me_mu_re | me_mu_we
}

