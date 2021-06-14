package cpu

import cpu.defs.ConstantVal._
import cpu.defs.Mips32Inst
import cpu.defs.Mips32InstImplicits._
import lib.{Key, Record}
import spinal.core._
import spinal.lib.{cpu => _, _}

class Stage extends Area {
  @dontName protected implicit val stage = this

  // Input and output. Automatically connected to StageComponent's,
  // but actual connection user configurable.
  // read and output are interfaces of the stage,
  // while input and produced are linked to StageComponent's.
  val read               = Record() // Values read from last stage
  val input              = Record() // Values serving as StageComponent input
  val produced           = Record() // Values produced by current stage
  val output             = Record() // Values output to the next stage
  private val inputReset = Record() // Reset values for input

  // Arbitration. Note that reset comes from constructor.
  val prevProducing = True  // Previous stage is passing in a new instruction
  val stallsBySelf  = False // User settable
  val stallsByLater = False
  val stalls        = stallsBySelf || stallsByLater
  val willReset     = !stalls && !prevProducing
  val reset         = RegNext(willReset) init True
  val firing        = !reset
  val producing     = firing && !stallsBySelf

  def ensureReadKey[T <: Data](key: Key[T]) = {
    if (!(read contains key)) {
      read += key
      read(key).setAsReg()
    }
  }

  def ensureInputKey[T <: Data](key: Key[T]) = {
    if (!(input contains key)) {
      input += key
      ensureReadKey(key)
      input(key) := read(key)
      input(key).allowOverride
      if (!(output contains key)) {
        output += key
        output(key) := input(key)
      }
    }
  }

  def ensureProducedKey[T <: Data](key: Key[T]) = {
    if (!(produced contains key)) {
      produced += key
      ensureOutputKey(key)
      output(key) := produced(key)
      output(key).allowOverride
    }
  }

  def ensureOutputKey[T <: Data](key: Key[T]) = {
    if (!(output contains key)) {
      output += key
    }
  }

  /** Called at initialization of StageComponent.
    */
  def addComponent(component: StageComponent) = {
    component.input.keys foreach { case key =>
      ensureInputKey(key)
      component.input(key) := input(key)
    }

    component.produced.keys foreach { case key =>
      ensureProducedKey(key)
      produced(key) := component.produced(key)
    }

    when(component.stalls) {
      stallsBySelf := True
    }
  }

  def setInputReset[T <: Data](key: Key[T], init: T) = {
    ensureInputKey(key)
    inputReset(key) = init
//    read(key) init init Unnecessary for the assignment below?
    read(key) := init
  }

  def connect(next: Stage) = {
    when(!this.producing) {
      next.prevProducing := False
    }

    next.read.keys foreach { case key =>
      if (!(output contains key)) {
        ensureInputKey(key)
      }
    }
    when(next.stalls) {
      stallsByLater := True
    } elsewhen(producing) {
      next.read.keys foreach { case key =>
        next.read(key) := output(key)
      }
    }
  }
}

class StageComponent(
    inputKeys: Seq[Key[_ <: Data]] = Seq(),
    producedKeys: Seq[Key[_ <: Data]] = Seq(),
    val stalls: Bool = False
)(implicit stage: Stage)
    extends Area {
  val input    = Record(inputKeys)
  val produced = Record(producedKeys)

  stage.addComponent(this)
}

class PU extends Component {
  /// 数据流水线

  // in
  val hlu_hi_v   = in Bits (32 bits) // reads
  val hlu_lo_v   = in Bits (32 bits) // reads
  val rfu_ra_v   = in Bits (32 bits) // reads
  val rfu_rb_v   = in Bits (32 bits) // reads
  val cp0_read_v = in Bits (32 bits) // reads

  val if_pcu_pc    = in UInt (32 bits) // inc
  val if_icu_stall = in Bool           // inc
  val if_icu_inst  = in(Mips32Inst())  // inc

  val id_ju_jump    = in Bool
  val id_alu_op     = in(ALU_OP)       // inc
  val id_alu_a_src  = in(ALU_A_SRC)    // inc
  val id_alu_b_src  = in(ALU_B_SRC)    // inc
  val id_cp0_re     = in Bool          // inc
  val id_cp0_we     = in Bool          // inc
  val id_dcu_re     = in Bool          // inc
  val id_dcu_we     = in Bool          // inc
  val id_dcu_be     = in UInt (2 bits) // inc
  val id_mu_ex      = in(MU_EX)        // inc
  val id_hlu_hi_we  = in Bool          // inc
  val id_hlu_hi_src = in(HLU_SRC)      // inc
  val id_hlu_lo_we  = in Bool          // inc
  val id_hlu_lo_src = in(HLU_SRC)      // inc
  val id_rfu_we     = in Bool          // inc
  val id_rfu_rd     = in UInt (5 bits) // inc
  val id_rfu_rd_src = in(RFU_RD_SRC)   // inc
  val id_use_rs     = in Bool          // inc
  val id_use_rt     = in Bool          // inc
  val id_exception  = in(EXCEPTION())  // inc

