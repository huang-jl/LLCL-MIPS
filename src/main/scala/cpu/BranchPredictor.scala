package cpu

import spinal.core._
import ip._
import lib._
import defs.Config._

class BranchPredictor(
    number: Int,
    btbNumEntries: Int = BTB.NUM_ENTRIES,
    btbNumWays: Int = BTB.NUM_WAYS,
    btbIndexWidth: Int = BTB.INDEX_WIDTH,
    bhtNumEntries: Int = BHT.NUM_ENTRIES,
    bhtIndexWidth: Int = BHT.INDEX_WIDTH,
    bhtDataWidth: Int = BHT.DATA_WIDTH,
    phtNumEntries: Int = PHT.NUM_ENTRIES,
    phtIndexWidth: Int = PHT.INDEX_WIDTH
) extends Area {
  setName(s"bp_$number")

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

  val pc      = Key(Address())
  val isDJump = Key(Bool())
  val jumpPC  = Key(UInt(32 bits))

  val btbHitLine = Key(Bits(btbNumWays bits))
  val btbHit     = Key(Bool())
  val btbOH      = Key(Bits(btbNumWays bits))
  val btbMeta    = Key(Bits(Address.btbMetaWidth bits))
  val btbData    = Key(Bits(30 bits))
  val btbP       = Key(Bits(btbNumWays - 1 bits))

  val bhtI  = Key(UInt(bhtIndexWidth bits))
  val bhtV  = Key(Bits(bhtDataWidth bits))
  val phtI  = Key(UInt(phtIndexWidth bits))
  val phtV  = Key(UInt(2 bits))
  val phtVP = Key(UInt(2 bits))
  val phtVM = Key(UInt(2 bits))

  val btb = new Cache(btbNumEntries / btbNumWays, btbNumWays, btbIndexWidth, Address.btbMetaWidth, 30)
  val bpu = new BPU(bhtNumEntries, bhtIndexWidth, bhtDataWidth, phtNumEntries, phtIndexWidth)

  val write2 = new ComponentStage {
    val io = new Bundle {
      val assignJump = Bool()
    }
    /**/
    btb.io.w.en := stored(btbHit) | stored(isDJump)
    btb.io.w.oh := stored(btbOH)
    btb.io.w.index := stored(pc).btbIndex
    btb.io.w.meta := stored(btbMeta)
    btb.io.w.data := stored(btbData)
    btb.io.w.p := stored(btbP)
    /**/
    bpu.io.w.bhtEn := stored(isDJump)
    bpu.io.w.phtEn := stored(isDJump)
    bpu.io.w.bhtI := stored(bhtI)
    bpu.io.w.bhtV := stored(bhtV)(1, bhtDataWidth - 1 bits) ## io.assignJump
    bpu.io.w.phtI := stored(phtI)
    bpu.io.w.phtV := io.assignJump ? stored(phtVP) | stored(phtVM)
  }

  val write1 = new ComponentStage {
    val valid = stored(isDJump)
    val we    = stored(btbHit) ? stored(btbHitLine) | btb.plru(stored(btbP))
    output(btbOH) := we
    output(btbMeta) := valid ## stored(pc).btbTag
    output(btbData) := B(stored(jumpPC))(2, 30 bits)
    btb.getP(stored(btbP), we, valid, output(btbP))
    /**/
    output(bhtI) := stored(pc).bhtBase ^ stored(pc).bhtIndex
    output(phtI) := U(stored(bhtV), phtIndexWidth bits) ^ stored(pc).phtIndex
    output(phtVP) := U((stored(phtV)(1) | stored(phtV)(0)) ## (stored(phtV)(1) | !stored(phtV)(0)))
    output(phtVM) := U((stored(phtV)(1) & stored(phtV)(0)) ## (stored(phtV)(1) & !stored(phtV)(0)))
  }

  val write0 = new ComponentStage {
    val io = new Bundle {
      val issue = IssueEntry()
    }
    /**/
    output(pc) := io.issue.pc
    output(isDJump) := io.issue.branch.isDjump
    output(jumpPC) := io.issue.target
    /**/
    output(btbHitLine) := io.issue.predict.btbHitLine
    output(btbHit) := io.issue.predict.btbHit
    output(btbP) := io.issue.predict.btbP
    /**/
    output(bhtV) := io.issue.predict.bhtV
    output(phtV) := io.issue.predict.phtV
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
    io.assignJump := stored(btbHit) & output(phtV)(1)
    io.jumpPC := U(stored(btbData) ## B"00")
    /**/
    io.btbHitLine := stored(btbHitLine)
    io.btbHit := stored(btbHit)
    io.btbP := stored(btbP)
    io.bhtV := stored(bhtV)
    io.phtV := output(phtV)
  }

  val read1 = new ComponentStage {
    val io = new Bundle {
      val nextPC = Address()
      val pc     = Address()
    }
    /**/
    btb.io.r.en := will.input
    btb.io.r.index := io.nextPC.btbIndex
    val metaLine = btb.io.r.metaLine
    val dataLine = btb.io.r.dataLine
    output(btbP) := btb.io.r.p
    for (i <- 0 to 3) {
      output(btbHitLine)(i) := (True ## io.pc.btbTag) === metaLine(i)
    }
    output(btbHit) := output(btbHitLine).orR
    output(btbData) := dataLine.oneHotAccess(output(btbHitLine))
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
  write0.send(write1)
  write0.interConnect()

  read2.interConnect()
  read1.send(read2)
  read1.interConnect()
}
