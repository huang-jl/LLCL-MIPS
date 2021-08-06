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

class BranchPredictor(numStages: Int) extends Area {
  val pc      = Key(Address())
  val isDJump = Key(Bool())

  val btbMetaLine = Key(Vec(Bits(Address.btbMetaWidth bits), BTB.NUM_WAYS))
  val btbDataLine = Key(Vec(Bits(30 bits), BTB.NUM_WAYS))
  val btbHitLine  = Key(Bits(BTB.NUM_WAYS bits))
  val btbHit      = Key(Bool())
  val btbData     = Key(UInt(30 bits))
  val btbPC_2     = Key(Bool())
  val btbP        = Key(Bits(BTB.NUM_WAYS - 1 bits))

  val bhtI  = Key(UInt(BHT.INDEX_WIDTH bits))
  val bhtV  = Key(Bits(BHT.DATA_WIDTH bits))
  val phtI  = Key(UInt(PHT.INDEX_WIDTH bits))
  val phtV  = Key(UInt(2 bits))
  val phtVP = Key(UInt(2 bits))
  val phtVM = Key(UInt(2 bits))

  val io = new Bundle {
    val pc     = Address()
    val nextPC = Address()

    val isDJump1 = Bool()
    val jumpPC1  = UInt(32 bits)

    val isDJump2 = Bool()
    val jumpPC2  = UInt(32 bits)

    val jump = Bool()

    val assignJump = Bits(2 bits)
    val jumpPC     = UInt(32 bits)
  }

  val btb = new Cache(BTB.NUM_ENTRIES, BTB.NUM_WAYS, BTB.INDEX_WIDTH, Address.btbMetaWidth, 30)
  val bpu = new BPU

  val write1 = new ComponentStage {
    btb.io.w.en := stored(btbHit) | stored(isDJump)
    btb.io.w.pEn := btb.io.w.en
    btb.io.w.index := stored(pc).btbIndex
    btb.io.w.metaLine := stored(btbMetaLine)
    btb.io.w.dataLine := stored(btbDataLine)
    btb.io.w.p := stored(btbP)
    /**/
    bpu.io.w.bhtEn := stored(isDJump)
    bpu.io.w.phtEn := stored(isDJump)
    bpu.io.w.bhtI := stored(bhtI)
    bpu.io.w.bhtV := stored(bhtV)(1, BHT.DATA_WIDTH - 1 bits) ## io.jump
    bpu.io.w.phtI := stored(phtI)
    bpu.io.w.phtV := io.jump ? stored(phtVP) | stored(phtVM)
  }

  val calc1 = new ComponentStage {
    val pc_2  = io.isDJump2
    val valid = io.isDJump1 | io.isDJump2
    val we    = stored(btbHit) ? stored(btbHitLine) | btb.plru(stored(btbP))
    output(isDJump) := valid
    output(btbMetaLine) := stored(btbMetaLine)
    output(btbDataLine) := stored(btbDataLine)
    output(btbMetaLine).oneHotAccess(we) := pc_2 ## valid ## stored(pc).btbTag
    output(btbDataLine).oneHotAccess(we) := B(pc_2 ? io.jumpPC2 | io.jumpPC1)
    btb.getP(stored(btbP), we, valid, output(btbP))
    /**/
    output(phtVP) := U((stored(phtV)(1) | stored(phtV)(0)) ## (stored(phtV)(1) | !stored(phtV)(0)))
    output(phtVM) := U((stored(phtV)(1) & stored(phtV)(0)) ## (stored(phtV)(1) & !stored(phtV)(0)))
  }

  val read2 = new ComponentStage {
    bpu.io.r.phtI := stored(phtI)
    output(phtV) := bpu.io.r.phtV
    /**/
    val j = stored(btbHit) & output(phtV)(1)
    io.assignJump := (j & stored(btbPC_2)) ## (j & !stored(btbPC_2))
    io.jumpPC := stored(btbData) @@ U"00"
  }

  val read1 = new ComponentStage {
    btb.io.r.en := will.input
    btb.io.r.index := io.nextPC.btbIndex
    output(btbMetaLine) := btb.io.r.metaLine
    output(btbDataLine) := btb.io.r.dataLine
    for (i <- 0 to 3) {
      output(btbHitLine)(i) := B"1" ## io.pc.btbTag === output(btbMetaLine)(i)(0, 1 + Address.btbTagWidth bits)
    }
    output(btbHit) := output(btbHitLine).orR
    output(btbData) := U(output(btbDataLine).oneHotAccess(output(btbHitLine)))
    output(btbPC_2) := output(btbMetaLine).oneHotAccess(output(btbHitLine))(Address.btbMetaWidth - 1)
    /**/
    bpu.io.r.bhtI := io.pc.bhtBase ^ io.pc.bhtIndex
    output(bhtI) := bpu.io.r.bhtI
    output(bhtV) := bpu.io.r.bhtV
    output(phtI) := U(output(bhtV), PHT.INDEX_WIDTH bits) ^ io.pc.phtIndex
    /**/
    output(pc) := io.pc
  }

  write1.interConnect()
  calc1.send(write1)
  calc1.interConnect()
  read2.send(calc1)
  read2.interConnect()
  read1.send(read2)
  read1.interConnect()
}

object BranchPredictor {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new Component {
      val bp = new BranchPredictor(5)
    })
  }
}