  val ex_alu_c         = in Bits (32 bits)
  val ex_alu_d         = in Bits (32 bits)
  val ex_alu_exception = in(EXCEPTION()) // inc

  val ex_cp0_to_write = in Bits (32 bits)

  val me_dcu_stall     = in Bool         // inc
  val me_mu_data_out   = in Bits (32 bits)
  val me_dcu_exception = in(EXCEPTION()) // inc

  // out
  val id_du_inst = out(Mips32Inst())  // inc
  val id_du_rs_v = out Bits (32 bits) // reads
  val id_du_rt_v = out Bits (32 bits) // reads

  val ex_alu_input = out(ALUInput())
  val ex_cp0_input = out(Reg(CP0ExecUnitInput()))

  val me_dcu_addr   = out(UInt(32 bits))
  val me_dcu_data   = out(Bits(32 bits))
  val me_dcu_re     = out(Bool)
  val me_dcu_we     = out(Bool)
  val me_dcu_be     = out(UInt(2 bits))
  val me_dcu_ex     = out(MU_EX)
  val me_hlu_hi_we  = out(Bool)
  val me_hlu_new_hi = out(Bits(32 bits))
  val me_hlu_lo_we  = out(Bool)
  val me_hlu_new_lo = out(Bits(32 bits))

  val wb_pcu_pc = out(UInt(32 bits))
  val wb_rfu    = out(Flow(RegWrite()))
  val wb_cp0    = out(Reg(Flow(CP0Write())))
  wb_cp0.valid init False

  val if_stall = out Bool

  //
  val id_du_cp0_v = Bits(32 bits)
  id_du_cp0_v := 0 // TODO

  val ex_cp0_we   = RegInit(False)
  val ex_cp0_addr = Reg(CP0Addr())

  val me_cp0_we       = RegInit(False)
  val me_cp0_addr     = Reg(CP0Addr())
  val me_cp0_to_write = Reg(Bits(32 bits))

  // PC to provide as input of IF
//  val nextPc = Reg(UInt(32 bits)) init INIT_PC

  val pc        = Key(UInt(32 bits))
  val inst      = Key(Mips32Inst())
  val exception = Key(EXCEPTION())
  val aluOp     = Key(ALU_OP())
  val aluASrc   = Key(ALU_A_SRC())
  val aluBSrc   = Key(ALU_B_SRC())
  val memRe     = Key(Bool)
  val memWe     = Key(Bool)
  val memBe     = Key(UInt(2 bits))
  val memEx     = Key(MU_EX())
  val memAddr   = Key(UInt(32 bits))
  val memData   = Key(Bits(32 bits))
  val hluHiWe   = Key(Bool)
  val hluHiSrc  = Key(HLU_SRC())
  val hluLoWe   = Key(Bool)
  val hluLoSrc  = Key(HLU_SRC())
  val hluHiData = Key(Bits(32 bits))
  val hluLoData = Key(Bits(32 bits))
  val cp0Re     = Key(Bool)
  val cp0We     = Key(Bool)
  val rfuWe     = Key(Bool)
  val rfuAddr   = Key(UInt(5 bits))
  val rfuRdSrc  = Key(RFU_RD_SRC())
  val rfuData   = Key(Bits(32 bits))
  val useRs     = Key(Bool)
  val useRt     = Key(Bool)
  val rsValue   = Key(Bits(32 bits))
  val rtValue   = Key(Bits(32 bits))

  // Later stages may depend on earlier stages, so stages are list in reversed order.

  val WB = new Stage {
    val rfu = new StageComponent(inputKeys = Seq(rfuWe, rfuAddr, rfuData)) {
      wb_rfu.valid := input(rfuWe)
      wb_rfu.index := input(rfuAddr)
      wb_rfu.data := input(rfuData)
      setInputReset(rfuWe, False)
    }

    val pc_debug = new StageComponent(inputKeys = Seq(pc)) {
      wb_pcu_pc := input(pc)
    }
  }

