package cpu

import spinal.core._

package object Utils {
    def signExtend(b: Bits, length: Int = 32) = b.asSInt.resize(length).asBits
    def zeroExtend(b: Bits, length: Int = 32) = b.asUInt.resize(length).asBits
}