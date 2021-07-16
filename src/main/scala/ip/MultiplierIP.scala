package ip

import cpu.Utils
import ip.sim._
import spinal.core._
import spinal.core.sim._

import scala.language.postfixOps

/** @param dataWidth  被除数和除数的宽度 */
class MultiplierIP(dataWidth: Int = 32, name: String = "multiplier") extends BlackBox {
  setDefinitionName(name)
  val io = new Bundle {
    val CLK = in Bool ()
    val CE  = in Bool ()    //clock enable, active high
    val A   = in UInt (32 bits)
    val B   = in UInt (32 bits)
    val P   = out UInt (64 bits)
  }

  noIoPrefix()
  mapClockDomain(clock = io.CLK)
}
