package cpu

import spinal.core._
import ip._
import lib._
import defs.Config._

object BranchPredictor {
  val btbNumEntries = BTB.NUM_ENTRIES
  val btbNumWays    = BTB.NUM_WAYS
  val btbIndexWidth = BTB.INDEX_WIDTH
  val bhtNumEntries = BHT.NUM_ENTRIES
  val bhtIndexWidth = BHT.INDEX_WIDTH
  val bhtDataWidth  = BHT.DATA_WIDTH
  val phtNumEntries = PHT.NUM_ENTRIES
  val phtIndexWidth = PHT.INDEX_WIDTH

  object Address {
    val btbIndexOffset = 3
    val btbTagOffset   = btbIndexOffset + btbIndexWidth
    val btbTagWidth    = 32 - btbTagOffset

    val btbMetaWidth = 1 + btbTagWidth

    val bhtIndexOffset = 3
    val bhtBaseOffset  = bhtIndexOffset + bhtIndexWidth

    val phtIndexOffset = 3

    def apply(): Address = new Address()
  }

  class Address extends UInt {
    setWidth(32)

    import Address._
    def btbIndex: UInt = this(btbIndexOffset, btbIndexWidth bits)
    def btbTag: UInt   = this(btbTagOffset, btbTagWidth bits)
    def bhtBase: UInt  = this(bhtBaseOffset, bhtIndexWidth bits)
    def bhtIndex: UInt = this(bhtIndexOffset, bhtIndexWidth bits)
    def phtIndex: UInt = this(phtIndexOffset, phtIndexWidth bits)
  }

  val pc      = Key(Address()).setName("bp_pc")
  val isDJump = Key(Bool()).setEmptyValue(False).setName("bp_isDJump")
  val jumpPC  = Key(UInt(32 bits)).setName("bp_jumpPC")

  val btbHitLine = Key(Bits(btbNumWays bits)).setName("bp_btbHitLine")
  val btbHit     = Key(Bool()).setEmptyValue(False).setName("bp_btbHit")
  val btbOH      = Key(Bits(btbNumWays bits)).setName("bp_btbOH")
  val btbMeta    = Key(Bits(Address.btbMetaWidth bits)).setName("bp_btbMeta")
  val btbData    = Key(Bits(30 bits)).setName("bp_btbData")
  val btbP       = Key(Bits(btbNumWays - 1 bits)).setName("bp_btbP")

  val bhtI  = Key(UInt(bhtIndexWidth bits)).setName("bp_bhtI")
  val bhtV  = Key(Bits(bhtDataWidth bits)).setName("bp_bhtV")
  val phtI  = Key(UInt(phtIndexWidth bits)).setName("bp_phtI")
  val phtV  = Key(UInt(2 bits)).setName("bp_phtV")
  val phtVP = Key(UInt(2 bits)).setName("bp_phtVP")
  val phtVM = Key(UInt(2 bits)).setName("bp_phtVM")
}

class BranchPredictor(number: Int) extends Area {
  setName(s"bp_$number")

  import BranchPredictor._
  val btb = new Cache(btbNumEntries / btbNumWays, btbNumWays, btbIndexWidth, Address.btbMetaWidth, 30)
  val bpu = new BPU(bhtNumEntries, bhtIndexWidth, bhtDataWidth, phtNumEntries, phtIndexWidth)

  val write2 = new ComponentStage {
    val io = new Bundle {
      val assignJump = Bool()
    }
    /**/
    btb.io.w.en := stored(btbHit) | !!!(isDJump)
    btb.io.w.oh := stored(btbOH)
    btb.io.w.index := stored(pc).btbIndex
    btb.io.w.meta := stored(btbMeta)
    btb.io.w.data := stored(btbData)
    btb.io.w.p := stored(btbP)
    /**/
    bpu.io.w.bhtEn := !!!(isDJump)
    bpu.io.w.phtEn := !!!(isDJump)
    bpu.io.w.bhtI := stored(bhtI)
    bpu.io.w.bhtV := stored(bhtV)(0, bhtDataWidth - 1 bits) ## io.assignJump
    bpu.io.w.phtI := stored(phtI)
    bpu.io.w.phtV := io.assignJump ? stored(phtVP) | stored(phtVM)
  }

  val write1 = new ComponentStage {
    val io = new Bundle {
      val issue = IssueEntry()
    }
    /**/
    val jumpPC     = io.issue.target
    val btbHitLine = io.issue.predict.btbHitLine
    val btbP       = io.issue.predict.btbP
    val phtV       = io.issue.predict.phtV
    /**/
    output(pc) := io.issue.pc
    output(isDJump) := io.issue.branch.isDjump
    output(btbHit) := io.issue.predict.btbHit
    output(bhtV) := io.issue.predict.bhtV
    /**/
    val valid = output(isDJump)
    val we    = output(btbHit) ? btbHitLine | btb.plru(btbP)
    output(btbOH) := we
    output(btbMeta) := valid ## output(pc).btbTag
    output(btbData) := B(jumpPC)(2, 30 bits)
    btb.getP(btbP, we, valid, output(BranchPredictor.btbP))
    /**/
    output(bhtI) := output(pc).bhtBase ^ output(pc).bhtIndex
    output(phtI) := U(output(bhtV), phtIndexWidth bits) ^ output(pc).phtIndex
    output(phtVP) := U((phtV(1) | phtV(0)) ## (phtV(1) | !phtV(0)))
    output(phtVM) := U((phtV(1) & phtV(0)) ## (phtV(1) & !phtV(0)))
  }

  val read2 = new ComponentStage {
    val io = new Bundle {
      val btbHitLine = Bits(btbNumWays bits)
      val btbHit     = Bool()
      val btbP       = Bits(btbNumWays - 1 bits)
      val bhtV       = Bits(bhtDataWidth bits)
      val phtV       = UInt(2 bits)
      /**/
      val assignJump = Bool()
      val jumpPC     = UInt(32 bits)
    }
    /**/
    bpu.io.r.phtI := stored(phtI)
    output(phtV) := bpu.io.r.phtV
    /**/
    io.assignJump := output(btbHit) & output(phtV)(1)
    io.jumpPC := U(output(btbData) ## B"00")
    /**/
    io.btbHitLine := output(btbHitLine)
    io.btbHit := output(btbHit)
    io.btbP := output(btbP)
    io.bhtV := stored(bhtV)
    io.phtV := output(phtV)
    /**/
    btb.io.r.en := will.input
    val metaLine = btb.io.r.metaLine
    val dataLine = btb.io.r.dataLine
    output(btbP) := btb.io.r.p
    for (i <- 0 to 3) {
      output(btbHitLine)(i) := (True ## stored(pc).btbTag) === metaLine(i)
    }
    output(btbHit) := output(btbHitLine).orR
    output(btbData) := dataLine.oneHotAccess(output(btbHitLine))
  }

  val read1 = new ComponentStage {
    val io = new Bundle {
      val nextPC = Address()
      val pc     = Address()
    }
    /**/
    btb.io.r.index := io.pc.btbIndex
    /**/
    bpu.io.r.bhtI := io.pc.bhtBase ^ io.pc.bhtIndex
    output(bhtV) := bpu.io.r.bhtV
    output(phtI) := U(output(bhtV), phtIndexWidth bits) ^ io.pc.phtIndex
    /**/
    output(pc) := io.pc
  }

  write2.interConnect()
  write1.send(write2)
  write1.interConnect()

  read2.interConnect()
  read1.send(read2)
  read1.interConnect()
}
