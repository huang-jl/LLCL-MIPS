package cpu

import _root_.cpu.defs.ConstantVal
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi.Axi4

class ConfReg(simulation: Boolean) extends BlackBox {
  setDefinitionName("confreg")

  val io = new Bundle {
    val aclk      = in Bool
    val timer_clk = in Bool
    val aresetn   = in Bool

    val axi = slave(Axi4(ConstantVal.AXI_BUS_CONFIG)).setName("")
    val wid = in Bits (4 bits)

    //    val ram_random_mask = out Bits(4 bits)

    val led     = out Bits (15 bits)
    val led_rg0 = out Bits (2 bits)
    val led_rg1 = out Bits (2 bits)
    val num_csn = out Bits (8 bits)
    val num_a_g = out Bits (7 bits)
    val switch  = in Bits (8 bits)
    val btn = new Bundle {
      val key_col = out UInt (4 bits)
      val key_row = in UInt (4 bits)
      val step    = in Bits (2 bits)
    }
  }

  noIoPrefix()
  addPrePopTask { () =>
    for (bt <- io.axi.flatten) {
      bt.setName(
        bt.getName()
          .replace("payload", "")
          .replace("_", "")
      )
    }
  }

  mapCurrentClockDomain(
    clock = io.aclk,
    reset = io.aresetn,
    resetActiveLevel = LOW
  )

  addRTLPath("official/soc_axi_func/rtl/CONFREG/confreg.v")
}
