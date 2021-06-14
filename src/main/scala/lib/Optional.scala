package lib

import spinal.core._
import spinal.lib._

class Optional[T <: Data](val `type`: HardType[T]) extends Bundle {
  // If necessary, may apply Rust-like niche optimization to this?
  val isDefined = Bool
  val bits      = Bits(`type`.getBitsWidth bits)

  def copy() = Optional(`type`)

  def :=(data: T) = {
    bits := data.asBits
    isDefined := True
  }

  def :=(none: None.type) = {
    bits.assignDontCare()
    isDefined := False
  }

  def whenIsDefined(block: T => Unit) = {
    when(isDefined) {
      block(get)
    }
  }

  def get = {
    assert(isDefined)
    val t = `type`()
    t assignFromBits bits
    t
  }

  def isEmpty = !isDefined
  def nonEmpty = isDefined

  def getOrElse(other: T) = {
    val result = `type`()
    when(isDefined) {
      result assignFromBits bits
    } otherwise {
      result := other
    }
    result
  }

  def orElse(other: Optional[T]) = {
    val result = Optional(`type`)
    when(isDefined) {
      result := this
    } elsewhen(other.isDefined) {
      result := other
    } otherwise {
      result := None
    }
    result
  }

  def toFlow = {
    val flow = Flow(`type`)
    flow.valid := isDefined
    flow.payload assignFromBits bits
  }
}

object Optional {
  def apply[T <: Data](`type`: HardType[T]) = new Optional(`type`)

  def fromFlow[T <: Data](flow: Flow[T]) = {
    val optional = Optional(flow.payloadType)
    when(flow.valid) {
      optional := flow.payload
    } otherwise {
      optional := None
    }
    optional
  }
}
