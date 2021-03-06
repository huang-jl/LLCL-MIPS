package cpu

import cache._
import lib._
import spinal.core._
import spinal.lib._
import defs._
import Config._
import bus.amba4.axi._
import Mips32InstImplicits._
import tlb._

import scala.collection.immutable.ListMap
import scala.language.postfixOps

object STAGES extends Enumeration {
  val IF1, IF2, ID, EXE, MEM1, MEM2, WB = Value
}
import STAGES._

object FwdFrom extends SpinalEnum {
  val p = Seq(
    ListMap(EXE  -> newElement(), MEM1 -> newElement(), MEM2 -> newElement(), WB -> newElement()),
    ListMap(MEM1 -> newElement(), MEM2 -> newElement(), WB   -> newElement())
  )
  val p0 = p(0)
  val p1 = p(1)
  val ns = Map(EXE -> MEM1, MEM1 -> MEM2, MEM2 -> WB)

  val none = newElement()
}

class MultiIssueCPU extends Component {
  def condWhen(cond: Boolean, whenCond: Bool)(block: => Unit): Unit = {
    if (cond) { when(whenCond) { block } }
    else { block }
  }

  val io = new Bundle {
    val externalInterrupt = in Bits (6 bits)
    val icacheAXI         = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
    val dcacheAXI         = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
    val uncacheAXI        = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
  }
  val icacheConfig =
    CacheRamConfig(
      blockSize = ConstantVal.IcacheLineSize,
      wayNum = ConstantVal.IcacheWayNum,
      bankSize = 8
    )
  val dcacheConfig =
    CacheRamConfig(blockSize = ConstantVal.DcacheLineSize, wayNum = ConstantVal.DcacheWayNum)
  val icache = new ICache(icacheConfig)
  val dcache = new DCache(dcacheConfig, ConstantVal.DcacheFifoDepth)
  val ibus   = new CPUICacheInterface(icacheConfig)
  val dbus   = new CPUDCacheInterface(dcacheConfig)
  icache.io.axi <> io.icacheAXI
  icache.io.cpu <> ibus
  dcache.io.axi <> io.dcacheAXI
  dcache.io.uncacheAXI <> io.uncacheAXI
  dcache.io.cpu <> dbus

  val pc           = Key(UInt(32 bits))
  val paddr        = Key(UInt(32 bits))
  val excOnFetch   = Key(Bool())
  val ibusStage2En = Key(Bool()).setEmptyValue(False)
  val pc1          = Key(UInt(32 bits))
  val pc2          = Key(UInt(32 bits))
  val entry1       = Key(IssueEntry()).setEmptyValue(IssueEntry().getZero)
  val entry2       = Key(IssueEntry()).setEmptyValue(IssueEntry().getZero)
  val inst         = Key(Mips32Inst())
  val eret         = Key(Bool()).setEmptyValue(False)
  val isJump       = Key(Bool()).setEmptyValue(False)
  val isDJump      = Key(Bool())
  val jumpCond     = Key(JU_OP()).setEmptyValue(JU_OP.f)
  val jumpPC       = Key(UInt(32 bits))
  val inSlot       = Key(Bool())
  val useMem       = Key(Bool()).setEmptyValue(False)
  val modifyCP0    = Key(Bool()).setEmptyValue(False)
  val useRs        = Key(Bool())
  val useRt        = Key(Bool())
  val rsValue      = Key(Bits(32 bits))
  val rtValue      = Key(Bits(32 bits))
  val aluOp        = Key(ALU_OP()).setEmptyValue(ALU_OP.sll())
  val aluASrc      = Key(ALU_A_SRC())
  val aluBSrc      = Key(ALU_B_SRC())
  val complexOp    = Key(Bool()) //?????????????????????
  val aluC         = Key(UInt(32 bits))
  val cp0RE        = Key(Bool()).setEmptyValue(False)
  val cp0WE        = Key(Bool()).setEmptyValue(False)
  val memAddr      = Key(UInt(32 bits))
  val memBEType    = Key(UInt(2 bits))
  val memBE        = Key(Bits(4 bits))
  val memEx        = Key(MU_EX())
  val memRE        = Key(Bool()).setEmptyValue(False)
  val memWE        = Key(Bool()).setEmptyValue(False)
  val memWData     = Key(Bits(32 bits))
  val hiWE         = Key(Bool()).setEmptyValue(False)
  val newHiSrc     = Key(HLU_SRC())
  val newHi        = Key(Bits(32 bits))
  val loWE         = Key(Bool()).setEmptyValue(False)
  val newLoSrc     = Key(HLU_SRC())
  val newLo        = Key(Bits(32 bits))
  val rfuWE        = Key(Bool()).setEmptyValue(False)
  val rfuIndex     = Key(UInt(5 bits))
  val rfuSrc       = Key(RFU_RD_SRC())
  val rfuData      = Key(Bits(32 bits))

