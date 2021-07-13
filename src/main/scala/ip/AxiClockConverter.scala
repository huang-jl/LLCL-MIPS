package ip

import cpu.defs.ConstantVal
import spinal.core._
import spinal.lib.bus.amba4.axi._
import spinal.lib.{cpu => _, _}
import ip.sim._

class AxiClockConverter extends BlackBox {
  setDefinitionName("axi_clock_converter")

  val io = new Bundle {
    val s_axi_aclk    = in Bool ()
    val s_axi_aresetn = in Bool ()
    val s_axi         = slave(Axi4(ConstantVal.AXI_BUS_CONFIG))

    val m_axi_aclk    = in Bool ()
    val m_axi_aresetn = in Bool ()
    val m_axi         = master(Axi4(ConstantVal.AXI_BUS_CONFIG))
  }

  noIoPrefix()
  addPrePopTask(() => {
    for (
      axi <- Seq(io.s_axi, io.m_axi);
      bt  <- axi.flatten
    ) {
      val idx = bt.getName().lastIndexOf('_')
      if (idx >= 0) {
        bt.setName(bt.getName().substring(0, idx).concat(bt.getName().substring(idx + 1)))
      }
    }
  })

  if (isInSim) {
    addRTLPath("./rtl/axi_clock_converter_sim_netlist.v")
  }
}
