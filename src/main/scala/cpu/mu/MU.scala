package cpu.mu

import cpu.defs.ConstantVal._
import cpu.defs.{SramBus, SramBusConfig}
import spinal.core._
import spinal.lib.fsm._
import spinal.lib.master

class MU extends Component {
  // in
  val addr = in UInt (32 bits)
  val data_in = in Bits (32 bits)
  val re = in Bool
  val we = in Bool
  val be = in UInt (2 bits)
  val ex = in(MU_EX)
  // out
  val data_out = out Bits (32 bits)
  val stall = out Bool
  //sram-like bus
  val sramBus = master(SramBus(SramBusConfig(32, 32, 2)))
  val signExt = Bits(32 bits)
  val unsignExt = Bits(32 bits)
  val byteIndex: UInt = addr(1 downto 0)
  val MUFsm = new StateMachine {
    val IDLE: State = new State with EntryPoint {
      whenIsActive {
        when(we)(goto(WRITE_START))
          .elsewhen(re)(goto(READ_START))
      }
    }
    val READ_START = new State {
      whenIsActive {
        sramBus.req := True
        when(sramBus.addrOk)(goto(READ))
      }
    }
    val READ = new State {
      whenIsActive {
        when(sramBus.dataOk)(goto(DONE))
      }
    }
    val WRITE_START = new State {
      whenIsActive {
        sramBus.wr := True
        when(sramBus.addrOk)(goto(WRITE))
      }
    }
    val WRITE = new State {
      whenIsActive {
        when(sramBus.dataOk)(goto(DONE))
      }
    }
    val DONE = new State {
      whenIsActive(goto(IDLE))
    }
  }

  //concat sign bit
  def signExtend(signal: Bits): Bits = signal.asSInt.resize(32).asBits

  sramBus.req := re | we
  sramBus.wr := we
  sramBus.addr := addr
  sramBus.wdata := data_in
  sramBus.size := be.asBits

  signExt := sramBus.rdata
  unsignExt := sramBus.rdata
  switch(be) {
    is(0) {
      signExt := signExtend(sramBus.rdata((byteIndex << 3), 8 bits))
      unsignExt := unsignExtend(sramBus.rdata((byteIndex << 3), 8 bits))
    }
    is(1) {
      // the byteIndex can only be 0 or 2
      signExt := signExtend(sramBus.rdata((byteIndex << 3), 16 bits))
      unsignExt := unsignExtend(sramBus.rdata((byteIndex << 3), 16 bits))
    }
    is(2) {
      signExt := sramBus.rdata
      unsignExt := sramBus.rdata
    }
  }
  data_out := (ex === MU_EX.s) ? signExt | unsignExt

  //concat zero
  def unsignExtend(signal: Bits): Bits = signal.resize(32)

  stall := !MUFsm.isActive(MUFsm.DONE) //stall when fsm not in DONE
}

object MU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MU)
  }
}