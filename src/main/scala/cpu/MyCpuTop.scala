package cpu

import defs.ConstantVal
import ip.CrossBarIP

import spinal.core._
import spinal.lib.{cpu => _, _}
import spinal.lib.bus.amba4.axi._

case class DebugInterface() extends Bundle {
  val wb = new Bundle {
    val pc = UInt(32 bits)
    val rf = new Bundle {
      val wen   = Bits(4 bits)
      val wnum  = UInt(5 bits)
      val wdata = Bits(32 bits)
    }
  }
}

class MyCPUTop extends Component {
  setDefinitionName("mycpu_top")

  val io = new Bundle {
    val ext_int = in Bits (6 bits)

    val aclk    = in Bool
    val aresetn = in Bool

    val axi = master(Axi4(ConstantVal.AXI_BUS_CONFIG))

    val wid = out(UInt(ConstantVal.AXI_BUS_CONFIG.idWidth bits))

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
    val cpu      = new CPU
    val crossbar = CrossBarIP(3, 1) //3 x 1 AXI4 CrossBar
    CrossBarIP.connect(
      crossbar,
      Array(cpu.io.icacheAXI, cpu.io.dcacheAXI, cpu.io.uncacheAXI),
      Array(io.axi)
    )
    cpu.io.externalInterrupt := io.ext_int
    // 每次发出写请求时就寄存一次wid
    val wid = RegNextWhen(io.axi.aw.id, io.axi.aw.valid, U(0))
    io.wid := wid

    val wb = io.debug.wb
    wb.rf.wen := B(4 bits, default -> cpu.WB.input(cpu.rfuWe).pull)
    wb.rf.wdata := cpu.WB.input(cpu.rfuData).pull
    wb.rf.wnum := cpu.WB.input(cpu.rfuAddr).pull
    wb.pc := cpu.WB.input(cpu.pc).pull
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
    val report = SpinalVerilog(SpinalConfig(removePruned = true))(new MyCPUTop)
    report.mergeRTLSource("mergeRTL")
    report.printPruned()
  }
}
