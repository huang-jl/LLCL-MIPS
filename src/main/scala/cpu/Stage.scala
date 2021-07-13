package cpu

import lib.{Key, Optional, Record}
import spinal.core._

class Stage extends Area with ValCallbackRec {
  //
  val stored   = Record().setAsReg() // stage input
  val input    = Record()            // stage component input
  val output   = Record()            // stage component output
  val produced = Record()            // stage output

  //
  val exceptionToRaise = Optional(EXCEPTION()) default None // User settable
  val prevException    = Reg(Optional(EXCEPTION())) init None
  val exception        = Optional(EXCEPTION())
  exception := prevException orElse exceptionToRaise

  //
  val is = new Bundle {
    val empty = RegInit(True)      // 当前该流水段是否为空转
    val done  = True.allowOverride // 当前该流水段是否已完成工作 用户定义
  }
  //
  val can = new Bundle {
    val flush = True.allowOverride // 当前该流水段能否进行结果的作废 用户定义
  }
  //
  val want = new Bundle {
    val flush = False.allowOverride // 是否要布置结果作废的任务 用户定义
  }
  //
  val to = new Bundle {
    val flush = RegInit(False) // 是否有结果作废的任务
  }
  //
  val will = new Bundle {
    val flush  = Bool // 当前处理结果将被作废
    val input  = Bool // 有一条有效指令将要移入
    val output = Bool // 有一条有效指令将要移出
  }
  //
  val prev = new Bundle {
    val will = new Bundle {
      val flush  = False.allowOverride
      val output = True.allowOverride
    }
  }
  val next = new Bundle {
    val is = new Bundle {
      val empty = False.allowOverride
    }
    val will = new Bundle {
      val output = True.allowOverride
    }
  }
  //
  is.empty := !will.input & (is.empty | will.output)
  to.flush := (to.flush | want.flush) & !can.flush
  will.flush := (to.flush | want.flush) & can.flush
  will.input := prev.will.output & !prev.will.flush
  will.output := !is.empty & (will.flush | is.done & (next.is.empty | next.will.output))

  //
  val currentScope = DslScopeStack.get

  // Add every StageComponent defined inside the body of this.
  override def valCallbackRec(obj: Any, name: String) = {
    obj match {
      case stageComponent: StageComponent => addComponent(stageComponent)
      case _                              => super.valCallbackRec(obj, name)
    }
  }

  stored.whenAddedKey(new Record.AddedKeyCallback {
    def apply[T <: Data](key: Key[T], value: T) = atTheBeginning {
      value.setAsReg()

      key.emptyValue match {
        case Some(v) =>
          value init v
          when(will.output && (!will.input || prevException.nonEmpty)) { value := v }
        case None =>
      }
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
      value.allowOverride
      if (!(output contains key)) {
        value := stored(key)
      }
    }
  })

  // Called at initialization of StageComponent.
  def addComponent(component: StageComponent) = {
    component.input.keys foreach { case key =>
      component.input(key) := input(key)
    }
    component.output.keys foreach { case key =>
      output(key) := component.output(key)
    }

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

  def connect(next: Stage) = {
    next.prev.will.flush := will.flush
    next.prev.will.output := will.output
    this.next.is.empty := next.is.empty
    this.next.will.output := next.will.output

    when(next.will.input) {
      next.prevException := exception
    } elsewhen (next.will.output) {
      next.prevException := None
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
  val input  = Record()
  val output = Record()
}
