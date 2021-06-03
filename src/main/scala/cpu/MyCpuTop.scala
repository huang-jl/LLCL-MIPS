package cpu

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._

import defs.{SramBus, ConstantVal}

class CpuAXIInterface extends BlackBox {
  setDefinitionName("cpu_axi_interface")

  val io = new Bundle {
    val clk = in Bool
    val resetn = in Bool

    val inst = slave(SramBus(ConstantVal.SRAM_BUS_CONFIG))
    val data = slave(SramBus(ConstantVal.SRAM_BUS_CONFIG))

    val axi = master(Axi4(ConstantVal.AXI_BUS_CONFIG))

    val wid = out Bits (ConstantVal.AXI_BUS_CONFIG.idWidth bits)
  }

  addPrePopTask { () =>
    for (bt <- io.inst.flatten ++ io.data.flatten) {
      bt.setName(bt.getName().replace("Ok", "_ok"))
    }

    io.axi.setName("")

    for (bt <- io.axi.flatten) {
      bt.setName(bt.getName().replace("payload", "").replace("_", ""))
    }
  }

  noIoPrefix()

  mapCurrentClockDomain(
    clock = io.clk,
    reset = io.resetn,
    resetActiveLevel = LOW
  )

  addRTLPath("official/cpu_axi_interface.v")
}

case class DebugInterface() extends Bundle {
  val wb = new Bundle {
    val pc = Bits (32 bits)
    val rf = new Bundle {
      val wen = Bits (4 bits)
      val wnum = UInt (5 bits)
      val wdata = Bits (32 bits)
    }
  }
}

class MyCPUTop extends Component {
  setDefinitionName("mycpu_top")

  val io = new Bundle {
    val ext_int = in Bits (6 bits)

    val aclk = in Bool
    val aresetn = in Bool

    val axi = master(Axi4(ConstantVal.AXI_BUS_CONFIG))

    val wid = out Bits (ConstantVal.AXI_BUS_CONFIG.idWidth bits)

    val debug = out(DebugInterface())
  }

  val aClockDomain = ClockDomain(
    clock = io.aclk,
    reset = io.aresetn,
    config = ClockDomainConfig(
      resetActiveLevel = LOW
    )
  )

  val clockingArea = new ClockingArea(aClockDomain) {
    val cpuAxiInterface = new CpuAXIInterface
    io.axi << cpuAxiInterface.io.axi
    io.wid := cpuAxiInterface.io.wid

    val cpu = new CPU
    cpu.io.iSramBus >> cpuAxiInterface.io.inst
    cpu.io.dSramBus >> cpuAxiInterface.io.data
    io.debug := cpu.io.debug
  }

  noIoPrefix()

  addPrePopTask { () =>
    io.axi.setName("")

    for (bt <- io.axi.flatten) {
      bt.setName(bt.getName().replace("payload", "").replace("_", ""))
    }
  }
}

object Generate {
  def main(args: Array[String]): Unit = {
    val report = SpinalVerilog(new MyCPUTop)
    report.mergeRTLSource("mergeRTL")
  }
}
