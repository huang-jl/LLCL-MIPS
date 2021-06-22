package lib

import spinal.core._

case class Updating[T <: Data](val dataType: HardType[T]) extends Bundle {
  val next = dataType().allowOverride
  val prev = RegNext(next)
  next := prev

  def init(that: T) = {
    prev init that
    this
  }
}
