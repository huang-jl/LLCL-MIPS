package cpu

import cpu.Utils._
import lib._
import spinal.core._

class ComponentStage extends Area {
  val stored   = Record().setAsReg()
  val output   = Record()
  val produced = Record()

  val will = new Bundle {
    val input  = Bool().default(True)
    val output = Bool().default(False)
  }
  val can = new Bundle {
    val input = Bool().default(will.input)
  }

  def interConnect(): Unit = {
    /**/
    produced.keys.foreach { case key =>
      if (!hasAssignment(produced(key))) {
        if (output.contains(key)) { produced(key) := output(key) }
        else { produced(key) := stored(key) }
      }
    }
    /**/
  }

  def bind(that: Stage): Unit = {
    will.output := that.will.output
    can.input := that.can.input
  }

  def send(that: ComponentStage): Unit = {
    when(that.will.input) {
      that.stored.keys.foreach { case key =>
        if (hasInit(that.stored(key))) { that.stored(key) := produced(key) }
      }
    }
    when(that.can.input) {
      that.stored.keys.foreach { case key =>
        if (!hasInit(that.stored(key))) { that.stored(key) := produced(key) }
      }
    }
  }
  def receive(that: Stage): Unit = {
    when(will.input) {
      stored.keys.foreach { case key =>
        if (hasInit(stored(key))) { stored(key) := that.produced(key) }
      }
    }
    when(can.input) {
      stored.keys.foreach { case key =>
        if (!hasInit(stored(key))) { stored(key) := that.produced(key) }
      }
    }
  }

  def clearWhenEmpty[T <: Data](key: Key[T]): Unit = {
    if (!hasInit(stored(key))) {
      stored(key).init(key.emptyValue.get)
      when(will.output & !will.input) {
        stored(key) := key.emptyValue.get
      }
    }
  }
  def !!![T <: Data](key: Key[T]): T = {
    clearWhenEmpty(key)
    stored(key)
  }
}
