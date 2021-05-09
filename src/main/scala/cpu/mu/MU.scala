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

  //
  val dataReg = Reg(Bits(32 bits)) init (0)

  //concat sign bit
  def signExtend(signal: Bits): Bits = signal.asSInt.resize(32).asBits

  sramBus.wr := we
  sramBus.addr := addr
  sramBus.wdata := data_in
  stall := True

  val MUFsm = new StateMachine {
    val IDLE: State = new State with EntryPoint {
      whenIsNext {
        when(!isStateRegBoot()) {
          stall := False
        }
      }
      whenIsActive {
        when(we)(goto(WRITE))
          .elsewhen(re)(goto(READ))
      }
    }
    //    val READ_START = new State {
    //      whenIsActive {
    ////        sramBus.req := True
    //        when(sramBus.addrOk)(goto(READ))
    //      }
    //    }
    val READ = new State {
      whenIsActive {
        when(sramBus.dataOk) {
          dataReg := sramBus.rdata
          goto(DONE)
        }
      }
    }
    //    val WRITE_START = new State {
    //      whenIsActive {
    //        sramBus.wr := True
    //        when(sramBus.addrOk)(goto(WRITE))
    //      }
    //    }
    val WRITE = new State {
      whenIsActive {
        when(sramBus.dataOk)(goto(DONE))
      }
    }
    val DONE = new State {
      whenIsActive(goto(IDLE))
    }
  }

  sramBus.req := (re | we) & (MUFsm.isActive(MUFsm.READ) | MUFsm.isActive(MUFsm.WRITE))

  switch(be) {
    is(0) {
      signExt := signExtend(dataReg((byteIndex << 3), 8 bits))
      unsignExt := unsignExtend(dataReg((byteIndex << 3), 8 bits))
      sramBus.size := 0
    }
    is(1) {
      // the byteIndex can only be 0 or 2
      signExt := signExtend(dataReg((byteIndex << 3), 16 bits))
      unsignExt := unsignExtend(dataReg((byteIndex << 3), 16 bits))
      sramBus.size := 1
    }
    is(3) {
      signExt := dataReg
      unsignExt := dataReg
      sramBus.size := 2
    }
    default {
      sramBus.size := 0
      signExt := dataReg
      unsignExt := dataReg
    }
  }
  data_out := (ex === MU_EX.s) ? signExt | unsignExt

  //concat zero
  def unsignExtend(signal: Bits): Bits = signal.resize(32)

  //  stall := !(MUFsm.isActive(MUFsm.DONE) || (MUFsm.isActive(MUFsm.IDLE) & !re & !we)) //stall when fsm not in DONE
}

object MU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MU)
  }
}