package cpu

import spinal.core._

class BPU(
    bhtNumEntries: Int,
    bhtIndexWidth: Int,
    bhtDataWidth: Int,
    phtNumEntries: Int,
    phtIndexWidth: Int
) extends Component {
  val bht = Mem(Bits(bhtDataWidth bits), bhtNumEntries) randBoot
  val pht = Mem(UInt(2 bits), phtNumEntries) randBoot

  val io = new Bundle {
    val r = new Bundle {
      val bhtI = in UInt (bhtIndexWidth bits)
      val bhtV = out Bits (bhtDataWidth bits)

      val phtI = in UInt (phtIndexWidth bits)
      val phtV = out UInt (2 bits)
    }
    val w = new Bundle {
      val bhtEn = in Bool ()
      val bhtI  = in UInt (bhtIndexWidth bits)
      val bhtV  = in Bits (bhtDataWidth bits)

      val phtEn = in Bool ()
      val phtI  = in UInt (phtIndexWidth bits)
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
