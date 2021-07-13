package lib

import spinal.core._
import spinal.idslplugin.PostInitCallback

import scala.collection.mutable
import scala.language.existentials
import scala.language.postfixOps

object NotConsidered extends Key(Bool)

class Decoder(inputWidth: BitCount, enableNotConsidered: Boolean = false) extends Component {
  private val keys     = mutable.Set[Key[_ <: Data]]()
  private val defaults = mutable.Map[Key[_ <: Data], () => Data]()
  private val encodings =
    mutable.Map[MaskedLiteral, mutable.Map[Key[_ <: Data], () => Data]]()

  val input  = in Bits (inputWidth)
  val output = out(Record(keys.toSeq))

  case class default[T <: Data](key: Key[T]) {
    keys.add(key)

    def to(value: => T): Unit = {
      assert(!(defaults contains key))
      defaults(key) = () => value
    }

    def to[E <: SpinalEnum](value: => SpinalEnumElement[E])(implicit
        ev: =:=[SpinalEnumCraft[E], T]
    ): Unit = {
      to(ev(value()))
    }
  }

  if (enableNotConsidered) {
    default(NotConsidered) to True
  }

  private var currentOn: Option[on] = None

  case class on(mask: MaskedLiteral) {
    assert(mask.width == inputWidth.value)
    assert(currentOn.isEmpty, "lib.Decoder: `on` must not be nested.")

    val map = encodings.getOrElseUpdate(mask, mutable.Map())

    def apply(body: => Unit) = {
      currentOn = Some(this)
      body
      currentOn = None
    }

    if (enableNotConsidered) {
      apply {
        set(NotConsidered) to False
      }
    }

  }

  case class set[T <: Data](key: Key[T]) {
    assert(
      currentOn.isDefined,
      "lib.Decoder: `set` must be called within an `on`."
    )

    keys.add(key)

    def to(value: => T): Unit = {
      currentOn.get.map(key) = () => value
    }

    def to[E <: SpinalEnum](value: => SpinalEnumElement[E])(implicit
        ev: =:=[SpinalEnumCraft[E], T]
    ): Unit = { to(ev(value())) }
  }

  private def build() = {
    val keys = this.keys.toSeq

    val offsets = (keys
      .scanLeft(0) { case (currentOffset, key) =>
        currentOffset + widthOf(key.`type`)
      })
    val outputWidth = offsets.last

    val outputVector = B(0, outputWidth bits)

    val outputSegments: Map[Key[_ <: Data], Bits] = Map.from(
      for (i <- keys.indices)
        yield (keys(i) -> outputVector(offsets(i) until offsets(i + 1)))
    )

    for (key <- keys) {
      output(key) assignFromBits outputSegments(key)
    }

    // Naive implementation
    for ((key, default) <- defaults) {
      outputSegments(key) := default().asBits
    }

    for ((maskedLiteral, map) <- encodings) {
      // The === is aware of the mask
      when(maskedLiteral === input) {
        for ((key, value) <- map) {
          outputSegments(key) := value().asBits
        }
      }
    }
  }

  addPrePopTask(build)
}
