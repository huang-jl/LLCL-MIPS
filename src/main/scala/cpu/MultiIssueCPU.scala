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
  val aluC         = Key(UInt(32 bits))
  val aluD         = Key(UInt(32 bits))
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

  val mmuRes = Key(MMUTranslationRes(ConstantVal.FINAL_MODE))

  val predJump   = Key(Bool()) setEmptyValue False
  val predJumpPC = Key(UInt(32 bits))

  val tlbRefillException = Key(Bool) //是否是TLB缺失异常（影响异常处理地址）

  val mmu  = new MMU
  val icu  = new ICU
  val ju   = new JU
  val dcu1 = new DCU1
  val dcu2 = new DCU2
  val hlu  = new HLU
  val cp0  = new CP0
  val rfu  = new RFU(2, 4)

  val bps = Seq(new BranchPredictor(0), new BranchPredictor(1))

  /* temp */
  mmu.io.asid.assignDontCare()
  /**/

  var p: Seq[Map[Value, Stage]] = Seq()

  for (i <- 0 to 1) {
    val du  = new DU
    val alu = new ALU

    val wb = new Stage {
      setName(s"wb_${i}")

      produced(pc)

      val writeReg = new StageComponent {
        rfu.io.w(i).valid := !!!(rfuWE)
        rfu.io.w(i).index := input(rfuIndex)
        rfu.io.w(i).data := input(rfuData)
      }
    }
    val mem2 = new Stage {
      setName(s"mem2_${i}")

      val accessMem = new StageComponent {
        output(rfuData) := input(memRE) ? dcu2.io.output.rdata | input(rfuData)
      }

      is.done := !dbus.stage2.stall
      can.flush := !dbus.stage2.stall
    }
    val mem1 = new Stage {
      setName(s"mem1_${i}")

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

        alu.io.will.input := will.input
        alu.io.will.output := will.output

        output(aluC) := alu.io.c
        output(aluD) := alu.io.d

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
          HLU_SRC.alu -> B(alu.io.c),
          HLU_SRC.rs  -> input(rsValue)
        )
        output(newLo) := input(newLoSrc).mux(
          HLU_SRC.alu -> B(alu.io.d),
          HLU_SRC.rs  -> input(rsValue)
        )
      }

      is.done := !alu.io.stall
      can.flush := !alu.io.stall
    }
    val id = new Stage {
      setName(s"id_${i}")

      val entry = if (i > 0) stored(entry2) else stored(entry1)
      input(pc) := entry.pc
      input(inst) := entry.inst
      input(isJump) := entry.branch.is
      input(isDJump) := entry.branch.isDjump
      input(predJump) := entry.predict.taken
      input(predJumpPC) := entry.predict.target
      input(jumpPC) := entry.target
      when(!entry.valid)(assign.flush := True)

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

        exceptionToRaise := du.io.exception
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

  val correct = Flow(UInt(32 bits)).setIdle()
  val IF = new Stage {
    val fetchInst = new FetchInst(icacheConfig)
    // input
    fetchInst.io.jump := correct
    fetchInst.io.except.valid := cp0.io.exceptionBus.jumpPc.isDefined
    fetchInst.io.except.payload := cp0.io.exceptionBus.jumpPc.value
    fetchInst.io.mmuRes := mmu.io.instRes
    fetchInst.io.fetch := p(0)(ID).can.input & p(1)(ID).can.input
    for (i <- 0 until 2) fetchInst.io.bp.predict(i).assignAllByName(bps(i).read2.io)
    //output
    for (i <- 0 until 2) bps(i).read1.io.assignAllByName(fetchInst.io.bp.query)
    mmu.io.instVaddr := fetchInst.io.vaddr
    produced(entry1) := fetchInst.io.issue(0)
    produced(entry2) := fetchInst.io.issue(1)
    produced(tlbRefillException) := fetchInst.io.exception.refillException
    produced(excOnFetch) := fetchInst.io.exception.code.isDefined

    exceptionToRaise := fetchInst.io.exception.code

    ibus <> fetchInst.io.ibus

    /**/
    bps(0).read2.will.input := fetchInst.io.bp.will.input.S2
    bps(1).read2.will.input := fetchInst.io.bp.will.input.S2
    /**/
    when(fetchInst.io.issue(1).pc(2)) {
      bps(0).write0.io.issue := fetchInst.io.issue(0)
      bps(0).write1.will.input := p(0)(ID).will.input
      bps(1).write0.io.issue := fetchInst.io.issue(1)
      bps(1).write1.will.input := p(1)(ID).will.input
    } otherwise {
      bps(0).write0.io.issue := fetchInst.io.issue(1)
      bps(0).write1.will.input := p(1)(ID).will.input
      bps(1).write0.io.issue := fetchInst.io.issue(0)
      bps(1).write1.will.input := p(0)(ID).will.input
    }
    /**/
    when(p(1)(ID).produced(pc)(2)) {
      bps(0).write2.will.input := p(0)(EXE).will.input
      bps(1).write2.will.input := p(1)(EXE).will.input
    } otherwise {
      bps(0).write2.will.input := p(1)(EXE).will.input
      bps(1).write2.will.input := p(0)(EXE).will.input
    }
    /**/
    when(p1(EXE).stored(pc)(2)) {
      bps(0).write2.io.assignJump := ju.jump
      bps(1).write2.io.assignJump := False
    } otherwise {
      bps(0).write2.io.assignJump := False
      bps(1).write2.io.assignJump := ju.jump
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
    output(mmuRes) := RegNextWhen(mmu.io.dataRes, will.input)

    dcu1.io.input.byteOffset := output(mmuRes).paddr(0, 2 bits)
    dcu1.io.input.byteEnable := stored(memBEType)
    dcu1.io.input.wdata := stored(rtValue)

    output(memBE) := dcu1.io.output.byteEnable
    output(memWData) := dcu1.io.output.wdata

//    dbus.stage1.write := stored(memWE)
    dbus.stage1.paddr := output(mmuRes).paddr
    dbus.stage1.wdata := dcu1.io.output.wdata
    dbus.stage1.byteEnable := dcu1.io.output.byteEnable
  }

  accessMem2.interConnect()
  accessMem1.send(accessMem2)
  accessMem1.interConnect()
  /**/

  /* CP0 相关控制 */
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

  /* 关注点 rfu 读写前传 */
  for (i <- 0 to 1) {
    val self = p(i)(ID)
    self.addComponent(new StageComponent {
      rfu.io.r(i).index := input(inst).rs
      rfu.io.r(2 + i).index := input(inst).rt
      output(rsValue) := rfu.io.r(i).data
      output(rtValue) := rfu.io.r(2 + i).data

      val rsNotDone = False
      val rtNotDone = False

      for (j <- 0 to 1) {
        val stage = p(j)(MEM2)
        when(stage.!!!(rfuWE)) {
          when(stage.stored(rfuIndex) === input(inst).rs) {
            output(rsValue) := stage.produced(rfuData)
            rsNotDone := !stage.is.done
          }
          when(stage.stored(rfuIndex) === input(inst).rt) {
            output(rtValue) := stage.produced(rfuData)
            rtNotDone := !stage.is.done
          }
        }
      }

      for (j <- Seq(MEM1, EXE)) {
        for (k <- 0 to 1) {
          val stage = p(k)(j)
          when(stage.!!!(rfuWE)) {
            when(stage.stored(rfuIndex) === input(inst).rs) {
              output(rsValue) := stage.produced(rfuData)
              rsNotDone := stage.stored(memRE)
            }
            when(stage.stored(rfuIndex) === input(inst).rt) {
              output(rtValue) := stage.produced(rfuData)
              rtNotDone := stage.stored(memRE)
            }
          }
        }
      }

      if (i == 1) {
        val stage = p0(ID)
        stage.!!!(entry1)
        when(stage.produced(rfuWE)) {
          when(stage.produced(rfuIndex) === input(inst).rs) {
            rsNotDone := True
          }
          when(stage.produced(rfuIndex) === input(inst).rt) {
            rtNotDone := True
          }
        }
      }

      when(self.produced(useRs) & rsNotDone | self.produced(useRt) & rtNotDone) {
        self.is.done := False
      }
    })
  }
  /**/

  /* 关注点 hlu 读写前传 */
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

  /* 关注点 cp0 读写前传 */
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

  /* 关注点 mem 处理 */
  for (i <- 1 downto 0) {
    condWhen(i == 0, p(i)(EXE).!!!(useMem)) {
      mmu.io.dataVaddr := p(i)(EXE).produced(memAddr)

      accessMem1.will.input := p(i)(MEM1).will.input & p(i)(EXE).exception.isEmpty
      accessMem1.receive(p(i)(EXE))
    }
    condWhen(i == 0, p(i)(MEM1).!!!(useMem)) {
      accessMem1.will.output := p(i)(MEM1).will.output
      accessMem2.will.input := p(i)(MEM2).will.input
      // 注意只有访存才会在MEM1触发异常
      dbus.stage1.write := p(i)(MEM1).stored(memWE) & p(i)(MEM1).exception.isEmpty
    }
    condWhen(i == 0, p(i)(MEM2).!!!(useMem)) {
      accessMem2.will.output := p(i)(MEM2).will.output
    }
  }

  when(p0(ID).produced(useMem) & p1(ID).produced(useMem)) {
    p1(ID).is.done := False
  }
  /**/

  /* 关注点 cp0 处理 */
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
      cp0.io.softwareWrite.data := self.stored(rtValue)

      cp0.io.exceptionBus.exception.excCode := self.exception
      cp0.io.exceptionBus.exception.instFetch := self.stored(excOnFetch)
      cp0.io.exceptionBus.exception.tlbRefill := self.stored(tlbRefillException)
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

  /* 关注点: 延迟槽的计算 */
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

  /* 关注点 异常相关 */
  p1(MEM1).addComponent(new StageComponent {
    output(hiWE) := p0(MEM1).exception.isEmpty & p1(MEM1).!!!(hiWE)
    output(loWE) := p0(MEM1).exception.isEmpty & p1(MEM1).!!!(loWE)
  })
  /**/

  /* 关注点 2 号流水线不能超过 1 号流水线 */
  when(!p0(ID).is.done) { p1(ID).is.done := False }
  when(!p0(EXE).is.done) { p1(EXE).is.done := False }
  /**/

  /* 关注点: exe 阶段的跳转纠正 */
  ju.op := p0(EXE).stored(jumpCond)
  ju.a := S(p0(EXE).stored(rsValue))
  ju.b := S(p0(EXE).stored(rtValue))
  val juJumpPC = p0(EXE).stored(isDJump) ? p0(EXE).stored(jumpPC) | U(ju.a)

  val p0ExeIsNew = RegNext(p0(EXE).will.input)
  val p1ExeIsNew = RegNext(p1(EXE).will.input)
  when(p0(EXE).stored(isJump)) {
    correct.payload := ju.jump ? juJumpPC | p0(EXE).stored(pc) + 8
    when(p0ExeIsNew) {
      when(ju.jump & (!p0(EXE).stored(predJump) | p0(EXE).stored(predJumpPC) =/= juJumpPC) | !ju.jump & p0(EXE).stored(predJump)) {
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
  /**/

  /* 连接各个阶段 */
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