  val ME = new Stage {
    val dcu = new StageComponent(
      inputKeys = Seq(memRe, memWe, memBe, memEx, memAddr, memData),
      producedKeys = Seq(rfuData, exception),
      stalls = me_dcu_stall
    ) {
      me_dcu_re := input(memRe)
      me_dcu_we := input(memWe)
      me_dcu_be := input(memBe)
      me_dcu_ex := input(memEx)
      me_dcu_addr := input(memAddr)
      me_dcu_data := input(memData)
      setInputReset(memRe, False)
      setInputReset(memWe, False)

      produced(exception) := me_dcu_exception
      produced(rfuData) := me_mu_data_out
    }

    val hlu = new StageComponent(
      inputKeys = Seq(hluHiWe, hluHiSrc, hluLoWe, hluLoSrc, rsValue),
      producedKeys = Seq(hluHiData, hluLoData)
    ) {
      me_hlu_hi_we := input(hluHiWe)
      produced(hluHiData) := (input(hluHiSrc) === HLU_SRC.rs) ? input(
        rsValue
      ) | ex_alu_c
      me_hlu_new_hi := produced(hluHiData)
      me_hlu_lo_we := input(hluLoWe)
      produced(hluLoData) := (input(hluLoSrc) === HLU_SRC.rs) ? input(
        rsValue
      ) | ex_alu_d
      me_hlu_new_lo := produced(hluLoData)

      setInputReset(hluHiWe, False)
      setInputReset(hluLoWe, False)
    }

    val cp0 = new StageComponent(inputKeys = Seq(cp0Re, cp0We)) {
      wb_cp0.valid := input(cp0We)
      setInputReset(cp0We, False)
    }

    ensureInputKey(rfuRdSrc)
    ensureInputKey(rfuData)
    output(rfuData) := ((input(rfuRdSrc) === RFU_RD_SRC.mu)
      ? produced(rfuData)
      | input(rfuData))
  }

  val EX = new Stage {
    ensureInputKey(rsValue)
    ensureInputKey(rtValue)
    ensureInputKey(inst)

    ME.ensureInputKey(rfuWe)
    ME.ensureInputKey(rfuAddr)
    when(
      ME.input(rfuWe) && ME.input(rfuAddr) === input(
        inst
      ).rs
    ) {
      input(rsValue) := ME.produced(rfuData)
    }
    when(
      ME.input(rfuWe) && ME.input(rfuAddr) === input(
        inst
      ).rt
    ) {
      input(rtValue) := ME.produced(rfuData)
    }

    val alu = new StageComponent(
      inputKeys = Seq(aluOp, aluASrc, aluBSrc, rsValue, rtValue, inst),
      producedKeys = Seq(exception)
    ) {
      ex_alu_input.op := input(aluOp)
      ex_alu_input.a := input(aluASrc).mux(
        ALU_A_SRC.rs -> U(input(rsValue)),
        ALU_A_SRC.sa -> input(inst).sa.resize(32)
      )
      ex_alu_input.b := input(aluBSrc).mux(
        ALU_B_SRC.rt  -> U(input(rtValue)),
        ALU_B_SRC.imm -> input(inst).immExtended.asUInt
      )

      produced(exception) := ex_alu_exception
    }

    val agu = new StageComponent(
      inputKeys = Seq(inst, rsValue),
      producedKeys = Seq(memAddr)
    ) {
      produced(memAddr) := U(S(input(rsValue)) + input(inst).offset)
    }

    ensureInputKey(rfuRdSrc)
    ensureInputKey(rfuData)
    ensureProducedKey(rfuData)
    produced(rfuData) := input(rfuRdSrc) mux (
      RFU_RD_SRC.alu -> ex_alu_c,
      RFU_RD_SRC.hi -> (ME.input(hluHiWe) ? ME.produced(
        hluHiData
      ) | hlu_hi_v),
      RFU_RD_SRC.lo -> (ME.input(hluLoWe) ? ME.produced(
        hluLoData
      ) | hlu_lo_v),
      default -> input(rfuData)
    )
  }

