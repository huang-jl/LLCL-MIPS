package cpu

import defs.ConstantVal
import ip.CrossBarIP

import spinal.core._
import spinal.lib.{cpu => _, _}
import spinal.lib.bus.amba4.axi._
import scala.language.postfixOps
import STAGES._

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

    val aclk    = in Bool ()
    val daclk   = in Bool ()
    val aresetn = in Bool ()

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
    val cpu      = new MultiIssueCPU
    val crossbar = CrossBarIP(3, 1) //3 x 1 AXI4 CrossBar
    CrossBarIP.connect(
      crossbar,
      Array(cpu.io.icacheAXI, cpu.io.dcacheAXI, cpu.io.uncacheAXI),
      Array(io.axi)
    )
    cpu.io.externalInterrupt := io.ext_int(0, 5 bits)
    // 每次发出写请求时就寄存一次wid
    val wid = RegNextWhen(io.axi.aw.id, io.axi.aw.valid, U(0))
    io.wid := wid
  }.setName("")

  val ca = new ClockingArea(
    ClockDomain(
      io.daclk,
      io.aresetn,
      config = ClockDomainConfig(
        resetActiveLevel = LOW
      )
    )
  ) {
    val sel = RegInit(False)
    sel := !sel

    val cpu = clockingArea.cpu

    val wb = io.debug.wb
    when(sel) {
      wb.rf.wen := B(4 bits, default -> cpu.p1(WB).stored(cpu.rfuWE).pull)
      wb.rf.wdata := cpu.p1(WB).stored(cpu.rfuData).pull
      wb.rf.wnum := cpu.p1(WB).stored(cpu.rfuIndex).pull
      wb.pc := cpu.p1(WB).stored(cpu.pc).pull
    } otherwise {
      wb.rf.wen := B(4 bits, default -> cpu.p0(WB).stored(cpu.rfuWE).pull)
      wb.rf.wdata := cpu.p0(WB).stored(cpu.rfuData).pull
      wb.rf.wnum := cpu.p0(WB).stored(cpu.rfuIndex).pull
      wb.pc := cpu.p0(WB).stored(cpu.pc).pull
    }
  }

  noIoPrefix()

  addPrePopTask { () =>
    io.axi.setName("")

    for (bt <- io.axi.flatten) {
      bt.setName(bt.getName().replace("payload", "").replace("_", ""))
    }
  }
}

class LLCL_CPU extends Component {
  setDefinitionName("llcl_cpu")

  val io = new Bundle {
    val ext_int = in Bits (5 bits)

    val aclk    = in Bool ()
    val aresetn = in Bool ()

    val icacheAXI = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
    val dcacheAXI = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
    val uncacheAXI = master(Axi4(ConstantVal.AXI_BUS_CONFIG))

    val pc = out UInt(64 bits)
  }

  val aClockDomain = ClockDomain(
    clock = io.aclk,
    reset = io.aresetn,
    config = ClockDomainConfig(
      resetActiveLevel = LOW
    )
  )

  val clockingArea = new ClockingArea(aClockDomain) {
    val cpu      = new MultiIssueCPU
    cpu.io.externalInterrupt := io.ext_int
    cpu.io.icacheAXI <> io.icacheAXI
    cpu.io.dcacheAXI <> io.dcacheAXI
    cpu.io.uncacheAXI <> io.uncacheAXI
    io.pc(0, 32 bits) := cpu.p0(WB).stored(cpu.pc).pull
    io.pc(32, 32 bits) := cpu.p1(WB).stored(cpu.pc).pull
  }.setName("")

  noIoPrefix()

  addPrePopTask { () =>
    io.icacheAXI.setName("icache")
    io.dcacheAXI.setName("dcache")
    io.uncacheAXI.setName("uncache")
    val target = Array("r", "ar", "w", "aw", "b")
    for (bt <- io.flatten) {
      var name = bt.getName().replace("payload_", "").
        replace("AXI", "")
      for(t <- target) {
        name = name.replace(t + "_", t)
      }
      bt.setName(name)
    }
  }
}

object LLCL_CPU {
  def main(args: Array[String]): Unit = {
    val report = SpinalVerilog(SpinalConfig(removePruned = true))(new LLCL_CPU)
    report.mergeRTLSource("mergeRTL")
  }
}


object Generate {
  def main(args: Array[String]): Unit = {
    val report = SpinalVerilog(SpinalConfig(removePruned = true))(new MyCPUTop)
    report.mergeRTLSource("mergeRTL")
    report.printPruned()
  }
}
