module divider(
	input aclk /* verilator public */,
	input s_axis_divisor_tvalid /* verilator public */,
	input [31:0] s_axis_divisor_tdata  /* verilator public */,
	input s_axis_dividend_tvalid /* verilator public */,
	input [31:0] s_axis_dividend_tdata /* verilator public */,
	output m_axis_dout_tvalid /* verilator public */,
	output [63:0] m_axis_dout_tdata /* verilator public */
);
endmodule
