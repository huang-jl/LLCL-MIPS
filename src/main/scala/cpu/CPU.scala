package cpu

import defs.Mips32InstImplicits._
import defs.{ConstantVal, Mips32Inst}
import cache.CacheRamConfig
import lib.{Key, Optional, Updating}
import tlb.{MMU, TLBEntry, TLBConfig}

import spinal.core._
import spinal.lib.{cpu => _, _}
import spinal.lib.bus.amba4.axi._

class CPU extends Component {
  val io = new Bundle {
    val externalInterrupt = in Bits (6 bits)
    val icacheAXI         = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
    val dcacheAXI         = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
    val uncacheAXI        = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
  }
  val icacehConfig = CacheRamConfig(blockSize = ConstantVal.IcacheLineSize, wayNum = ConstantVal.IcacheWayNum)
  val dcacehConfig = CacheRamConfig(blockSize = ConstantVal.IcacheLineSize, wayNum = ConstantVal.IcacheWayNum)
  val icu = new ICU(icacehConfig)
  val du  = new DU
  val ju  = new JU
  val alu = new ALU
  val dcu = new DCU(dcacehConfig, fifoDepth = ConstantVal.DcacheFifoDepth)
  val hlu = new HLU
  val rfu = new RFU
  val cp0 = new CP0
  val mmu = new MMU(useTLB = ConstantVal.USE_TLB)

  val jumpPc = Key(Optional(UInt(32 bits)))

  val pc         = Key(UInt(32 bits))
  val bd         = Key(Bool)
  val inst       = Key(Mips32Inst())
  val exception  = Key(Optional(EXCEPTION()))
  val eret       = Key(Bool)
  val aluOp      = Key(ALU_OP())
  val aluASrc    = Key(ALU_A_SRC())
  val aluBSrc    = Key(ALU_B_SRC())
  val aluResultC = Key(UInt(32 bits))
  val aluResultD = Key(UInt(32 bits))
  val memRe      = Key(Bool)
  val memWe      = Key(Bool)
  val memBe      = Key(UInt(2 bits))
  val memEx      = Key(MU_EX())
  val memAddr    = Key(UInt(32 bits))
  val hluHiWe    = Key(Bool)
  val hluHiSrc   = Key(HLU_SRC())
  val hluLoWe    = Key(Bool)
  val hluLoSrc   = Key(HLU_SRC())
  val hluHiData  = Key(Bits(32 bits))
  val hluLoData  = Key(Bits(32 bits))
  val cp0Re      = Key(Bool)
  val cp0We      = Key(Bool)
  val rfuWe      = Key(Bool)
  val rfuAddr    = Key(UInt(5 bits))
  val rfuRdSrc   = Key(RFU_RD_SRC())
  val rfuData    = Key(Bits(32 bits))
  val useRs      = Key(Bool)
  val useRt      = Key(Bool)
  val rsValue    = Key(Bits(32 bits))
  val rtValue    = Key(Bits(32 bits))

  val ifPaddr    = Key(UInt(32 bits))
  val if2En      = Key(Bool)
  val mePaddr    = Key(UInt(32 bits))

  val tlbr       = Key(Bool)  //ID解码
  val tlbw       = Key(Bool)  //ID解码
  val tlbp       = Key(Bool)  //ID解码
  val tlbIndexSrc= Key(TLBIndexSrc())  //ID解码
  val instFetch  = Key(Bool)  //异常是否是取值时发生的

  Stage.setInputReset(if2En, False)
  Stage.setInputReset(rfuWe, False)
  Stage.setInputReset(memRe, False)
  Stage.setInputReset(memWe, False)
  Stage.setInputReset(hluHiWe, False)
  Stage.setInputReset(hluLoWe, False)
  Stage.setInputReset(cp0We, False)
  Stage.setInputReset(inst, ConstantVal.INST_NOP)
  Stage.setInputReset(eret, False)
  if(ConstantVal.USE_TLB) {
    Stage.setInputReset(tlbw, False)
    Stage.setInputReset(tlbp, False)
    Stage.setInputReset(tlbr, False)
  }

  //MMU input signal
  //目前MMU和CP0直接相连，MMU拿到的CP0寄存器值都是实时的
  //CP0拿到的MMU的rdata和probeIndex也都是实时的
  if(!ConstantVal.USE_TLB) {
    mmu.io.asid.assignDontCare()
  }else{
    mmu.io.asid := cp0.io.tlbBus.tlbwEntry.asid
    mmu.io.probeVPN2 := cp0.io.tlbBus.tlbwEntry.vpn2
    mmu.io.probeASID := cp0.io.tlbBus.tlbwEntry.asid
    mmu.io.index := cp0.io.tlbBus.index //默认是读index寄存器的值
    mmu.io.wdata := cp0.io.tlbBus.tlbwEntry

    cp0.io.tlbBus.probeIndex := mmu.io.probeIndex
    cp0.io.tlbBus.tlbrEntry := mmu.io.rdata
  }

