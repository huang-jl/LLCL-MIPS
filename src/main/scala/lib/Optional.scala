package lib

import spinal.core._
import spinal.lib._
import spinal.core.internals.BitsLiteral

class Optional[T <: Data](val `type`: HardType[T]) extends Bundle {
  // If necessary, may apply Rust-like niche optimization to this?
  val isDefined = Bool
  val bits      = Bits(widthOf(`type`) bits)

  def copy() = Optional(`type`)

  def :=(data: T) = {
    bits := data.asBits
    isDefined := True
  }
  def init(data: T) = {
    bits init data.asBits
    isDefined init True
    this
  }
  def default(data: T): Optional[T] = {
    bits default data.asBits
    isDefined default True
    this
  }

  def :=(none: None.type) = {
    bits.assignDontCare()
    isDefined := False
  }
  def init(none: None.type) = {
    isDefined init False
    this
  }
  def default(none: None.type) = {
    val defaultValue = cloneOf(bits)
    defaultValue.assignDontCare()
    bits default defaultValue
    isDefined default False
    this
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

  def isEmpty  = !isDefined
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
    } elsewhen (other.isDefined) {
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
    flow
  }
}

object Optional {
  def apply[T <: Data](`type`: HardType[T]) = new Optional(`type`)

  def fromNone[T <: Data](`type`: HardType[T]) = {
    val optional = new Optional(`type`)
    optional := None
    optional
  }

  def from[T <: Data](`type`: HardType[T])(value: T) = {
    val optional = new Optional(`type`)
    optional := value
    optional
  }

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
