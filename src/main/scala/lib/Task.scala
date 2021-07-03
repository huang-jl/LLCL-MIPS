package lib

import spinal.core._

class Task[T <: Data](assign: Bool, newValue: T, clear: Bool) {
  val th  = RegInit(False)
  val has = th | assign
  th := clear ? False | has

  val tv    = RegNextWhen(newValue, assign)
  val value = assign ? newValue | tv
}

object Task {
  def apply[T <: Data](assign: Bool, newValue: T, clear: Bool): Task[T] =
    new Task(assign, newValue, clear)
}
