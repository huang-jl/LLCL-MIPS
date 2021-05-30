package lib.decoder

import scala.collection.mutable._
import spinal.core._

import lib.Key

class DecoderFactory(val inputWidth: Int) {
  private[decoder] val keys = HashSet[Key[_ <: Data]]()
  private[decoder] val defaults = HashMap[Key[_ <: Data], () => Data]()
  private[decoder] val spec =
    HashMap[MaskedLiteral, HashMap[Key[_ <: Data], () => Data]]()

  class When private[DecoderFactory] (
      map: HashMap[Key[_ <: Data], () => Data]
  ) {
    def set[T <: Data](key: Key[T], value: => T) = {
      keys.add(key)
      map(key) = () => value
      this
    }

    def set[T <: SpinalEnum](
        key: Key[SpinalEnumCraft[T]],
        value: SpinalEnumElement[T]
    ): Unit = set(key, value())
  }

  def when(mask: MaskedLiteral) = {
    assert(mask.width == inputWidth)
    new When(
      spec.getOrElseUpdate(mask, HashMap())
    )
  }

  def addDefault[T <: Data](key: Key[T], default: => T) = {
    assert(!defaults.contains(key))
    keys.add(key)
    defaults(key) = () => default
  }

  def addDefault[T <: SpinalEnum](
      key: Key[SpinalEnumCraft[T]],
      default: SpinalEnumElement[T]
  ): Unit =
    addDefault(key, default())

  def checkConflicts() = ()

  def createDecoder() = {
    checkConflicts()
    new Decoder(this)
  }
}

class Decoder(factory: DecoderFactory) extends Component {
  def width = factory.inputWidth

  val io = new Bundle {
    val input = in Bits (width bits)

    private[Decoder] val outputs =
      HashMap[Key[_ <: Data], Data](((factory.keys map { key =>
        (key -> out(key.`type`()))
      }).toSeq): _*)

    def output[T <: Data](key: Key[T]): T = {
      assert(factory.keys.contains(key))
      outputs(key).asInstanceOf[T]
    }
  }

  val keys = factory.keys.toSeq

  val offsets = (keys
    .scanLeft(0) { case (currentOffset, key) =>
      currentOffset + key.`type`.getBitsWidth
    })
  val outputWidth = offsets.last

  val outputVector = B(0, outputWidth bits)

  val outputSegments: HashMap[Key[_ <: Data], Bits] = HashMap(
    (for (i <- 0 until keys.length)
      yield (keys(i) -> outputVector(offsets(i) until offsets(i + 1)))): _*
  )

  for (key <- keys) {
    io.output(key) assignFromBits outputSegments(key)
  }

  // Naive implementation
  for ((key, default) <- factory.defaults) {
    outputSegments(key) := default().asBits
  }

  for ((maskedLiteral, map) <- factory.spec) {
    // The === is aware of the mask
    when(maskedLiteral === io.input) {
      for ((key, value) <- map) {
        outputSegments(key) := value().asBits
      }
    }
  }
}