  val mmuRes = Key(MMUTranslationRes())

  val predJump   = Key(Bool()) setEmptyValue False
  val predJumpPC = Key(UInt(32 bits))

  val rsFromMem = Key(Bool())
  val rtFromMem = Key(Bool())

  val dataNotReady  = Key(Bool())
  val canUseMem1ALU = Key(Bool())
  val useMem1ALU    = Key(Bool())

  val rsFwdFrom = Key(FwdFrom())
  val rtFwdFrom = Key(FwdFrom())

  val mmu        = new MMU
  val icu        = new ICU
  val ju         = new JU
  val dcu1       = new DCU1
  val dcu2       = new DCU2
  val hlu        = new HLU
  val cp0        = new CP0
  val rfu        = new RFU(2, 4)
  val complexALU = new ComplexALU

  val bps = Seq(new BranchPredictor(0), new BranchPredictor(1))

  /**/

  var p: Seq[Map[Value, Stage]] = Seq()

  for (i <- 0 to 1) {
    val du  = new DU
    val alu = new ALU

    val mem1ALU = new SimpleALU

    val wb = new Stage {
      setName(s"wb_${i}")

      produced(pc)

      produced(dataNotReady) := False

      val writeReg = new StageComponent {
        rfu.io.w(i).valid := !!!(rfuWE)
        rfu.io.w(i).index := input(rfuIndex)
        rfu.io.w(i).data := input(rfuData)
      }
    }
    val mem2 = new Stage {
      setName(s"mem2_${i}")

      produced(dataNotReady) := stored(memRE) & dbus.stage2.stall

      val accessMem = new StageComponent {
        output(rfuData) := input(memRE) ? dcu2.io.output.rdata | input(rfuData)
      }

      is.done := !dbus.stage2.stall
      can.flush := !dbus.stage2.stall
    }
    val mem1 = new Stage {
      setName(s"mem1_${i}")

      produced(dataNotReady) := stored(memRE)

      val memData = RegNextWhen(dcu2.io.output.rdata, can.input)
      input(rsValue) := stored(rsFromMem) ? memData | stored(rsValue)
      input(rtValue) := stored(rtFromMem) ? memData | stored(rtValue)

      val calc = new StageComponent {
        mem1ALU.op := input(aluOp)
        mem1ALU.a := input(aluASrc).mux(
          ALU_A_SRC.rs -> U(input(rsValue)),
          ALU_A_SRC.sa -> input(inst).sa.resize(32)
        )
        mem1ALU.b := input(aluBSrc).mux(
          ALU_B_SRC.rt  -> U(input(rtValue)),
          ALU_B_SRC.imm -> U(input(inst).immExtended)
        )
      }

      produced(rfuData) := stored(useMem1ALU) ? B(mem1ALU.c) | stored(rfuData)

      !!!(memRE)
      !!!(memWE)
      exceptionToRaise := None
      when(!dcu1.io.output.addrValid) {
        when(stored(memRE)) { exceptionToRaise := ExcCode.loadAddrError }
        when(stored(memWE)) { exceptionToRaise := ExcCode.storeAddrError }
      }
    }
    val exe = new Stage {
      setName(s"exe_${i}")

      produced(dataNotReady) := stored(memRE) | stored(useMem1ALU)

      val memData = RegNextWhen(dcu2.io.output.rdata, can.input)
      input(rsValue) := stored(rsFromMem) ? memData | stored(rsValue)
      input(rtValue) := stored(rtFromMem) ? memData | stored(rtValue)

      val calc = new StageComponent {
        alu.io.input.op := !!!(aluOp)
        alu.io.input.a := input(aluASrc).mux(
          ALU_A_SRC.rs -> U(input(rsValue)),
          ALU_A_SRC.sa -> input(inst).sa.resize(32)
        )
        alu.io.input.b := input(aluBSrc).mux(
          ALU_B_SRC.rt  -> U(input(rtValue)),
          ALU_B_SRC.imm -> U(input(inst).immExtended)
        )

        output(aluC) := alu.io.c

        exceptionToRaise := alu.io.exception
      }

      val calcAddr = new StageComponent {
        output(memAddr) := U(S(input(rsValue)) + input(inst).offset)
      }

      val readCP0 = new StageComponent {
        cp0.io.read(i).addr.rd := input(inst).rd
        cp0.io.read(i).addr.sel := input(inst).sel
      }

      val calcHiLo = new StageComponent {
        output(newHi) := input(newHiSrc).mux(
          HLU_SRC.alu -> B(complexALU.io.c),
          HLU_SRC.rs  -> input(rsValue)
        )
        output(newLo) := input(newLoSrc).mux(
          HLU_SRC.alu -> B(complexALU.io.d),
          HLU_SRC.rs  -> input(rsValue)
        )
      }

      produced(aluC) := input(complexOp) ? complexALU.io.c | calc.output(aluC)
      is.done := input(complexOp) ? !complexALU.io.stall | True
    }
    val id = new Stage {
      setName(s"id_${i}")

      val entry = if (i == 0) !!!(entry1) else !!!(entry2)
      input(pc) := entry.pc
      input(inst) := entry.inst
      input(isJump) := entry.branch.is
      input(isDJump) := entry.branch.isDjump
      input(predJump) := entry.predict.taken
      input(predJumpPC) := entry.predict.target
      input(jumpPC) := entry.target
      when(!entry.valid) {
        assign.flush := True
        if (i == 0) { input(inst) := NOP }
      }.elsewhen(entry.exception.code.isDefined) (input(inst) := NOP)
      input(excOnFetch) := entry.exception.code.isDefined & entry.valid

      val decode = new StageComponent {
        du.io.inst := input(inst)

        output(eret) := du.io.eret
        output(jumpCond) := du.io.ju_op
        output(useMem) := du.io.dcu_re | du.io.dcu_we
        output(useRs) := du.io.use_rs
        output(useRt) := du.io.use_rt
        output(aluOp) := du.io.alu_op
        output(aluASrc) := du.io.alu_a_src
        output(aluBSrc) := du.io.alu_b_src
        output(complexOp) := du.io.complex_op
        output(memBEType) := du.io.dcu_be
        output(memEx) := du.io.mu_ex
        output(memRE) := du.io.dcu_re
        output(memWE) := du.io.dcu_we
        output(hiWE) := du.io.hlu_hi_we
        output(newHiSrc) := du.io.hlu_hi_src
        output(loWE) := du.io.hlu_lo_we
        output(newLoSrc) := du.io.hlu_lo_src
        output(cp0RE) := du.io.cp0_re
        output(cp0WE) := du.io.cp0_we
        output(modifyCP0) := du.io.cp0_we | du.io.eret
        output(rfuWE) := du.io.rfu_we
        output(rfuIndex) := du.io.rfu_rd
        output(rfuSrc) := du.io.rfu_rd_src

        output(canUseMem1ALU) := du.io.canUseMem1ALU

        exceptionToRaise := du.io.exception
        when(entry.valid & entry.exception.code.isDefined) {
          exceptionToRaise := entry.exception.code
        }
      }

      val calcRFUData = new StageComponent {
        output(rfuData) := B(input(pc) + 8)
      }
    }

    p = p :+ ListMap(
      ID   -> id,
      EXE  -> exe,
      MEM1 -> mem1,
      MEM2 -> mem2,
      WB   -> wb
    )
  }

