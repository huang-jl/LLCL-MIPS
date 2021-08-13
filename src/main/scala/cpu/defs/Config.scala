package cpu.defs

import spinal.core._
import scala.language.postfixOps

class Config {}

object Config {
  def NOP     = B"32'0"
  def INIT_PC = U"hBFC00000"

  val ADDR_WIDTH = 32
  val INST_WIDTH = 32
  val BYTE_WIDTH = 8

  object BTB {
    val NUM_ENTRIES = 2048
    val NUM_WAYS    = 4
    val INDEX_WIDTH = log2Up(NUM_ENTRIES / NUM_WAYS)
  }

  object BHT {
    val NUM_ENTRIES = 2048
    val INDEX_WIDTH = log2Up(NUM_ENTRIES)
    val DATA_WIDTH  = 4
  }

  object PHT {
    val NUM_ENTRIES = BHT.NUM_ENTRIES * 4
    val INDEX_WIDTH = log2Up(NUM_ENTRIES)
  }
}
