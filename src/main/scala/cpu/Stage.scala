package cpu

import spinal.core._
import lib.{Record, Key}

class Stage extends Area {
  @dontName protected implicit val stage = this

  // Input and output. Automatically connected to StageComponent's,
  // but actual connection user configurable.
  // read and output are interfaces of the stage,
  // while input and produced are linked to StageComponent's.
  val read               = Record() // Values read from last stage
  val input              = Record() // Values serving as StageComponent input
  val produced           = Record() // Values produced by current stage
  val output             = Record() // Values output to the next stage
  private val inputReset = Record() // Reset values for input

  // Arbitration. Note that reset comes from constructor.
  val prevProducing = True  // Previous stage is passing in a new instruction
  val stallsBySelf  = False // User settable
  val stallsByLater = False
  val stalls        = stallsBySelf || stallsByLater
  val willReset     = !stalls && !prevProducing
  val reset         = RegNext(willReset) init True
  val firing        = !reset
  val producing     = firing && !stallsBySelf

  def ensureReadKey[T <: Data](key: Key[T]) = {
    if (!(read contains key)) {
      read += key
      read(key).setAsReg()
    }
  }

  def ensureInputKey[T <: Data](key: Key[T]) = {
    if (!(input contains key)) {
      input += key
      ensureReadKey(key)
      input(key) := read(key)
      input(key).allowOverride
      if (!(output contains key)) {
        output += key
        output(key) := input(key)
      }
    }
  }

  def ensureProducedKey[T <: Data](key: Key[T]) = {
    if (!(produced contains key)) {
      produced += key
      ensureOutputKey(key)
      output(key) := produced(key)
      output(key).allowOverride
    }
  }

  def ensureOutputKey[T <: Data](key: Key[T]) = {
    if (!(output contains key)) {
      output += key
    }
  }

  /** Called at initialization of StageComponent.
    */
  def addComponent(component: StageComponent) = {
    component.input.keys foreach { case key =>
      ensureInputKey(key)
      component.input(key) := input(key)
    }

    component.produced.keys foreach { case key =>
      ensureProducedKey(key)
      produced(key) := component.produced(key)
    }

    when(component.stalls) {
      stallsBySelf := True
    }
  }

  def setInputReset[T <: Data](key: Key[T], init: T) = {
    ensureInputKey(key)
    inputReset(key) = init
//    read(key) init init Unnecessary for the assignment below?
    read(key) := init
  }

  def connect(next: Stage) = {
    when(!this.producing) {
      next.prevProducing := False
    }

    next.read.keys foreach { case key =>
      if (!(output contains key)) {
        ensureInputKey(key)
      }
    }
    when(next.stalls) {
      stallsByLater := True
    } elsewhen(producing) {
      next.read.keys foreach { case key =>
        next.read(key) := output(key)
      }
    }
  }
}

class StageComponent(
    inputKeys: Seq[Key[_ <: Data]] = Seq(),
    producedKeys: Seq[Key[_ <: Data]] = Seq(),
    val stalls: Bool = False
)(implicit stage: Stage)
    extends Area {
  val input    = Record(inputKeys)
  val produced = Record(producedKeys)

  stage.addComponent(this)
}