  val p0 = p(0)
  val p1 = p(1)

  val multiCycleCompute = new ComponentStage {
    val memData = RegNextWhen(dcu2.io.output.rdata, can.input)
    complexALU.io.input.op := !!!(aluOp)
    // ???????????????????????????????????????GPR
    complexALU.io.input.a := U(stored(rsFromMem) ? memData | stored(rsValue))
    complexALU.io.input.b := U(stored(rtFromMem) ? memData | stored(rtValue))
  }

  val correct = Flow(UInt(32 bits)).setIdle()
  val IF = new Stage {
    val fetchInst = new FetchInst(icacheConfig)
    // input
    fetchInst.io.jump := correct
    fetchInst.io.except.valid := cp0.io.exceptionBus.jumpPc.isDefined
    fetchInst.io.except.payload := cp0.io.exceptionBus.jumpPc.value
    fetchInst.io.mmuRes := mmu.io.instRes
    fetchInst.io.issue.ready := p(0)(ID).can.input & p(1)(ID).can.input
    for (i <- 0 until 2) fetchInst.io.bp.predict(i).assignAllByName(bps(i).read2.io)
    //output
    for (i <- 0 until 2) bps(i).read1.io.assignAllByName(fetchInst.io.bp.query)
    mmu.io.instVaddr := fetchInst.io.vaddr
    produced(entry1) := fetchInst.io.issue.payload(0)
    produced(entry2) := fetchInst.io.issue.payload(1)
    when(!fetchInst.io.issue.valid) {
      produced(entry1).valid := False
      produced(entry2).valid := False
    }
//    produced(tlbRefillException) := fetchInst.io.exception.refillException
//    produced(excOnFetch) := fetchInst.io.exception.code.isDefined
//
//    exceptionToRaise := fetchInst.io.exception.code

    ibus <> fetchInst.io.ibus

    /**/
    bps(0).read2.will.input := fetchInst.io.bp.will.input.S2
    bps(1).read2.will.input := fetchInst.io.bp.will.input.S2
    /* ?????? write1 ?????????????????????????????? */
    bps(0).write1.io.issue := p0(ID).stored(entry1)
    bps(1).write1.io.issue := p1(ID).stored(entry2)
    /**/
    bps(0).write2.will.input := p0(EXE).will.input
    bps(1).write2.will.input := p1(EXE).will.input
    when(p1(ID).stored(entry2).valid & !p1(ID).input(pc)(2) | p0(ID).stored(entry1).valid & p0(ID).input(pc)(2)) {
      bps(0).write2.will.input := p1(EXE).will.input
      bps(1).write2.will.input := p0(EXE).will.input
      bps(0).write1.send(bps(1).write2)
      bps(1).write1.send(bps(0).write2)
    }
    /**/
    bps(0).write2.io.assignJump := ju.jump
    bps(1).write2.io.assignJump := False
    bps(0).write2.bind(p0(EXE))
    bps(1).write2.bind(p1(EXE))
    when(!p1(EXE).is.empty & !p1(EXE).stored(pc)(2) | !p0(EXE).is.empty & p0(EXE).stored(pc)(2)) {
      bps(0).write2.io.assignJump := False
      bps(1).write2.io.assignJump := ju.jump
      bps(0).write2.bind(p1(EXE))
      bps(1).write2.bind(p0(EXE))
    }
    /**/
  }