  // Later stages may depend on earlier stages, so stages are list in reversed order.

  val WB = new Stage {
    val rfuC = new StageComponent {
      rfu.io.write.valid := input(rfuWe)
      rfu.io.write.index := input(rfuAddr)
      rfu.io.write.data := input(rfuData)
    }

    input(pc)
  }

  val ME2 = new Stage {
    val dcuC = new StageComponent {
      io.dcacheAXI <> dcu.io.axi
      io.uncacheAXI <> dcu.io.uncacheAXI
      //dbus
      dcu.io.stage2.read := input(memRe)
      dcu.io.stage2.write := input(memWe)
      dcu.io.stage2.byteEnable := input(memBe)
      dcu.io.stage2.paddr := mmu.io.dataRes.paddr
      dcu.io.stage2.extend := input(memEx)
      dcu.io.stage2.uncache := !mmu.io.dataCached
      dcu.io.stage2.wdata := input(rtValue)

      output(rfuData) := dcu.io.stage2.rdata
    }

    //TODO 是不是有点啰嗦
    produced(rfuData) := ((stored(rfuRdSrc) === RFU_RD_SRC.mu) ?
      output(rfuData) | stored(rfuData))

  }

  val ME1 = new Stage {
    val dcuC = new StageComponent {
      dcu.io.stage1.offset := input(memAddr)(0, dcacehConfig.offsetWidth bits)
      dcu.io.stage1.index := input(memAddr)(dcacehConfig.offsetWidth, dcacehConfig.indexWidth bits)
      dcu.io.stage1.keepRData := !ME2.is.empty & !ME2.will.input
      dcu.io.stage1.byteEnable := input(memBe)
    }
    // MMU translate
    mmu.io.dataVaddr := input(memAddr)

    //因为写hi lo的指令以及写cp0本身不会触发异常
    //因此全部放到ME第一阶段完成
    val hluC = new StageComponent {
      hlu.hi_we := input(hluHiWe)
      output(hluHiData) := ((input(hluHiSrc) === HLU_SRC.rs) ?
        input(rsValue) | input(aluResultC).asBits)
      hlu.new_hi := output(hluHiData)

      hlu.lo_we := input(hluLoWe)
      output(hluLoData) := ((input(hluLoSrc) === HLU_SRC.rs) ?
        input(rsValue) | input(aluResultD).asBits)
      hlu.new_lo := output(hluLoData)
    }

    val cp0C = new StageComponent {
      //CP0 write
      cp0.io.softwareWrite.valid := input(cp0We)
      cp0.io.softwareWrite.addr.rd := input(inst).rd
      cp0.io.softwareWrite.addr.sel := input(inst).sel
      cp0.io.softwareWrite.data := input(rtValue)

      cp0.io.exceptionInput.exception := exception
      cp0.io.exceptionInput.eret := input(eret)
      cp0.io.exceptionInput.memAddr := input(memAddr)
      cp0.io.exceptionInput.pc := input(pc)
      cp0.io.exceptionInput.bd := input(bd)
      cp0.io.exceptionInput.instFetch := input(instFetch)

      cp0.io.instStarting := RegNext(will.input)
      //      when(cp0.io.interruptOnNextInst && will.input) {
      //        // Causes stall to keep low
      //        willReset := True
      //      }

      cp0.io.externalInterrupt := io.externalInterrupt
    }


    //TLB相关：写MMU和写CP0的逻辑信号
    val cp0TLB = new StageComponent {
      if(ConstantVal.USE_TLB){
        //used for TLBR
        cp0.io.tlbBus.tlbr := input(tlbr)
        //used for tlbp
        cp0.io.tlbBus.tlbp := input(tlbp)
      }
    }
    val MMU = new StageComponent {
      if(ConstantVal.USE_TLB) {
        mmu.io.write := input(tlbw)
        when(input(tlbw) & input(tlbIndexSrc) === TLBIndexSrc.Random) {
          mmu.io.index := cp0.io.tlbBus.random //如果是写tlbwr那么用random
        }
      }
    }

    // 异常
    exceptionToRaise := None
    // TLB异常
    if(ConstantVal.USE_TLB){
      val refillException = mmu.io.dataMapped & mmu.io.dataRes.miss
      val invalidException = mmu.io.dataMapped & !mmu.io.dataRes.miss & !mmu.io.dataRes.valid
      val modifiedException = mmu.io.dataMapped & input(memWe) &
        !mmu.io.dataRes.miss & mmu.io.dataRes.valid & !mmu.io.dataRes.dirty
      when(refillException | invalidException) {
        when(input(memRe)) (exceptionToRaise := EXCEPTION.TLBL)
          .elsewhen(input(memWe)) (exceptionToRaise := EXCEPTION.TLBS)
      }.elsewhen(modifiedException) (exceptionToRaise := EXCEPTION.Mod)
    }
    // 访存地址不对齐异常（更优先）
    when(!dcu.io.stage1.addrValid) {
      when(input(memRe)) (exceptionToRaise := EXCEPTION.AdEL)
        .elsewhen(input(memWe))(exceptionToRaise := EXCEPTION.AdES)
    }
  }

