package ip

import ip.RamPortDelayedPipelineImplicits._
import ip.sim._
import spinal.core._
import spinal.lib._

/** @param dataWidth 数据宽度，单位是bit
  * @param size      ram总的大小，单位是bit
  * @param latency   读端口的延迟
  */
case class BRamIPConfig(
    dataWidth: Int = 32,
    size: Int = 4 * 1024 * 8,
    latency: Int = 1,
    writeModeA: WriteMode.Value = WriteMode.NoChange,
    writeModeB: WriteMode.Value = WriteMode.NoChange
) {
  def depth = size / dataWidth
}

class DualPortBRam(config: BRamIPConfig) extends SimulatedBlackBox {
  setDefinitionName("dual_port_bram")
  val generic = new Generic {
    val DATA_WIDTH       = config.dataWidth
    val DEPTH            = config.depth
    val MEMORY_PRIMITIVE = "block"
    val LATENCY          = config.latency
    val WRITE_MODE_A     = config.writeModeA.toString
    val WRITE_MODE_B     = config.writeModeB.toString
  }

  val io = new Bundle {
    val clk   = in Bool
    val rst   = in Bool
    val portA = slave(RamPort(config.dataWidth, log2Up(config.depth), config.writeModeA))
    val portB = slave(RamPort(config.dataWidth, log2Up(config.depth), config.writeModeB))
  }

  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  noIoPrefix()
  addPrePopTask { () =>
    {
      for (bt <- io.flatten) {
        val name = bt.getName()
        if (name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
        if (name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
      }
    }
  }

  addRTLPath("./rtl/dual_port_ram.v")

  override def createSimJob() = {
    val storage = new Array[BigInt](config.size / config.dataWidth)

    Pipeline(config.latency)
      .whenReset {
        for (i <- storage.indices) {
          storage(i) = 0
        }
      }
      .handlePort(io.portA, storage)
      .handlePort(io.portB, storage)
      .toJob
  }
}

/** @param dataWidth 数据宽度，单位是bit
  * @param size      ram总的大小，单位是bit
  * @param latency   读端口的延迟
  */
class DualPortLutRam(dataWidth: Int = 32, size: Int = 4 * 1024 * 8, latency: Int = 1)
    extends SimulatedBlackBox {
  setDefinitionName("dual_port_lutram")
  val generic = new Generic {
    val DATA_WIDTH = dataWidth
    val DEPTH      = size / dataWidth
    val LATENCY    = latency
  }

  val io = new Bundle {
    val clk = in Bool
    val rst = in Bool
    // TODO: Is it no change?
    val portA = slave(RamPort(dataWidth, log2Up(size / dataWidth), WriteMode.NoChange))
    val portB = slave(ReadOnlyRamPort(dataWidth, log2Up(size / dataWidth)))
  }

  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  noIoPrefix()
  addPrePopTask { () =>
    {
      for (bt <- io.flatten) {
        val name = bt.getName()
        if (name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
        if (name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
      }
    }
  }

  addRTLPath("./rtl/dual_port_ram.v")

  override def createSimJob() = {
    val storage = new Array[BigInt](size / dataWidth)

    Pipeline(latency)
      .whenReset {
        for (i <- storage.indices) {
          storage(i) = 0
        }
      }
      .handlePort(io.portA, storage)
      .handlePort(io.portB, storage)
      .toJob
  }
}

object DualPortRam {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new Component {
      val bram   = new DualPortBRam(BRamIPConfig(32 * 8, 4 * 1024 * 8))
      val lutRam = new DualPortLutRam(32 * 8, 4 * 1024 * 8, 0)
      bram.io.portA.addr.assignDontCare()
      bram.io.portA.en.assignDontCare()
      bram.io.portA.we.assignDontCare()
      bram.io.portA.din.assignDontCare()
      bram.io.portB.addr.assignDontCare()
      bram.io.portB.en.assignDontCare()
      bram.io.portB.we.assignDontCare()
      bram.io.portB.din.assignDontCare()

      lutRam.io.portA.addr.assignDontCare()
      lutRam.io.portA.en.assignDontCare()
      lutRam.io.portA.we.assignDontCare()
      lutRam.io.portA.din.assignDontCare()
      lutRam.io.portB.addr.assignDontCare()
      lutRam.io.portB.en.assignDontCare()
    })
  }
}
