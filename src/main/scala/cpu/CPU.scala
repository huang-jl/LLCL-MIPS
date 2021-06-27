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
    val debug             = out(DebugInterface())
  }
  val icu = new ICU(CacheRamConfig(blockSize = ConstantVal.IcacheLineSize,
    indexWidth = ConstantVal.IcacheIndexWIdth, wayNum = ConstantVal.IcacheWayNum, sim = ConstantVal.SIM))
  val du  = new DU
  val ju  = new JU
  val alu = new ALU
  val dcu = new DCU(CacheRamConfig(blockSize = ConstantVal.DcacheLineSize,
    indexWidth = ConstantVal.DcacheIndexWIdth, wayNum = ConstantVal.DcacheWayNum, sim = ConstantVal.SIM),
    fifoDepth = ConstantVal.DcacheFifoDepth)
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

  val tlbr       = Key(Bool)  //ID解码
  val tlbw       = Key(Bool)  //ID解码
  val tlbp       = Key(Bool)  //ID解码
  val tlbIndexSrc= Key(TLBIndexSrc())  //ID解码
  val instFetch  = Key(Bool)  //异常是否是取值时发生的

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
      io.debug.wb.rf.wen := B(4 bits, default -> rfu.io.write.valid)
      io.debug.wb.rf.wdata := rfu.io.write.data
      io.debug.wb.rf.wnum := rfu.io.write.index

      rfu.io.write.valid := input(rfuWe)
      rfu.io.write.index := input(rfuAddr)
      rfu.io.write.data := input(rfuData)
      setInputReset(rfuWe, False)
    }

    val pcDebug = new StageComponent {
      io.debug.wb.pc := input(pc)
    }
  }

  val ME = new Stage {
    val dcuC = new StageComponent {
      io.dcacheAXI <> dcu.io.axi
      io.uncacheAXI <> dcu.io.uncacheAXI
      //dbus
      dcu.io.read := input(memRe)
      dcu.io.write := input(memWe)
      dcu.io.byteEnable := input(memBe)
      dcu.io.extend := input(memEx)
      dcu.io.addr := mmu.io.dataRes.paddr
      dcu.io.uncache := !mmu.io.dataCached
      dcu.io.wdata := input(rtValue)
      // MMU translate
      mmu.io.dataVaddr := input(memAddr)
      setInputReset(memRe, False)
      setInputReset(memWe, False)

      when(dcu.io.stall) {
        stalls := True
        flushable := False
      }

      produced(rfuData) := dcu.io.rdata
    }

    val hluC = new StageComponent {
      hlu.hi_we := input(hluHiWe)
      produced(hluHiData) := ((input(hluHiSrc) === HLU_SRC.rs)
        ? input(rsValue)
        | input(aluResultC).asBits)
      hlu.new_hi := produced(hluHiData)
      hlu.lo_we := input(hluLoWe)
      produced(hluLoData) := ((input(hluLoSrc) === HLU_SRC.rs)
        ? input(rsValue)
        | input(aluResultD).asBits)
      hlu.new_lo := produced(hluLoData)

      setInputReset(hluHiWe, False)
      setInputReset(hluLoWe, False)
    }

    val cp0C = new StageComponent {
      //CP0 write
      cp0.io.softwareWrite.valid := input(cp0We)
      cp0.io.softwareWrite.addr.rd := input(inst).rd
      cp0.io.softwareWrite.addr.sel := input(inst).sel
      cp0.io.softwareWrite.data := input(rtValue)
      setInputReset(cp0We, False)

      cp0.io.exceptionInput.exception := exception.next
      cp0.io.exceptionInput.eret := input(eret)
      cp0.io.exceptionInput.memAddr := input(memAddr)
      cp0.io.exceptionInput.pc := input(pc)
      cp0.io.exceptionInput.bd := input(bd)
      cp0.io.exceptionInput.instFetch := input(instFetch)

      cp0.io.instStarting := RegNext(receiving)
      when(cp0.io.interruptOnNextInst && receiving) {
        // Causes stall to keep low
        willReset := True
      }

      cp0.io.externalInterrupt := io.externalInterrupt
      when(valid && cp0.io.jumpPc.isDefined) {
        signalsFlush := True
      }
    }

    //TODO 是不是有点啰嗦
    output(rfuData) := ((input(rfuRdSrc) === RFU_RD_SRC.mu)
      ? produced(rfuData)
      | input(rfuData))

      //只包括写MMU和写CP0的逻辑信号
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
    when(dcu.io.exception.nonEmpty) {
      exceptionToRaise := dcu.io.exception
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
    when(alu.io.stall) {
      stalls := True
      flushable := False
    }

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

      produced(aluResultC) := alu.io.c
      produced(aluResultD) := alu.io.d
      exceptionToRaise := alu.io.exception
    }

    val aguC = new StageComponent {
      produced(memAddr) := U(S(input(rsValue)) + input(inst).offset)
    }

    val cp0Read = new StageComponent {
      cp0.io.read.addr.rd := input(inst).rd
      cp0.io.read.addr.sel := input(inst).sel
      produced(rfuData) := cp0.io.read.data
    }

    produced(rfuData) := input(rfuRdSrc) mux (
      RFU_RD_SRC.alu -> produced(aluResultC).asBits,
      RFU_RD_SRC.hi -> ((ME.valid && ME.currentInput(hluHiWe))
        ? ME.currentOutput(hluHiData)
        | hlu.hi_v),
      RFU_RD_SRC.lo -> ((ME.valid && ME.currentInput(hluLoWe))
        ? ME.currentOutput(hluLoData)
        | hlu.lo_v),
      RFU_RD_SRC.cp0 -> cp0Read.produced(rfuData),
      default -> input(rfuData)
    )

    val cp0_RAW_harzard = new Area {
      //当前指令mfc0，上一条指令mtc0 | tlbp | tlbr ，则需要暂停一个周期
      val mfc0_mtc0_harzard = input(cp0Re) & ME.currentInput(cp0We) &
        input(inst).rd === ME.currentInput(inst).rd &
        input(inst).sel === ME.currentInput(inst).sel
      val mfc0_tlb_harzard = input(cp0Re) &
        (
          (ME.currentInput(tlbp) & input(inst).rd === 0 & input(inst).sel === 0) |
            (ME.currentInput(tlbr) & Utils.equalAny(input(inst).rd, U(2), U(3), U(5), U(10)) & input(inst).sel === 0)
          )
      when(ME.valid & (mfc0_mtc0_harzard | mfc0_tlb_harzard)) {
        stalls := True
      }
    }
  }

  val ID = new Stage {
    val duC = new StageComponent {
      du.io.inst := input(inst)
      setInputReset(inst, ConstantVal.INST_NOP)

      produced(aluOp) := du.io.alu_op
      produced(aluASrc) := du.io.alu_a_src
      produced(aluBSrc) := du.io.alu_b_src
      produced(memRe) := du.io.dcu_re
      produced(memWe) := du.io.dcu_we
      produced(memBe) := du.io.dcu_be
      produced(memEx) := du.io.mu_ex
      produced(hluHiWe) := du.io.hlu_hi_we
      produced(hluHiSrc) := du.io.hlu_hi_src
      produced(hluLoWe) := du.io.hlu_lo_we
      produced(hluLoSrc) := du.io.hlu_lo_src
      produced(cp0Re) := du.io.cp0_re
      produced(cp0We) := du.io.cp0_we
      produced(rfuWe) := du.io.rfu_we
      produced(rfuAddr) := du.io.rfu_rd
      produced(rfuRdSrc) := du.io.rfu_rd_src
      produced(useRs) := du.io.use_rs
      produced(useRt) := du.io.use_rt
      produced(eret) := du.io.eret
      if(ConstantVal.USE_TLB){
        produced(tlbr) := du.io.tlbr
        produced(tlbw) := du.io.tlbw
        produced(tlbp) := du.io.tlbp
        produced(tlbIndexSrc) := du.io.tlbIndexSrc
      }
      exceptionToRaise := du.io.exception
    }

    val rfuRead = new StageComponent {
      rfu.io.ra.index := input(inst).rs
      rfu.io.rb.index := input(inst).rt
      produced(rsValue) := rfu.io.ra.data
      produced(rtValue) := rfu.io.rb.data
    }

//    val cp0Read = new StageComponent {
//      cp0.io.read.addr.rd := input(inst).rd
//      cp0.io.read.addr.sel := input(inst).sel
//      produced(rfuData) := cp0.io.read.data
//    }

    val juC = new StageComponent {
      ju.op := du.io.ju_op
      ju.a := output(rsValue).asSInt
      ju.b := output(rtValue).asSInt
      ju.pc_src := du.io.ju_pc_src
      ju.pc := input(pc) + 4
      ju.offset := input(inst).offset
      ju.index := input(inst).index

      when(ju.jump) {
        produced(jumpPc) := ju.jump_pc
      } otherwise {
        produced(jumpPc) := None
      }
    }

    val bdValue = Reg(Bool) init False
    when(finishing) {
      bdValue := du.io.ju_op =/= JU_OP.f
    }
    when(wantsFlush) {
      bdValue := False
    }
    produced(bd) := bdValue

    when(
      EX.valid && EX.currentInput(rfuWe) &&
        EX.currentInput(rfuAddr) === input(inst).rs
    ) {
      when(EX.stalls) (stalls := True)
      output(rsValue) := EX.currentOutput(rfuData)
    } elsewhen (ME.valid && ME.currentInput(rfuWe) &&
      ME.currentInput(rfuAddr) === input(inst).rs) {
      when(ME.stalls) (stalls := True)
      output(rsValue) := ME.currentOutput(rfuData)
    } elsewhen (WB.valid && WB.currentInput(rfuWe) &&
      WB.currentInput(rfuAddr) === input(inst).rs) {
      output(rsValue) := WB.currentInput(rfuData)
    } otherwise {
      output(rsValue) := produced(rsValue)
    }

    when(
      EX.valid && EX.currentInput(rfuWe) &&
        EX.currentInput(rfuAddr) === input(inst).rt
    ) {
      when(EX.stalls) (stalls := True)
      output(rtValue) := EX.currentOutput(rfuData)
    } elsewhen (ME.valid && ME.currentInput(rfuWe) &&
      ME.currentInput(rfuAddr) === input(inst).rt) {
      when(ME.stalls) (stalls := True)
      output(rtValue) := ME.currentOutput(rfuData)
    } elsewhen (WB.valid && WB.currentInput(rfuWe) &&
      WB.currentInput(rfuAddr) === input(inst).rt) {
      output(rtValue) := WB.currentInput(rfuData)
    } otherwise {
      output(rtValue) := produced(rtValue)
    }


//    produced(rfuData) := produced(rfuRdSrc) mux (
//      RFU_RD_SRC.cp0 -> cp0Read.produced(rfuData),
//      default        -> B(input(pc) + 8)
//    )
    produced(rfuData) := B(input(pc) + 8)

    when(
      EX.valid && EX.currentInput(rfuWe) &&
        EX.currentInput(rfuRdSrc) === RFU_RD_SRC.mu &&
        (EX.currentInput(rfuAddr) === input(inst).rs && produced(useRs) ||
          EX.currentInput(rfuAddr) === input(inst).rt && produced(useRt))
    ) {
      stalls := True
    }

    when(
      du.io.ju_op =/= JU_OP.f && ME.valid && ME.stalls &&
        ME.currentInput(rfuWe) && ME.currentInput(rfuRdSrc) === RFU_RD_SRC.mu &&
        (ME.currentInput(rfuAddr) === input(inst).rs && produced(useRs) ||
          ME.currentInput(rfuAddr) === input(inst).rt && produced(useRt))
    ) {
      stalls := True
    }

  }

  val IF = new Stage {
    val icuC = new StageComponent {
      io.icacheAXI <> icu.io.axi
      //ibus
      icu.io.ibus.addr := mmu.io.instRes.paddr
      icu.io.ibus.read := True
      // MMU Translate
      mmu.io.instVaddr := input(pc)
      when(icu.io.ibus.stall) {
        stalls := True
        flushable := False
      }

      produced(inst) := icu.io.ibus.data
      // 异常
      produced(instFetch) := False  //默认取值没有产生异常
      exceptionToRaise := None
      // TLB异常
      if(ConstantVal.USE_TLB){
        val refillException = mmu.io.instMapped & mmu.io.instRes.miss
        val invalidException = mmu.io.instMapped & !mmu.io.instRes.miss & !mmu.io.instRes.valid
        when(refillException | invalidException) {
          exceptionToRaise := EXCEPTION.TLBL
          produced(instFetch) := True
        }
      }
      // 访存地址不对齐异常（更优先）
      when(icu.io.exception.nonEmpty) {
        exceptionToRaise := icu.io.exception
        produced(instFetch) := True
      }

    }
  }

  // Pseudo-stage for producing PCU.
  val PC = new Stage {
    val pcValue = Updating(UInt(32 bits)) init ConstantVal.INIT_PC

    val wasSending = RegNext(sending) init False
    when(wasSending) { // Starting a new instruction
      pcValue.next := IF.currentInput(pc) + 4
    }
    when(ID.valid) {
      ID.currentOutput(jumpPc).whenIsDefined { jumpPc =>
        pcValue.next := jumpPc
      }
    }
    when(ME.valid) {
      cp0.io.jumpPc.whenIsDefined { pc =>
        pcValue.next := pc
      }
    }

    produced(pc) := pcValue.next
  }

  val stages = Seq(PC, IF, ID, EX, ME, WB)
  for ((prev, next) <- (stages zip stages.tail).reverse) {
    prev connect next
  }

  PC.signalsFlush.allowOverride
  PC.signalsFlush := False
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}
