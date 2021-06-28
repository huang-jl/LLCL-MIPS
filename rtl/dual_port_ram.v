module dual_port_bram #
(
    parameter DATA_WIDTH = 32,  //default data width of xpm ram(bits)
    parameter DEPTH = 128,   //default depth of memory
    parameter MEMORY_PRIMITIVE = "auto",    //"auto | distributed | block"
    parameter LATENCY = 1,      //default latency is 1 cycle
    parameter WRITE_MODE_A = "no_change",
    parameter WRITE_MODE_B = "no_change"
)
(
   input wire                      clk,
   input wire                      rst,
   input wire                      ena,    //ram enable
   input wire                      enb,    //ram enable
   input wire                      wea,    //write enable
   input wire                      web,    //write enable
   input wire [$clog2(DEPTH) -1:0] addra,
   input wire [$clog2(DEPTH) -1:0] addrb,
   input wire [DATA_WIDTH - 1:0]    dina,    //write data
   input wire [DATA_WIDTH - 1:0]    dinb,    //write data
   input wire [DATA_WIDTH - 1:0]    douta,
   input wire [DATA_WIDTH - 1:0]    doutb
);
   // xpm_memory_tdpram: True Dual Port RAM
   // Xilinx Parameterized Macro, version 2019.2

    xpm_memory_tdpram #(
        .ADDR_WIDTH_A($clog2(DEPTH)),               // DECIMAL
        .ADDR_WIDTH_B($clog2(DEPTH)),               // DECIMAL
        .AUTO_SLEEP_TIME(0),            // DECIMAL
        .BYTE_WRITE_WIDTH_A(DATA_WIDTH),        // DECIMAL
        .BYTE_WRITE_WIDTH_B(DATA_WIDTH),        // DECIMAL
        .CASCADE_HEIGHT(0),             // DECIMAL
        .CLOCKING_MODE("common_clock"), // String
        .ECC_MODE("no_ecc"),            // String
        .MEMORY_INIT_FILE("none"),      // String
        .MEMORY_INIT_PARAM("0"),        // String
        .MEMORY_OPTIMIZATION("true"),   // String
        .MEMORY_PRIMITIVE("block"),      // String
        .MEMORY_SIZE(DATA_WIDTH * DEPTH),             // DECIMAL
        .MESSAGE_CONTROL(0),            // DECIMAL
        .READ_DATA_WIDTH_A(DATA_WIDTH),         // DECIMAL
        .READ_DATA_WIDTH_B(DATA_WIDTH),         // DECIMAL
        .READ_LATENCY_A(LATENCY),             // DECIMAL
        .READ_LATENCY_B(LATENCY),             // DECIMAL
        .READ_RESET_VALUE_A("0"),       // String
        .READ_RESET_VALUE_B("0"),       // String
        .RST_MODE_A("SYNC"),            // String
        .RST_MODE_B("SYNC"),            // String
        .SIM_ASSERT_CHK(0),             // DECIMAL; 0=disable simulation messages, 1=enable simulation messages
        .USE_EMBEDDED_CONSTRAINT(0),    // DECIMAL
        .USE_MEM_INIT(0),               // DECIMAL
        .WAKEUP_TIME("disable_sleep"),  // String
        .WRITE_DATA_WIDTH_A(DATA_WIDTH),        // DECIMAL
        .WRITE_DATA_WIDTH_B(DATA_WIDTH),        // DECIMAL
        .WRITE_MODE_A(WRITE_MODE_A),     // String
        .WRITE_MODE_B(WRITE_MODE_B)      // String
   )
    xpm_memory_tdpram_inst (
        .dbiterra(),             // 1-bit output: Status signal to indicate double bit error occurrence
                                         // on the data output of port A.

        .dbiterrb(),             // 1-bit output: Status signal to indicate double bit error occurrence
                                         // on the data output of port A.

        .douta(douta),                   // READ_DATA_WIDTH_A-bit output: Data output for port A read operations.
        .doutb(doutb),                   // READ_DATA_WIDTH_B-bit output: Data output for port B read operations.
        .sbiterra(),             // 1-bit output: Status signal to indicate single bit error occurrence
                                         // on the data output of port A.

        .sbiterrb(),             // 1-bit output: Status signal to indicate single bit error occurrence
                                         // on the data output of port B.

        .addra(addra),                   // ADDR_WIDTH_A-bit input: Address for port A write and read operations.
        .addrb(addrb),                   // ADDR_WIDTH_B-bit input: Address for port B write and read operations.
        .clka(clk),                     // 1-bit input: Clock signal for port A. Also clocks port B when
                                         // parameter CLOCKING_MODE is "common_clock".

        .clkb(clk),                     // 1-bit input: Clock signal for port B when parameter CLOCKING_MODE is
                                         // "independent_clock". Unused when parameter CLOCKING_MODE is
                                         // "common_clock".

        .dina(dina),                     // WRITE_DATA_WIDTH_A-bit input: Data input for port A write operations.
        .dinb(dinb),                     // WRITE_DATA_WIDTH_B-bit input: Data input for port B write operations.
        .ena(ena),                       // 1-bit input: Memory enable signal for port A. Must be high on clock
                                         // cycles when read or write operations are initiated. Pipelined
                                         // internally.

        .enb(enb),                       // 1-bit input: Memory enable signal for port B. Must be high on clock
                                         // cycles when read or write operations are initiated. Pipelined
                                         // internally.

        .injectdbiterra(), // 1-bit input: Controls double bit error injection on input data when
                                         // ECC enabled (Error injection capability is not available in
                                         // "decode_only" mode).

        .injectdbiterrb(), // 1-bit input: Controls double bit error injection on input data when
                                         // ECC enabled (Error injection capability is not available in
                                         // "decode_only" mode).

        .injectsbiterra(), // 1-bit input: Controls single bit error injection on input data when
                                         // ECC enabled (Error injection capability is not available in
                                         // "decode_only" mode).

        .injectsbiterrb(), // 1-bit input: Controls single bit error injection on input data when
                                         // ECC enabled (Error injection capability is not available in
                                         // "decode_only" mode).

        .regcea(1'b0),                 // 1-bit input: Clock Enable for the last register stage on the output
                                         // data path.

        .regceb(1'b0),                 // 1-bit input: Clock Enable for the last register stage on the output
                                         // data path.

        .rsta(rst),                     // 1-bit input: Reset signal for the final port A output register stage.
                                         // Synchronously resets output port douta to the value specified by
                                         // parameter READ_RESET_VALUE_A.

        .rstb(rst),                     // 1-bit input: Reset signal for the final port B output register stage.
                                         // Synchronously resets output port doutb to the value specified by
                                         // parameter READ_RESET_VALUE_B.

        .sleep(1'b0),                   // 1-bit input: sleep signal to enable the dynamic power saving feature.
        .wea(wea),                       // WRITE_DATA_WIDTH_A/BYTE_WRITE_WIDTH_A-bit input: Write enable vector
                                         // for port A input data port dina. 1 bit wide when word-wide writes are
                                         // used. In byte-wide write configurations, each bit controls the
                                         // writing one byte of dina to address addra. For example, to
                                         // synchronously write only bits [15-8] of dina when WRITE_DATA_WIDTH_A
                                         // is 32, wea would be 4'b0010.

        .web(web)                        // WRITE_DATA_WIDTH_B/BYTE_WRITE_WIDTH_B-bit input: Write enable vector
                                         // for port B input data port dinb. 1 bit wide when word-wide writes are
                                         // used. In byte-wide write configurations, each bit controls the
                                         // writing one byte of dinb to address addrb. For example, to
                                         // synchronously write only bits [15-8] of dinb when WRITE_DATA_WIDTH_B
                                         // is 32, web would be 4'b0010.

   );

   // End of xpm_memory_tdpram_inst instantiation

endmodule



module dual_port_lutram #
(
    parameter DATA_WIDTH = 32,  //default data width of xpm ram(bits)
    parameter DEPTH = 128,   //default depth of memory
    parameter LATENCY = 0      //default latency is 1 cycle
)
(
   input wire                      clk,
   input wire                      rst,
   input wire                      ena,    //ram enable
   input wire                      enb,    //ram enable
   input wire                      wea,    //write enable
   input wire [$clog2(DEPTH) -1:0] addra,
   input wire [$clog2(DEPTH) -1:0] addrb,
   input wire [DATA_WIDTH - 1:0]    dina,    //write data
   input wire [DATA_WIDTH - 1:0]    douta,
   input wire [DATA_WIDTH - 1:0]    doutb
);

   // xpm_memory_dpdistram: Dual Port Distributed RAM
   // Xilinx Parameterized Macro, version 2019.2

    xpm_memory_dpdistram #(
        .ADDR_WIDTH_A($clog2(DEPTH)),               // DECIMAL
        .ADDR_WIDTH_B($clog2(DEPTH)),               // DECIMAL
        .BYTE_WRITE_WIDTH_A(DATA_WIDTH),        // DECIMAL
        .CLOCKING_MODE("common_clock"), // String
        .MEMORY_INIT_FILE("none"),      // String
        .MEMORY_INIT_PARAM("0"),        // String
        .MEMORY_OPTIMIZATION("true"),   // String
        .MEMORY_SIZE(DATA_WIDTH * DEPTH),             // DECIMAL
        .MESSAGE_CONTROL(0),            // DECIMAL
        .READ_DATA_WIDTH_A(DATA_WIDTH),         // DECIMAL
        .READ_DATA_WIDTH_B(DATA_WIDTH),         // DECIMAL
        .READ_LATENCY_A(LATENCY),             // DECIMAL
        .READ_LATENCY_B(LATENCY),             // DECIMAL
        .READ_RESET_VALUE_A("0"),       // String
        .READ_RESET_VALUE_B("0"),       // String
        .RST_MODE_A("SYNC"),            // String
        .RST_MODE_B("SYNC"),            // String
        .SIM_ASSERT_CHK(0),             // DECIMAL; 0=disable simulation messages, 1=enable simulation messages
        .USE_EMBEDDED_CONSTRAINT(0),    // DECIMAL
        .USE_MEM_INIT(0),               // DECIMAL
        .WRITE_DATA_WIDTH_A(DATA_WIDTH)         // DECIMAL
   )
    xpm_memory_dpdistram_inst (
        .douta(douta),   // READ_DATA_WIDTH_A-bit output: Data output for port A read operations.
        .doutb(doutb),   // READ_DATA_WIDTH_B-bit output: Data output for port B read operations.
        .addra(addra),   // ADDR_WIDTH_A-bit input: Address for port A write and read operations.
        .addrb(addrb),   // ADDR_WIDTH_B-bit input: Address for port B write and read operations.
        .clka(clk),     // 1-bit input: Clock signal for port A. Also clocks port B when parameter CLOCKING_MODE
                         // is "common_clock".

        .clkb(clk),     // 1-bit input: Clock signal for port B when parameter CLOCKING_MODE is
                         // "independent_clock". Unused when parameter CLOCKING_MODE is "common_clock".

        .dina(dina),     // WRITE_DATA_WIDTH_A-bit input: Data input for port A write operations.
        .ena(ena),       // 1-bit input: Memory enable signal for port A. Must be high on clock cycles when read
                         // or write operations are initiated. Pipelined internally.

        .enb(enb),       // 1-bit input: Memory enable signal for port B. Must be high on clock cycles when read
                         // or write operations are initiated. Pipelined internally.

        .regcea(1'b0), // 1-bit input: Clock Enable for the last register stage on the output data path.
        .regceb(1'b0), // 1-bit input: Do not change from the provided value.
        .rsta(rst),     // 1-bit input: Reset signal for the final port A output register stage. Synchronously
                         // resets output port douta to the value specified by parameter READ_RESET_VALUE_A.

        .rstb(rst),     // 1-bit input: Reset signal for the final port B output register stage. Synchronously
                         // resets output port doutb to the value specified by parameter READ_RESET_VALUE_B.

        .wea(wea)        // WRITE_DATA_WIDTH_A/BYTE_WRITE_WIDTH_A-bit input: Write enable vector for port A input
                         // data port dina. 1 bit wide when word-wide writes are used. In byte-wide write
                         // configurations, each bit controls the writing one byte of dina to address addra. For
                         // example, to synchronously write only bits [15-8] of dina when WRITE_DATA_WIDTH_A is
                         // 32, wea would be 4'b0010.

   );

   // End of xpm_memory_dpdistram_inst instantiation

endmodule