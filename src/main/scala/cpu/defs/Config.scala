package cpu.defs

import spinal.core._
import scala.language.postfixOps

class Config {}

object Config {
  def INIT_PC = U"hBFC00000"

  val ADDR_WIDTH = 32
  val INST_WIDTH = 32
  val BYTE_WIDTH = 8

  val ADDR_USELESS_WIDTH   = log2Up(2 * INST_WIDTH / BYTE_WIDTH)
  def ADDR_USELESS_PADDING = UInt(ADDR_USELESS_WIDTH bits)

  object BTB {
    val NUM_ENTRIES = 4096
    val NUM_WAYS    = 4

    val ADDR_OFFSET  = log2Up(INST_WIDTH / BYTE_WIDTH)
    def ADDR_PADDING = U(0, ADDR_OFFSET bits)
    val ADDR_WIDTH   = Config.ADDR_WIDTH - ADDR_OFFSET

    val INDEX_WIDTH = log2Up(NUM_ENTRIES / NUM_WAYS)

    val TAG_OFFSET = INDEX_WIDTH
    val TAG_WIDTH  = ADDR_WIDTH - TAG_OFFSET

    val ADDR_RANGE  = ADDR_OFFSET until ADDR_OFFSET + ADDR_WIDTH
    val INDEX_RANGE = ADDR_OFFSET until ADDR_OFFSET + INDEX_WIDTH
    val TAG_RANGE   = ADDR_OFFSET + TAG_OFFSET until ADDR_OFFSET + TAG_OFFSET + TAG_WIDTH
  }

  val BHT = new Config {
    val NUM_ENTRIES = 4096

    val ADDR_OFFSET = log2Up(INST_WIDTH / BYTE_WIDTH)

    val INDEX_WIDTH = log2Up(NUM_ENTRIES)

    val DATA_WIDTH = 4

    val BASE_RANGE  = ADDR_OFFSET + INDEX_WIDTH until ADDR_OFFSET + 2 * INDEX_WIDTH
    val INDEX_RANGE = ADDR_OFFSET until ADDR_OFFSET + INDEX_WIDTH
  }
  val PHT = new Config {
    val NUM_ENTRIES = BHT.NUM_ENTRIES * 4

    val ADDR_OFFSET = log2Up(INST_WIDTH / BYTE_WIDTH)

    val INDEX_WIDTH = log2Up(NUM_ENTRIES)

    val INDEX_RANGE = ADDR_OFFSET until ADDR_OFFSET + INDEX_WIDTH
  }
}
