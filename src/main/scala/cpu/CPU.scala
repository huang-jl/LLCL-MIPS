package cpu

import cache._
import cpu.alu._
import cpu.defs.ConstantVal.MU_EX
import cpu.du._
import cpu.ju._
import cpu.mu._
import cpu.pcu._
import cpu.rfu._
import cpu.pu._
import spinal.core._

class CPU extends Component {
  val pcu = new PCU
  val icu = new MU
  val du = new DU
  val ju = new JU
  val alu = new ALU
  val dcu = new MU
  val rfu = new RFU
  val pu = new PU

  pcu.stall := pu.if_stall
  pcu.we := ju.jump
  pcu.new_pc := ju.jump_pc

  icu.addr := pcu.pc
  icu.data_in := 0
  icu.re := True
  icu.we := False
  icu.be := U"11"
  icu.ex := MU_EX.u

  du.inst := pu.id_du_inst

  ju.op := du.ju_op
  ju.pc_src := du.ju_pc_src
  ju.a := pu.du_rs_v.asSInt
  ju.b := pu.du_rt_v.asSInt
  ju.pc := pu.id_pcu_pc
  ju.offset := du.offset
  ju.index := du.index

  alu.op := pu.ex_alu_op
  alu.a := pu.ex_alu_a
  alu.b := pu.ex_alu_b

  dcu.addr := pu.me_dcu_addr
  dcu.data_in := pu.me_dcu_data
  dcu.re := pu.me_dcu_re
  dcu.we := pu.me_dcu_we
  dcu.be := pu.me_dcu_be
  dcu.ex := pu.me_dcu_ex

  rfu.we := pu.wb_rfu_we
  rfu.rd := pu.wb_rfu_rd
  rfu.rd_v := pu.wb_rfu_rd_v
  rfu.ra := du.rs
  rfu.rb := du.rt

  pu.rfu_ra_v := rfu.ra_v
  pu.rfu_rb_v := rfu.rb_v
  pu.if_pcu_pc := pcu.pc
  pu.if_icu_stall := icu.stall
  pu.if_icu_data := icu.data_out
  pu.id_du_rs := du.rs
  pu.id_du_rt := du.rt
  pu.id_du_sa := du.sa
  pu.id_du_imm := du.imm
  pu.id_du_offset := du.offset
  pu.id_alu_op := du.alu_op
  pu.id_alu_a_src := du.alu_a_src
  pu.id_alu_b_src := du.alu_b_src
  pu.id_dcu_re := du.dcu_re
  pu.id_dcu_we := du.dcu_we
  pu.id_dcu_be := du.dcu_be
  pu.id_mu_ex := du.mu_ex
  pu.id_rfu_we := du.rfu_we
  pu.id_rfu_rd := du.rfu_rd
  pu.id_rfu_rd_src := du.rfu_rd_src
  pu.id_use_rs := du.use_rs
  pu.id_use_rt := du.use_rt
  pu.ex_alu_c := alu.c.asBits
  pu.me_dcu_stall := dcu.stall
  pu.me_mu_data_out := dcu.data_out
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}