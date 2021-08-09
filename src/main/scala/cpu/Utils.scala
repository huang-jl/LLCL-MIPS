package cpu

import spinal.core._
import spinal.lib._
import scala.language.postfixOps

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

  def instantiateWhen[T <: SpinalEnum](data: => SpinalEnumElement[T], when: Boolean): SpinalEnumElement[T] = {
    if (when) data else null.asInstanceOf[SpinalEnumElement[T]]
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

  object VAddr {
    def isUncachedSection(vaddr: UInt): Bool = {
      vaddr(29, 3 bits) === U"3'b101"
    }

    def isUnmappedSection(vaddr: UInt): Bool = {
      vaddr(30, 2 bits) === U"2'b10"
    }

    def isKseg0(vaddr: UInt): Bool = {
      vaddr(29, 3 bits) === U"100"
    }

    def isKseg1(vaddr: UInt): Bool = {
      vaddr(29, 3 bits) === U"101"
    }

    def isUseg(vaddr: UInt): Bool = {
      vaddr(31).asUInt === U"0"
    }

    def isUncached(vaddr: UInt, cacheAttr: Bits, K0: Bits, ERL: Bool): Bool = {
      //Uncache的情况：
      //1. 落在Uncached区域(kseg1)
      //2. 在mapped区域并且tlb的cacheAttr为2
      //3. 在kseg0并且此时CP0.Config.K0 = 2
      //4. 在kuseg并且此时CP0.Status.ERL = 1
      isUncachedSection(vaddr) |
        (!isUnmappedSection(vaddr) & cacheAttr === 2) |
        (isKseg0(vaddr) & K0 === 2) |
        (isUseg(vaddr) & ERL)
    }

    def isUnmapped(vaddr: UInt, ERL: Bool): Bool = {
      //Unmapped的情况
      //1. 在Unmapped区域
      //2. 在Kuseg并且CP0.Status.ERL = 1
      isUnmappedSection(vaddr) | (isUseg(vaddr) & ERL)
    }
  }

}