  val accessMem2 = new ComponentStage {
    dcu2.io.input.byteEnable := stored(memBE)
    dcu2.io.input.extend := stored(memEx)
    dcu2.io.input.rdata := dbus.stage2.rdata

    dbus.stage2.read := !!!(memRE)
    dbus.stage2.write := !!!(memWE)
    dbus.stage2.paddr := stored(mmuRes).paddr
    dbus.stage2.byteEnable := stored(memBE)
    dbus.stage2.wdata := stored(memWData)
    dbus.stage2.uncache := !stored(mmuRes).cached
  }
  val accessMem1 = new ComponentStage {
    output(mmuRes) := RegNextWhen(mmu.io.dataRes, can.input)

    dcu1.io.input.byteOffset := output(mmuRes).paddr(0, 2 bits)
    dcu1.io.input.byteEnable := stored(memBEType)
    dcu1.io.input.wdata := stored(rtValue)

    output(memBE) := dcu1.io.output.byteEnable
    output(memWData) := dcu1.io.output.wdata

    if (ConstantVal.DISABLE_VIRT_UART) {
      output(memRE) := output(mmuRes).paddr =/= U"32'h1faf_fff0" & stored(memRE)
      output(memWE) := output(mmuRes).paddr =/= U"32'h1faf_fff0" & stored(memWE)
    }

    dbus.stage1.paddr := output(mmuRes).paddr
    dbus.stage1.wdata := dcu1.io.output.wdata
    dbus.stage1.byteEnable := dcu1.io.output.byteEnable
  }

