package cache

import spinal.core._

case class CacheLineConfig(
  wordNum:Int,
  tagWidth:Int
)

class ICacheLine(config: CacheLineConfig) extends Bundle {
  val datas = Vec(Reg(Bits(32 bits)) init(0), config.wordNum)
  val tag = Reg(Bits(config.tagWidth bits)) init(0)
  val valid = Bool
}
