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
  mapClockDomain(clock = io.clka)
  mapClockDomain(clock = io.clkb, reset = io.rstb, resetActiveLevel = HIGH)
}

class xpm_memory_tdpram_generic extends Generic {
  var ADDR_WIDTH_A            = 6               // DECIMAL
  var ADDR_WIDTH_B            = 6               // DECIMAL
  var AUTO_SLEEP_TIME         = 0               // DECIMAL
  var BYTE_WRITE_WIDTH_A      = 32              // DECIMAL
  var BYTE_WRITE_WIDTH_B      = 32              // DECIMAL
  var CASCADE_HEIGHT          = 0               // DECIMAL
  var CLOCKING_MODE           = "common_clock"  // String
  var ECC_MODE                = "no_ecc"        // String
  var MEMORY_INIT_FILE        = "none"          // String
  var MEMORY_INIT_PARAM       = "0"             // String
  var MEMORY_OPTIMIZATION     = "true"          // String
  var MEMORY_PRIMITIVE        = "block"         // String
  var MEMORY_SIZE             = 2048            // DECIMAL
  var MESSAGE_CONTROL         = 0               // DECIMAL
  var READ_DATA_WIDTH_A       = 32              // DECIMAL
  var READ_DATA_WIDTH_B       = 32              // DECIMAL
  var READ_LATENCY_A          = 1               // DECIMAL
  var READ_LATENCY_B          = 1               // DECIMAL
  var READ_RESET_VALUE_A      = "0"             // String
  var READ_RESET_VALUE_B      = "0"             // String
  var RST_MODE_A              = "SYNC"          // String
  var RST_MODE_B              = "SYNC"          // String
  var SIM_ASSERT_CHK          = 0               // DECIMAL; 0=disable simulation messages 1=enable simulation messages
  var USE_EMBEDDED_CONSTRAINT = 0               // DECIMAL
  var USE_MEM_INIT            = 0               // DECIMAL
  var WAKEUP_TIME             = "disable_sleep" // String
  var WRITE_DATA_WIDTH_A      = 32              // DECIMAL
  var WRITE_DATA_WIDTH_B      = 32              // DECIMAL
  var WRITE_MODE_A            = "no_change"     // String
  var WRITE_MODE_B            = "no_change"     // String
}

class xpm_memory_tdpram(param: xpm_memory_tdpram_generic) extends BlackBox {
  val generic = param