  val EX = new Stage {
//    when(
//      ME.valid && ME.currentInput(rfuWe) &&
//        ME.currentInput(rfuAddr) === input(inst).rs
//    ) {
//      input(rsValue) := ME.currentOutput(rfuData)
//      when(ME.stalls) {
//        stalls := True
//      }
//    }
//    when(
//      ME.valid && ME.currentInput(rfuWe) &&
//        ME.currentInput(rfuAddr) === input(inst).rt
//    ) {
//      input(rtValue) := ME.currentOutput(rfuData)
//      when(ME.stalls) {
//        stalls := True
//      }
//    }
    val aluC = new StageComponent {
      alu.io.input.op := input(aluOp)
      alu.io.input.a := input(aluASrc).mux(
        ALU_A_SRC.rs -> U(input(rsValue)),
        ALU_A_SRC.sa -> input(inst).sa.resize(32)
      )
      alu.io.input.b := input(aluBSrc).mux(
        ALU_B_SRC.rt  -> U(input(rtValue)),
        ALU_B_SRC.imm -> input(inst).immExtended.asUInt
      )

      output(aluResultC) := alu.io.c
      output(aluResultD) := alu.io.d
    }

    val aguC = new StageComponent {
      output(memAddr) := U(S(input(rsValue)) + input(inst).offset)
    }

    val cp0Read = new StageComponent {
      cp0.io.read.addr.rd := input(inst).rd
      cp0.io.read.addr.sel := input(inst).sel
      output(rfuData) := cp0.io.read.data
    }

    produced(rfuData) := stored(rfuRdSrc).mux(
      RFU_RD_SRC.alu -> produced(aluResultC).asBits,
      RFU_RD_SRC.hi  -> (ME1.stored(hluHiWe) ? ME1.produced(hluHiData) | hlu.hi_v),
      RFU_RD_SRC.lo  -> (ME1.stored(hluLoWe) ? ME1.produced(hluLoData) | hlu.lo_v),
      RFU_RD_SRC.cp0 -> cp0Read.output(rfuData),
      default        -> stored(rfuData)
    )

    // CP0 harzard : 根据ME1的情况暂停EX阶段
    val cp0_RAW_hazard = new Area {
      //当前指令mfc0，上一条指令mtc0 | tlbp | tlbr ，则需要暂停一个周期
      val mfc0_mtc0_hazard:Bool = !ME1.is.empty & input(cp0Re) & ME1.stored(cp0We) &
        input(inst).rd === ME1.stored(inst).rd &
        input(inst).sel === ME1.stored(inst).sel

      val mfc0_tlb_hazard = if(ConstantVal.USE_TLB) Bool else null
      if(ConstantVal.USE_TLB) {
        mfc0_tlb_hazard := !ME1.is.empty & input(cp0Re) &
          (
            (ME1.stored(tlbp) & input(inst).rd === 0 & input(inst).sel === 0) |
              (ME1.stored(tlbr) & Utils.equalAny(input(inst).rd, U(2), U(3), U(5), U(10)) & input(inst).sel === 0)
            )
      }
    }

    exceptionToRaise := alu.io.exception
  }


