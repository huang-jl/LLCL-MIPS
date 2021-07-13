package ram

import spinal.core._
import spinal.lib.bus.amba4.axi.{Axi4, Axi4Config}
import spinal.lib._

case class AXIRamConfig(
    dataWidth: Int = 32,
    addrWidth: Int = 16,
    idWidth: Int = 8,
    pipelineOutput: Int = 0
) {
  def strbWidth: Int = dataWidth / 8
}

class axi_ram(config: AXIRamConfig) extends BlackBox {
  val generic = new Generic {
    val DATA_WIDTH      = config.dataWidth
    val ADDR_WIDTH      = config.addrWidth
    val STRB_WIDTH      = config.strbWidth
    val ID_WIDTH        = config.idWidth
    val PIPELINE_OUTPUT = config.pipelineOutput
  }

  val io = new Bundle {
    val axi = slave(
      new Axi4(
        Axi4Config(
          addressWidth = config.addrWidth,
          dataWidth = config.dataWidth,
          idWidth = config.idWidth,
          useRegion = false,
          useQos = false
        )
      )
    )
    val clk = in Bool ()
    val rst = in Bool ()
  }

  noIoPrefix()
  mapClockDomain(clock = io.clk, reset = io.rst)

  private def renameIO(): Unit = {
    io.flatten.foreach(bt => {
      val pattern = "(.*?)_(ar|r|w|aw|b)_(.*?)".r
      var target  = pattern.replaceFirstIn(bt.getName(), "s_$1_$2$3")
      target = "payload_".r.replaceAllIn(target, "")
//      println(s"origin = ${bt.getName()}, target = $target")
      bt.setName(target)
    })
  }

  addPrePopTask(() => renameIO())

  addRTLPath("./rtl/axi_ram.v")
}
