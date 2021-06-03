package cpu

import spinal.core._
import spinal.lib.master

import defs.{SramBus, SramBusConfig}
import defs.Mips32InstImplicits._

class CPU extends Component {
  val io = new Bundle {
    val iSramBus = master(SramBus(SramBusConfig(32, 32, 2)))
    val dSramBus = master(SramBus(SramBusConfig(32, 32, 2)))
    val debug = new DebugInterface
  }
  val pcu = new PCU
  val icu = new MU
  val du = new DU
  val ju = new JU
  val alu = new ALU
  val dcu = new MU
  val hlu = new HLU
  val rfu = new RFU
  val pu = new PU

  io.iSramBus <> icu.sramBus
  io.dSramBus <> dcu.sramBus
  io.debug.wb.rf.wen := B(4 bits, default -> rfu.io.write.valid)
  io.debug.wb.rf.wdata := rfu.io.write.data
  io.debug.wb.rf.wnum := rfu.io.write.index
  io.debug.wb.pc := B(pu.wb_pcu_pc)

  pcu.stall := pu.if_stall
  pcu.we := ju.jump
  pcu.new_pc := ju.jump_pc

  icu.addr := pcu.pc
  icu.data_in := 0
  icu.re := True
  icu.we := False
  icu.be := U"11"
  icu.ex.assignFromBits(B"0")

  val inst = pu.id_du_inst

  du.io.inst := inst

  ju.op := du.io.ju_op
  ju.pc_src := du.io.ju_pc_src
  ju.a := S(pu.id_du_rs_v)
  ju.b := S(pu.id_du_rt_v)
  ju.pc := pcu.pc
  ju.offset := inst.offset
  ju.index := inst.index

  alu.io.input := pu.ex_alu_input

  dcu.addr := pu.me_dcu_addr
  dcu.data_in := pu.me_dcu_data
  dcu.re := pu.me_dcu_re
  dcu.we := pu.me_dcu_we
  dcu.be := pu.me_dcu_be
  dcu.ex := pu.me_dcu_ex

  hlu.hi_we := pu.me_hlu_hi_we
  hlu.new_hi := pu.me_hlu_new_hi
  hlu.lo_we := pu.me_hlu_lo_we
  hlu.new_lo := pu.me_hlu_new_lo

  rfu.io.write := pu.wb_rfu
  rfu.io.ra.index := inst.rs
  rfu.io.rb.index := inst.rt

  pu.hlu_hi_v := hlu.hi_v
  pu.hlu_lo_v := hlu.lo_v
  pu.rfu_ra_v := rfu.io.ra.data
  pu.rfu_rb_v := rfu.io.rb.data
  pu.if_pcu_pc := pcu.pc
  pu.if_icu_stall := icu.stall
  pu.if_icu_data := icu.data_out
  pu.id_ju_jump := ju.jump
  pu.id_alu_op := du.io.alu_op
  pu.id_alu_a_src := du.io.alu_a_src
  pu.id_alu_b_src := du.io.alu_b_src
  pu.id_dcu_re := du.io.dcu_re
  pu.id_dcu_we := du.io.dcu_we
  pu.id_dcu_be := du.io.dcu_be
  pu.id_mu_ex := du.io.mu_ex
  pu.id_hlu_hi_we := du.io.hlu_hi_we
  pu.id_hlu_hi_src := du.io.hlu_hi_src
  pu.id_hlu_lo_we := du.io.hlu_lo_we
  pu.id_hlu_lo_src := du.io.hlu_lo_src
  pu.id_rfu_we := du.io.rfu_we
  pu.id_rfu_rd := du.io.rfu_rd
  pu.id_rfu_rd_src := du.io.rfu_rd_src
  pu.id_use_rs := du.io.use_rs
  pu.id_use_rt := du.io.use_rt
  pu.ex_alu_c := B(alu.c)
  pu.ex_alu_d := B(alu.d)
  pu.me_dcu_stall := dcu.stall
  pu.me_mu_data_out := dcu.data_out
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}