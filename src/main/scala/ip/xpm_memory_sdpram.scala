package ip

import spinal.core._

class xpm_memory_sdpram_generic extends Generic {
  var ADDR_WIDTH_A            = 6
  var ADDR_WIDTH_B            = 6
  var AUTO_SLEEP_TIME         = 0
  var BYTE_WRITE_WIDTH_A      = 32
  var CASCADE_HEIGHT          = 0
  var CLOCKING_MODE           = "common_clock"
  var ECC_MODE                = "no_ecc"
  var MEMORY_INIT_FILE        = "none"
  var MEMORY_INIT_PARAM       = "0"
  var MEMORY_OPTIMIZATION     = "true"
  var MEMORY_PRIMITIVE        = "auto"
  var MEMORY_SIZE             = 2048
  var MESSAGE_CONTROL         = 0
  var READ_DATA_WIDTH_B       = 32
  var READ_LATENCY_B          = 2
  var READ_RESET_VALUE_B      = "0"
  var RST_MODE_A              = "SYNC"
  var RST_MODE_B              = "SYNC"
  var SIM_ASSERT_CHK          = 0
  var USE_EMBEDDED_CONSTRAINT = 0
  var USE_MEM_INIT            = 0
  var WAKEUP_TIME             = "disable_sleep"
  var WRITE_DATA_WIDTH_A      = 32
  var WRITE_MODE_B            = "no_change"
}

class xpm_memory_sdpram(param: xpm_memory_sdpram_generic) extends BlackBox {
  val generic = param

  val io = new Bundle {
    val dbiterrb       = out Bool ()
    val doutb          = out Bits (generic.READ_DATA_WIDTH_B bits)
    val sbiterrb       = out Bool ()
    val addra          = in UInt (generic.ADDR_WIDTH_A bits) default 0
    val addrb          = in UInt (generic.ADDR_WIDTH_B bits) default 0
    val clka           = in Bool () default False
    val clkb           = in Bool () default False
    val dina           = in Bits (generic.WRITE_DATA_WIDTH_A bits) default 0
    val ena            = in Bool () default False
    val enb            = in Bool () default False
    val injectdbiterra = in Bool () default False
    val injectsbiterra = in Bool () default False
    val regceb         = in Bool () default False
    val rstb           = in Bool () default False
    val sleep          = in Bool () default False
    val wea            = in Bits (generic.WRITE_DATA_WIDTH_A / generic.BYTE_WRITE_WIDTH_A bits) default 0
  }

  noIoPrefix
}
