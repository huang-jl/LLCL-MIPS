package lib

import spinal.core._

class Key[T <: Data](val `type`: HardType[T]) extends Nameable {
  setWeakName(this.getClass.getSimpleName.replace("$", ""))
}
