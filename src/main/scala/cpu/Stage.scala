package cpu

import spinal.core._
import lib.{Record, Key}

class Stage extends Area with ValCallbackRec {
  // Input and output. Automatically connected to StageComponent's,
  // but actual connection user configurable.
  // read and output are interfaces of the stage,
  // while input and produced are linked to StageComponent's.
  val read     = Record() // Values read from last stage
  val input    = Record() // Values serving as StageComponent input
  val produced = Record() // Values produced by current stage
  val output   = Record() // Values output to the next stage

  // Arbitration. Note that reset comes from constructor.
  val prevProducing = True  // Previous stage is passing in a new instruction
  val stallsBySelf  = False // User settable
  val stallsByLater = False
  val stalls        = stallsBySelf || stallsByLater
  val willReset     = !stalls && !prevProducing
  val reset         = RegNext(willReset) init True
  val firing        = !reset
  val producing     = firing && !stallsBySelf

  // Default linkings between read, input, produced and output.

  read.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = {
      value.setAsReg()
    }
  })

  input.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = {
      value := read(key)
      value.allowOverride
      output += key
    }
  })

  produced.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = {
      output(key) := value
    }
  })

  output.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = {
      value.allowOverride
      if (!(produced contains key)) {
        value := input(key)
      }
    }
  })

  /** Called at initialization of StageComponent.
    */
  def addComponent(component: StageComponent) = {
    component.input.keys foreach { case key =>
      component.input(key) := input(key)
    }

    component.produced.keys foreach { case key =>
      produced(key) := component.produced(key)
    }

    component.input.whenAddedKey(new Record.AddedKeyCallback {
      def apply[T <: Data](key: Key[T], value: T) = {
        value := input(key)
      }
    })

    component.produced.whenAddedKey(new Record.AddedKeyCallback {
      def apply[T <: Data](key: Key[T], value: T) = {
        produced(key) := value
      }
    })

    when(component.stalls) {
      stallsBySelf := True
    }
  }

  override def valCallbackRec(obj: Any, name: String) = {
    obj match {
      case stageComponent: StageComponent =>
        addComponent(stageComponent)
      case _ => super.valCallbackRec(obj, name)
    }
  }

  def setInputReset[T <: Data](key: Key[T], init: T) = {
    read(key) init init
    read(key) := init
  }

  def connect(next: Stage) = {
    when(!this.producing) {
      next.prevProducing := False
    }

    when(next.stalls) {
      stallsByLater := True
    } elsewhen (producing) {
      next.read.keys foreach { case key =>
        next.read(key) := output(key)
      }
    }
  }
}

class StageComponent extends Area {
  val input    = Record()
  val produced = Record()
  val stalls   = False
}
