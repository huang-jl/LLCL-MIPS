package ip

import ip.RamPortDelayedPipelineImplicits._
import ip.sim._
import spinal.core._
import spinal.lib._

class SinglePortRamBase(dataWidth: Int, size: Int, latency: Int, writeMode: WriteMode.Value)
    extends SimulatedBlackBox {
  val io = new Bundle {
    val clk  = in Bool
    val rst  = in Bool
    val port = slave(RamPort(dataWidth, log2Up(size / dataWidth), writeMode)).setName("")
  }

  noIoPrefix()
  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  addRTLPath("./rtl/single_port_ram.v")

  override def createSimJob() = {
    val storage = Array.fill[BigInt](size / dataWidth)(0)
    DelayedPipeline(latency).handlePort(io.port, storage).toJob
  }
}

/** @param dataWidth  数据宽度，单位是bit
  * @param size ram总的大小，单位是bit
  * @param latency    读端口的延迟
  */
class SinglePortBRam(dataWidth: Int = 32, size: Int = 4 * 1024 * 8, latency: Int = 1)
    extends SinglePortRamBase(dataWidth, size, latency, WriteMode.WriteFirst) {
  setDefinitionName("single_port_ram")
  val generic = new Generic {
    val DATA_WIDTH       = dataWidth
    val DEPTH            = size / dataWidth
    val MEMORY_PRIMITIVE = "block"
    val LATENCY          = latency
    val WRITE_MODE       = WriteMode.WriteFirst.toString
  }
}

/** @param dataWidth  数据宽度，单位是bit
  * @param size ram总的大小，单位是bit
  * @param latency    读端口的延迟
  */
class SinglePortLUTRam(dataWidth: Int = 32, size: Int = 4 * 1024 * 8, latency: Int = 0)
    extends SinglePortRamBase(dataWidth, size, latency, WriteMode.ReadFirst) {
  setDefinitionName("single_port_ram")
  val generic = new Generic {
    val DATA_WIDTH       = dataWidth
    val DEPTH            = size / dataWidth
    val MEMORY_PRIMITIVE = "distributed"
    val LATENCY          = latency
    val WRITE_MODE       = WriteMode.ReadFirst.toString
  }
}
