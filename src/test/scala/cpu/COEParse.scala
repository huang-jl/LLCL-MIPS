package cpu

import util.Using

import java.io.File
import scala.io.Source
import scala.util.{Success, Try}
import scala.collection.mutable

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

      for (line <- lines if line.trim.nonEmpty) {
        val numeral = if (line.last == ',') line.init else line
        for (byte <- BigInt(numeral, radix = radix).toByteArray.reverseIterator) {
          buffer += byte
        }
      }

      buffer.toArray
    }
  }
}
