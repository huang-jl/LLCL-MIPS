package cpu

import lib.{Key, Optional, Record, Updating}
import spinal.core._

class Stage extends Area with ValCallbackRec {
  //
  val stored = Record() // stage input
  val input = Record() // stage component input
  val output = Record() // stage component output
  val produced = Record() // stage output

  //
  val exceptionToRaise = Optional(EXCEPTION()) default None // User settable
  val prevException = Reg(Optional(EXCEPTION())) init None
  val exception = Optional(EXCEPTION())
  exception := prevException orElse exceptionToRaise

  //
  val is = new Area {
    val empty = Reg(Bool)
    val done = True.allowOverride // 用户定义
  }
  //
  val can = new Area {
    val flush = True.allowOverride // 用户定义
  }
  //
  val want = new Area {
    val flush = False.allowOverride // 用户定义
  }
  //
  val will = new Area {
    val flush = Bool
    val input = Bool
    val output = Bool
  }
  //
  val prev = new Area {
    val is = new Area {
      val empty = False.allowOverride
    }
    val will = new Area {
      val flush = False.allowOverride
      val output = True.allowOverride
    }
  }
  val next = new Area {
    val will = new Area {
      val output = True.allowOverride
    }
  }
  //
  is.empty := will.input ? prev.is.empty | will.output
  will.flush := want.flush & can.flush
  will.input := prev.will.output & !prev.will.flush // output 到 input
  will.output := next.will.output & is.done | will.flush // output 到 input / output 到 trash

  //
  val currentScope = GlobalData.get.currentScope

  // Add every StageComponent defined inside the body of this.
  override def valCallbackRec(obj: Any, name: String) = {
    obj match {
      case stageComponent: StageComponent =>
        addComponent(stageComponent)
      case _ => super.valCallbackRec(obj, name)
    }
  }

  stored.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      value.setAsReg
    }
  })

  input.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      value := stored(key)
    }
  })

  output.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      produced(key) := value
    }
  })

  produced.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      if (!(output contains key)) {
        value := stored(key)
      }
    }
  })

  // Called at initialization of StageComponent.
  def addComponent(component: StageComponent) = {
    component.input.keys foreach { case key => component.input(key) := input(key) }
    component.output.keys foreach { case key => output(key) := component.output(key) }

    component.input.whenAddedKey(new Record.AddedKeyCallback {
      def apply[T <: Data](key: Key[T], value: T) = {
        value := input(key)
      }
    })

    component.output.whenAddedKey(new Record.AddedKeyCallback {
      def apply[T <: Data](key: Key[T], value: T) = {
        output(key) := value
      }
    })
  }

  // Set reset value for a specific input key.
  // If not set, then that input is preserved een when willReset.
  def setInputReset[T <: Data](key: Key[T], resetValue: T) = {
    when(!will.input & will.output) {
      stored(key) := resetValue
    }
  }

  def connect(next: Stage) = {
    next.prev.is.empty := is.empty
    next.prev.will.flush := will.flush
    next.prev.will.output := will.output
    this.next.will.output := next.will.output

    when(next.will.input) {
      next.prevException := exception
    }

    // Prioritize reset over send.
    atTheBeginning {
      when(next.will.input) {
        next.stored.keys foreach { case key => next.stored(key) := produced(key) }
      }
    }
  }

  def atTheBeginning(body: => Unit) = {
    currentScope.push()
    val swapContext = currentScope.swap()
    body
    swapContext.appendBack()
    currentScope.pop()
  }
}

class StageComponent extends Area {
  val input = Record()
  val output = Record()
}
