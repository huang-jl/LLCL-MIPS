module single_port_ram #
(
    parameter DATA_WIDTH = 32,  //default data width of xpm ram(bits)
    parameter DEPTH = 128,   //default depth of memory
    parameter MEMORY_PRIMITIVE = "auto",    //"auto | distributed | block"
    parameter LATENCY = 1,      //default latency is 1 cycle
    parameter WRITE_MODE = "write_first"
)
(
    input wire                      clk /* verilator public */,
    input wire                      rst /* verilator public */,
    input wire                      en /* verilator public */,    //ram enable
    input wire                      we /* verilator public */,    //write enable
    input wire [$clog2(DEPTH) -1:0] addr /* verilator public */,
    input wire [DATA_WIDTH - 1:0]    din /* verilator public */,    //write data
    output wire [DATA_WIDTH - 1:0]    dout /* verilator public */
);

`ifndef VERILATOR
   // xpm_memory_spram: Single Port RAM
   // Xilinx Parameterized Macro, version 2019.2

   xpm_memory_spram #(
      .ADDR_WIDTH_A($clog2(DEPTH)),              // DECIMAL
      .AUTO_SLEEP_TIME(0),           // DECIMAL
      .ECC_MODE("no_ecc"),           // String
      .MEMORY_PRIMITIVE(MEMORY_PRIMITIVE),     // String
      .MEMORY_SIZE(DATA_WIDTH * DEPTH),            // DECIMAL
      .MESSAGE_CONTROL(0),           // DECIMAL
      .READ_DATA_WIDTH_A(DATA_WIDTH),        // DECIMAL
      .READ_LATENCY_A(LATENCY),            // DECIMAL
      .READ_RESET_VALUE_A("0"),      // String
      .USE_MEM_INIT(0),              // DECIMAL
      .WRITE_DATA_WIDTH_A(DATA_WIDTH),       // DECIMAL
      .WRITE_MODE_A(WRITE_MODE),    // String
      .BYTE_WRITE_WIDTH_A(DATA_WIDTH)
   )
   xpm_memory_spram_inst (
      .douta(dout),                   // READ_DATA_WIDTH_A-bit output: Data output for port A read operations.
      .addra(addr),                   // ADDR_WIDTH_A-bit input: Address for port A write and read operations.
      .clka(clk),                     // 1-bit input: Clock signal for port A.
      .dina(din),                     // WRITE_DATA_WIDTH_A-bit input: Data input for port A write operations.
      .ena(en),                       // 1-bit input: Memory enable signal for port A. Must be high on clock
                                       // cycles when read or write operations are initiated. Pipelined
                                       // internally.

      .regcea(1'b0),                 // 1-bit input: Clock Enable for the last register stage on the output
                                       // data path.

      .rsta(rst),                     // 1-bit input: Reset signal for the final port A output register stage.
                                       // Synchronously resets output port douta to the value specified by
                                       // parameter READ_RESET_VALUE_A.

      .sleep(1'b0),                   // 1-bit input: sleep signal to enable the dynamic power saving feature.
      .wea(we)                        // WRITE_DATA_WIDTH_A/BYTE_WRITE_WIDTH_A-bit input: Write enable vector
                                       // for port A input data port dina. 1 bit wide when word-wide writes are
                                       // used. In byte-wide write configurations, each bit controls the
                                       // writing one byte of dina to address addra. For example, to
                                       // synchronously write only bits [15-8] of dina when WRITE_DATA_WIDTH_A
                                       // is 32, wea would be 4'b0010.

   );

   // End of xpm_memory_spram_inst instantiation
`endif
    
endmodule
