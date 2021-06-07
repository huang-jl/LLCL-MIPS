package lib

import spinal.core._
import scala.collection.mutable

case class HardMap(keys: Seq[Key[_ <: Data]]) extends Bundle {
  private val valuesByKey = mutable.Map((keys map { key =>
    key -> (
      key.`type`().setCompositeName(this, key.getName)
    )
  }): _*)

  def values = valuesByKey.values

  def apply[T <: Data](key: Key[T]): T = valuesByKey(key).asInstanceOf[T]

  def update[T <: Data](key: Key[T], value: T) = valuesByKey(key) = value
}