  val ID = new Stage {
    val du = new StageComponent(
      inputKeys = Seq(inst),
      producedKeys = Seq(
        aluOp,
        aluASrc,
        aluBSrc,
        memRe,
        memWe,
        memBe,
        memEx,
        hluHiWe,
        hluHiSrc,
        hluLoWe,
        hluLoSrc,
        cp0Re,
        cp0We,
        rfuWe,
        rfuAddr,
        rfuRdSrc,
        useRs,
        useRt,
        exception
      )
    ) {
      id_du_inst := input(inst)
      setInputReset(inst, INST_NOP)

      produced(aluOp) := id_alu_op
      produced(aluASrc) := id_alu_a_src
      produced(aluBSrc) := id_alu_b_src
      produced(cp0Re) := id_cp0_re
      produced(cp0We) := id_cp0_we
      produced(memRe) := id_dcu_re
      produced(memWe) := id_dcu_we
      produced(memBe) := id_dcu_be
      produced(memEx) := id_mu_ex
      produced(hluHiWe) := id_hlu_hi_we
      produced(hluHiSrc) := id_hlu_hi_src
      produced(hluLoWe) := id_hlu_lo_we
      produced(hluLoSrc) := id_hlu_lo_src
      produced(rfuWe) := id_rfu_we
      produced(rfuAddr) := id_rfu_rd
      produced(rfuRdSrc) := id_rfu_rd_src
      produced(useRs) := id_use_rs
      produced(useRt) := id_use_rt
      produced(exception) := id_exception
    }

    val rfuRead = new StageComponent(
      inputKeys = Seq(inst),
      producedKeys = Seq(rsValue, rtValue)
    ) {
      EX.ensureInputKey(rfuWe)
      EX.ensureInputKey(rfuAddr)
      when(EX.input(rfuWe) && EX.input(rfuAddr) === input(inst).rs) {
        produced(rsValue) := EX.produced(rfuData)
      } elsewhen (ME.input(rfuWe) &&
        ME.input(rfuAddr) === input(inst).rs) {
        produced(rsValue) := ME.produced(rfuData)
      } elsewhen (WB.input(rfuWe) && WB.input(rfuAddr) === input(
        inst
      ).rs) {
        produced(rsValue) := wb_rfu.data
      } otherwise {
        produced(rsValue) := rfu_ra_v
      }

      when(EX.input(rfuWe) && EX.input(rfuAddr) === input(inst).rt) {
        produced(rtValue) := EX.produced(rfuData)
      } elsewhen (ME
        .input(rfuWe) && ME.input(rfuAddr) === input(inst).rt) {
        produced(rtValue) := ME.produced(rfuData)
      } elsewhen (WB.input(rfuWe) && WB.input(rfuAddr) === input(
        inst
      ).rt) {
        produced(rtValue) := wb_rfu.data
      } otherwise {
        produced(rtValue) := rfu_rb_v
      }
    }

    id_du_rs_v := output(rsValue)
    id_du_rt_v := output(rtValue)

    ensureProducedKey(rfuData)
    ensureInputKey(pc)
    produced(rfuData) := produced(rfuRdSrc) mux (
      RFU_RD_SRC.cp0 -> id_du_cp0_v,
      default        -> B(input(pc) + 8)
    )
    ensureProducedKey(memData)
    produced(memData) := produced(rtValue)

    EX.ensureInputKey(rfuWe)
    EX.ensureInputKey(rfuRdSrc)
    EX.ensureInputKey(rfuAddr)
    when(
      (EX.input(rfuWe) &&
        EX.input(rfuRdSrc) === RFU_RD_SRC.mu &&
        (EX.input(rfuAddr) === input(inst).rs && produced(useRs) ||
          EX.input(rfuAddr) === input(inst).rt && produced(useRt)))
    ) {
      stallsBySelf := True
    }
  }

  val IF = new Stage {
    val icu = new StageComponent(
      producedKeys = Seq(pc, inst, exception),
      stalls = if_icu_stall
    ) {
      produced(pc) := if_pcu_pc
      produced(inst) := if_icu_inst
    }

    if_stall := stalls
  }

  // Pseudo stage that provides pc to IF.
//  val PC = new Stage {
//    val pcu = new StageComponent(
//      producedKeys = Seq(pc)
//    ) {
//      produced(pc) := nextPc
//    }
//  }

  val stages = Seq(IF, ID, EX, ME, WB)
  for ((prev, next) <- (stages zip stages.tail).reverse) {
    prev connect next
  }

//  when(!ex_stall) {
//    when(!id_stall) {
//      ex_cp0_input.addr.rd := id_du_inst.rd
//      ex_cp0_input.addr.sel := id_du_inst.sel
//      ex_cp0_input.new_value := id_du_rt_v
//      ex_cp0_input.old_value := id_du_cp0_v
//      ex_cp0_we := id_cp0_we
//      ex_cp0_addr.rd := id_du_inst.rd
//      ex_cp0_addr.sel := id_du_inst.sel
//    }
//  }

//  when(!me_stall) {
//    me_cp0_we := ex_cp0_we
//    me_cp0_addr := ex_cp0_addr
//    me_cp0_to_write := ex_cp0_to_write
//  }
//
//  when(!me_stall) {
//    wb_cp0.valid := me_cp0_we
//    wb_cp0.addr := me_cp0_addr
//    wb_cp0.data := me_cp0_to_write
//  }

//  when(ex_cp0_we && ex_cp0_addr === id_du_inst.addr) {
//    id_du_cp0_v := ex_cp0_to_write
//  } elsewhen (me_cp0_we && me_cp0_addr === id_du_inst.addr) {
//    id_du_cp0_v := me_cp0_to_write
//  } elsewhen (wb_cp0.valid && wb_cp0.addr === id_du_inst.addr) {
//    id_du_cp0_v := wb_cp0.data
//  } otherwise {
//    id_du_cp0_v := cp0_read_v
//  }
}

object PU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new PU)
  }
}
