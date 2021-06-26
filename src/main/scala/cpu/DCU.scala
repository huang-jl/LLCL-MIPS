package cpu

import spinal.core._
import spinal.lib.{master, slave}
import spinal.lib.bus.amba4.axi.Axi4
import cache.{CPUDCacheInterface, CacheRamConfig, DCache}
import Utils.{signExtend, zeroExtend}
import defs.ConstantVal
import lib.Optional

//暂时把ICache包了一层
class DCU(config: CacheRamConfig, fifoDepth: Int = 16) extends Component {
  val io = new Bundle {
    // in
    val addr = in UInt (32 bits)
    val wdata = in Bits (32 bits)
    val read = in Bool
    val write = in Bool
    val byteEnable = in UInt (2 bits)
    val extend = in(MU_EX())
    // out
    val rdata = out Bits (32 bits)
    val stall = out Bool

    val uncache = in Bool
    val exception = out(Optional(EXCEPTION()))

    val axi = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
    val uncacheAXI = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
  }

  val dcache = new DCache(config, fifoDepth)
  dcache.io.axi <> io.axi
  dcache.io.uncacheAXI <> io.uncacheAXI

  //TODO 可以考虑直接拿过来就是对应的be?
  val byteOffset = io.addr(0, 2 bits)
  val addrValid = Bool
  val byteEnable = Bits(4 bits)
  val wdata = B(0, 32 bits)

  val rdata = Bits(32 bits)
  val signExt = Bits(32 bits)
  val unsignExt = Bits(32 bits)
  switch(io.byteEnable) {
    is(0) {
      addrValid := True
      byteEnable := (B"1'b1" << byteOffset).resize(4)
      wdata(byteOffset << 3, 8 bits) := io.wdata(0, 8 bits)
      signExt := signExtend(rdata((byteOffset << 3), 8 bits))
      unsignExt := zeroExtend(rdata((byteOffset << 3), 8 bits))
    }
    is(1) {
      addrValid := io.addr(0).asBits === 0
      byteEnable := (B"2'b11" << byteOffset).resize(4)
      wdata(byteOffset << 3, 16 bits) := io.wdata(0, 16 bits)
      signExt := signExtend(rdata((byteOffset << 3), 16 bits))
      unsignExt := zeroExtend(rdata((byteOffset << 3), 16 bits))
    }
    is(3) {
      addrValid := io.addr(0, 2 bits) === 0
      byteEnable := B"4'b1111"
      wdata := io.wdata
      signExt := rdata
      unsignExt := rdata
    }
    default {
      addrValid := False
      byteEnable := B"4'b1111"
      signExt := rdata
      unsignExt := rdata
    }
  }

  io.exception := None
  when(!addrValid) {
    when(io.read)(io.exception := EXCEPTION.AdEL)
      .elsewhen(io.write)(io.exception := EXCEPTION.AdES)
  }

  io.rdata := (io.extend === MU_EX.s) ? signExt | unsignExt
  when(io.uncache) {
    dcache.io.uncache.read := io.read & addrValid
    dcache.io.uncache.write := io.write & addrValid
    dcache.io.uncache.addr := io.addr
    dcache.io.uncache.byteEnable := byteEnable
    dcache.io.uncache.wdata := wdata

    io.stall := dcache.io.uncache.stall
    rdata := dcache.io.uncache.rdata

    dcache.io.cpu.read := False
    dcache.io.cpu.write := False
    dcache.io.cpu.addr.assignDontCare()
    dcache.io.cpu.wdata.assignDontCare()
    dcache.io.cpu.byteEnable.assignDontCare()
  }.otherwise {
    dcache.io.cpu.read := io.read & addrValid
    dcache.io.cpu.write := io.write & addrValid
    dcache.io.cpu.addr := io.addr
    dcache.io.cpu.byteEnable := byteEnable
    dcache.io.cpu.wdata := wdata

    io.stall := dcache.io.cpu.stall
    rdata := dcache.io.cpu.rdata

    dcache.io.uncache.read := False
    dcache.io.uncache.write := False
    dcache.io.uncache.addr.assignDontCare()
    dcache.io.uncache.wdata.assignDontCare()
    dcache.io.uncache.byteEnable.assignDontCare()
  }
}

object DCU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DCU(CacheRamConfig(sim = true), 8))
  }
}
