package lib.decoder

import scala.collection.mutable._
import spinal.core._

import lib.Key

class DecoderFactory(val inputWidth: Int) {
  private[decoder] val keys = HashSet[Key[_ <: Data]]()
  private[decoder] val defaults = HashMap[Key[_ <: Data], Bits]()
  private[decoder] val spec =
    HashMap[MaskedLiteral, HashMap[Key[_ <: Data], Bits]]()

  class When private[DecoderFactory] (map: HashMap[Key[_ <: Data], Bits]) {
    def set[T <: Data](key: Key[T], value: T) = {
      assert(!map.contains(key))
      keys.add(key)
      map(key) = value.asBits
    }
  }

  def when(mask: MaskedLiteral) = {
    assert(mask.width == inputWidth)
    new When(
      spec.getOrElseUpdate(mask, HashMap())
    )
  }

  def addDefault[T <: Data](key: Key[T], default: T) = {
    assert(!defaults.contains(key))
    keys.add(key)
    defaults(key) = default.asBits
  }

  def createDecoder() = new Decoder(this)
}

class Decoder(factory: DecoderFactory) extends Component {
  def width = factory.inputWidth

  private[Decoder] case class IOBundle() extends Bundle {
    val input = in Bits (width bits)

    private[Decoder] val outputs =
      HashMap[Key[_ <: Data], Data](((factory.keys map { key =>
        (key, out(key.`type`()))
      }).toSeq): _*)

    def output[T <: Data](key: Key[T]): T = {
      assert(factory.keys.contains(key))
      outputs(key).asInstanceOf[T]
    }
  }

  val io = IOBundle()

  val keys = factory.keys.toSeq

  val offsetsAndOutputWidth = (keys
    .scanLeft(0) { case (currentOffset, key) =>
      currentOffset + key.`type`.getBitsWidth
    })
  val offsets = offsetsAndOutputWidth.init zip offsetsAndOutputWidth.tail
  val outputWidth = offsetsAndOutputWidth.last

  val outputVector = Bits(outputWidth bits)
  for ((key, offset) <- keys zip offsets) {
    io.outputs(key) := outputVector(offset._1 to offset._2)
  }

  // Naive implementation
  for ((key, offset) <- keys zip offsets) {
    factory.defaults.get(key) match {
      case Some(default) =>
        outputVector(offset._1 to offset._2) := default.asBits
      case None =>
    }
  }

  for ((maskedLiteral, map) <- factory.spec) {
    // The === is aware of the mask
    when(maskedLiteral === io.input) {
      for ((key, offset) <- keys zip offsets) {
        map.get(key) match {
          case Some(value) =>
            outputVector(offset._1 to offset._2) := value.asBits
          case None =>
        }
      }
    }
  }
}
