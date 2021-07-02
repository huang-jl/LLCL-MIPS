package lib

import spinal.core._

case class Key[T <: Data](val `type`: HardType[T]) extends Nameable {
  setWeakName(this.getClass.getSimpleName.replace("$", ""))

  var emptyValue = Option.empty[T]
  def setEmptyValue(v: T) = {
    emptyValue = Some(v)

    this
  }
}
