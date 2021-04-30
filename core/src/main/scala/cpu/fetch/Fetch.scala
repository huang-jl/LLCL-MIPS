package cpu.fetch

import spinal.core._
import cpu.defs._
import spinal.lib._


class Fetch extends Component {
  val io = new Bundle {
    val dataOut = out(FetchData(32))
    val bus = master(SramBus(SramBusConfig(dataWidth = 32, addrWidth = 32, selWidth = 2)))
  }

  val pcReg = Reg(UInt(32 bits))
  pcReg := pcReg + U"32'd4"

  val default = new Area {
    io.dataOut.pcOut := pcReg
    io.dataOut.instOut := B"32'b0"
    io.dataOut.stall := 0
  }

  val bus = new Area {
    io.bus.wr := False
    io.bus.wdata := B"32'b0"
    io.bus.size := B"2'd2"
    io.bus.addr := pcReg
    when(io.bus.addrOk) {
      io.bus.addr := B"32'b0"
      io.bus.size := B"2'd2"
      io.dataOut.stall := True
    }.elsewhen(io.bus.dataOk) {
      io.dataOut.instOut := io.bus.rdata
    }
  }
}