  multiCycleCompute.interConnect()

  accessMem2.interConnect()
  accessMem1.send(accessMem2)
  accessMem1.interConnect()
  /**/

  /* CP0 ???????????? */
  when(cp0.io.exceptionBus.jumpPc.isDefined) {
//    if1.assign.flush := True
//    if2.assign.flush := True
    p0(ID).assign.flush := True
    p1(ID).assign.flush := True
    p0(EXE).assign.flush := True
    p1(EXE).assign.flush := True
    p1(MEM1).assign.flush := True
    when(p0(MEM1).produced(modifyCP0)) {
      p0(MEM1).assign.flush := True
    }
  }

//  cp0.io.instOnInt.valid := !p0(EXE).is.empty
//  cp0.io.instOnInt.bd := p0(EXE).produced(inSlot)
//  cp0.io.instOnInt.pc := p0(EXE).stored(pc)
  /**/

  /* ????????? rfu ???????????? */
  for (i <- 0 to 1) {
    val self = p(i)(ID)
    self.addComponent(new StageComponent {
      rfu.io.r(i).index := input(inst).rs
      rfu.io.r(2 + i).index := input(inst).rt
      output(rsValue) := rfu.io.r(i).data
      output(rtValue) := rfu.io.r(2 + i).data

      val rsNotReady = False
      val rtNotReady = False

      output(rsFromMem) := False
      output(rtFromMem) := False

      output(rsFwdFrom).assignDontCare()
      output(rtFwdFrom).assignDontCare()

      for (j <- Seq(MEM2, MEM1, EXE)) {
        for (k <- 0 to 1) {
          val stage = p(k)(j)
          when(stage.!!!(rfuWE)) {
            when(stage.stored(rfuIndex) === input(inst).rs) {
              if (j == MEM2) {
                output(rsValue) := stage.stored(rfuData)
                output(rsFromMem) := stage.stored(memRE)
              } else {
                output(rsValue) := stage.produced(rfuData)
                output(rsFromMem) := False
              }
              rsNotReady := stage.produced(dataNotReady)

              j match {
                case MEM2 => output(rsFwdFrom) := FwdFrom.p(k)(MEM2)
                case MEM1 => output(rsFwdFrom) := stage.will.output ? FwdFrom.p(k)(MEM2) | FwdFrom.p(k)(MEM1)
                case EXE  => output(rsFwdFrom) := FwdFrom.p(k)(MEM1)
              }
            }
            when(stage.stored(rfuIndex) === input(inst).rt) {
              if (j == MEM2) {
                output(rtValue) := stage.stored(rfuData)
                output(rtFromMem) := stage.stored(memRE)
              } else {
                output(rtValue) := stage.produced(rfuData)
                output(rtFromMem) := False
              }
              rtNotReady := stage.produced(dataNotReady)

              j match {
                case MEM2 => output(rtFwdFrom) := FwdFrom.p(k)(MEM2)
                case MEM1 => output(rtFwdFrom) := stage.will.output ? FwdFrom.p(k)(MEM2) | FwdFrom.p(k)(MEM1)
                case EXE  => output(rtFwdFrom) := FwdFrom.p(k)(MEM1)
              }
            }
          }
        }
      }

      if (i == 1) {
        val stage = p0(ID)
        when(stage.produced(rfuWE)) {
          when(stage.produced(rfuIndex) === input(inst).rs) {
            rsNotReady := True

            output(rsFwdFrom) := FwdFrom.p0(EXE)
          }
          when(stage.produced(rfuIndex) === input(inst).rt) {
            rtNotReady := True

            output(rtFwdFrom) := FwdFrom.p0(EXE)
          }
        }
        // ????????????????????????????????????
        when(self.produced(complexOp) & stage.produced(complexOp)) {
          self.is.done := False
        }
      }

      output(useMem1ALU) := False
      self.produced(useRs) := self.output(useRs) & rsNotReady
      self.produced(useRt) := self.output(useRt) & rtNotReady

      when(self.produced(useRs) | self.produced(useRt)) {
        when(self.produced(canUseMem1ALU)) {
          output(useMem1ALU) := True
        } otherwise {
          self.is.done := False
        }
      }
    })
  }
  /**/

