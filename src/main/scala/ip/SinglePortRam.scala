package ip

import spinal.core._


/**
 * @param dataWidth  数据宽度，单位是bit
 * @param depth      深度 dataWidth * depth即ram的大小
 * @param memoryType "auto | distributed | block"
 * @param latency    读端口的延迟
 */
class SinglePortRam(dataWidth: Int = 32, depth: Int = 128,
                    memoryType: String = "auto", latency: Int = 1) extends BlackBox {
  setDefinitionName("singel_port_ram")
  val generic = new Generic {
    val DATA_WIDTH = dataWidth
    val DEPTH = depth
    val MEMORY_PRIMITIVE = memoryType
    val LATENCY = latency
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
