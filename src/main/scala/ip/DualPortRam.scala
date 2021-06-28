package ip

import spinal.core._
import spinal.lib._

/**
 * @param dataWidth 数据宽度，单位是bit
 * @param addrWidth ram地址宽度
 * */
case class RamPort(dataWidth: Int, addrWidth: Int) extends Bundle with IMasterSlave {
  val en = Bool
  val we = Bool //write enable
  val addr = UInt(log2Up(addrWidth) bits)
  val din = in(Bits(dataWidth bits))
  val dout = out(Bits(dataWidth bits))

  override def asMaster(): Unit = {
    in(dout)
    out(en, we, addr, din)
  }
}

/**
 * @param dataWidth 数据宽度，单位是bit
 * @param size      ram总的大小，单位是bit
 * @param latency   读端口的延迟
 */
class DualPortBram(dataWidth: Int = 32, size: Int = 4 * 1024 * 8,
                   latency: Int = 1) extends BlackBox {
  setDefinitionName("dual_port_bram")
  val generic = new Generic {
    val DATA_WIDTH = dataWidth
    val DEPTH = size / dataWidth
    val MEMORY_PRIMITIVE = "block"
    val LATENCY = latency
    val WRITE_MODE = "no_change"
  }

  val io = new Bundle {
    val clk = in Bool
    val rst = in Bool
    val portA = slave(RamPort(dataWidth, log2Up(size / dataWidth)))
    val portB = slave(RamPort(dataWidth, log2Up(size / dataWidth)))
  }
  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  noIoPrefix()
  addPrePopTask{() => {
    for(bt <- io.flatten) {
      val name = bt.getName()
      if(name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
      if(name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
    }
  }}
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
    val portB = slave(RamPort(dataWidth, log2Up(size / dataWidth)))
  }

  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  noIoPrefix()
  addPrePopTask{() => {
    for(bt <- io.flatten) {
      val name = bt.getName()
      if(name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
      if(name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
    }
  }}
  addRTLPath("./rtl/dual_port_ram.v")
}

object DualPortRam {
  def main(args:Array[String]): Unit ={
    SpinalVerilog(new Component {
      val bram = new DualPortBram(32 * 8, 4 * 1024 * 8)
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
