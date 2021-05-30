package lib.decoder

import scala.collection
import scala.collection.mutable.{HashSet, HashMap}
import scala.language.existentials
import spinal.core._

import lib.Key

case class DecoderConfig(
    inputWidth: Int,
    keys: Seq[Key[_ <: Data]],
    defaults: Seq[(Key[T], () => T) forSome { type T <: Data }],
    encodings: Map[MaskedLiteral, Seq[
      (Key[T], () => T) forSome { type T <: Data }
    ]]
)

class DecoderFactory(val inputWidth: Int) {
  // Mutable constituents of DecoderConfig
  private[decoder] val keys = HashSet[Key[_ <: Data]]()
  private[decoder] val defaults = HashMap[Key[_ <: Data], () => Data]()
  private[decoder] val encodings =
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
    new When(encodings.getOrElseUpdate(mask, HashMap()))
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

  def createDecoder() =
    Decoder(
      DecoderConfig( // Creates immutable DecoderConfig
        inputWidth,
        keys.toSeq,
        defaults.toSeq
          .asInstanceOf[Seq[(Key[T], () => T) forSome { type T <: Data }]],
        encodings.map { case (maskedLiteral, map) =>
          (maskedLiteral -> (map.toSeq.asInstanceOf[Seq[
            (Key[T], () => T) forSome { type T <: Data }
          ]]))
        }.toMap
      )
    )
}

case class Decoder(
    config: DecoderConfig
) extends Component {
  val io = new Bundle {
    val input = in Bits (config.inputWidth bits)

    private[Decoder] val outputs =
      HashMap[Key[_ <: Data], Data](((config.keys map { key =>
        (key -> out(key.`type`()))
      }).toSeq): _*)

    def output[T <: Data](key: Key[T]): T = {
      assert(config.keys.contains(key))
      outputs(key).asInstanceOf[T]
    }
  }

  val keys = config.keys

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
  for ((key, default) <- config.defaults) {
    outputSegments(key) := default().asBits
  }

  for ((maskedLiteral, map) <- config.encodings) {
    // The === is aware of the mask
    when(maskedLiteral === io.input) {
      for ((key, value) <- map) {
        outputSegments(key) := value().asBits
      }
    }
  }
}