  /**/
  for (k <- 0 to 1) {
    for (l <- 0 to 1) {
      val rt = l.toBoolean

      val rFwdFrom = if (rt) rtFwdFrom else rsFwdFrom
      val rValue   = if (rt) rtValue else rsValue
      val useR     = if (rt) useRt else useRs

      val rFromMem = if (rt) rtFromMem else rsFromMem

      val self        = p(k)(EXE)
      val prev        = p(k)(ID)
      val willOutput  = Bool()
      val rNotReady   = Bool()
      val data        = Bits(32 bits)
      val nextFwdFrom = FwdFrom()

      val task = RegNextWhen(data, self.stored(rFwdFrom) =/= FwdFrom.none)

      switch(self.stored(rFwdFrom)) {
        is(FwdFrom.none) {
          willOutput := True
          rNotReady := False
          nextFwdFrom := FwdFrom.none

          data := task
          self.produced(rFromMem) := False
        }
        for (i <- 0 to 1) {
          for (j <- Seq(MEM1, MEM2, WB)) {
            val stage = p(i)(j)
            is(FwdFrom.p(i)(j)) {
              if (j == WB) {
                willOutput := True
                rNotReady := False
                nextFwdFrom := FwdFrom.none
              } else {
                willOutput := stage.will.output
                rNotReady := stage.produced(dataNotReady)
                nextFwdFrom := FwdFrom.p(i)(FwdFrom.ns(j))
              }
              if (j == MEM2) {
                data := stage.stored(rfuData)
                self.produced(rFromMem) := self.stored(useR) & stage.stored(memRE)
              } else {
                data := stage.produced(rfuData)
                self.produced(rFromMem) := False
              }
            }
          }
        }
        default {
          willOutput := p0(EXE).will.output
          rNotReady := p0(EXE).produced(dataNotReady)
          data := p0(EXE).produced(rfuData)
          nextFwdFrom := FwdFrom.p0(MEM1)
          self.produced(rFromMem) := False
        }
      }

      when(self.will.input) {
        self.stored(rFwdFrom) := prev.produced(rFwdFrom)
      } elsewhen willOutput {
        self.stored(rFwdFrom) := nextFwdFrom
      }

      self.produced(rValue) := self.stored(useR) ? data | self.input(rValue)
      when(self.stored(useR) & rNotReady) { self.is.done := False }
    }
  }
  /**/

