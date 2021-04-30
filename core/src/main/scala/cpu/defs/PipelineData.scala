package cpu.defs

import spinal.core._

case class FetchData(width: Int) extends Bundle{
  val pcOut = Bits(width bits)
  val instOut = Bits(width bits)
  val stall = Bool
}