  val ID = new Stage {
    val duC = new StageComponent {
      du.io.inst := input(inst)

      output(aluOp) := du.io.alu_op
      output(aluASrc) := du.io.alu_a_src
      output(aluBSrc) := du.io.alu_b_src
      output(memRe) := du.io.dcu_re
      output(memWe) := du.io.dcu_we
      output(memBe) := du.io.dcu_be
      output(memEx) := du.io.mu_ex
      output(hluHiWe) := du.io.hlu_hi_we
      output(hluHiSrc) := du.io.hlu_hi_src
      output(hluLoWe) := du.io.hlu_lo_we
      output(hluLoSrc) := du.io.hlu_lo_src
      output(cp0Re) := du.io.cp0_re
      output(cp0We) := du.io.cp0_we
      output(rfuWe) := du.io.rfu_we
      output(rfuAddr) := du.io.rfu_rd
      output(rfuRdSrc) := du.io.rfu_rd_src
      output(useRs) := du.io.use_rs
      output(useRt) := du.io.use_rt
      output(eret) := du.io.eret
      if (ConstantVal.USE_TLB) {
        output(tlbr) := du.io.tlbr
        output(tlbw) := du.io.tlbw
        output(tlbp) := du.io.tlbp
        output(tlbIndexSrc) := du.io.tlbIndexSrc
      }
    }

    val rfuRead = new StageComponent {
      rfu.io.ra.index := input(inst).rs
      rfu.io.rb.index := input(inst).rt
      output(rsValue) := rfu.io.ra.data
      output(rtValue) := rfu.io.rb.data
    }

//    val cp0Read = new StageComponent {
//      cp0.io.read.addr.rd := input(inst).rd
//      cp0.io.read.addr.sel := input(inst).sel
//    output(rfuData) := cp0.io.read.data
    //    }

    val juC = new StageComponent {
      ju.op := du.io.ju_op
      ju.a := produced(rsValue).asSInt
      ju.b := produced(rtValue).asSInt
      ju.pc_src := du.io.ju_pc_src
      ju.pc := input(pc) + 4
      ju.offset := input(inst).offset
      ju.index := input(inst).index

      when(ju.jump) {
        output(jumpPc) := ju.jump_pc
      } otherwise {
        output(jumpPc) := None
      }
    }

    val bdValue = RegInit(False)
    when(will.output) {
      bdValue := du.io.ju_op =/= JU_OP.f
    }
    when(will.flush) {
      bdValue := False
    }
    output(bd) := bdValue

    when(EX.stored(rfuWe) && EX.stored(rfuAddr) === stored(inst).rs) {
      produced(rsValue) := EX.produced(rfuData)
    } elsewhen (ME1.stored(rfuWe) && ME1.stored(rfuAddr) === stored(inst).rs) {
      produced(rsValue) := ME1.produced(rfuData)
    }elsewhen(ME2.stored(rfuWe) && ME2.stored(rfuAddr) === stored(inst).rs) {
      produced(rsValue) := ME2.produced(rfuData)
    } elsewhen (WB.stored(rfuWe) && WB.stored(rfuAddr) === stored(inst).rs) {
      produced(rsValue) := WB.stored(rfuData)
    }

    when(EX.stored(rfuWe) && EX.stored(rfuAddr) === stored(inst).rt) {
      produced(rtValue) := EX.produced(rfuData)
    } elsewhen (ME1.stored(rfuWe) && ME1.stored(rfuAddr) === stored(inst).rt) {
      produced(rtValue) := ME1.produced(rfuData)
    }elsewhen (ME2.stored(rfuWe) && ME2.stored(rfuAddr) === stored(inst).rt){
      produced(rtValue) := ME2.produced(rfuData)
    } elsewhen (WB.stored(rfuWe) && WB.stored(rfuAddr) === stored(inst).rt) {
      produced(rtValue) := WB.stored(rfuData)
    }


//    produced(rfuData) := output(rfuRdSrc) mux (
//      RFU_RD_SRC.cp0 -> cp0Read.output(rfuData),
//      default        -> B(input(pc) + 8)
//    )
    output(rfuData) := B(input(pc) + 8)

    val wantForwardFromEX =
      EX.stored(rfuWe) && (EX.stored(rfuAddr) === stored(inst).rs && produced(useRs) ||
        EX.stored(rfuAddr) === stored(inst).rt && produced(useRt))
    val wantForwardFromME1 =
      ME1.stored(rfuWe) && (ME1.stored(rfuAddr) === stored(inst).rs && produced(useRs) ||
        ME1.stored(rfuAddr) === stored(inst).rt && produced(useRt))
    val wantForwardFromME2 =
      ME2.stored(rfuWe) && (ME2.stored(rfuAddr) === stored(inst).rs && produced(useRs) ||
        ME2.stored(rfuAddr) === stored(inst).rt && produced(useRt))

    exceptionToRaise := du.io.exception
  }

