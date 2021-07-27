package cpu

import spinal.core._

package object Utils {
  def hasAssignment(d: Data): Boolean = {
    d match {
      case b: BaseType => b.hasAssignement
      case o           => o.flatten.forall(b => b.hasAssignement)
    }
  }
  def hasInit(d: Data): Boolean = {
    d match {
      case b: BaseType => b.hasInit
      case o           => o.flatten.forall(b => b.hasInit)
    }
  }

  def signExtend(b: Bits, length: Int = 32) = b.asSInt.resize(length).asBits

  def zeroExtend(b: Bits, length: Int = 32) = b.asUInt.resize(length).asBits

  def instantiateWhen[T <: Data](data: => T, when: Boolean): T = {
    if (when) data else null.asInstanceOf[T]
  }

  def equalAny[T <: Data](left: T, right: T*): Bool = {
    val ans = Bits(right.length bits)
    right.zipWithIndex.foreach(ele => ans(ele._2) := (ele._1 === left))
    ans.orR
  }

  def equalAny[T <: SpinalEnum](left: SpinalEnumCraft[T], right: SpinalEnumCraft[T]*): Bool = {
    val ans = Bits(right.length bits)
    right.zipWithIndex.foreach(ele => ans(ele._2) := (ele._1 === left))
    ans.orR
  }

  /** 用于初始化CP0寄存器的 */
  implicit class IntToFixedLengthBinaryString(value: Int) {
    def toBinaryString(length: Int): String = {
      val str = Integer.toBinaryString(value)
      assert(length >= str.length)
      if (length > str.length) {
        Array.fill(length - str.length)("0").mkString.concat(str)
      } else {
        str
      }
    }
  }
}
