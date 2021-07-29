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
  val twoInsts     = Key(Bits(2 * 32 bits)).setEmptyValue(ConstantVal.INST_NOP ## ConstantVal.INST_NOP)
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

  val mmuRes = Key(new MMUTranslationRes(ConstantVal.USE_TLB))

  val mmu  = new MMU(useTLB = ConstantVal.USE_TLB)
  val icu  = new ICU
  val ju   = new JU
  val dcu1 = new DCU1
  val dcu2 = new DCU2
  val hlu  = new HLU
  val cp0  = new CP0
  val rfu  = new RFU(2, 4)

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

      output(hiWE) := stored(hiWE) & !want.flush
      output(loWE) := stored(loWE) & !want.flush

      val calcModifyCP0 = new StageComponent {
        output(modifyCP0) := !!!(modifyCP0) | exception.isDefined
      }

      !!!(memRE)
      !!!(memWE)
      exceptionToRaise := None
      when(!dcu1.io.output.addrValid) {
        when(stored(memRE)) { exceptionToRaise := EXCEPTION.AdEL }
        when(stored(memWE)) { exceptionToRaise := EXCEPTION.AdES }
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

      output(inst) := bitsToInst(!!!(twoInsts)(i * 32, 32 bits))

      val readPC = new StageComponent {
        if (i == 0) { output(pc) := input(pc) }
        else { output(pc) := input(pc)(31 downto 3) @@ U"1" @@ input(pc)(1 downto 0) }
      }

      val decode = new StageComponent {
        du.io.inst := produced(inst)

        output(eret) := du.io.eret
        output(isJump) := du.io.ju_op =/= JU_OP.f
        output(isDJump) := du.io.ju_pc_src =/= JU_PC_SRC.rs
        val upperPC = produced(pc)(31 downto 2)
        output(jumpPC) := du.io.ju_pc_src.mux(
          JU_PC_SRC.offset -> U(S(upperPC) + du.io.inst.offset + 1) @@ U"00",
          default          -> (upperPC + 1)(29 downto 26) @@ du.io.inst.index @@ U"00"
        )
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
        output(rfuData) := B(produced(pc) + 8)
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

  val p1 = p(0)
  val p2 = p(1)

  /* 处理 exe 阶段的 hlu 前传 */
  for (i <- 0 to 1) {
    val self = p(i)(EXE)
    self.addComponent(new StageComponent {
      val hi = Bits(32 bits)
      val lo = Bits(32 bits)

      hi := hlu.hi_v
      lo := hlu.lo_v
      if (i == 1) {
        val stage = p1(EXE)
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

  // 维护 delay slot 信号
  p1(ID).addComponent(new StageComponent {
    val dsReg = RegInit(False)
    when(p1(ID).will.output | p1(ID).assign.flush) { dsReg := False }
    when(p2(EXE).will.input) { dsReg := p2(ID).produced(isJump) }
    output(inSlot) := dsReg
  })
  p2(EXE).addComponent(new StageComponent {
    val dsReg = RegInit(False)
    when(p2(EXE).will.output | p2(EXE).assign.flush) { dsReg := False }
    when(p1(EXE).will.input) { dsReg := p1(ID).produced(isJump) }
    output(inSlot) := dsReg
  })
  //

  /**/
  val decideJump = new ComponentStage {
    val assignJump = RegNext(will.input) & ju.jump

    ju.op := !!!(jumpCond)
    ju.a := stored(rsValue).asSInt
    ju.b := stored(rtValue).asSInt

    output(jumpPC) := stored(isDJump) ? stored(jumpPC) | U(stored(rsValue))
  }
  /**/

  val if2 = new Stage {
    val fetchInst = new StageComponent {
      ibus.stage2.paddr := input(paddr)
      ibus.stage2.en := !!!(ibusStage2En)
      output(twoInsts) := ibus.stage2.rdata
    }

    is.done := !ibus.stage2.stall
    can.flush := !ibus.stage2.stall
  }
  val if1 = new Stage {
    val writePC = new StageComponent {
      val pcReg = RegInit(INIT_PC)
      output(pc) := pcReg

      val jumpTask = Task(decideJump.assignJump, decideJump.produced(jumpPC), will.output)

      when(will.output) {
        pcReg := (pcReg(31 downto 3) + 1) @@ U"000"
        when(jumpTask.has) { pcReg := jumpTask.value }
        when(cp0.io.jumpPc.isDefined) { pcReg := cp0.io.jumpPc.value }
      }
    }

    val fetchInst = new StageComponent {
      icu.io.offset := writePC.pcReg(1 downto 0)

      ibus.stage1.read := True
      ibus.stage1.index := writePC.pcReg(icacheConfig.offsetWidth, icacheConfig.indexWidth bits)
      ibus.stage1.keepRData := !if2.will.input

      mmu.io.instVaddr := writePC.pcReg(31 downto 3) @@ U"000"
      output(paddr) := mmu.io.instRes.paddr
      output(ibusStage2En) := True

      exceptionToRaise := None
      output(excOnFetch) := False
      when(!icu.io.addrValid) {
        exceptionToRaise := EXCEPTION.AdEL
        output(excOnFetch) := True
        output(ibusStage2En) := False
      }
    }
  }

  val accessMem2 = new ComponentStage {
    dcu2.io.input.byteEnable := stored(memBE)
    dcu2.io.input.extend := stored(memEx)
    dcu2.io.input.wdata := stored(rtValue)
    dcu2.io.input.rdata := dbus.stage2.rdata

    dbus.stage2.read := !!!(memRE)
    dbus.stage2.write := !!!(memWE)
    dbus.stage2.paddr := stored(mmuRes).paddr
    dbus.stage2.byteEnable := stored(memBE)
    dbus.stage2.wdata := dcu2.io.output.wdata
    dbus.stage2.uncache := !stored(mmuRes).cached
  }
  val accessMem1 = new ComponentStage {
    output(mmuRes) := RegNextWhen(mmu.io.dataRes, will.input)

    dcu1.io.input.paddr := output(mmuRes).paddr
    dcu1.io.input.byteEnable := stored(memBEType)
    output(memBE) := dcu1.io.output.byteEnable

    dbus.stage1.keepRData := !accessMem2.will.input
    dbus.stage1.paddr := output(mmuRes).paddr
  }

  decideJump.interConnect()

  accessMem2.interConnect()
  accessMem1.send(accessMem2)
  accessMem1.interConnect()

  for (i <- 1 downto 0) {
    condWhen(i == 0, p(i)(ID).produced(isJump)) {
      decideJump.will.input := p(i)(EXE).will.input
      decideJump.receive(p(i)(ID))
    }
    condWhen(i == 0, p(i)(EXE).!!!(isJump)) {
      decideJump.will.output := p(i)(EXE).will.output
    }

    condWhen(i == 0, p(i)(EXE).!!!(useMem)) {
      mmu.io.dataVaddr := p(i)(EXE).produced(memAddr)

      accessMem1.will.input := p(i)(MEM1).will.input & p(i)(EXE).exception.isEmpty
      accessMem1.receive(p(i)(EXE))
    }
    condWhen(i == 0, p(i)(MEM1).!!!(useMem)) {
      accessMem1.will.output := p(i)(MEM1).will.output
      accessMem2.will.input := p(i)(MEM2).will.input
    }
    condWhen(i == 0, p(i)(MEM2).!!!(useMem)) {
      accessMem2.will.output := p(i)(MEM2).will.output
    }

    condWhen(i == 0, p(i)(MEM1).produced(modifyCP0)) {
      cp0.io.softwareWrite.valid := p(i)(MEM1).!!!(cp0WE)
      cp0.io.softwareWrite.addr.rd := p(i)(MEM1).stored(inst).rd
      cp0.io.softwareWrite.addr.sel := p(i)(MEM1).stored(inst).sel
      cp0.io.softwareWrite.data := p(i)(MEM1).stored(rtValue)

      cp0.io.exceptionInput.exception := p(i)(MEM1).exception
      cp0.io.exceptionInput.eret := p(i)(MEM1).stored(eret)
      cp0.io.exceptionInput.memAddr := p(i)(MEM1).stored(memAddr)
      cp0.io.exceptionInput.pc := p(i)(MEM1).stored(pc)
      cp0.io.exceptionInput.bd := p(i)(MEM1).stored(inSlot)
      cp0.io.exceptionInput.instFetch := p(i)(MEM1).stored(excOnFetch)

      cp0.io.externalInterrupt := io.externalInterrupt
    }
  }
  /**/

  /**/
  for (i <- 0 to 1) {
    condWhen(i == 1, p(i)(MEM1).produced(hiWE)) {
      hlu.hi_we := p(i)(MEM1).stored(hiWE)
      hlu.new_hi := p(i)(MEM1).stored(newHi)
    }
    condWhen(i == 1, p(i)(MEM1).produced(loWE)) {
      hlu.lo_we := p(i)(MEM1).stored(loWE)
      hlu.new_lo := p(i)(MEM1).stored(newLo)
    }
  }
  /**/

  /* 处理跳转的一些情况 */
  val p2IDFlushNextInst = // 第 2 流水线的 ID 阶段可能需要作废下一条进入的指令
    SimpleTask(
      decideJump.assignJump & p2(EXE).!!!(isJump),
      p2(ID).will.output | p2(ID).assign.flush
    )
  when(p1(ID).stored(pc)(2)) { p1(ID).assign.flush := True }                     // 跳转到奇数地址
  when(p2IDFlushNextInst.has & !p2(ID).is.empty) { p2(ID).assign.flush := True } // flush 下一条进入的指令
  /**/

  /* 给出控制信号 */
  when(p1(ID).produced(useMem) & p2(ID).produced(useMem)) { p2(ID).is.done := False }

  when(decideJump.assignJump) {
    if1.assign.flush := True
    when(p1(EXE).!!!(isJump)) {
      if2.assign.flush := True
      when(!p2(EXE).is.empty) {
        p1(ID).assign.flush := True
        p2(ID).assign.flush := True
      }
    } elsewhen !p1(ID).is.empty {
      if2.assign.flush := True
    }
  }

  when(!p1(EXE).is.done) { p2(EXE).is.done := False }
  /**/

  /* CP0 相关控制 */
  when(p1(EXE).!!!(cp0RE) & (p1(MEM1).!!!(modifyCP0) | p2(MEM1).!!!(modifyCP0))) {
    p1(EXE).is.done := False
  }
  when(
    p2(EXE).!!!(cp0RE) &
      (p1(MEM1).!!!(modifyCP0) | p2(MEM1).!!!(modifyCP0) | p1(EXE).!!!(modifyCP0))
  ) {
    p2(EXE).is.done := False
  }

  when(p2(MEM1).produced(modifyCP0) & p1(MEM1).produced(modifyCP0)) {
    p2(MEM1).is.done := False
  }

  when(cp0.io.jumpPc.isDefined) {
    if1.assign.flush := True
    if2.assign.flush := True
    p1(ID).assign.flush := True
    p2(ID).assign.flush := True
    p1(EXE).assign.flush := True
    p2(EXE).assign.flush := True
    p2(MEM1).assign.flush := True
    when(p1(MEM1).produced(modifyCP0)) {
      p1(MEM1).assign.flush := True
    }
  }

  cp0.io.instOnInt.valid := !p1(EXE).is.empty
  cp0.io.instOnInt.bd := p1(EXE).stored(inSlot)
  cp0.io.instOnInt.pc := p1(EXE).stored(pc)
  /**/

  /* 关注点 rfu 读写前传 */
  for (i <- 0 to 1) {
    val self = p(i)(ID)
    self.addComponent(new StageComponent {
      rfu.io.r(i).index := self.produced(inst).rs
      rfu.io.r(2 + i).index := self.produced(inst).rt
      output(rsValue) := rfu.io.r(i).data
      output(rtValue) := rfu.io.r(2 + i).data

      for (j <- 0 to 1) {
        val stage = p(j)(MEM2)
        when(stage.!!!(rfuWE)) {
          when(stage.stored(rfuIndex) === self.produced(inst).rs) {
            output(rsValue) := stage.produced(rfuData)
            when(self.produced(useRs) & !stage.is.done) {
              self.is.done := False
            }
          }
          when(stage.stored(rfuIndex) === self.produced(inst).rt) {
            output(rtValue) := stage.produced(rfuData)
            when(self.produced(useRt) & !stage.is.done) {
              self.is.done := False
            }
          }
        }
      }

      for (j <- Seq(MEM1, EXE)) {
        for (k <- 0 to 1) {
          val stage = p(k)(j)
          when(stage.!!!(rfuWE)) {
            when(stage.stored(rfuIndex) === self.produced(inst).rs) {
              output(rsValue) := stage.produced(rfuData)
              when(self.produced(useRs) & stage.stored(memRE)) {
                self.is.done := False
              }
            }
            when(stage.stored(rfuIndex) === self.produced(inst).rt) {
              output(rtValue) := stage.produced(rfuData)
              when(self.produced(useRt) & stage.stored(memRE)) {
                self.is.done := False
              }
            }
          }
        }
      }

      if (i == 1) {
        val stage = p1(ID)
        stage.!!!(twoInsts)
        when(stage.produced(rfuWE)) {
          when(stage.produced(rfuIndex) === self.produced(inst).rs & self.produced(useRs)) {
            self.is.done := False
          }
          when(stage.produced(rfuIndex) === self.produced(inst).rt & self.produced(useRt)) {
            self.is.done := False
          }
        }
      }
    })
  }
  /**/

  /* 关注点 hlu 读写前传 */
  /* 关注点 cp0 读写前传 */
  /* 关注点 2 号流水线不能超过 1 号流水线 */
  when(!p1(ID).is.done) {
    p2(ID).is.done := False
  }
  /**/

  /* 连接各个阶段 */
  val seq = (if2, if2) +: p1.values.zip(p2.values).toSeq
  for ((curr, next) <- seq.zip(seq.tail).reverse) {
    val canPass = next._1.can.input & next._2.can.input

    next._1.interConnect()
    curr._1.connect(next._1, canPass)

    next._2.interConnect()
    curr._2.connect(next._2, canPass)
  }

  if2.interConnect()

  if1.connect(if2, if2.can.input)
  if1.interConnect()
  /**/
}

object MultiIssueCPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MultiIssueCPU)
  }
}
