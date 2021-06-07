package cpu

import defs.Mips32InstImplicits._
import defs.{SramBus, SramBusConfig}
import spinal.core._
import spinal.lib.master

class CPU extends Component {
  val io = new Bundle {
    val iSramBus = master(SramBus(SramBusConfig(32, 32, 2)))
    val dSramBus = master(SramBus(SramBusConfig(32, 32, 2)))
    val debug    = out(DebugInterface())
  }
  val pcu    = new PCU
  val icu    = new MU
  val du     = new DU
  val ju     = new JU
  val alu    = new ALU
  val dcu    = new MU
  val hlu    = new HLU
  val rfu    = new RFU
  val pu     = new PU
  val cp0    = new CP0
  val cp0exu = new CP0ExecUnit

  io.iSramBus <> icu.io.sramBus
  io.dSramBus <> dcu.io.sramBus
  io.debug.wb.rf.wen := B(4 bits, default -> rfu.io.write.valid)
  io.debug.wb.rf.wdata := rfu.io.write.data
  io.debug.wb.rf.wnum := rfu.io.write.index
  io.debug.wb.pc := B(pu.wb_pcu_pc)

  pcu.stall := pu.if_stall
  pcu.we := ju.jump
  pcu.new_pc := ju.jump_pc

  icu.io.addr := pcu.pc
  icu.io.data_in := 0
  icu.io.re := True
  icu.io.we := False
  icu.io.be := U"11"
  icu.io.ex.assignFromBits(B"0")

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

  cp0exu.io.input := pu.ex_cp0_input

  dcu.io.addr := pu.me_dcu_addr
  dcu.io.data_in := pu.me_dcu_data
  dcu.io.re := pu.me_dcu_re
  dcu.io.we := pu.me_dcu_we
  dcu.io.be := pu.me_dcu_be
  dcu.io.ex := pu.me_dcu_ex

  hlu.hi_we := pu.me_hlu_hi_we
  hlu.new_hi := pu.me_hlu_new_hi
  hlu.lo_we := pu.me_hlu_lo_we
  hlu.new_lo := pu.me_hlu_new_lo

  rfu.io.write := pu.wb_rfu
  rfu.io.ra.index := inst.rs
  rfu.io.rb.index := inst.rt

  cp0.io.softwareWrite := pu.wb_cp0
  cp0.io.read.addr.rd := inst.rd
  cp0.io.read.addr.sel := inst.sel

  pu.hlu_hi_v := hlu.hi_v
  pu.hlu_lo_v := hlu.lo_v
  pu.rfu_ra_v := rfu.io.ra.data
  pu.rfu_rb_v := rfu.io.rb.data
  pu.cp0_read_v := cp0.io.read.data
  pu.if_pcu_pc := pcu.pc
  pu.if_icu_stall := icu.io.stall
  pu.if_icu_data := icu.io.data_out
  pu.id_ju_jump := ju.jump
  pu.id_alu_op := du.io.alu_op
  pu.id_alu_a_src := du.io.alu_a_src
  pu.id_alu_b_src := du.io.alu_b_src
  pu.id_cp0_re := du.io.cp0_re
  pu.id_cp0_we := du.io.cp0_we
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
  pu.id_exception := du.io.exception
  pu.ex_alu_c := B(alu.io.c)
  pu.ex_alu_d := B(alu.io.d)
  pu.ex_alu_exception := alu.io.exception
  pu.ex_cp0_to_write := cp0exu.io.to_write
  pu.me_dcu_stall := dcu.io.stall
  pu.me_mu_data_out := dcu.io.data_out
  pu.me_dcu_exception := dcu.io.exception
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}
