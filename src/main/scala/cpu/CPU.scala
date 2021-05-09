package cpu

import cpu.alu._
import cpu.du._
import cpu.ju._
import cpu.mu._
import cpu.pcu._
import cpu.rfu._
import cpu.pu._
import spinal.core._

class CPU extends Component {
  val pcu = new PCU
  val du = new DU
  val ju = new JU
  val alu = new ALU
  val mu = new MU
  val rfu = new RFU
  val pu = new PU

  pcu.stall := pu.if_stall
  pcu.we := ju.jump
  pcu.new_pc := ju.jump_pc

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

  mu.addr := pu.mu_addr
  mu.data_in := pu.mu_data_in
  mu.re := pu.mu_re
  mu.we := pu.mu_we
  mu.be := pu.mu_be
  mu.ex := pu.mu_ex

  rfu.we := pu.wb_rfu_we
  rfu.rd := pu.wb_rfu_rd
  rfu.rd_v := pu.wb_rfu_rd_v
  rfu.ra := du.rs
  rfu.rb := du.rt

  pu.mu_data_out := mu.data_out
  pu.rfu_ra_v := rfu.ra_v
  pu.rfu_rb_v := rfu.rb_v
  pu.if_pcu_pc := pcu.pc
  pu.id_du_rs := du.rs
  pu.id_du_rt := du.rt
  pu.id_du_sa := du.sa
  pu.id_du_imm := du.imm
  pu.id_du_offset := du.offset
  pu.id_alu_op := du.alu_op
  pu.id_alu_a_src := du.alu_a_src
  pu.id_alu_b_src := du.alu_b_src
  pu.id_mu_re := du.mu_re
  pu.id_mu_we := du.mu_we
  pu.id_mu_be := du.mu_be
  pu.id_mu_ex := du.mu_ex
  pu.id_rfu_we := du.rfu_we
  pu.id_rfu_rd := du.rfu_rd
  pu.id_rfu_rd_src := du.rfu_rd_src
  pu.id_use_rs := du.use_rs
  pu.id_use_rt := du.use_rt
  pu.ex_alu_c := alu.c.asBits
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}