  val io = new Bundle {
    // 1-bit output: Status signal to indicate double bit error occurrence
    // on the data output of port A.
    val dbiterra = out Bool ()

    // 1-bit output: Status signal to indicate double bit error occurrence
    // on the data output of port Aval
    val dbiterrb = out Bool ()

    // READ_DATA_WIDTH_A-bit output: Data output for port A read operationsval
    val douta = out Bits (generic.READ_DATA_WIDTH_A bits)
    // READ_DATA_WIDTH_B-bit output: Data output for port B read operationsval
    val doutb = out Bits (generic.READ_DATA_WIDTH_B bits)
    // 1-bit output: Status signal to indicate single bit error occurrence
    // on the data output of port Aval
    val sbiterra = out Bool ()

    // 1-bit output: Status signal to indicate single bit error occurrence
    // on the data output of port Bval
    val sbiterrb = out Bool ()

    // ADDR_WIDTH_A-bit input: Address for port A write and read operationsval
    val addra = in UInt (generic.ADDR_WIDTH_A bits) default 0
    // ADDR_WIDTH_B-bit input: Address for port B write and read operationsval
    val addrb = in UInt (generic.ADDR_WIDTH_B bits) default 0
    // 1-bit input: Clock signal for port Aval  Also clocks port B when
    // parameter CLOCKING_MODE is "common_clock"val
    val clka = in Bool () default False

    // 1-bit input: Clock signal for port B when parameter CLOCKING_MODE is
    // "independent_clock"val  Unused when parameter CLOCKING_MODE is
    // "common_clock"val
    val clkb = in Bool () default False

    // WRITE_DATA_WIDTH_A-bit input: Data input for port A write operationsval
    val dina = in Bits (generic.WRITE_DATA_WIDTH_A bits) default 0
    // WRITE_DATA_WIDTH_B-bit input: Data input for port B write operationsval
    val dinb = in Bits (generic.WRITE_DATA_WIDTH_A bits) default 0
    // 1-bit input: Memory enable signal for port Aval  Must be high on clock
    // cycles when read or write operations are initiatedval  Pipelined
    // internallyval
    val ena = in Bool () default False

    // 1-bit input: Memory enable signal for port Bval  Must be high on clock
    // cycles when read or write operations are initiatedval  Pipelined
    // internallyval
    val enb = in Bool () default False

    // 1-bit input: Controls double bit error injection on input data when
    // ECC enabled (Error injection capability is not available in
    // "decode_only" mode)val
    val injectdbiterra = in Bool () default False

    // 1-bit input: Controls double bit error injection on input data when
    // ECC enabled (Error injection capability is not available in
    // "decode_only" mode)val
    val injectdbiterrb = in Bool () default False

    // 1-bit input: Controls single bit error injection on input data when
    // ECC enabled (Error injection capability is not available in
    // "decode_only" mode)val
    val injectsbiterra = in Bool () default False

    // 1-bit input: Controls single bit error injection on input data when
    // ECC enabled (Error injection capability is not available in
    // "decode_only" mode)val
    val injectsbiterrb = in Bool () default False

    // 1-bit input: Clock Enable for the last register stage on the output
    // data pathval
    val regcea = in Bool () default False

    // 1-bit input: Clock Enable for the last register stage on the output
    // data pathval
    val regceb = in Bool () default False

    // 1-bit input: Reset signal for the final port A output register stageval
    // Synchronously resets output port douta to the value specified by
    // parameter READ_RESET_VALUE_Aval
    val rsta = in Bool () default False

    // 1-bit input: Reset signal for the final port B output register stageval
    // Synchronously resets output port doutb to the value specified by
    // parameter READ_RESET_VALUE_Bval
    val rstb = in Bool () default False

    // 1-bit input: sleep signal to enable the dynamic power saving featureval
    val sleep = in Bool () default False
    // WRITE_DATA_WIDTH_A/BYTE_WRITE_WIDTH_A-bit input: Write enable vector
    // for port A input data port dinaval  1 bit wide when word-wide writes are
    // usedval  In byte-wide write configurations, each bit controls the
    // writing one byte of dina to address addraval  For example, to
    // synchronously write only bits [15-8] of dina when WRITE_DATA_WIDTH_A
    // is 32, wea would be 4'b0010val
    val wea = in Bits (generic.WRITE_DATA_WIDTH_A / generic.BYTE_WRITE_WIDTH_A bits) default 0

    // WRITE_DATA_WIDTH_B/BYTE_WRITE_WIDTH_B-bit input: Write enable vector
    // for port B input data port dinbval  1 bit wide when word-wide writes are
    // usedval  In byte-wide write configurations, each bit controls the
    // writing one byte of dinb to address addrbval  For example, to
    // synchronously write only bits [15-8] of dinb when WRITE_DATA_WIDTH_B
    // is 32, web would be 4'b0010val
    val web = in Bits (generic.WRITE_DATA_WIDTH_B / generic.BYTE_WRITE_WIDTH_B bits) default 0
  }

  noIoPrefix
  mapClockDomain(clock = io.clka, reset = io.rsta, resetActiveLevel = HIGH)
  mapClockDomain(clock = io.clkb, reset = io.rstb, resetActiveLevel = HIGH)
}

