package lib.decoder

import lib.{Record, Key}
import spinal.core._

import scala.collection.mutable.{HashMap, HashSet}
import scala.language.existentials

case class DecoderConfig(
    inputWidth: Int,
    keys: Seq[Key[_ <: Data]],
    defaults: Seq[(Key[T], () => T) forSome { type T <: Data }],
    encodings: Map[MaskedLiteral, Seq[
      (Key[T], () => T) forSome { type T <: Data }
    ]]
)

object NotConsidered extends Key(Bool)

class DecoderFactory(
    val inputWidth: Int,
    val enableNotConsidered: Boolean = false
) {
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
    val result = new When(encodings.getOrElseUpdate(mask, HashMap()))
    if (enableNotConsidered) {
      result.set(NotConsidered, False)
    }
    result
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

  if (enableNotConsidered) {
    addDefault(NotConsidered, True)
  }

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

  val keys = config.keys
  val output = Record(keys)

  val offsets = (keys
    .scanLeft(0) { case (currentOffset, key) =>
      currentOffset + key.`type`.getBitsWidth
    })
  val outputWidth = offsets.last

  val outputVector = B(0, outputWidth bits)

  val outputSegments: Map[Key[_ <: Data], Bits] = Map(
    (for (i <- keys.indices)
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
