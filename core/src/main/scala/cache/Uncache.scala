package cache

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi.{Axi4, Axi4Config}
import spinal.lib.fsm.{EntryPoint, State, StateMachine}

object Uncache {
  val axiConfig = Axi4Config(
    addressWidth = 32, dataWidth = 32, idWidth = 4,
    useRegion = false, useQos = false
  )
}

class Uncache extends Component {
  def translateByteEnable(byteEnable: Bits, byteIndex: UInt): Bits = {
    var res = B"4'b1111"
    switch(byteEnable) {
      is(0) {
        res \= B"4'b0001"
      }
      is(1) {
        switch(byteIndex) {
          is(0)(res \= B"4'b0011")
          is(2)(res \= B"4'b1100")
        }
      }
      is(2)(res \= B"4'b1111")
    }
    res
  }

  val io = new Bundle {
    val cpu = slave(new CPUDCacheInterface)
    val axi = master(new Axi4(Uncache.axiConfig))
  }

  val recv = Reg(Bits(32 bits)) init (0) // register for data from axi bus

  //ar
  io.axi.ar.addr := (io.cpu.addr(31 downto 2) ## B(0, 2 bits)).asUInt
  io.axi.ar.id := 0
  io.axi.ar.lock := 0
  io.axi.ar.cache := 0
  io.axi.ar.prot := 0
  io.axi.ar.len := 0 // burst length is one for uncached transfer
  io.axi.ar.size := U"3'b010" //2^2 = 4Bytes
  io.axi.ar.burst := B"2'b01" //INCR
  io.axi.ar.valid := False
  //r
  io.axi.r.ready := False
  //Write
  //aw
  io.axi.aw.addr := (io.cpu.addr(31 downto 2) ## B(0, 2 bits)).asUInt
  io.axi.aw.id := 0
  io.axi.aw.lock := 0
  io.axi.aw.cache := 0
  io.axi.aw.prot := 0
  io.axi.aw.len := 0 // burst length is one for uncached transfer
  io.axi.aw.size := U"3'b010" //2^2 = 4Bytes
  io.axi.aw.burst := B"2'b01" //INCR
  io.axi.aw.valid := False
  //w
  io.axi.w.valid := False
  io.axi.w.last := False
  io.axi.w.strb := translateByteEnable(io.cpu.byteEnable, io.cpu.addr(1 downto 0))
  io.axi.w.data := io.cpu.wdata
  //b
  io.axi.b.ready := True

  val uncacheFSM = new StateMachine {
    val IDLE: State = new State with EntryPoint {
      whenIsActive {
        when(io.cpu.write)(goto(WRITE_START))
          .elsewhen(io.cpu.read)(goto(READ_START))
      }
    }
    val READ_START: State = new State {
      whenIsActive {
        io.axi.ar.valid := True
        when(io.axi.ar.ready)(goto(READ))
      }
    }
    val READ: State = new State {
      whenIsActive {
        when(io.axi.r.last & io.axi.r.valid) {
          io.axi.r.ready := True
          recv := io.axi.r.data
          goto(DONE)
        }
      }
    }
    val WRITE_START: State = new State {
      whenIsActive {
        io.axi.aw.valid := True
        when(io.axi.aw.ready)(goto(WRITE))
      }
    }
    val WRITE: State = new State {
      whenIsActive {
        io.axi.w.last := True
        io.axi.w.valid := True
        when(io.axi.w.ready)(goto(WAIT_B))
      }
    }
    val WAIT_B: State = new State {
      whenIsActive {
        when(io.axi.b.valid)(goto(DONE))
      }
    }
    val DONE: State = new State {
      whenIsActive(goto(IDLE))
    }
  }

  //cpu send uncached request have to wait one cycle
  io.cpu.stall := !(uncacheFSM.isActive(uncacheFSM.DONE) ||
    (uncacheFSM.isActive(uncacheFSM.IDLE) && !io.cpu.read && !io.cpu.write))
  io.cpu.rdata := recv
}
