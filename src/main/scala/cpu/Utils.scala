package cpu

import spinal.core._

package object Utils {
  def signExtend(b: Bits, length: Int = 32) = b.asSInt.resize(length).asBits

  def zeroExtend(b: Bits, length: Int = 32) = b.asUInt.resize(length).asBits

  def instantiateWhen[T <: Data](data: => T, when: Boolean): T = {
    if (when) data else null.asInstanceOf[T]
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
