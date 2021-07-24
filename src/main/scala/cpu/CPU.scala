package cpu

import defs.Mips32InstImplicits._
import defs.{ConstantVal, Mips32Inst}
import cache.{CPUDCacheInterface, CPUICacheInterface, CacheRamConfig, DCache, ICache}
import lib.{Key, Task, Updating}
import tlb.{MMU, MMUTranslationRes}
import cpu.defs.Config._
import ip._
import spinal.core._
import spinal.lib.{cpu => _, _}
import spinal.lib.bus.amba4.axi._

import scala.language.postfixOps

class CPU extends Component {
  val io = new Bundle {
    val externalInterrupt = in Bits (5 bits)
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

  val icu  = new ICU
  val du   = new DU
  val ju   = new JU
  val alu  = new ALU
  val dcu1 = new DCU1
  val dcu2 = new DCU2
  val hlu  = new HLU
  val rfu  = new RFU
  val cp0  = new CP0
  val mmu  = new MMU

  val btb = new Cache(
    BTB.NUM_ENTRIES,
    BTB.NUM_WAYS,
    BTB.ADDR_WIDTH,
    0,
    BTB.INDEX_WIDTH,
    BTB.TAG_OFFSET,
    BTB.TAG_WIDTH,
    BTB.ADDR_WIDTH
  )
  btb.io.w.en.clear
  btb.io.w.pEn.clear
  val bpu = new BPU
  bpu.io.w.bhtEn.clear
  bpu.io.w.phtEn.clear

  val bhtI = Key(UInt(BHT.INDEX_WIDTH bits))
  val bhtV = Key(Bits(BHT.DATA_WIDTH bits))
  val phtI = Key(UInt(PHT.INDEX_WIDTH bits))
  val phtV = Key(UInt(2 bits))

  val btbDataLine = Key(Bits(BTB.NUM_WAYS * BTB.ADDR_WIDTH bits))
  val btbData     = Key(Bits(BTB.ADDR_WIDTH bits))
  val btbTagLine  = Key(Bits(BTB.NUM_WAYS * (1 + BTB.TAG_WIDTH) bits))
  val btbTagLine_ = Key(Bits(BTB.NUM_WAYS * (1 + BTB.TAG_WIDTH) bits))
  val btbP        = Key(Bits(BTB.NUM_WAYS - 1 bits))
  val btbHit      = Key(Bool()) setEmptyValue False
  val btbSetP     = Key(Bits(BTB.NUM_WAYS - 1 bits))
  val btbSetP_    = Key(Bits(BTB.NUM_WAYS - 1 bits))

  val isDJump  = Key(Bool()) setEmptyValue False
  val jumpCond = Key(JU_OP()) setEmptyValue JU_OP.f
  val wantJump = Key(Bool()) setEmptyValue False
  val jumpPC   = Key(UInt(ADDR_WIDTH bits))

  val pc             = Key(UInt(32 bits))
  val pcPlus4        = Key(UInt(32 bits))
  val bd             = Key(Bool)
  val inst           = Key(Mips32Inst()) setEmptyValue ConstantVal.INST_NOP
  val instFetchError = Key(Bool)
  val eret           = Key(Bool) setEmptyValue False
  val aluOp          = Key(ALU_OP()) setEmptyValue ALU_OP.sll()
  val aluASrc        = Key(ALU_A_SRC())
  val aluBSrc        = Key(ALU_B_SRC())
  val aluResultC     = Key(UInt(32 bits))
  val aluResultD     = Key(UInt(32 bits))
  val memRe          = Key(Bool) setEmptyValue False
  val memWe          = Key(Bool) setEmptyValue False
  val memBe          = Key(UInt(2 bits))
  val memEx          = Key(MU_EX())
  val byteEnable     = Key(Bits(4 bits)) //访存时的byteEnable
  val memAddr        = Key(UInt(32 bits))
  val hluHiWe        = Key(Bool) setEmptyValue False
  val hluHiSrc       = Key(HLU_SRC())
  val hluLoWe        = Key(Bool) setEmptyValue False
  val hluLoSrc       = Key(HLU_SRC())
  val hluHiData      = Key(Bits(32 bits))
  val hluLoData      = Key(Bits(32 bits))
  val cp0Re          = Key(Bool) setEmptyValue False
  val cp0We          = Key(Bool) setEmptyValue False
  val rfuWe          = Key(Bool) setEmptyValue False
  val rfuAddr        = Key(UInt(5 bits))
  val rfuRdSrc       = Key(RFU_RD_SRC())
  val rfuData        = Key(Bits(32 bits))
  val useRs          = Key(Bool)
  val useRt          = Key(Bool)
  val rsValue        = Key(Bits(32 bits))
  val rtValue        = Key(Bits(32 bits))

  val ifPaddr = Key(UInt(32 bits))
  val if2En   = Key(Bool) setEmptyValue False

  val fuck      = Key(Bool) setEmptyValue False //类似特权级的指令，会暂停整个流水线
  val privilege = Key(Bool) setEmptyValue False //是否是COP0特权指令

  val dataMMURes         = Key(new MMUTranslationRes(ConstantVal.FINAL_MODE)) //EX阶段查询数据TLB
  val tlbr               = Key(Bool) setEmptyValue (False)                    //ID解码
  val tlbw               = Key(Bool) setEmptyValue (False)                    //ID解码
  val tlbp               = Key(Bool) setEmptyValue (False)                    //ID解码
  val tlbIndexSrc        = Key(TLBIndexSrc())                                 //ID解码
  val tlbRefillException = Key(Bool)                                          //是否是TLB缺失异常（影响异常处理地址）
  val isTrap             = Key(Bool) setEmptyValue False

  val invalidateICache = Key(Bool) setEmptyValue False
  val invalidateDCache = Key(Bool) setEmptyValue False

  val meType = Key(ME_TYPE())

  val compareOp    = Key(CompareOp())
  val conditionMov = Key(Bool)

  //MMU input signal
  //目前MMU和CP0直接相连，MMU拿到的CP0寄存器值都是实时的
  //CP0拿到的MMU的rdata和probeIndex也都是实时的
  if (!ConstantVal.FINAL_MODE) {
    mmu.io.asid.assignDontCare()
  } else {
    mmu.io.asid := cp0.io.tlbBus.tlbwEntry.asid
    mmu.io.probeVPN2 := cp0.io.tlbBus.tlbwEntry.vpn2
    mmu.io.probeASID := cp0.io.tlbBus.tlbwEntry.asid
    mmu.io.index := cp0.io.tlbBus.index //默认是读index寄存器的值
    mmu.io.wdata := cp0.io.tlbBus.tlbwEntry
    mmu.io.K0 := cp0.io.machineState.K0
    mmu.io.ERL := cp0.io.machineState.ERL
    mmu.io.userMode := cp0.io.machineState.userMode

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
    input(fuck)
  }

  val ME2 = new Stage {
    val dcu2C = new StageComponent {
      dcu2.io.input.byteEnable := input(byteEnable)
      dcu2.io.input.extend := input(memEx)
      dcu2.io.input.wdata := input(rtValue)
      dcu2.io.input.rdata := dbus.stage2.rdata
      if (ConstantVal.FINAL_MODE) {
        dcu2.io.input.meType := input(meType)
        dcu2.io.input.byteOffset := input(dataMMURes).paddr(0, 2 bits)
      }

      dbus.stage2.read := input(memRe)
      dbus.stage2.write := input(memWe)
      dbus.stage2.paddr := input(dataMMURes).paddr
      dbus.stage2.byteEnable := input(byteEnable)
      dbus.stage2.wdata := dcu2.io.output.wdata
      dbus.stage2.uncache := !input(dataMMURes).cached

      output(rfuData) := dcu2.io.output.rdata
    }

    // 在这个阶段清理cache
    ibus.invalidate.en := input(invalidateICache)
    ibus.invalidate.addr := input(dataMMURes).paddr
    dbus.invalidate.en := input(invalidateDCache)

    produced(rfuData) := stored(memRe) ? output(rfuData) | stored(rfuData)

  }

  val ME1 = new Stage {
    val assignSetTask =
      !RegNext(cp0.io.exceptionBus.jumpPc.isDefined) & cp0.io.exceptionBus.jumpPc.isDefined

    val dcu1C = new StageComponent {
      dcu1.io.input.paddr := input(dataMMURes).paddr
      dcu1.io.input.byteEnable := input(memBe)
      if (ConstantVal.FINAL_MODE) {
        dcu1.io.input.meType := input(meType)
      }
      output(byteEnable) := dcu1.io.output.byteEnable

      dbus.stage1.keepRData := !ME2.is.empty & !ME2.will.input
      dbus.stage1.paddr := input(dataMMURes).paddr

      if(!ConstantVal.FINAL_MODE) {
        output(memRe) := input(dataMMURes).paddr =/= U"32'h1faf_fff0" & input(memRe)
        output(memWe) := input(dataMMURes).paddr =/= U"32'h1faf_fff0" & input(memWe)
      }
    }

    //因为写hi lo的指令以及写cp0本身不会触发异常
    //因此全部放到ME第一阶段完成
    val hluC = new StageComponent {
      hlu.hi_we := input(hluHiWe)
      output(hluHiData) := (input(hluHiSrc) === HLU_SRC.rs) ?
        input(rsValue) | input(aluResultC).asBits
      hlu.new_hi := output(hluHiData)

      hlu.lo_we := input(hluLoWe)
      output(hluLoData) := (input(hluLoSrc) === HLU_SRC.rs) ?
        input(rsValue) | input(aluResultD).asBits
      hlu.new_lo := output(hluLoData)
    }

    val cp0C = new StageComponent {
      val cp0WeLogic = new Area {
        val curr = new Bundle {
          val we = Bool
          val rd = UInt(5 bits)
          val sel = UInt(3 bits)
        }
        val prev = new Bundle {
          val we = RegNext(curr.we)
          val rd = RegNext(curr.rd)
          val sel = RegNext(curr.sel)
        }
        when(will.output) {
          curr.we := input(cp0We)
          curr.rd := input(inst).rd
          curr.sel := input(inst).sel
        }.otherwise {
          curr.assignAllByName(prev)
        }
      }
      //CP0 write
      cp0.io.softwareWrite.valid := cp0WeLogic.curr.we & will.input
      cp0.io.softwareWrite.addr.rd := cp0WeLogic.curr.rd
      cp0.io.softwareWrite.addr.sel := cp0WeLogic.curr.sel
      cp0.io.softwareWrite.data := input(rtValue)

      cp0.io.exceptionBus.exception.excCode := exception
      cp0.io.exceptionBus.exception.instFetch := input(instFetchError)
      cp0.io.exceptionBus.eret := input(eret)
      cp0.io.exceptionBus.vaddr := input(memAddr)
      cp0.io.exceptionBus.pc := input(pc)
      cp0.io.exceptionBus.bd := input(bd)

      cp0.io.externalInterrupt := io.externalInterrupt
    }

    //数据前传：解决先load 后store的数据冲突
    when(ME2.stored(rfuWe) && ME2.stored(rfuAddr) === input(inst).rs & ME2.stored(memRe)) {
      produced(rsValue) := ME2.produced(rfuData)
    }
    when(ME2.stored(rfuWe) && ME2.stored(rfuAddr) === input(inst).rt & ME2.stored(memRe)) {
      produced(rtValue) := ME2.produced(rfuData)
    }

    // 异常
    exceptionToRaise := None
    // TLB异常
    val refillException = input(dataMMURes).tlbRefillException
    val invalidException =
      input(dataMMURes).tlbInvalidException
    val modifiedException = input(dataMMURes).tlbModException & input(memWe)
    when(refillException | invalidException) {
      when(input(memRe))(exceptionToRaise := ExcCode.loadTLBError)
        .elsewhen(input(memWe))(exceptionToRaise := ExcCode.storeTLBError)
    }.elsewhen(modifiedException)(exceptionToRaise := ExcCode.tlbModError)
    // 当memRe或memWe时才会由本阶段触发refillException
    cp0.io.exceptionBus.exception.tlbRefill := input(tlbRefillException) |
      (refillException & (input(memRe) | input(memWe)))

    // 访存地址异常（更优先）
    //访问权限不够的异常 或 引起地址异常的错误
    // Cache指令不允许引起异常
    when(
      input(dataMMURes).illegal |
        (!dcu1.io.output.addrValid & !input(invalidateDCache) & !input(invalidateICache))
    ) {
      when(input(memRe))(exceptionToRaise := ExcCode.loadAddrError)
        .elsewhen(input(memWe))(exceptionToRaise := ExcCode.storeAddrError)
    }
    if (ConstantVal.FINAL_MODE) {
      // Trap异常，当aluResultC的第0位为1时发生
      when(input(isTrap) && input(aluResultC)(0)) {
        exceptionToRaise := ExcCode.trapError
      }
      // TLB
      cp0.io.tlbBus.tlbr := input(tlbr)
      cp0.io.tlbBus.tlbp := input(tlbp)
      // MMU
      mmu.io.write := input(tlbw)
      when(input(tlbw) & input(tlbIndexSrc) === TLBIndexSrc.Random) {
        cp0.io.tlbBus.tlbwr := True
        mmu.io.index := cp0.io.tlbBus.random //如果是写tlbwr那么用random
      }.otherwise(cp0.io.tlbBus.tlbwr := False)
      // 权限检查：当遇到COP0寄存器 & 是用户态 & Status.cu0 === 0的时候发生异常
      when(input(privilege) & cp0.io.machineState.userMode & !cp0.io.machineState.cu0) {
        exceptionToRaise := ExcCode.copUnusable
      }
    }
  }

  val EX = new Stage {
    val writeBTB = new StageComponent {
      btb.io.w.addr := stored(pc)(BTB.ADDR_RANGE)
      btb.io.w.dataLine := stored(btbDataLine)
      btb.io.w.tagLine := stored(btbTagLine)
      btb.io.w.p := stored(btbSetP)

      bpu.io.w.bhtI := stored(bhtI)
      bpu.io.w.bhtV := stored(bhtV) |<< 1 | B(ju.jump, BHT.DATA_WIDTH bits)
      bpu.io.w.phtI := stored(phtI)
      bpu.io.w.phtV := ju.jump ?
        ((stored(phtV) === stored(phtV).maxValue) ? U(stored(phtV).maxValue) | (stored(phtV) + 1)) |
        ((stored(phtV) === stored(phtV).minValue) ? U(stored(phtV).minValue) | (stored(phtV) - 1))

      output(jumpPC) := stored(pcPlus4) + 4

      when(stored(isDJump)) {
        btb.io.w.en := True
        btb.io.w.pEn := True

        bpu.io.w.bhtEn := True
        bpu.io.w.phtEn := True

        when(ju.jump) {
          output(jumpPC) := stored(jumpPC)
          output(wantJump) := !stored(wantJump) |
            (stored(jumpPC).asBits =/= (stored(btbData) ## BTB.ADDR_PADDING))
        } otherwise {
          output(wantJump) := stored(wantJump)
        }
      } otherwise {
        btb.io.w.tagLine := stored(btbTagLine_)
        btb.io.w.p := stored(btbSetP_)

        output(wantJump) := ju.jump | stored(wantJump)
        when(ju.jump) { output(jumpPC) := U(stored(rsValue)) }

        when(stored(btbHit)) { // 刷掉 btb
          btb.io.w.en := True
          btb.io.w.pEn := True
        }
      }
    }

    val assignJumpTask = RegNext(will.input) & produced(wantJump)

    val juC = new StageComponent {
      ju.op := input(jumpCond)
      ju.a := input(rsValue).asSInt
      ju.b := input(rtValue).asSInt
    }

    val bdValue = RegInit(False)
    when(will.output) {
      bdValue := stored(jumpCond) =/= JU_OP.f
    }
    when(will.flush) {
      bdValue := False
    }
    output(bd) := bdValue

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
      if (ConstantVal.FINAL_MODE) {
        alu.io.input.hi := U(hlu.hi_we ? hlu.new_hi | hlu.hi_v)
        alu.io.input.lo := U(hlu.lo_we ? hlu.new_lo | hlu.lo_v)
      }

      alu.io.will.input := will.input
      alu.io.will.output := will.output

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

    //EX阶段计算出地址后再去查TLB
    val AddrTranslateC = new StageComponent {
      // MMU translate
      mmu.io.dataVaddr := aguC.output(memAddr)
      output(dataMMURes) := mmu.io.dataRes
    }

    //数据前传：解决load + 中间一条无关指令 + store的数据冲突
    when(ME2.stored(rfuWe) && ME2.stored(rfuAddr) === input(inst).rs & ME2.stored(memRe)) {
      produced(rsValue) := ME2.produced(rfuData)
    }
    when(ME2.stored(rfuWe) && ME2.stored(rfuAddr) === input(inst).rt & ME2.stored(memRe)) {
      produced(rtValue) := ME2.produced(rfuData)
    }

    // CP0 harzard : 根据ME1的情况暂停EX阶段
    val cp0_RAW_hazard = new Area {
      //当前指令mfc0，上一条指令mtc0 | tlbp | tlbr ，则需要暂停一个周期
      val mfc0_mtc0_hazard: Bool = !ME1.is.empty & input(cp0Re) & ME1.stored(cp0We) &
        input(inst).rd === ME1.stored(inst).rd &
        input(inst).sel === ME1.stored(inst).sel
    }

    exceptionToRaise := alu.io.exception
    // 针对MOV指令
    val conditionMovC = new StageComponent {
      if (ConstantVal.FINAL_MODE) {
        val comparator = new Comparator
        comparator.io.op := input(compareOp)
        comparator.io.operand := input(rtValue)
        output(rfuWe) := input(conditionMov) & comparator.io.mov
      }
    }
    if (ConstantVal.FINAL_MODE) {
      produced(rfuWe) := conditionMovC.output(rfuWe) | input(rfuWe)
      when(conditionMovC.output(rfuWe)) {
        produced(rfuData) := input(rsValue)
      }
    }
  }

  val ID = new Stage {
    val preCalcBTB = new StageComponent {
      output(isDJump) := du.io.ju_op =/= JU_OP.f & du.io.ju_pc_src =/= JU_PC_SRC.rs
      output(jumpPC) := du.io.ju_pc_src.mux(
        JU_PC_SRC.offset -> U(S(stored(pcPlus4)) + S(du.io.inst.offset ## B"00")),
        default          -> U(stored(pcPlus4)(31 downto 28) ## du.io.inst.index ## B"00")
      )

      val wea = btb.plru(stored(btbP))
      output(btbDataLine) := btb.dataMem.write(
        stored(btbDataLine),
        wea,
        B(output(jumpPC)(BTB.ADDR_RANGE))
      )
      output(btbTagLine) := btb.tagMem.write(
        stored(btbTagLine),
        wea,
        True ## stored(pc)(BTB.TAG_RANGE)
      )
      output(btbTagLine_) := btb.tagMem.write(
        stored(btbTagLine),
        wea,
        B(0, 1 + BTB.TAG_WIDTH bits)
      )
      btb.getP(stored(btbP), wea, True, output(btbSetP))
      btb.getP(stored(btbP), wea, False, output(btbSetP_))
    }

    val duC = new StageComponent {
      du.io.inst := input(inst)

      output(aluOp) := du.io.alu_op
      output(aluASrc) := du.io.alu_a_src
      output(aluBSrc) := du.io.alu_b_src
      output(jumpCond) := du.io.ju_op
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
      output(fuck) := du.io.fuck

      if (ConstantVal.FINAL_MODE) {
        output(tlbr) := du.io.tlbr
        output(tlbw) := du.io.tlbw
        output(tlbp) := du.io.tlbp
        output(tlbIndexSrc) := du.io.tlbIndexSrc
        produced(isTrap) := du.io.is_trap
        output(invalidateICache) := du.io.invalidate_icache
        output(invalidateDCache) := du.io.invalidate_dcache
        output(meType) := du.io.me_type
        output(compareOp) := du.io.compare_op
        output(conditionMov) := du.io.condition_mov
        output(privilege) := du.io.privilege
      } else {
        output(invalidateICache) := False
        output(invalidateDCache) := False
      }
    }

    val rfuRead = new StageComponent {
      rfu.io.ra.index := input(inst).rs
      rfu.io.rb.index := input(inst).rt
      output(rsValue) := rfu.io.ra.data
      output(rtValue) := rfu.io.rb.data
    }

    when(EX.stored(rfuWe) && EX.stored(rfuAddr) === stored(inst).rs) {
      produced(rsValue) := EX.produced(rfuData)
    } elsewhen (ME1.stored(rfuWe) && ME1.stored(rfuAddr) === stored(inst).rs) {
      produced(rsValue) := ME1.produced(rfuData)
    } elsewhen (ME2.stored(rfuWe) && ME2.stored(rfuAddr) === stored(inst).rs) {
      produced(rsValue) := ME2.produced(rfuData)
    } elsewhen (WB.stored(rfuWe) && WB.stored(rfuAddr) === stored(inst).rs) {
      produced(rsValue) := WB.stored(rfuData)
    }

    when(EX.stored(rfuWe) && EX.stored(rfuAddr) === stored(inst).rt) {
      produced(rtValue) := EX.produced(rfuData)
    } elsewhen (ME1.stored(rfuWe) && ME1.stored(rfuAddr) === stored(inst).rt) {
      produced(rtValue) := ME1.produced(rfuData)
    } elsewhen (ME2.stored(rfuWe) && ME2.stored(rfuAddr) === stored(inst).rt) {
      produced(rtValue) := ME2.produced(rfuData)
    } elsewhen (WB.stored(rfuWe) && WB.stored(rfuAddr) === stored(inst).rt) {
      produced(rtValue) := WB.stored(rfuData)
    }

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

    if (ConstantVal.FINAL_MODE) {
      // 解决MOV指令的前传问题
      when(EX.produced(rfuWe) && EX.stored(rfuAddr) === stored(inst).rs) {
        produced(rsValue) := EX.produced(rfuData)
      }
      when(EX.produced(rfuWe) && EX.stored(rfuAddr) === stored(inst).rt) {
        produced(rtValue) := EX.produced(rfuData)
      }
    }
  }

  val IF2 = new Stage {
    output(pcPlus4) := stored(pc) + 4

    val readPHT = new StageComponent {
      bpu.io.r.phtI := stored(phtI)
      output(phtV) := bpu.io.r.phtV
    }

    val decideJump = new StageComponent {
      output(btbDataLine) := btb.io.r.dataLine
      output(btbTagLine) := btb.io.r.tagLine
      output(btbP) := btb.io.r.p

      val btbHitLine = Bits(BTB.NUM_WAYS bits)

      btb.getHitLineAndData(
        btb.io.r.tagLine,
        btb.io.r.dataLine,
        stored(pc)(BTB.ADDR_RANGE),
        btbHitLine,
        output(btbData)
      )

      output(btbHit) := btbHitLine.orR

      output(jumpPC) := U(output(btbData) ## BTB.ADDR_PADDING)
      output(wantJump) := produced(phtV)(1) & output(btbHit)
    }

    val assignJumpTask = RegNext(will.input) & produced(wantJump)

    val icuC = new StageComponent {
      //ibus
      ibus.stage2.paddr := input(ifPaddr)
      ibus.stage2.en := input(if2En)
      output(inst) := ibus.stage2.rdata
    }
  }

  val IF1 = new Stage {
    val readBHT = new StageComponent {
      output(bhtI) := stored(pc)(BHT.BASE_RANGE) ^ stored(pc)(BHT.INDEX_RANGE)
      bpu.io.r.bhtI := output(bhtI)
      output(bhtV) := bpu.io.r.bhtV

      output(phtI) := U(output(bhtV), PHT.INDEX_WIDTH bits) ^ stored(pc)(PHT.INDEX_RANGE)
    }

    val readBTB = new StageComponent {
      btb.io.r.en := will.output
      btb.io.r.addr := stored(pc)(BTB.ADDR_RANGE)
    }

    val jumpTask =
      Task(IF2.assignJumpTask, IF2.produced(jumpPC), will.output)
    val jumpTask2 =
      Task(EX.assignJumpTask, EX.produced(jumpPC), will.output)
    val setTask = Task(ME1.assignSetTask, cp0.io.exceptionBus.jumpPc.value, will.output)
    stored(pc) init ConstantVal.INIT_PC
    when(will.output) {
      stored(pc) := setTask.has ?
        setTask.value |
        (jumpTask2.has ? jumpTask2.value | (jumpTask.has ? jumpTask.value | stored(pc) + 4))
    }

    val icuC = new StageComponent {
      //ibus
      icu.io.offset := input(pc)(0, 2 bits)
      ibus.stage1.read := True
      ibus.stage1.index := input(pc)(icacheConfig.offsetWidth, icacheConfig.indexWidth bits)
      ibus.stage1.keepRData := !IF2.is.empty & !IF2.will.input

      // MMU Translate
      mmu.io.instVaddr := input(pc)
      output(ifPaddr) := mmu.io.instRes.paddr
      output(if2En) := True

      // 异常
      exceptionToRaise := None
      output(instFetchError) := False
      // TLB异常
      val refillException  = mmu.io.instRes.tlbRefillException
      val invalidException = mmu.io.instRes.tlbInvalidException
      when(refillException | invalidException) {
        exceptionToRaise := ExcCode.loadTLBError
        output(instFetchError) := True
        output(if2En) := False
      }
      output(tlbRefillException) := refillException
      //访问权限不够的异常 或 访存地址异常（更优先）
      when(!icu.io.addrValid | mmu.io.instRes.illegal) {
        exceptionToRaise := ExcCode.loadAddrError
        output(instFetchError) := True
        output(if2En) := False
      }
    }
  }

//  cp0.io.instOnInt.valid := !EX.is.empty
//  cp0.io.instOnInt.bd := EX.bdValue
//  cp0.io.instOnInt.pc := EX.stored(pc)
  val stages = Seq(IF1, IF2, ID, EX, ME1, ME2, WB)
  for ((prev, next) <- (stages zip stages.tail).reverse) {
    prev connect next
  }

  IF1.prevException := None
  IF2.is.done := !ibus.stage2.stall
  IF2.can.flush := !ibus.stage2.stall

  ID.is.done :=
    !(ID.wantForwardFromEX & EX.stored(memRe) |
      ID.wantForwardFromME1 & ME1.stored(memRe) |
      ID.wantForwardFromME2 & ME2.stored(memRe) & !ME2.is.done)

  EX.is.done := !alu.io.stall
  EX.can.flush := !alu.io.stall

  when(EX.cp0_RAW_hazard.mfc0_mtc0_hazard) {
    EX.is.done := False
  }

  ME2.is.done := !dbus.stage2.stall
  ME2.can.flush := !dbus.stage2.stall

  // 异常清空流水线
  when(ME1.assignSetTask) {
    IF1.want.flush := True
    IF2.want.flush := True
    ID.want.flush := True
    EX.want.flush := True
    ME1.want.flush := True
  }

  when(EX.assignJumpTask) {
    when(!ID.is.empty) {
      IF2.want.flush := True
    }
    when(!(ID.is.empty & IF2.is.empty)) {
      IF1.want.flush := True
    }
  }
  // 当遇到特殊指令的时候，会让后面的指令全部不要流水
  when(ID.produced(fuck) | EX.produced(fuck) | ME1.produced(fuck) | ME2.produced(fuck)) {
    IF2.is.done := False
    IF1.is.done := False
  }

  IF1.input(pc).addAttribute("mark_debug", "true")

  IF2.decideJump.btbHitLine.addAttribute("mark_debug", "true")
  IF2.produced(btbData).addAttribute("mark_debug", "true")
  IF2.produced(btbTagLine).addAttribute("mark_debug", "true")

  ID.output(jumpCond).addAttribute("mark_debug", "true")

  EX.stored(pc).addAttribute("mark_debug", "true")
  EX.stored(inst).addAttribute("mark_debug", "true")
  ju.op.addAttribute("mark_debug", "true")
  ju.jump.addAttribute("mark_debug", "true")

  WB.stored(rfuWe).addAttribute("mark_debug", "true")
  WB.stored(rfuAddr).addAttribute("mark_debug", "true")
  WB.stored(rfuData).addAttribute("mark_debug", "true")


//
  ME1.stored(pc).addAttribute("mark_debug", "true")
  ME1.stored(memRe).addAttribute("mark_debug", "true")
  ME1.stored(memWe).addAttribute("mark_debug", "true")
  ME1.stored(memAddr).addAttribute("mark_debug", "true")
  ME1.stored(dataMMURes).addAttribute("mark_debug", "true")
  ME1.refillException.addAttribute("mark_debug", "true")
  ME1.invalidException.addAttribute("mark_debug", "true")
  ME1.modifiedException.addAttribute("mark_debug", "true")
  dbus.stage2.rdata.addAttribute("mark_debug", "true")
  dbus.stage2.wdata.addAttribute("mark_debug", "true")

  cp0.regs.status.addAttribute("mark_debug", "true")
  cp0.regs.badVaddr.addAttribute("mark_debug", "true")
  cp0.regs.epc.addAttribute("mark_debug", "true")
  cp0.regs.cause.addAttribute("mark_debug", "true")
  cp0.io.exceptionBus.exception.addAttribute("mark_debug", "true")
  cp0.io.exceptionBus.jumpPc.addAttribute("mark_debug", "true")
//
//  io.uncacheAXI.aw.valid.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.ready.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.addr.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.len.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.size.addAttribute("mark_debug", "true")
//  io.uncacheAXI.aw.burst.addAttribute("mark_debug", "true")
//
//  io.uncacheAXI.w.ready.addAttribute("mark_debug", "true")
//  io.uncacheAXI.w.valid.addAttribute("mark_debug", "true")
//  io.uncacheAXI.w.data.addAttribute("mark_debug", "true")
//  io.uncacheAXI.w.strb.addAttribute("mark_debug", "true")
//  io.uncacheAXI.b.valid.addAttribute("mark_debug", "true")
//  io.uncacheAXI.b.ready.addAttribute("mark_debug", "true")
}

object CPU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new CPU)
  }
}
