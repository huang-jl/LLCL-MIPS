package lib

import spinal.core._

case class Coproduct(hardTypes: HardType[_ <: Data]*) extends Bundle {
  object Type extends SpinalEnum {
    val elementsByType = hardTypes map (t =>
      t -> newElement().setWeakName(t().getClass.getSimpleName)
    )
  }

  val `type` = Type()
  val bits = Bits((hardTypes map {
    _.getBitsWidth
  }).max bits)

  def copy() = Coproduct(hardTypes: _*)

  private def find[T <: Data](data: T) =
    Type.elementsByType
      .find {
        data.getClass == _._1().getClass
      }
      .map {
        _._2
      }
      .get

  def assignFrom[T <: Data](data: T) = {
    `type` := find(data)
    bits := data.asBits.resized
  }

  def is[T <: Data](target: HardType[T]) = `type` === find(target())

  def whenIs[T <: Data](target: HardType[T])(block: T => Unit) = {
    when(`type` === find(target())) {
      block(asType(target))
    }
    this
  }

  def asType[T <: Data](target: HardType[T]): T = {
    assert(`type` === find(target()))
    val result = target()
    result assignFromBits bits.resize(target.getBitsWidth)
    result
  }
}