  val IF2 = new Stage {
    val icuC = new StageComponent {
      io.icacheAXI <> icu.io.axi
      //ibus
      icu.io.ibus.stage2.paddr := input(ifPaddr)
//      icu.io.ibus.stage2.en := prevException.isEmpty & firing
      icu.io.ibus.stage2.en := input(if2En)
//      produced(inst) := input(if2En) ? icu.io.ibus.stage2.rdata | ConstantVal.INST_NOP
      output(inst) := icu.io.ibus.stage2.rdata
    }
  }

  val IF1 = new Stage {

    val toJump = Reg(Optional(UInt(32 bits)))
    val toSet  = Reg(Optional(UInt(32 bits)))
    stored(pc) init ConstantVal.INIT_PC
    when(will.output) {
      stored(pc) := stored(pc) + 4
      toJump.whenIsDefined(v => stored(pc) := v)
      when(ID.is.done) {
        ID.output(jumpPc).whenIsDefined(v => stored(pc) := v)
      }
      toSet.whenIsDefined(v => stored(pc) := v)
      when(ME1.is.done) {
        cp0.io.jumpPc.whenIsDefined(v => stored(pc) := v)
      }
      toJump := None
      toSet := None
    } otherwise {
      when(ID.is.done) {
        ID.output(jumpPc).whenIsDefined(v => toJump := v)
      }
      when(ME1.is.done) {
        cp0.io.jumpPc.whenIsDefined(v => toSet := v)
      }
    }

    val icuC = new StageComponent {
      //ibus
      icu.io.offset := input(pc)(0, icacehConfig.offsetWidth bits)
      icu.io.ibus.stage1.read := True
      icu.io.ibus.stage1.index := input(pc)(icacehConfig.offsetWidth, icacehConfig.indexWidth bits)

      // MMU Translate
      mmu.io.instVaddr := input(pc)
      output(ifPaddr) := mmu.io.instRes.paddr
      output(if2En) := True

//      icu.io.ibus.stage1.keepRData := IF2.valid & !IF2.sending & !IF2.wantsFlush
      icu.io.ibus.stage1.keepRData := !IF2.is.empty & !IF2.will.input

      // 异常
      output(instFetch) := False  //默认取指没有产生异常
      exceptionToRaise := None
      // TLB异常
      if(ConstantVal.USE_TLB){
        val refillException = mmu.io.instMapped & mmu.io.instRes.miss
        val invalidException = mmu.io.instMapped & !mmu.io.instRes.miss & !mmu.io.instRes.valid
        when(refillException | invalidException) {
          exceptionToRaise := EXCEPTION.TLBL
          output(instFetch) := True
          output(if2En) := False
        }
      }
      // 访存地址不对齐异常（更优先）
      icu.io.exception.whenIsDefined ( _ => {
        exceptionToRaise := icu.io.exception
        output(instFetch) := True
        output(if2En) := False
      })
    }
  }


  val stages = Seq(IF1, IF2, ID, EX, ME1, ME2, WB)
  for ((prev, next) <- (stages zip stages.tail).reverse) {
    prev connect next
  }

  IF1.prevException := None
  IF2.is.done := !icu.io.ibus.stage2.stall
  IF2.can.flush := !icu.io.ibus.stage2.stall

  ID.is.done :=
    !(ID.wantForwardFromEX && EX.stored(rfuRdSrc) === RFU_RD_SRC.mu ||
      ID.wantForwardFromME1 && ME1.stored(rfuRdSrc) === RFU_RD_SRC.mu && !ME1.is.done ||
      ID.wantForwardFromME2 && ME2.stored(rfuRdSrc) === RFU_RD_SRC.mu && !ME2.is.done)

  EX.is.done := !alu.io.stall
  EX.can.flush := !alu.io.stall

  when(EX.cp0_RAW_hazard.mfc0_mtc0_hazard) {
    EX.is.done := False
  }
  if(ConstantVal.USE_TLB) {
    when(EX.cp0_RAW_hazard.mfc0_tlb_hazard) {
      EX.is.done := False
    }
  }

  ME2.is.done := !dcu.io.stage2.stall
  ME2.can.flush := !dcu.io.stage2.stall

  // 异常清空流水线
  when(cp0.io.jumpPc.isDefined && !ME1.to.flush) {
    ME1.want.flush := True
    IF1.want.flush := True
    IF2.want.flush := True
    ID.want.flush := True
    EX.want.flush := True
  }

  // 一旦检测到跳转就让给IF2的指令无效
  when(ID.is.done & ID.output(jumpPc).isDefined) {
    IF1.want.flush := True
  }
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}