  /* ????????? hlu ???????????? */
  val hi_v = B(hlu.hi_v)
  val lo_v = B(hlu.lo_v)
  for (i <- 0 to 1) {
    val self = p(i)(MEM1)
    when(self.!!!(hiWE)) { hi_v := self.stored(newHi) }
    when(self.!!!(loWE)) { lo_v := self.stored(newLo) }

    condWhen(i == 1, self.produced(hiWE)) {
      hlu.hi_we := self.stored(hiWE)
      hlu.new_hi := self.stored(newHi)
    }
    condWhen(i == 1, self.produced(loWE)) {
      hlu.lo_we := self.stored(loWE)
      hlu.new_lo := self.stored(newLo)
    }
  }

  for (i <- 0 to 1) {
    val self = p(i)(EXE)
    self.addComponent(new StageComponent {
      val hi = B(hi_v)
      val lo = B(lo_v)
      if (i == 1) {
        val stage = p0(EXE)
        when(stage.!!!(hiWE)) { hi := stage.produced(newHi) }
        when(stage.!!!(loWE)) { lo := stage.produced(newLo) }
      }

      output(rfuData) := input(rfuSrc).mux(
        RFU_RD_SRC.alu -> B(self.produced(aluC)),
        RFU_RD_SRC.hi  -> hi,
        RFU_RD_SRC.lo  -> lo,
        RFU_RD_SRC.cp0 -> cp0.io.read(i).data,
        default        -> input(rfuData)
      )
    })
  }
  /**/

  /* ????????? cp0 ???????????? */
  when(p0(EXE).!!!(cp0RE) & (p0(MEM1).!!!(modifyCP0) | p1(MEM1).!!!(modifyCP0))) {
    p0(EXE).is.done := False
  }
  when(
    p1(EXE).!!!(cp0RE) &
      (p0(MEM1).!!!(modifyCP0) | p1(MEM1).!!!(modifyCP0) | p0(EXE).!!!(modifyCP0))
  ) {
    p1(EXE).is.done := False
  }
  /**/

  /* ????????? mem ?????? */
  for (i <- 1 downto 0) {
    condWhen(i == 0, p(i)(EXE).!!!(useMem)) {
      mmu.io.dataVaddr := p(i)(EXE).produced(memAddr)

      accessMem1.will.input := p(i)(MEM1).will.input & p(i)(EXE).exception.isEmpty
      accessMem1.receive(p(i)(EXE))
    }
    condWhen(i == 0, p(i)(MEM1).!!!(useMem)) {
      accessMem1.bind(p(i)(MEM1))
      accessMem2.will.input := p(i)(MEM2).will.input
      // ???????????????????????????MEM1????????????
      dbus.stage1.write := p(i)(MEM1).stored(memWE) & p(i)(MEM1).exception.isEmpty
    }
    condWhen(i == 0, p(i)(MEM2).!!!(useMem)) {
      accessMem2.bind(p(i)(MEM2))
    }
  }

  when(p0(ID).produced(useMem) & p1(ID).produced(useMem)) {
    p1(ID).is.done := False
  }
  /**/

  /* ????????? cp0 ?????? */
  for (i <- 1 downto 0) {
    val self = p(i)(MEM1)
    self.addComponent(new StageComponent {
      output(modifyCP0) := self.!!!(modifyCP0) | self.exception.isDefined
    })

    self.!!!(cp0WE)
    self.!!!(eret)
    condWhen(i == 0, self.produced(modifyCP0)) {
      cp0.io.softwareWrite.valid := self.stored(cp0WE)
      cp0.io.softwareWrite.addr.rd := self.stored(inst).rd
      cp0.io.softwareWrite.addr.sel := self.stored(inst).sel
      cp0.io.softwareWrite.data := self.input(rtValue)

      cp0.io.exceptionBus.exception.excCode := self.exception
      cp0.io.exceptionBus.exception.instFetch := self.stored(excOnFetch)
      cp0.io.exceptionBus.eret := self.stored(eret)
      cp0.io.exceptionBus.vaddr := self.stored(memAddr)
      cp0.io.exceptionBus.pc := self.stored(pc)
      cp0.io.exceptionBus.bd := self.stored(inSlot)
      cp0.io.exceptionBus.valid := !self.is.empty
    }
  }
  cp0.io.externalInterrupt := io.externalInterrupt(0, 5 bits)

