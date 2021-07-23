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

  val pc        = Key(UInt(32 bits))
  val twoInsts  = Key(Bits(2 * 32 bits))
  val inst      = Key(Mips32Inst())
  val aluOp     = Key(ALU_OP()).setEmptyValue(ALU_OP.sll)
  val aluASrc   = Key(ALU_A_SRC())
  val aluBSrc   = Key(ALU_B_SRC())
  val aluC      = Key(UInt(32 bits))
  val aluD      = Key(UInt(32 bits))
  val rsValue   = Key(Bits(32 bits))
  val rsValue_  = Key(Bits(32 bits))
  val rtValue   = Key(Bits(32 bits))
  val memAddr   = Key(UInt(32 bits))
  val memBEType = Key(UInt(2 bits))
  val memBE     = Key(Bits(4 bits))
  val memEx     = Key(MU_EX())
  val memRE     = Key(Bool())
  val memWE     = Key(Bool())
  val hluHiWE   = Key(Bool())
  val hluHiSrc  = Key(HLU_SRC())
  val hluLoWE   = Key(Bool())
  val hluLoSrc  = Key(HLU_SRC())
  val rfuWE     = Key(Bool()).setEmptyValue(False)
  val rfuIndex  = Key(UInt(5 bits))
  val rfuData   = Key(Bits(32 bits))

  val mmuRes = Key(new MMUTranslationRes(ConstantVal.USE_TLB))

  val mmu  = new MMU(useTLB = ConstantVal.USE_TLB)
  val icu  = new ICU
  val ju   = new JU
  val dcu1 = new DCU1
  val dcu2 = new DCU2
  val hlu  = new HLU
  val cp0  = new CP0
  val rfu  = new RFU(2, 4)

  var pipelines: Seq[Map[Value, Stage]] = Seq()
  val p1                                = pipelines.head
  val p2                                = pipelines(1)

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
    }
    val mem1 = new Stage {
      input(hluHiWE) := stored(hluHiWE) & !want.flush
      input(hluLoWE) := stored(hluLoWE) & !want.flush
    }
    val exe = new Stage {
      val calc = new StageComponent {
        alu.io.input.op := input(aluOp)
        alu.io.input.a := input(aluASrc).mux(
          ALU_A_SRC.rs -> input(rsValue).asUInt,
          ALU_A_SRC.sa -> input(inst).sa
        )
        alu.io.input.b := input(aluBSrc).mux(
          ALU_B_SRC.rt  -> input(rtValue).asUInt,
          ALU_B_SRC.imm -> input(inst).imm.asUInt
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
        cp0.io.read.addr.rd(i) := input(inst).rd
        cp0.io.read.addr.sel(i) := input(inst).sel
        output(rfuData) := cp0.io.read.data(i)
      }
    }
    val id = new Stage {
      input(inst) := bitsToInst(stored(twoInsts)(i * 32, 32 bits))

      val readPC = new StageComponent {
        output(pc) := stored(pc)(3 to 31) @@ i @@ U"00"
      }

      val decode = new StageComponent {
        du.io.inst := input(inst)

        output(aluOp) := du.io.alu_op
        output(aluASrc) := du.io.alu_a_src
        output(aluBSrc) := du.io.alu_b_src

        /* TODO 补全 DU */

        exceptionToRaise := du.io.exception
      }

      val readReg = new StageComponent {
        rfu.io.r(i).index := input(inst).rs
        output(rsValue) := rfu.io.r(i).data

        rfu.io.r(2 + i).index := input(inst).rt
        output(rtValue) := rfu.io.r(2 + i).data
      }

      // TODO 数据前传
    }

    pipelines = pipelines :+ Map(
      ID   -> id,
      EXE  -> exe,
      MEM1 -> mem1,
      MEM2 -> mem2,
      WB   -> wb
    )
  }

  // 维护 delay slot 信号
  p1(ID).addComponent(new StageComponent {
    val dsReg = RegInit(False)
    when(p1(ID).will.output) { dsReg := p2(ID).produced(useJU) }
    when(p1(ID).want.flush) { dsReg := False }
    output(ds) := dsReg
  })
  p2(EXE).addComponent(new StageComponent {
    val dsReg = RegInit(False)
    when(p2(EXE).will.output | p2(EXE).assign.flush) { dsReg := False }
    when(p1(EXE).will.input) { dsReg := p1(ID).produced(useJU) }
    output(ds) := dsReg
  })
  //

  val if2 = new Stage {
    val fetchInst = new StageComponent {
      ibus.stage2.paddr := input(paddr)
      ibus.stage2.en := input(ibusStage2En)
      output(inst) := ibus.stage2.rdata
    }
  }
  val if1 = new Stage {
    val writePC = new StageComponent {
      val pcReg = RegInit(INIT_PC)
      pcReg := (pcReg(31 to 3) + 1) @@ U"000"

      output(pc) := pcReg

      // TODO 跳转
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
      output(fetchExc) := False
      when(!icu.io.addrValid) {
        exceptionToRaise := EXCEPTION.AdEL
        output(fetchExc) := True
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

  val decideJump = new ComponentStage {
    ju.op := stored(jumpCond)
    ju.a := stored(rsValue).asSInt
    ju.b := stored(rtValue).asSInt
  }

  val p = pipelines
  for (i <- 1 downto 0) {
    condWhen(i == 0, p(i)(ID).produced(useJU)) {
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

    condWhen(i == 0, p(i)(MEM1).produced(useCP0)) {
      cp0.io.softwareWrite.valid := p(i)(MEM1).stored(cp0WE)
      cp0.io.softwareWrite.addr.rd := p(i)(MEM1).stored(inst).rd
      cp0.io.softwareWrite.addr.sel := p(i)(MEM1).stored(inst).sel
      cp0.io.softwareWrite.data := p(i)(MEM1).stored(rtValue)

      cp0.io.exceptionInput.exception := p(i)(MEM1).exception
      cp0.io.exceptionInput.eret := p(i)(MEM1).stored(eret)
      cp0.io.exceptionInput.memAddr := p(i)(MEM1).stored(memAddr)
      cp0.io.exceptionInput.pc := p(i)(MEM1).stored(pc)
      cp0.io.exceptionInput.bd := p(i)(MEM1).stored(bd)
      cp0.io.exceptionInput.instFetch := p(i)(MEM1).stored(instFetch)

      cp0.io.externalInterrupt := io.externalInterrupt
    }
  }
  /**/

  /**/
  for (i <- 0 to 1) {
    condWhen(i == 1, p(i)(MEM1).input(hluHiWE)) {
      hlu.hi_we := p(i)(MEM1).stored(hluHiWE)
      hlu.new_hi := p(i)(MEM1)
        .stored(hluHiSrc)
        .mux(
          HLU_SRC.alu -> p(i)(MEM1).stored(aluC).asBits,
          HLU_SRC.rs  -> p(i)(MEM1).stored(rsValue)
        )
    }
    condWhen(i == 1, p(i)(MEM1).input(hluLoWE)) {
      hlu.lo_we := p(i)(MEM1).stored(hluLoWE)
      hlu.new_lo := p(i)(MEM1)
        .stored(hluLoSrc)
        .mux(
          HLU_SRC.alu -> p(i)(MEM1).stored(aluD).asBits,
          HLU_SRC.rs  -> p(i)(MEM1).stored(rsValue)
        )
    }
  }
  /**/

  /* 处理跳转的一些情况 */
  val p2IDFlushNextInst = // 第 2 流水线的 ID 阶段可能需要作废下一条进入的指令
    SimpleTask(p2(EXE).stored(useJU) & ju.jump, p2(ID).will.output | p2(ID).assign.flush)
  /* TODO p1(ID).stored(pc) := if2.produced(pc) */
  /* TODO p1(ID).prevException := if2.exception */
  /* TODO p2(ID).stored(pc) := if2.produced(pc)(31 downto 3) @@ U"1" @@ if2.produced(pc)(1 downto 0) */
  /* TODO p2(ID).prevException := if2.exception */
  when(p1(ID).stored(pc)(2)) { p1(ID).assign.flush := True }  // 跳转到奇数地址
  when(p2IDFlushNextInst.has) { p2(ID).assign.flush := True } // flush 下一条进入的指令
  /**/
}
