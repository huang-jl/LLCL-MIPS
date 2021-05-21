package cache

import spinal.core._

case class CacheRamConfig(
                           blockSize: Int = 32, //数据块的大小, bytes
                           indexWidth: Int = 7, //Index的宽度
                           wayNum: Int = 2 //组相连个数
                         ) {
  def tagWidth: Int = 32 - indexWidth - offsetWidth

  def offsetWidth: Int = log2Up(blockSize)

  def wordSize: Int = blockSize / 4

  def bitSize: Int = blockSize * 8

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

//blockSize: 一个数据块的大小:bytes
case class Block(blockSize: Int) extends Bundle {
  val banks = Vec(Bits(32 bits), blockSize / 4)

}

object Block {
  def fromBits(value: Bits, bz: Int): Block = {
    val res = Block(bz)
    res.assignFromBits(value.resize(res.getBitsWidth))
    res
  }
}

case class Meta(tagWidth: Int) extends Bundle {
  val tag = Bits(tagWidth bits)
  val valid = Bool()
}

object Meta {
  def fromBits(value: Bits, tagWidth: Int): Meta = {
    val res = Meta(tagWidth)
    res.assignFromBits(value.resize(res.getBitsWidth))
    res
  }
}
