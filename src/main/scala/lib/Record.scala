package lib

import spinal.core._
import scala.collection.mutable

class Record extends Bundle {
  private val data = mutable.Map[Key[_ <: Data], Data]()

  def keys   = data.keys
  def values = data.values

  def apply[T <: Data](key: Key[T]): T = data(key).asInstanceOf[T]

  /** @note Be careful that this actually replaces the old signal with the new one.
    */
  def update[T <: Data](key: Key[T], value: T) = {
    data(key) = value
  }

  def +=[T <: Data](key: Key[T]) = {
    if (!(data contains key)) {
      val value = key.`type`().setCompositeName(this, key.getName)
      val pair  = (key, value)
      data += pair
    }
  }

  def contains[T <: Data](key: Key[T]) = data contains key
  def isEmpty                          = data.isEmpty
  def nonEmpty                         = data.nonEmpty
}

object Record {
  def apply(): Record = new Record
  def apply(key: Key[_ <: Data], keys: Key[_ <: Data]*): Record = apply(key +: keys)
  def apply(keys: Seq[Key[_ <: Data]]) = {
    val record = new Record
    for (key <- keys) {
      record += key
    }
    record
  }
}
