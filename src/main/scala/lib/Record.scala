package lib

import spinal.core._
import scala.collection.mutable

class Record extends Bundle {
  private val data              = mutable.Map[Key[_ <: Data], Data]()
  private val addedKeyCallbacks = mutable.ArrayBuffer[Record.AddedKeyCallback]()

  def keys   = data.keys
  def values = data.values

  def apply[T <: Data](key: Key[T]): T = {
    this += key
    data(key).asInstanceOf[T]
  }

  // Add a default value for the key if not exists
  def +=[T <: Data](key: Key[T]) = {
    if (!(data contains key)) {
      // Pretend the value was generated in the same scope as the record.
      GlobalData.get.dslScope.push(parentScope)

      val value = key.`type`().setCompositeName(this, key.getName)
      val pair  = (key, value)
      data += pair

      for (cb <- addedKeyCallbacks) {
        cb(key, value)
      }

      GlobalData.get.dslScope.pop()
    }
  }

  def whenAddedKey(cb: Record.AddedKeyCallback) = {
    addedKeyCallbacks += cb
  }

  def contains[T <: Data](key: Key[T]) = data contains key
  def isEmpty                          = data.isEmpty
  def nonEmpty                         = data.nonEmpty
}

object Record {
  def apply(): Record = new Record
  def apply(key: Key[_ <: Data], keys: Key[_ <: Data]*): Record = apply(
    key +: keys
  )
  def apply(keys: Seq[Key[_ <: Data]]) = {
    val record = new Record
    for (key <- keys) {
      record += key
    }
    record
  }

  trait AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T): Unit
  }
}
