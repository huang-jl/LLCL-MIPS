package cpu.defs

import spinal.core._

object config {
  val ADDR_WIDTH = 32
  val INST_WIDTH = 32
  val BYTE_WIDTH = 8

  object BTB {
    val NUM_ENTRIES = 4096
    val NUM_WAYS    = 4

    val ADDR_OFFSET  = log2Up(INST_WIDTH / BYTE_WIDTH)
    def ADDR_PADDING = U(0, ADDR_OFFSET bits)
    val ADDR_WIDTH   = config.ADDR_WIDTH - ADDR_OFFSET

    val INDEX_WIDTH = log2Up(NUM_ENTRIES / NUM_WAYS)

    val TAG_OFFSET = INDEX_WIDTH
    val TAG_WIDTH  = ADDR_WIDTH - TAG_OFFSET

    val ADDR_RANGE  = ADDR_OFFSET until ADDR_OFFSET + ADDR_WIDTH
    val INDEX_RANGE = ADDR_OFFSET until ADDR_OFFSET + INDEX_WIDTH
    val TAG_RANGE   = ADDR_OFFSET + TAG_OFFSET until ADDR_OFFSET + TAG_OFFSET + TAG_WIDTH
  }
}
