package cpu.defs

import spinal.core._

class config {}

object config {
  val ADDR_WIDTH = 32

  val BTB = new config {
    val NUM_ENTRIES = 4096
    val NUM_WAYS    = 4

    val INDEX_OFFSET = 2
    val INDEX_WIDTH  = log2Up(NUM_ENTRIES / NUM_WAYS)

    val TAG_OFFSET = INDEX_OFFSET + INDEX_WIDTH
    val TAG_WIDTH  = ADDR_WIDTH - TAG_OFFSET
  }
}
