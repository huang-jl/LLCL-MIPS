package cache

import spinal.core._
import scala.language.postfixOps

/** @param blockSize 数据块的大小, bytes
  * @param wayNum    路个数
  *   @param bankSize 一个bank的大小（例如ICache双发射就是8）, bytes
  * @note ICache采用实Tag和虚Index，
  *       因此Cache一路大小必须小于等于一个页的大小。
  *       由于只支持4KiB的页，这里默认一路的大小为4KiB
  * @note DCache由于把TLB查询放到了EX阶段，因此理论上支持更大的单路大小
  */
case class CacheRamConfig(
    blockSize: Int = 32,
    depth: Int = 4 * 1024 / 32,
    wayNum: Int = 2,
    bankSize: Int = 4
) {
  def indexWidth: Int = log2Up(depth)

  def tagWidth: Int = 32 - indexWidth - offsetWidth

  /** Cache Line的字节偏移宽度 */
  def offsetWidth: Int = log2Up(blockSize)

  /** Cache Line的bank个数 */
  def bankNum: Int = blockSize / bankSize

  /** Cache Line的bank偏移宽度 */
  def bankOffsetWidth: Int = log2Up(bankNum)

  /** Cache Line的word个数 */
  def wordNum: Int = blockSize / 4

  /** Cache Line的word个数 */
  def wordOffsetWidth: Int = log2Up(wordNum)

  /** Cache Line的bit数 */
  def bitNum: Int = blockSize * 8

  /** Cache Line的组数 */
  def setNum: Int = 1 << indexWidth
}

/** @param blockSize 一个数据块的大小:bytes
  * @param bankSize 一个bank的大小:bytes
  */
case class Block(blockSize: Int, bankSize: Int = 4) extends Bundle {
  val banks = Vec(Bits(bankSize * 8 bits), blockSize / bankSize)

  def apply(addr: UInt): Bits = banks(addr)

  def apply(idx: Int): Bits = banks(idx)
}

object Block {
  def getBitWidth(blockSize: Int): Int = blockSize * 8

  def fromBits(value: Bits, blockSize: Int, bankSize: Int): Block = {
    val res = Block(blockSize, bankSize)
    res.assignFromBits(value)
    res
  }
}

case class Meta(tagWidth: Int) extends Bundle {
  val tag   = Bits(tagWidth bits)
  val valid = Bool
}

object Meta {
  def getBitWidth(tagWidth: Int): Int = tagWidth + 1

  def fromBits(value: Bits, tagWidth: Int): Meta = {
    val res = Meta(tagWidth)
    res.assignFromBits(value.resize(res.getBitsWidth))
    res
  }
}

case class DMeta(tagWidth: Int) extends Bundle {
  val tag   = Bits(tagWidth bits)
  val valid = Bool
  val dirty = Bool
}

object DMeta {
  def getBitWidth(tagWidth: Int): Int = tagWidth + 2
  def fromBits(value: Bits, tagWidth: Int): DMeta = {
    val res = DMeta(tagWidth)
    res.assignFromBits(value.resize(res.getBitsWidth))
    res
  }
}