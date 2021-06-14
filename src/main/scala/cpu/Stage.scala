package cpu

import lib.{Key, Record}
import spinal.core._

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
  val willReset     = Bool
  val reset         = RegNext(willReset) init True
  val stallsBySelf  = False                   // User settable
  val stallsByLater = False
  val firing        = !reset
  val stalls        = firing && (stallsBySelf || stallsByLater)
  val producing     = firing && !stallsBySelf // Can pass new inst to next stage
  val prevProducing = True                    // Previous stage can pass in new inst
  willReset := !(firing && stalls) && !prevProducing

  val linkingScope = GlobalData.get.currentScope

  def inLinkingScope(body: => Unit) = {
    linkingScope.push()
    val swapContext = linkingScope.swap()
    body
    swapContext.appendBack()
    linkingScope.pop()
  }

  read.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = inLinkingScope {
      value.setAsReg()
    }
  })

  input.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = inLinkingScope {
      value := read(key)
      value.allowOverride
      output += key
    }
  })

  produced.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = inLinkingScope {
      output(key) := value
    }
  })

  output.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = inLinkingScope {
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

  // Add every StageComponent defined inside the body of this.
  override def valCallbackRec(obj: Any, name: String) = {
    obj match {
      case stageComponent: StageComponent =>
        addComponent(stageComponent)
      case _ => super.valCallbackRec(obj, name)
    }
  }

  def setInputReset[T <: Data](key: Key[T], resetValue: T) = {
    read(key) init resetValue
    when(willReset) {
      read(key) := resetValue
    }
  }

  // Link read, input, produced and output as default.
  def linkIO() = {
    input.keys foreach { case key =>
      input(key) := read(key)
      if (!(produced contains key)) {
        output(key) := input(key)
      }
    }
    produced.keys foreach { case key =>
      output(key) := produced(key)
    }
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
