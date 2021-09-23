package lib

import spinal.core._
import scala.collection.mutable

class Record extends Bundle {
  private val data              = mutable.Map[Key[_ <: Data], Data]()
  private val addedKeyCallbacks = mutable.ArrayBuffer[Record.AddedKeyCallback]()
  
  private var direction: Option[IODirection] = None
  private var btFlags = 0

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
      parentScope.push()
      val value = key.`type`().setCompositeName(this, key.getName)
      
      direction match {
        case Some(d) => d(value)
        case None =>
      }
      if ((btFlags & BaseType.isRegMask) != 0) value.setAsReg()
      if ((btFlags & BaseType.isAnalogMask) != 0) value.setAsAnalog()

      parentScope.pop()

      val pair = (key, value)
      data += pair

      for (cb <- addedKeyCallbacks) {
        cb(key, value)
      }
    }
  }

  def whenAddedKey(cb: Record.AddedKeyCallback) = {
    addedKeyCallbacks += cb
  }
  
  override def asInput(): this.type = {
    direction = Some(in)
    super.asInput()
  }

  override def asOutput(): this.type = {
    direction = Some(out)
    super.asOutput()
  }

  override def asInOut(): this.type = {
    direction = Some(inout)
    super.asInOut()
  }

  override def setAsDirectionLess(): this.type = {
    direction = None
    super.setAsDirectionLess()
  }

  override def copyDirectionOfImpl(that: Data): this.type = {
    direction = that.asInstanceOf[Record].direction
    super.copyDirectionOfImpl(that)
  }

  override def setAsReg(): this.type = {
    btFlags |= BaseType.isRegMask
    super.setAsReg()
  }

  override def setAsAnalog(): this.type = {
    btFlags |= BaseType.isAnalogMask
    super.setAsAnalog()
  }

  override def setAsComb(): this.type = {
    btFlags &= ~(BaseType.isRegMask | BaseType.isAnalogMask)
    super.setAsComb()
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
