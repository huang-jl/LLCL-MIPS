package cache

import spinal.core._

/**
 * @param blockSize 数据块的大小, bytes
 * @param wayNum 路个数
 * @note Cache采用实Tag和虚Index，
 *       因此Cache一路大小必须小于等于一个页的大小。
 *       由于只支持4KiB的页，这里默认一路的大小为4KiB
 * */
case class CacheRamConfig(
                           blockSize: Int = 32,
                           wayNum: Int = 2,
                           sim: Boolean = true
                         ) {
  def indexWidth: Int = log2Up(4 * 1024) - offsetWidth

  def tagWidth: Int = 32 - indexWidth - offsetWidth

  /** Cache Line的字节偏移宽度 */
  def offsetWidth: Int = log2Up(blockSize)

  /** Cache Line的字个数 */
  def wordSize: Int = blockSize / 4

  /** Cache Line的bit数 */
  def bitSize: Int = blockSize * 8

  /** Cache Line的组数 */
  def setSize: Int = 1 << indexWidth

  def lruLength: Int = {
    var sum = 0
    var temp = wayNum / 2
    while (temp > 0) {
      sum += temp
      temp = temp / 2
    }
    sum
  }
}

/**
 * @param blockSize 一个数据块的大小:bytes
 */
case class Block(blockSize: Int) extends Bundle {
  val banks = Vec(Bits(32 bits), blockSize / 4)
}

object Block {
  def getBitWidth(blockSize: Int): Int = blockSize * 8

  def fromBits(value: Bits, bz: Int): Block = {
    val res = Block(bz)
    res.assignFromBits(value.resize(res.getBitsWidth))
    res
  }
}

case class Meta(tagWidth: Int) extends Bundle {
  val tag = Bits(tagWidth bits)
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
  val tag = Bits(tagWidth bits)
  val valid = Bool
  val dirty = Bool
}

object DMeta {
  def getBitWidth(tagWidth: Int): Int = tagWidth + 2
}
