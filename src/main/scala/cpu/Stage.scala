package cpu

import cpu.Utils._
import lib._
import spinal.core._

class Stage extends Area with ValCallbackRec {
  //
  val stored   = Record().setAsReg() // stage input
  val input    = Record()            // stage component input
  val output   = Record()            // stage component output
  val produced = Record()            // stage output

  //
  val exceptionToRaise = Optional(EXCEPTION()) default None // User settable
  val prevException    = Reg(Optional(EXCEPTION())) init None default None
  val exception        = Optional(EXCEPTION())
  exception := prevException orElse exceptionToRaise

  //
  val is = new Bundle {
    val empty = RegInit(True)      // 当前该流水段是否为空转
    val done  = True allowOverride // 当前该流水段是否已完成工作 用户定义
  }
  //
  val can = new Bundle {
    val flush = True allowOverride // 当前该流水段能否进行结果的作废 用户定义
    val input = Bool               // 该流水段在下一个上升沿是否能接受一条有效指令
  }
  //
  val assign = new Bundle {
    val flush = False allowOverride // 是否要布置结果作废的任务 用户定义
  }
  //
  val to = new Bundle {
    val flush = RegInit(False) // 是否有结果作废的任务
  }
  //
  val want = new Bundle {
    val flush = Bool // 该流水段当前是否需要进行结果的作废
  }
  //
  val will = new Bundle {
    val flush  = Bool               // 当前处理结果将被作废
    val input  = True allowOverride // 有一条有效指令将要移入
    val output = True allowOverride // 有一条有效指令将要移出
  }
  //
  is.empty := can.input & !will.input

  can.input := is.empty | will.output

  to.flush := want.flush & !can.flush

  want.flush := to.flush | assign.flush

  will.flush := want.flush & can.flush

  //
  val currentScope = DslScopeStack.get

  // Add every StageComponent defined inside the body of this.
  override def valCallbackRec(obj: Any, name: String) = {
    obj match {
      case stageComponent: StageComponent => addComponent(stageComponent)
      case _                              => super.valCallbackRec(obj, name)
    }
  }

  def interConnect(): Unit = {
    /**/
    produced.keys.foreach { case key =>
      if (!hasAssignment(produced(key))) {
        if (output.contains(key)) { produced(key) := output(key) }
        else { produced(key) := input(key) }
      }
    }
    /**/
    input.keys.foreach { case key =>
      if (!hasAssignment(input(key))) {
        input(key) := stored(key)
      }
    }
    /**/
  }

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

  def connect(next: Stage, canPass: Bool) = {
    next.will.input := will.output & !will.flush
    will.output := !is.empty & (will.flush | is.done & canPass)

    when(next.will.input) {
      next.prevException := exception
    } elsewhen next.will.output {
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

  def clearWhenEmpty[T <: Data](key: Key[T]): Unit = {
    if (!hasInit(stored(key))) {
      stored(key).init(key.emptyValue.get)
      when(will.output & (!will.input | prevException.nonEmpty)) {
        stored(key) := key.emptyValue.get
      }
    }
  }
  def !!![T <: Data](key: Key[T]): T = {
    clearWhenEmpty(key)
    stored(key)
  }
}

class StageComponent extends Area {
  val input  = Record()
  val output = Record()
}
