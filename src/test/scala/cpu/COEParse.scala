package cpu

import util.Using
import util.binary._

import java.io.File
import scala.collection.mutable
import scala.io.Source
import scala.util.Try

object COEParse {
  val radixRegex  = raw"memory_initialization_radix = ([1-9][0-9]*);?".r
  val vectorRegex = raw"memory_initialization_vector =".r

  def apply(file: File): Try[Array[Byte]] = Try {
    Using(Source.fromFile(file)) { source =>
      val lines = source.getLines
      val radix = lines.next match {
        case radixRegex(radixString) =>
          radixString.toInt
        case _ =>
          throw new Exception(s"Invalid radix in COE file ${file.getName}")
      }
      val buffer = mutable.ArrayBuffer[Byte]()

      lines.next match {
        case vectorRegex() =>
        case _ =>
          throw new Exception(s"Invalid format")
      }

      val widthInBytes = 4

      var x = 0
      for (line <- lines if line.trim.nonEmpty) {
        val numeral = if (line.last == ',') line.init else line

        x += 1
        for (byte <- BigInt(numeral, radix = radix).toBinary(widthInBytes)) {
          buffer += byte
        }
      }

      buffer.toArray
    }
  }
}
