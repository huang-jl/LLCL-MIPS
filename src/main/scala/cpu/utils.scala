package cpu

import spinal.core._

package object Utils {
  def signExtend(b: Bits, length: Int = 32) = b.asSInt.resize(length).asBits

  def zeroExtend(b: Bits, length: Int = 32) = b.asUInt.resize(length).asBits

  def instantiateWhen[T <: Data](data: HardType[T], when: Boolean): T = {
    val signal: T = if (when) data() else null.asInstanceOf[T]
    signal
  }

  def instantiateWhen[T <: Data](data: HardType[T], when: Boolean, direction: IODirection): T = {
    val signal: T = if (when) data() else null.asInstanceOf[T]
    if (when) {
      direction(signal)
    } else {
      signal
    }
  }

  def equalAny[T <: Data](left: T, right: T*): Bool = {
    val ans = Bits(right.length bits)
    right.zipWithIndex.foreach(ele  => ans(ele._2) := (ele._1 === left))
    ans.orR
  }

  def equalAny[T <: SpinalEnum](left: SpinalEnumCraft[T], right: SpinalEnumCraft[T]*): Bool = {
    val ans = Bits(right.length bits)
    right.zipWithIndex.foreach(ele  => ans(ele._2) := (ele._1 === left))
    ans.orR
  }

}
