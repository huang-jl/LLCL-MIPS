package cpu

import _root_.cpu.Utils._
import _root_.cpu.defs.{SramBus, SramBusConfig}
import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

object MU_EX extends SpinalEnum {
  val s, u = newElement()
}

class MU extends Component {
  val io = new Bundle {
    // in
    val addr    = in UInt (32 bits)
    val data_in = in Bits (32 bits)
    val re      = in Bool
    val we      = in Bool
    val be      = in UInt (2 bits)
    val ex      = in(MU_EX)
    // out
    val data_out  = out Bits (32 bits)
    val stall     = out Bool
    val exception = out(EXCEPTION())

    //sram-like bus
    val sramBus = master(SramBus(SramBusConfig(32, 32, 2)))
  }

  // Check unaligned address before sending real request
  val addrValid = io.be mux (
    0       -> True,
    1       -> (io.addr(0 downto 0) === 0),
    3       -> (io.addr(1 downto 0) === 0),
    default -> False
  )

  io.exception := EXCEPTION.None
  when (!addrValid) {
    when (io.we) {
      io.exception := EXCEPTION.AdEDS
    } elsewhen(io.re) {
      io.exception := EXCEPTION.AdEDL
    }
  }

  val we = io.we && addrValid
  val re = io.re && addrValid

  val signExt         = Bits(32 bits)
  val unsignExt       = Bits(32 bits)
  val byteIndex: UInt = io.addr(1 downto 0)

  //
  val dataReg = Reg(Bits(32 bits)) init (0)

  io.sramBus.wr := we
  io.sramBus.addr := io.addr(28 downto 0).resize(32)
  io.sramBus.wdata := io.data_in
  io.stall := True

  val addrByteIndex = io.addr(0, 2 bits)
  switch(io.sramBus.size) {
    is(B"2'b00")(
      io.sramBus.wdata(addrByteIndex << 3, 8 bits) := io.data_in(0, 8 bits)
    )
    is(B"2'b01")(
      io.sramBus.wdata(addrByteIndex << 3, 16 bits) := io.data_in(0, 16 bits)
    )
  }

  val MUFsm = new StateMachine {
    val IDLE: State = new State with EntryPoint {
      whenIsNext {
        when(!isStateRegBoot()) {
          io.stall := False
        }
      }
      whenIsActive {
        when(we)(goto(WRITE))
          .elsewhen(re)(goto(READ))
      }
    }
    val READ = new State {
      whenIsActive {
        when(io.sramBus.dataOk) {
          dataReg := io.sramBus.rdata
          goto(DONE)
        }
      }
    }
    val WRITE = new State {
      whenIsActive {
        when(io.sramBus.dataOk)(goto(DONE))
      }
    }
    val DONE = new State {
      whenIsActive(goto(IDLE))
    }
  }

  io.sramBus.req := (re | we) & (MUFsm.isActive(MUFsm.READ) | MUFsm.isActive(
    MUFsm.WRITE
  ))

  switch(io.be) {
    is(0) {
      signExt := signExtend(dataReg((byteIndex << 3), 8 bits))
      unsignExt := zeroExtend(dataReg((byteIndex << 3), 8 bits))
      io.sramBus.size := 0
    }
    is(1) {
      // the byteIndex can only be 0 or 2
      signExt := signExtend(dataReg((byteIndex << 3), 16 bits))
      unsignExt := zeroExtend(dataReg((byteIndex << 3), 16 bits))
      io.sramBus.size := 1
    }
    is(3) {
      signExt := dataReg
      unsignExt := dataReg
      io.sramBus.size := 2
    }
    default {
      io.sramBus.size := 0
      signExt := dataReg
      unsignExt := dataReg
    }
  }
  io.data_out := (io.ex === MU_EX.s) ? signExt | unsignExt
}

object MU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MU)
  }
}
