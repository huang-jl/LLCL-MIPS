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
    CacheRamConfig(blockSize = ConstantVal.IcacheLineSize, wayNum = ConstantVal.IcacheWayNum)
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
  val ibusStage2En = Key(Bool())
  val twoInsts     = Key(Bits(2 * 32 bits))
  val inst         = Key(Mips32Inst())
  val eret         = Key(Bool())
  val isJump       = Key(Bool())
  val isDJump      = Key(Bool())
  val jumpCond     = Key(JU_OP())
  val jumpPC       = Key(UInt(32 bits))
  val inSlot       = Key(Bool())
  val useMem       = Key(Bool())
  val modifyCP0    = Key(Bool())
  val useRs        = Key(Bool())
  val useRt        = Key(Bool())
  val rsValue      = Key(Bits(32 bits))
  val rtValue      = Key(Bits(32 bits))
  val aluOp        = Key(ALU_OP()).setEmptyValue(ALU_OP.sll)
  val aluASrc      = Key(ALU_A_SRC())
  val aluBSrc      = Key(ALU_B_SRC())
  val aluC         = Key(UInt(32 bits))
  val aluD         = Key(UInt(32 bits))
  val cp0WE        = Key(Bool())
  val memAddr      = Key(UInt(32 bits))
  val memBEType    = Key(UInt(2 bits))
  val memBE        = Key(Bits(4 bits))
  val memEx        = Key(MU_EX())
  val memRE        = Key(Bool())
  val memWE        = Key(Bool())
  val hiWE         = Key(Bool())
  val newHiSrc     = Key(HLU_SRC())
  val newHi        = Key(Bits(32 bits))
  val loWE         = Key(Bool())
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
      val writeReg = new StageComponent {
        rfu.io.w(i).valid := input(rfuWE)
        rfu.io.w(i).index := input(rfuIndex)
        rfu.io.w(i).data := input(rfuData)
      }
    }
    val mem2 = new Stage {
      val accessMem = new StageComponent {
        output(rfuData) := input(memRE) ? dcu2.io.output.rdata | input(rfuData)
      }

      is.done := !dbus.stage2.stall
      can.flush := !dbus.stage2.stall
    }
    val mem1 = new Stage {
      output(hiWE) := stored(hiWE) & !want.flush
      output(loWE) := stored(loWE) & !want.flush
    }
    val exe = new Stage {
      val calc = new StageComponent {
        alu.io.input.op := input(aluOp)
        alu.io.input.a := input(aluASrc).mux(
          ALU_A_SRC.rs -> input(rsValue).asUInt,
          ALU_A_SRC.sa -> input(inst).sa.resize(32)
        )
        alu.io.input.b := input(aluBSrc).mux(
          ALU_B_SRC.rt  -> input(rtValue).asUInt,
          ALU_B_SRC.imm -> U(input(inst).imm, 32 bits)
        )

        alu.io.will.input := will.input
        alu.io.will.output := will.output

        output(aluC) := alu.io.c
        output(aluD) := alu.io.d

        exceptionToRaise := alu.io.exception
      }

      val calcAddr = new StageComponent {
        output(memAddr) := (input(rsValue).asSInt + input(inst).offset).asUInt
      }

      val readCP0 = new StageComponent {
        cp0.io.read(i).addr.rd := input(inst).rd
        cp0.io.read(i).addr.sel := input(inst).sel
      }

      val calcHiLo = new StageComponent {
        output(newHi) := input(newHiSrc).mux(
          HLU_SRC.alu -> alu.io.c.asBits,
          HLU_SRC.rs  -> input(rsValue)
        )
        output(newLo) := input(newLoSrc).mux(
          HLU_SRC.alu -> alu.io.d.asBits,
          HLU_SRC.rs  -> input(rsValue)
        )
      }

      is.done := !alu.io.stall
      can.flush := !alu.io.stall
    }
    val id = new Stage {
      output(inst) := bitsToInst(stored(twoInsts)(i * 32, 32 bits))

      val readPC = new StageComponent {
        output(pc) := input(pc)(31 downto 3) @@ U(i, 1 bits) @@ U"00"
      }

      val decode = new StageComponent {
        du.io.inst := produced(inst)

        output(eret) := du.io.eret
        output(isJump) := du.io.ju_op =/= JU_OP.f
        output(isDJump) := du.io.ju_pc_src =/= JU_PC_SRC.rs
        val upperPC = input(pc)(31 downto 3) @@ U(i, 1 bits)
        output(jumpPC) := du.io.ju_pc_src.mux(
          JU_PC_SRC.offset -> U(S(upperPC) + du.io.inst.offset + 1) @@ U"00",
          default          -> (upperPC + 1)(29 downto 26) @@ du.io.inst.index @@ U"00"
        )
        output(jumpCond) := du.io.ju_op
        output(useMem) := du.io.dcu_re | du.io.dcu_we
        output(modifyCP0) := False
        output(useRs) := du.io.use_rs
        output(useRt) := du.io.use_rt
        output(aluOp) := du.io.alu_op
        output(aluASrc) := du.io.alu_a_src
        output(aluBSrc) := du.io.alu_b_src
        output(cp0WE) := du.io.cp0_we
        output(memBEType) := du.io.dcu_be
        output(memEx) := du.io.mu_ex
        output(memRE) := du.io.dcu_re
        output(memWE) := du.io.dcu_we
        output(hiWE) := du.io.hlu_hi_we
        output(newHiSrc) := du.io.hlu_hi_src
        output(loWE) := du.io.hlu_lo_we
        output(newLoSrc) := du.io.hlu_lo_src
        output(rfuWE) := du.io.rfu_we
        output(rfuIndex) := du.io.rfu_rd
        output(rfuSrc) := du.io.rfu_rd_src

        exceptionToRaise := du.io.exception
      }

      val calcRFUData = new StageComponent {
        output(rfuData) := B(input(pc) + 8)
      }
    }

    p = p :+ Map(
      ID   -> id,
      EXE  -> exe,
      MEM1 -> mem1,
      MEM2 -> mem2,
      WB   -> wb
    )
  }

  val p1 = p(0)
  val p2 = p(1)

  // 维护 delay slot 信号
  p1(ID).addComponent(new StageComponent {
    val dsReg = RegInit(False)
    when(p1(ID).will.output) { dsReg := p2(ID).produced(isJump) }
    when(p1(ID).want.flush) { dsReg := False }
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
    ju.op := stored(jumpCond)
    ju.a := stored(rsValue).asSInt
    ju.b := stored(rtValue).asSInt

    output(jumpPC) := stored(isDJump) ? stored(jumpPC) | U(stored(rsValue))
  }
  /**/

  val if2 = new Stage {
    val fetchInst = new StageComponent {
      ibus.stage2.paddr := input(paddr)
      ibus.stage2.en := input(ibusStage2En)
      output(inst) := ibus.stage2.rdata
    }

    is.done := !ibus.stage2.stall
    can.flush := !ibus.stage2.stall
  }
  val if1 = new Stage {
    val writePC = new StageComponent {
      val pcReg = RegInit(INIT_PC)
      output(pc) := pcReg

      val jumpTask = Task(ju.jump, decideJump.produced(jumpPC), will.output)

      when(will.output) {
        pcReg := (pcReg(31 downto 3) + 1) @@ U"000"
        when(jumpTask.has) { pcReg := jumpTask.value }
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

    dbus.stage2.read := stored(memRE)
    dbus.stage2.write := stored(memWE)
    dbus.stage2.paddr := stored(mmuRes).paddr
    dbus.stage2.byteEnable := stored(memBE)
    dbus.stage2.wdata := dcu2.io.output.wdata
    dbus.stage2.uncache := !stored(mmuRes).cached
  }
  val accessMem1 = new ComponentStage {
    val mmuRes = RegNextWhen(mmu.io.dataRes, will.input)

    dcu1.io.input.paddr := mmuRes.paddr
    dcu1.io.input.byteEnable := stored(memBEType)
    output(memBE) := dcu1.io.output.byteEnable

    dbus.stage1.keepRData := !accessMem2.will.input
    dbus.stage1.paddr := mmuRes.paddr
  }
  accessMem1.send(accessMem2)

  for (i <- 1 downto 0) {
    condWhen(i == 0, p(i)(ID).produced(isJump)) {
      decideJump.will.input := p(i)(EXE).will.input
      decideJump.receive(p(i)(ID))
    }

    condWhen(i == 0, p(i)(EXE).stored(useMem)) {
      mmu.io.dataVaddr := p(i)(EXE).produced(memAddr)

      accessMem1.will.input := p(i)(MEM1).will.input & p(i)(EXE).exception.isEmpty
      accessMem1.receive(p(i)(EXE))
    }
    condWhen(i == 0, p(i)(MEM1).stored(useMem)) {
      accessMem1.will.output := p(i)(MEM1).will.output
      accessMem2.will.input := p(i)(MEM2).will.input
    }
    condWhen(i == 0, p(i)(MEM2).stored(useMem)) {
      accessMem2.will.output := p(i)(MEM2).will.output
    }

    condWhen(i == 0, p(i)(MEM1).produced(modifyCP0)) {
      cp0.io.softwareWrite.valid := p(i)(MEM1).stored(cp0WE)
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
    SimpleTask(p2(EXE).stored(isJump) & ju.jump, p2(ID).will.output | p2(ID).assign.flush)
  /* TODO p1(ID).stored(pc) := if2.produced(pc) */
  /* TODO p1(ID).prevException := if2.exception */
  /* TODO p2(ID).stored(pc) := if2.produced(pc)(31 downto 3) @@ U"1" @@ if2.produced(pc)(1 downto 0) */
  /* TODO p2(ID).prevException := if2.exception */
  when(p1(ID).stored(pc)(2)) { p1(ID).assign.flush := True }  // 跳转到奇数地址
  when(p2IDFlushNextInst.has) { p2(ID).assign.flush := True } // flush 下一条进入的指令
  /**/

  /* 处理 ID 阶段的 reg 前传 */
  for (i <- 0 to 1) {
    val self = p(i)(ID)
    self.addComponent(new StageComponent {
      rfu.io.r(i).index := input(inst).rs
      output(rsValue) := rfu.io.r(i).data
      rfu.io.r(2 + i).index := input(inst).rt
      output(rtValue) := rfu.io.r(2 + i).data

      for (j <- 0 to 1) {
        val stage = p(j)(MEM2)
        when(stage.stored(rfuWE)) {
          when(stage.stored(rfuIndex) === input(inst).rs) {
            output(rsValue) := stage.produced(rfuData)
          }
          when(stage.stored(rfuIndex) === input(inst).rt) {
            output(rtValue) := stage.produced(rfuData)
          }
        }
      }
      for (j <- Seq(MEM1, EXE)) {
        for (k <- 0 to 1) {
          val stage = p(k)(j)
          when(stage.stored(rfuWE)) {
            when(stage.stored(rfuIndex) === input(inst).rs) {
              output(rsValue) := stage.produced(rfuData)
              when(self.produced(useRs) & stage.stored(memRE)) { self.is.done := False }
            }
            when(stage.stored(rfuIndex) === input(inst).rt) {
              output(rtValue) := stage.produced(rfuData)
              when(self.produced(useRt) & stage.stored(memRE)) { self.is.done := False }
            }
          }
        }
      }
      if (i == 1) {
        val stage = p1(ID)
        when(
          stage.stored(rfuWE) &
            (stage.produced(rfuIndex) === input(inst).rs & self.produced(useRs) |
              stage.produced(rfuIndex) === input(inst).rt & self.produced(useRt))
        ) { self.is.done := False }
      }
    })
  }
  /**/

  /* 处理 exe 阶段的 HLU 前传 */
  for (i <- 0 to 1) {
    val self = p(i)(EXE)
    self.addComponent(new StageComponent {
      val hi = Bits(32 bits)
      val lo = Bits(32 bits)

      hi := hlu.hi_v
      lo := hlu.lo_v
      if (i == 1) {
        val stage = p1(EXE)
        when(stage.stored(hiWE)) {
          hi := stage.produced(newHi)
        }
        when(stage.stored(loWE)) {
          lo := stage.produced(newLo)
        }
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

  /* 给出控制信号 */
  p2(ID).is.done := !(p2(ID).produced(useMem) & p1(ID).produced(useMem))
  /**/

  /* 连接各个阶段 */
  val seq = (if2, if2) +: p1.values.zip(p2.values).toSeq
  for ((curr, next) <- seq.zip(seq.tail).reverse) {
    val canPass = next._1.can.input & next._2.can.input
    curr._1.connect(next._1, canPass)
    curr._2.connect(next._2, canPass)
  }
  if1.connect(if2, if2.can.input)
  /**/
}

object MultiIssueCPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MultiIssueCPU)
  }
}
