package ip

import spinal.core._

class xpm_memory_sdpram(
    ADDR_WIDTH_A: Int = 6,
    ADDR_WIDTH_B: Int = 6,
    AUTO_SLEEP_TIME: Int = 0,
    BYTE_WRITE_WIDTH_A: Int = 32,
    CASCADE_HEIGHT: Int = 0,
    CLOCKING_MODE: String = "common_clock",
    ECC_MODE: String = "no_ecc",
    MEMORY_INIT_FILE: String = "none",
    MEMORY_INIT_PARAM: String = "0",
    MEMORY_OPTIMIZATION: String = "true",
    MEMORY_PRIMITIVE: String = "auto",
    MEMORY_SIZE: Int = 2048,
    MESSAGE_CONTROL: Int = 0,
    READ_DATA_WIDTH_B: Int = 32,
    READ_LATENCY_B: Int = 2,
    READ_RESET_VALUE_B: String = "0",
    RST_MODE_A: String = "SYNC",
    RST_MODE_B: String = "SYNC",
    SIM_ASSERT_CHK: Int = 0,
    USE_EMBEDDED_CONSTRAINT: Int = 0,
    USE_MEM_INIT: Int = 0,
    WAKEUP_TIME: String = "disable_sleep",
    WRITE_DATA_WIDTH_A: Int = 32,
    WRITE_MODE_B: String = "no_change"
) extends BlackBox {
  addGenerics(
    ("ADDR_WIDTH_A", ADDR_WIDTH_A),
    ("ADDR_WIDTH_B", ADDR_WIDTH_B),
    ("AUTO_SLEEP_TIME", AUTO_SLEEP_TIME),
    ("BYTE_WRITE_WIDTH_A", BYTE_WRITE_WIDTH_A),
    ("CASCADE_HEIGHT", CASCADE_HEIGHT),
    ("CLOCKING_MODE", CLOCKING_MODE),
    ("ECC_MODE", ECC_MODE),
    ("MEMORY_INIT_FILE", MEMORY_INIT_FILE),
    ("MEMORY_INIT_PARAM", MEMORY_INIT_PARAM),
    ("MEMORY_OPTIMIZATION", MEMORY_OPTIMIZATION),
    ("MEMORY_PRIMITIVE", MEMORY_PRIMITIVE),
    ("MEMORY_SIZE", MEMORY_SIZE),
    ("MESSAGE_CONTROL", MESSAGE_CONTROL),
    ("READ_DATA_WIDTH_B", READ_DATA_WIDTH_B),
    ("READ_LATENCY_B", READ_LATENCY_B),
    ("READ_RESET_VALUE_B", READ_RESET_VALUE_B),
    ("RST_MODE_A", RST_MODE_A),
    ("RST_MODE_B", RST_MODE_B),
    ("SIM_ASSERT_CHK", SIM_ASSERT_CHK),
    ("USE_EMBEDDED_CONSTRAINT", USE_EMBEDDED_CONSTRAINT),
    ("USE_MEM_INIT", USE_MEM_INIT),
    ("WAKEUP_TIME", WAKEUP_TIME),
    ("WRITE_DATA_WIDTH_A", WRITE_DATA_WIDTH_A),
    ("WRITE_MODE_B", WRITE_MODE_B)
  )

  val io = new Bundle {
    val dbiterrb       = out Bool ()
    val doutb          = out Bits (READ_DATA_WIDTH_B bits)
    val sbiterrb       = out Bool ()
    val addra          = in Bits (ADDR_WIDTH_A bits) default _
    val addrb          = in Bits (ADDR_WIDTH_B bits) default _
    val clka           = in Bool () default _
    val clkb           = in Bool () default _
    val dina           = in Bits (WRITE_DATA_WIDTH_A bits) default _
    val ena            = in Bool () default _
    val enb            = in Bool () default _
    val injectdbiterra = in Bool () default _
    val injectsbiterra = in Bool () default _
    val regceb         = in Bool () default _
    val rstb           = in Bool () default _
    val sleep          = in Bool () default _
    val wea            = in Bits (WRITE_DATA_WIDTH_A / BYTE_WRITE_WIDTH_A bits) default _
  }

  noIoPrefix
}