  when(p0(MEM1).produced(modifyCP0) & p1(MEM1).produced(modifyCP0)) {
    p1(MEM1).is.done := False
  }
  /**/

  /* ?????????: ?????????????????? */
  p0(EXE).addComponent(new StageComponent {
    val dsReg = RegInit(False)
    when(p0(EXE).will.output | p0(EXE).assign.flush) { dsReg := False }
    when(p1(MEM1).will.input & p1(EXE).!!!(isJump)) { dsReg := True }
    output(inSlot) := dsReg
  })
  p1(EXE).addComponent(new StageComponent {
    val dsReg = RegInit(False)
    when(p1(EXE).will.output | p1(EXE).assign.flush) { dsReg := False }
    when(p0(EXE).will.input & p0(ID).produced(isJump)) { dsReg := True }
    output(inSlot) := dsReg
  })
  /**/

  /* ????????? ???????????? */
  p1(MEM1).addComponent(new StageComponent {
    output(hiWE) := p0(MEM1).exception.isEmpty & p1(MEM1).!!!(hiWE)
    output(loWE) := p0(MEM1).exception.isEmpty & p1(MEM1).!!!(loWE)
  })
  /**/

  /* ????????? 2 ???????????????????????? 1 ???????????? */
  when(!p0(ID).is.done) { p1(ID).is.done := False }
  when(!p0(EXE).is.done) { p1(EXE).is.done := False }
  /**/

  /* ?????????: exe ????????????????????? */
  ju.op := p0(EXE).stored(jumpCond)
  ju.a := S(p0(EXE).input(rsValue))
  ju.b := S(p0(EXE).input(rtValue))
  val juJumpPC = p0(EXE).stored(isDJump) ? p0(EXE).stored(jumpPC) | U(ju.a)

  val p0ExeIsNew = RegNext(p0(EXE).will.input)
  val p1ExeIsNew = RegNext(p1(EXE).will.input)
  when(p0(EXE).stored(isJump)) {
    correct.payload := ju.jump ? juJumpPC | p0(EXE).stored(pc) + 8
    when(p0ExeIsNew) {
      when(ju.jump & (!p0(EXE).stored(predJump) /* | p0(EXE).stored(predJumpPC) =/= juJumpPC */ ) | !ju.jump & p0(EXE).stored(predJump)) {
        correct.valid := True
        when(!p1(EXE).is.empty) {
          p0(ID).assign.flush := True
          p1(ID).assign.flush := True
        }
      }
    }
  } /* otherwise {
    when(p0(EXE).stored(predJump)) {
      correct.push(p0(EXE).stored(pc) + 4)
      p0(ID).assign.flush := p0ExeIsNew
      p1(ID).assign.flush := p0ExeIsNew
      p1(EXE).assign.flush := p0ExeIsNew
    } elsewhen p1(EXE).stored(predJump) {
      correct.push(p1(EXE).stored(pc) + 4)
      p0(ID).assign.flush := p1ExeIsNew
      p1(ID).assign.flush := p1ExeIsNew
    }
  }*/
  /* ?????????????????????????????? */
  // complexALU
  for (i <- 1 downto 0) {
    condWhen(i == 0, p(i)(ID).produced(complexOp)) {
      multiCycleCompute.will.input := p(i)(EXE).will.input
      multiCycleCompute.receive(p(i)(ID))
    }
    condWhen(i == 0, p(i)(EXE).stored(complexOp)) {
      multiCycleCompute.bind(p(i)(EXE))
      complexALU.io.flush := p(i)(EXE).will.flush
    }
  }

  /* ?????????????????? */
  val seq = (IF, IF) +: p0.values.zip(p1.values).toSeq
  for ((curr, next) <- seq.zip(seq.tail).reverse) {
    val canPass = next._1.can.input & next._2.can.input

    next._1.interConnect()
    curr._1.connect(next._1, canPass)

    next._2.interConnect()
    curr._2.connect(next._2, canPass)
  }
  /**/
}

object MultiIssueCPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MultiIssueCPU)
  }
}
