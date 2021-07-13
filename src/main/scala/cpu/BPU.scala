package cpu

import cpu.defs.Config._
import spinal.core._
import scala.language.postfixOps

class BPU extends Component {
  val bht = Mem(Bits(BHT.DATA_WIDTH bits), BHT.NUM_ENTRIES) randBoot
  val pht = Mem(UInt(2 bits), PHT.NUM_ENTRIES) randBoot

  val io = new Bundle {
    val r = new Bundle {
      val bhtI = in UInt (BHT.INDEX_WIDTH bits)
      val bhtV = out Bits (BHT.DATA_WIDTH bits)

      val phtI = in UInt (PHT.INDEX_WIDTH bits)
      val phtV = out UInt (2 bits)
    }
    val w = new Bundle {
      val bhtEn = in Bool ()
      val bhtI  = in UInt (BHT.INDEX_WIDTH bits)
      val bhtV  = in Bits (BHT.DATA_WIDTH bits)

      val phtEn = in Bool ()
      val phtI  = in UInt (PHT.INDEX_WIDTH bits)
      val phtV  = in UInt (2 bits)
    }
  }

  io.r.bhtV := (io.w.bhtEn & io.w.bhtI === io.r.bhtI) ?
    io.w.bhtV | bht.readAsync(io.r.bhtI, writeFirst)
//  io.r.phtV := (io.w.phtEn & io.w.phtI === io.r.phtI) ?
//    io.w.phtV | pht.readAsync(io.r.phtI, writeFirst)
  io.r.phtV := pht.readAsync(io.r.phtI, writeFirst)
  bht.write(io.w.bhtI, io.w.bhtV, io.w.bhtEn)
  pht.write(io.w.phtI, io.w.phtV, io.w.phtEn)
}
