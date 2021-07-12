package util

import scala.language.reflectiveCalls

object Using {
  def apply[T <: { def close() }, U](resource: T)(block: T => U): U = {
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }
}
