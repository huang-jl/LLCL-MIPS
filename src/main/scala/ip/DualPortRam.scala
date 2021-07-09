package ip

import cpu.Utils
import spinal.core._
import spinal.lib._

/**
 * @param dataWidth 数据宽度，单位是bit
 * @param addrWidth ram地址宽度
 * @param readOnly  是否是只读的端口，只读端口不会有we和din
 * */
case class RamPort(dataWidth: Int, addrWidth: Int, readOnly: Boolean = false) extends Bundle with IMasterSlave {
  val en = Bool
  val we = Utils.instantiateWhen(Bool, !readOnly)
//  val we = if (!readOnly) Bool else null //write enable
  val addr = UInt(addrWidth bits)
  val din = Utils.instantiateWhen(Bits(dataWidth bits), !readOnly)
//  val din = if (!readOnly) in(Bits(dataWidth bits)) else null
  val dout = Bits(dataWidth bits)

  override def asMaster(): Unit = {
    in(dout)
    out(en, addr)
    if (!readOnly) {
      out(we, din)
    }
  }
}

/**
 * @param dataWidth 数据宽度，单位是bit
 * @param size      ram总的大小，单位是bit
 * @param latency   读端口的延迟
 */
case class BRamIPConfig(dataWidth: Int = 32, size: Int = 4 * 1024 * 8, latency: Int = 1,
                        writeModeA: String = "no_change", writeModeB: String = "no_change") {
  def depth = size / dataWidth
}

class DualPortBram(config: BRamIPConfig) extends BlackBox {
  setDefinitionName("dual_port_bram")
  val generic = new Generic {
    val DATA_WIDTH = config.dataWidth
    val DEPTH = config.depth
    val MEMORY_PRIMITIVE = "block"
    val LATENCY = config.latency
    val WRITE_MODE_A = config.writeModeA
    val WRITE_MODE_B = config.writeModeB
  }

  val io = new Bundle {
    val clk = in Bool
    val rst = in Bool
    val portA = slave(RamPort(config.dataWidth, log2Up(config.depth)))
    val portB = slave(RamPort(config.dataWidth, log2Up(config.depth)))
  }
  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  noIoPrefix()
  addPrePopTask { () => {
    for (bt <- io.flatten) {
      val name = bt.getName()
      if (name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
      if (name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
    }
  }
  }
  addRTLPath("./rtl/dual_port_ram.v")
}

/**
 * @param dataWidth 数据宽度，单位是bit
 * @param size      ram总的大小，单位是bit
 * @param latency   读端口的延迟
 */
class DualPortLutram(dataWidth: Int = 32, size: Int = 4 * 1024 * 8,
                     latency: Int = 1) extends BlackBox {
  setDefinitionName("dual_port_lutram")
  val generic = new Generic {
    val DATA_WIDTH = dataWidth
    val DEPTH = size / dataWidth
    val LATENCY = latency
  }

  val io = new Bundle {
    val clk = in Bool
    val rst = in Bool
    val portA = slave(RamPort(dataWidth, log2Up(size / dataWidth)))
    val portB = slave(RamPort(dataWidth, log2Up(size / dataWidth), readOnly = true))
  }

  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  noIoPrefix()
  addPrePopTask { () => {
    for (bt <- io.flatten) {
      val name = bt.getName()
      if (name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
      if (name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
    }
  }
  }
  addRTLPath("./rtl/dual_port_ram.v")
}

object DualPortRam {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new Component {
      val bram = new DualPortBram(BRamIPConfig(32 * 8, 4 * 1024 * 8))
      val lutRam = new DualPortLutram(32 * 8, 4 * 1024 * 8, 0)
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
      lutRam.io.portB.we.assignDontCare()
      lutRam.io.portB.din.assignDontCare()
    })
  }
}
