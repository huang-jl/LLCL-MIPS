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
  private[decoder] val keys     = HashSet[Key[_ <: Data]]()
  private[decoder] val defaults = HashMap[Key[_ <: Data], () => Data]()
  private[decoder] val encodings =
    HashMap[MaskedLiteral, HashMap[Key[_ <: Data], () => Data]]()

  class When private[DecoderFactory] (
      map: HashMap[Key[_ <: Data], () => Data]
  ) {
    def set[T <: Data](key: Key[T], value: => T): this.type = {
      keys.add(key)
      map(key) = () => value
      this
    }

    def set[T <: SpinalEnum](
        key: Key[SpinalEnumCraft[T]],
        value: SpinalEnumElement[T]
    ): this.type = set(key, value())
  }

  def when(mask: MaskedLiteral) = {
    assert(mask.width == inputWidth)
    new When(encodings.getOrElseUpdate(mask, HashMap()))
  }

  def addDefault[T <: Data](key: Key[T], default: => T): this.type = {
    assert(!defaults.contains(key))
    keys.add(key)
    defaults(key) = () => default
    this
  }

  def addDefault[T <: SpinalEnum](
      key: Key[SpinalEnumCraft[T]],
      default: SpinalEnumElement[T]
  ): this.type =
    addDefault(key, default())

  def createDecoder(input: Bits) =
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
      ),
      input
    )
}

case class Decoder(
    config: DecoderConfig,
    input: Bits
) extends Area {
  assert(input.getBitsWidth == config.inputWidth)

  private[Decoder] val outputs =
    HashMap[Key[_ <: Data], Data](((config.keys map { key =>
      (key -> out(key.`type`()))
    }).toSeq): _*)

  def output[T <: Data](key: Key[T]): T = {
    assert(config.keys.contains(key))
    outputs(key).asInstanceOf[T]
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
    output(key) assignFromBits outputSegments(key)
  }

  // Naive implementation
  for ((key, default) <- config.defaults) {
    outputSegments(key) := default().asBits
  }

  for ((maskedLiteral, map) <- config.encodings) {
    // The === is aware of the mask
    when(maskedLiteral === input) {
      for ((key, value) <- map) {
        outputSegments(key) := value().asBits
      }
    }
  }
}
