package lib

import spinal.core._
import spinal.lib._

case class Optional[T <: Data](dataType: HardType[T]) extends Bundle {
  // If necessary, may apply Rust-like niche optimization to this?
  val isDefined = Bool
  val value     = dataType()

  def copy() = Optional(dataType)

  def :=(data: T) = {
    value := data
    isDefined := True
  }
  def init(data: T) = {
    value init data
    isDefined init True
    this
  }
  def default(data: T): Optional[T] = {
    value default data
    isDefined default True
    this
  }

  def :=(none: None.type) = {
    value.assignDontCare()
    isDefined := False
  }
  def init(none: None.type) = {
    isDefined init False
    this
  }
  def default(none: None.type) = {
    val defaultValue = dataType()
    defaultValue.assignDontCare()
    value default defaultValue
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
    value
  }

  def isEmpty  = !isDefined
  def nonEmpty = isDefined

  def getOrElse(other: T) = {
    val result = dataType()
    when(isDefined) {
      result := value
    } otherwise {
      result := other
    }
    result
  }

  def orElse(other: Optional[T]) = {
    val result = Optional(dataType)
    when(isDefined) {
      result := this
    } otherwise {
      result := other
    }
    result
  }

  def toFlow = {
    val flow = Flow(dataType)
    flow.valid := isDefined
    flow.payload := value
    flow
  }
}

object Optional {
  def noneOf[T <: Data](`type`: HardType[T]) = {
    val optional = new Optional(`type`)
    optional := None
    optional
  }

  def some[T <: Data](value: T) = {
    val optional = new Optional(value)
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
