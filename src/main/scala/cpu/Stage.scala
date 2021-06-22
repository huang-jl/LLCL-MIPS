package cpu

import lib.{Key, Optional, Record, Updating}
import spinal.core._

class Stage extends Area with ValCallbackRec {
  // Input and output. Automatically connected to StageComponent's,
  // but actual connection user configurable.
  // read and output are interfaces of the stage,
  // while input and produced are linked to StageComponent's.
  val read                 = Record() // Values read from last stage
  val input                = Record() // Values serving as StageComponent input
  val produced             = Record() // Values produced by current stage
  val output               = Record() // Values output to the next stage
  private val inputStored  = Record()
  private val outputStored = Record()
  val currentInput         = Record()
  val currentOutput        = Record()

  val exceptionToRaise = Optional(EXCEPTION()) default None
  val prevException    = Reg(Optional(EXCEPTION())) init None
  val exception =
    Updating(Optional(EXCEPTION())) init Optional.fromNone(EXCEPTION())

  // Start of arbitration.
  // States
  val empty   = Updating(Bool) init True
  val idle    = Updating(Bool) init True
  val toFlush = Reg(Bool) init False

  // Signals, user settable
  val stalls       = False.allowOverride
  val signalsFlush = False.allowOverride
  val flushable    = True.allowOverride

  // Connections (Default values are for the unconnected)
  val prevOutputReady  = Bool default True
  val nextWaitingInput = Bool default True
  val receiving        = Bool default True

  // Calculated. Should be hard to read, since it is already hard to write.
  val firing    = !empty.prev && !idle.prev
  val finishing = firing && !stalls

  val wantsFlush = toFlush || signalsFlush
  val doesFlush  = wantsFlush && flushable
  toFlush := wantsFlush && !doesFlush

  val outputReady = !empty.prev && !wantsFlush && (idle.prev || finishing)
  val sending     = outputReady && nextWaitingInput

  val waitingInput = empty.prev || doesFlush || sending

  empty.next := !receiving && waitingInput
  idle.next := !empty.prev && !receiving && !sending && (idle.prev || finishing) && !doesFlush

  val willReset = empty.next || idle.next
  val valid     = !empty.prev

  when(firing) {
    exception.next := exception.prev orElse exceptionToRaise
  }

  // End of arbitration.

  val currentScope = GlobalData.get.currentScope

  def atTheBeginning(body: => Unit) = {
    currentScope.push()
    val swapContext = currentScope.swap()
    body
    swapContext.appendBack()
    currentScope.pop()
  }

  read.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      value.setAsReg()
    }
  })

  input.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      value := read(key)
      value.allowOverride
      output += key

      inputStored(key).setAsReg()
      when(firing) {
        inputStored(key) := value
        currentInput(key) := value
      } otherwise {
        currentInput(key) := inputStored(key)
      }
    }
  })

  produced.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      output(key) := value
      value.allowOverride
    }
  })

  output.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      value.allowOverride
      if (!(produced contains key)) {
        value := input(key)
      }

      outputStored(key).setAsReg()
      when(finishing) {
        outputStored(key) := value
      }

      currentOutput(key) := finishing ? value | outputStored(key)
    }
  })

  currentInput.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      input += key
    }
  })

  currentOutput.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      output += key
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

  def connect(next: Stage) = {
    next.prevOutputReady := outputReady
    next.receiving := sending
    nextWaitingInput := next.waitingInput

    when(next.signalsFlush) {
      signalsFlush := True
    }

    when(sending) {
      next.prevException := exception.next
      next.exception.prev := exception.next
      when(exception.next.isDefined) {
        next.willReset := True
      }
    }

    // Prioritize reset over send.
    atTheBeginning {
      when(sending) {
        next.read.keys foreach { case key =>
          next.read(key) := currentOutput(key)
        }
      }
    }
  }
}

class StageComponent extends Area {
  val input    = Record()
  val produced = Record()
}