class xpm_memory_dpdistram_generic extends Generic {
  var ADDR_WIDTH_A            = 6              // DECIMAL
  var ADDR_WIDTH_B            = 6              // DECIMAL
  var BYTE_WRITE_WIDTH_A      = 32             // DECIMAL
  var CLOCKING_MODE           = "common_clock" // String
  var MEMORY_INIT_FILE        = "none"         // String
  var MEMORY_INIT_PARAM       = "0"            // String
  var MEMORY_OPTIMIZATION     = "true"         // String
  var MEMORY_SIZE             = 2048           // DECIMAL
  var MESSAGE_CONTROL         = 0              // DECIMAL
  var READ_DATA_WIDTH_A       = 32             // DECIMAL
  var READ_DATA_WIDTH_B       = 32             // DECIMAL
  var READ_LATENCY_A          = 1              // DECIMAL
  var READ_LATENCY_B          = 1              // DECIMAL
  var READ_RESET_VALUE_A      = "0"            // String
  var READ_RESET_VALUE_B      = "0"            // String
  var RST_MODE_A              = "SYNC"         // String
  var RST_MODE_B              = "SYNC"         // String
  var SIM_ASSERT_CHK          = 0              // DECIMAL; 0=disable simulation messages, 1=enable simulation messages
  var USE_EMBEDDED_CONSTRAINT = 0              // DECIMAL
  var USE_MEM_INIT            = 0              // DECIMAL
  var WRITE_DATA_WIDTH_A      = 32             // DECIMAL
}

class xpm_memory_dpdistram(param: xpm_memory_dpdistram_generic) extends BlackBox {
  val generic = param

  val io = new Bundle {
    // READ_DATA_WIDTH_A-bit output: Data output for port A read operationsval
    val douta = out Bits (generic.READ_DATA_WIDTH_A bits)
    // READ_DATA_WIDTH_B-bit output: Data output for port B read operationsval
    val doutb = out Bits (generic.READ_DATA_WIDTH_B bits)
    // ADDR_WIDTH_A-bit input: Address for port A write and read operationsval
    val addra = in UInt (generic.ADDR_WIDTH_A bits) default 0
    // ADDR_WIDTH_B-bit input: Address for port B write and read operationsval
    val addrb = in UInt (generic.ADDR_WIDTH_B bits) default 0
    // 1-bit input: Clock signal for port Aval  Also clocks port B when parameter CLOCKING_MODE
    // is "common_clock"val
    val clka = in Bool ()

    // 1-bit input: Clock signal for port B when parameter CLOCKING_MODE is
    // "independent_clock"val  Unused when parameter CLOCKING_MODE is "common_clock"val
    val clkb = in Bool ()

    // WRITE_DATA_WIDTH_A-bit input: Data input for port A write operationsval
    val dina = in Bits (generic.WRITE_DATA_WIDTH_A bits) default 0
    // 1-bit input: Memory enable signal for port Aval  Must be high on clock cycles when read
    // or write operations are initiatedval  Pipelined internallyval
    val ena = in Bool () default False

    // 1-bit input: Memory enable signal for port Bval  Must be high on clock cycles when read
    // or write operations are initiatedval  Pipelined internallyval
    val enb = in Bool () default False
    // 1-bit input: Clock Enable for the last register stage on the output data pathval
    val regcea = in Bool () default False
    // 1-bit input: Do not change from the provided valueval
    val regceb = in Bool () default False
    // 1-bit input: Reset signal for the final port A output register stageval  Synchronously
    // resets output port douta to the value specified by parameter READ_RESET_VALUE_Aval
    val rsta = in Bool () default False

    // 1-bit input: Reset signal for the final port B output register stageval  Synchronously
    // resets output port doutb to the value specified by parameter READ_RESET_VALUE_Bval
    val rstb = in Bool () default False

    // WRITE_DATA_WIDTH_A/BYTE_WRITE_WIDTH_A-bit input: Write enable vector for port A input
    // data port dinaval  1 bit wide when word-wide writes are usedval  In byte-wide write
    // configurations each bit controls the writing one byte of dina to address addraval  For
    // example to synchronously write only bits [15-8] of dina when WRITE_DATA_WIDTH_A is
    // 32 wea would be 4'b0010val
    val wea = in Bits (generic.WRITE_DATA_WIDTH_A / generic.BYTE_WRITE_WIDTH_A bits) default 0
  }
  noIoPrefix()
  mapClockDomain(clock = io.clka, reset = io.rsta, resetActiveLevel = HIGH)
  mapClockDomain(clock = io.clkb, reset = io.rstb, resetActiveLevel = HIGH)
}
