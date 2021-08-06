package cpu

import spinal.core._
import ip._
import defs.Config._
import lib._

object Address {
  val btbIndexOffset = 3
  val btbTagOffset   = btbIndexOffset + BTB.INDEX_WIDTH
  val btbTagWidth    = 32 - btbTagOffset

  val btbMetaWidth = 2 + btbTagWidth

  val bhtIndexOffset = 3
  val bhtBaseOffset  = bhtIndexOffset + BHT.INDEX_WIDTH

  val phtIndexOffset = 3

  def apply(): Address = new Address()
}

class Address extends UInt {
  setWidth(32)

  import Address._
  def btbIndex: UInt = this(btbIndexOffset, BTB.INDEX_WIDTH bits)
  def btbTag: UInt   = this(btbTagOffset, btbTagWidth bits)
  def bhtBase: UInt  = this(bhtBaseOffset, BHT.INDEX_WIDTH bits)
  def bhtIndex: UInt = this(bhtIndexOffset, BHT.INDEX_WIDTH bits)
  def phtIndex: UInt = this(phtIndexOffset, PHT.INDEX_WIDTH bits)
}

class BranchPredictor extends Area {
  val pc      = Key(Address())
  val isDJump = Key(Bool())

  val btbHitLine = Key(Bits(BTB.NUM_WAYS bits))
  val btbHit     = Key(Bool())
  val btbPC_2    = Key(Bool())
  val btbOH      = Key(Bits(BTB.NUM_WAYS bits))
  val btbMeta    = Key(Bits(Address.btbMetaWidth bits))
  val btbData    = Key(Bits(30 bits))
  val btbP       = Key(Bits(BTB.NUM_WAYS - 1 bits))

  val bhtI  = Key(UInt(BHT.INDEX_WIDTH bits))
  val bhtV  = Key(Bits(BHT.DATA_WIDTH bits))
  val phtI  = Key(UInt(PHT.INDEX_WIDTH bits))
  val phtV  = Key(UInt(2 bits))
  val phtVP = Key(UInt(2 bits))
  val phtVM = Key(UInt(2 bits))

  val btb = new Cache(BTB.NUM_ENTRIES / BTB.NUM_WAYS, BTB.NUM_WAYS, BTB.INDEX_WIDTH, Address.btbMetaWidth, 30)
  val bpu = new BPU

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
    bpu.io.w.bhtV := stored(bhtV)(1, BHT.DATA_WIDTH - 1 bits) ## io.assignJump
    bpu.io.w.phtI := stored(phtI)
    bpu.io.w.phtV := io.assignJump ? stored(phtVP) | stored(phtVM)
  }

  val write1 = new ComponentStage {
    val io = new Bundle {
      val pc         = Address()
      val isDJump1   = Bool()
      val jumpPC1    = UInt(32 bits)
      val isDJump2   = Bool()
      val jumpPC2    = UInt(32 bits)
      val btbHit     = Bool()
      val btbHitLine = Bits(BTB.NUM_WAYS bits)
      val btbP       = Bits(BTB.NUM_WAYS - 1 bits)
      val bhtV       = Bits(BHT.DATA_WIDTH bits)
      val phtV       = UInt(2 bits)
    }
    /**/
    val pc_2  = io.isDJump2
    val valid = io.isDJump1 | io.isDJump2
    val we    = io.btbHit ? io.btbHitLine | btb.plru(io.btbP)
    output(pc) := io.pc
    output(isDJump) := valid
    output(btbHit) := io.btbHit
    output(btbOH) := we
    output(btbMeta) := pc_2 ## valid ## io.pc.btbTag
    output(btbData) := B(pc_2 ? io.jumpPC2 | io.jumpPC1)(2, 30 bits)
    btb.getP(io.btbP, we, valid, output(btbP))
    /**/
    output(bhtI) := io.pc.bhtBase ^ io.pc.bhtIndex
    output(bhtV) := io.bhtV
    output(phtI) := U(io.bhtV, PHT.INDEX_WIDTH bits) ^ io.pc.phtIndex
    output(phtVP) := U((io.phtV(1) | io.phtV(0)) ## (io.phtV(1) | !io.phtV(0)))
    output(phtVM) := U((io.phtV(1) & io.phtV(0)) ## (io.phtV(1) & !io.phtV(0)))
  }

  val read2 = new ComponentStage {
    val io = new Bundle {
      val btbHit     = Bool()
      val btbHitLine = Bits(BTB.NUM_WAYS bits)
      val btbP       = Bits(BTB.NUM_WAYS - 1 bits)
      val bhtV       = Bits(BHT.DATA_WIDTH bits)
      val phtV       = UInt(2 bits)

      val assignJump = Bits(2 bits)
      val jumpPC     = UInt(32 bits)
    }
    /**/
    bpu.io.r.phtI := stored(phtI)
    output(phtV) := bpu.io.r.phtV
    /**/
    val j = stored(btbHit) & output(phtV)(1)
    io.assignJump := (j & stored(btbPC_2)) ## (j & !stored(btbPC_2))
    io.jumpPC := U(stored(btbData) ## B"00")
    /**/
    io.btbHit := stored(btbHit)
    io.btbHitLine := stored(btbHitLine)
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
    for (i <- 0 to 3) {
      output(btbHitLine)(i) := (B"1" ## io.pc.btbTag) === metaLine(i)(0, 1 + Address.btbTagWidth bits)
    }
    output(btbHit) := output(btbHitLine).orR
    output(btbData) := dataLine.oneHotAccess(output(btbHitLine))
    output(btbPC_2) := metaLine.oneHotAccess(output(btbHitLine))(Address.btbMetaWidth - 1)
    /**/
    bpu.io.r.bhtI := io.pc.bhtBase ^ io.pc.bhtIndex
    output(bhtV) := bpu.io.r.bhtV
    output(phtI) := U(output(bhtV), PHT.INDEX_WIDTH bits) ^ io.pc.phtIndex
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

object BranchPredictor {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new Component {
      val bp = new BranchPredictor()
    })
  }
}
