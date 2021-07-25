package cpu

import lib._
import spinal.core._
import spinal.lib._

class ComponentStage extends Area {
  val stored   = Record().setAsReg()
  val output   = Record()
  val produced = Record()

  val will = new Bundle {
    val input  = Bool()
    val output = Bool()
  }

  stored.whenAddedKey(new Record.AddedKeyCallback {
    override def apply[T <: Data](key: Key[T], value: T): Unit = {
      key.emptyValue match {
        case Some(v) =>
          value.init(v)
          when(!will.input & will.output) { value := v }
        case None =>
      }
    }
  })
  produced.whenAddedKey(new Record.AddedKeyCallback {
    override def apply[T <: Data](key: Key[T], value: T): Unit = {
      if (output.contains(key)) { value := output(key) }
      else { value := stored(key) }
    }
  })

  def send(that: ComponentStage): Unit = {
    that.stored.keys.foreach { case key =>
      when(that.will.input) { that.stored(key) := produced(key) }
    }
  }
  def send(that: Stage): Unit = {
    produced.keys.foreach { case key =>
      when(that.will.input) { that.stored(key) := produced(key) }
    }
  }
  def receive(that: Stage): Unit = {
    stored.keys.foreach { case key =>
      when(will.input) { stored(key) := that.produced(key) }
    }
  }
}
