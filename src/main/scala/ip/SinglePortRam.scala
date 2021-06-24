package ip

import spinal.core._


/**
 * @param dataWidth  数据宽度，单位是bit
 * @param depth      深度 dataWidth * depth即ram的大小
 * @param latency    读端口的延迟
 */
class SinglePortBRam(dataWidth: Int = 32, depth: Int = 128,
                    latency: Int = 1) extends BlackBox {
  setDefinitionName("single_port_ram")
  val generic = new Generic {
    val DATA_WIDTH = dataWidth
    val DEPTH = depth
    val MEMORY_PRIMITIVE = "block"
    val LATENCY = latency
    val WRITE_MODE = "write_first"
  }

  val io = new Bundle {
    val clk = in Bool
    val rst = in Bool
    val en = in Bool
    val we = in Bool //write enable
    val addr = in(UInt(log2Up(depth) bits))
    val din = in(Bits(dataWidth bits))
    val dout = out(Bits(dataWidth bits))
  }

  noIoPrefix()
  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  addRTLPath("./rtl/single_port_ram.v")
}

/**
 * @param dataWidth  数据宽度，单位是bit
 * @param depth      深度 dataWidth * depth即ram的大小
 * @param latency    读端口的延迟
 */
class SinglePortLUTRam(dataWidth: Int = 32, depth: Int = 128,
                     latency: Int = 0) extends BlackBox {
  setDefinitionName("single_port_ram")
  val generic = new Generic {
    val DATA_WIDTH = dataWidth
    val DEPTH = depth
    val MEMORY_PRIMITIVE = "distributed"
    val LATENCY = latency
    val WRITE_MODE = "read_first"
  }

  val io = new Bundle {
    val clk = in Bool
    val rst = in Bool
    val en = in Bool
    val we = in Bool //write enable
    val addr = in(UInt(log2Up(depth) bits))
    val din = in(Bits(dataWidth bits))
    val dout = out(Bits(dataWidth bits))
  }

  noIoPrefix()
  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  addRTLPath("./rtl/single_port_ram.v")
}
