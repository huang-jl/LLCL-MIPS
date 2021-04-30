package cpu.fetch

import spinal.core._
import cpu.defs._
import spinal.lib._
import spinal.lib.fsm._

class Fetch extends Component {
  val io = new Bundle {
    val dataOut = out(FetchData(32))
    val bus = master(
      SramBus(SramBusConfig(dataWidth = 32, addrWidth = 32, selWidth = 2))
    )
  }

  val pcReg = Reg(UInt(32 bits)) init (ConstantVal.INIT_PC)
  pcReg := pcReg + U"32'd4"

  val default = new Area {
    io.dataOut.pcOut := pcReg
    io.dataOut.instOut := B"32'b0"
    io.dataOut.stall := False
    //Do not write when IF
    io.bus.wr := False
    io.bus.wdata := 0
    io.bus.req := False
    io.bus.addr := 0
    io.bus.size := 0
  }

  val stateMachine = new StateMachine {
    val Idle_S: State = new State with EntryPoint {
      whenIsActive {
        io.bus.req := True
        io.bus.addr := pcReg
        io.bus.size := 2
        goto(AddrHandShake_S)
      }
    }
    val AddrHandShake_S = new State {
      onEntry(io.dataOut.stall := True)
      whenIsActive {
        when(io.bus.addrOk) {
          io.bus.req := False
          io.bus.addr := 0
          io.bus.size := 0
          goto(DataHandShake_S)
        }.otherwise(io.dataOut.stall := True)
      }
    }
    val DataHandShake_S = new State {
      onEntry(io.dataOut.stall := True)
      whenIsActive {
        when(io.bus.dataOk) {
          io.dataOut.instOut := io.bus.rdata
          io.dataOut.stall := False
          goto(Idle_S)
        }.otherwise(io.dataOut.stall := True)
      }
    }
  }
}

object Fetch {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new Fetch)
  }
}
