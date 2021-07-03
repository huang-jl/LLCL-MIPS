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
    val stage1 = new Bundle {
      val index = in UInt(config.indexWidth bits)
      val keepRData = in Bool
      val byteEnable = in UInt(2 bits)  //stage1用来检测地址异常
      val offset = in UInt(config.offsetWidth bits) //用来stage 1检测地址异常的

      val addrValid = out Bool  //stage1输出，表明地址是否合法
    }
    val stage2 = new Bundle {
      val read = in Bool
      val write = in Bool
      val uncache = in Bool
      val paddr = in UInt(32 bits)
      val byteEnable = in UInt(2 bits)
      val wdata = in Bits(32 bits)  //stage2输入，表示要写入的数据
      val extend = in(MU_EX())  //stage2输入，用来符号扩展或0扩展

      val rdata = out Bits(32 bits)
      val stall = out Bool
    }

    val axi = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
    val uncacheAXI = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
  }

  val dcache = new DCache(config, fifoDepth)
  dcache.io.axi <> io.axi
  dcache.io.uncacheAXI <> io.uncacheAXI

  //TODO 可以考虑直接拿过来就是对应的be?
  val wdata = B(0, 32 bits)

  val signExt = Bits(32 bits)
  val unsignExt = Bits(32 bits)
  //used by stage1
  switch(io.stage1.byteEnable) {
    is(0) {
      io.stage1.addrValid := True
    }
    is(1) {
      io.stage1.addrValid := io.stage1.offset(0, 1 bits) === 0
    }
    is(3) {
      io.stage1.addrValid := io.stage1.offset(0, 2 bits) === 0
    }
    default {
      io.stage1.addrValid := False
    }
  }

  val byteOffset = io.stage2.paddr(0, 2 bits)
  switch(io.stage2.byteEnable) {
    is(0){
      dcache.io.cpu.stage2.byteEnable := (B"1'b1" << byteOffset).resize(4)
      wdata(byteOffset << 3, 8 bits) := io.stage2.wdata(0, 8 bits)
      signExt := signExtend(dcache.io.cpu.stage2.rdata(byteOffset << 3, 8 bits))
      unsignExt := zeroExtend(dcache.io.cpu.stage2.rdata(byteOffset << 3, 8 bits))
    }
    is(1){
      dcache.io.cpu.stage2.byteEnable := (B"2'b11" << byteOffset).resize(4)
      wdata(byteOffset << 3, 16 bits) := io.stage2.wdata(0, 16 bits)
      signExt := signExtend(dcache.io.cpu.stage2.rdata(byteOffset << 3, 16 bits))
      unsignExt := zeroExtend(dcache.io.cpu.stage2.rdata(byteOffset << 3, 16 bits))
    }
    is(3){
      dcache.io.cpu.stage2.byteEnable := B"4'b1111"
      wdata := io.stage2.wdata
      signExt := dcache.io.cpu.stage2.rdata
      unsignExt := dcache.io.cpu.stage2.rdata
    }
    default {
      dcache.io.cpu.stage2.byteEnable:= B"4'b1111"
      signExt := dcache.io.cpu.stage2.rdata
      unsignExt := dcache.io.cpu.stage2.rdata
    }
  }

  dcache.io.cpu.stage1.index <> io.stage1.index
  dcache.io.cpu.stage1.keepRData <> io.stage1.keepRData

  dcache.io.cpu.stage2.read := io.stage2.read
  dcache.io.cpu.stage2.write := io.stage2.write
  dcache.io.cpu.stage2.uncache := io.stage2.uncache
  dcache.io.cpu.stage2.paddr := io.stage2.paddr
  dcache.io.cpu.stage2.wdata := wdata

  io.stage2.stall := dcache.io.cpu.stage2.stall
  io.stage2.rdata := (io.stage2.extend === MU_EX.s) ? signExt | unsignExt

//  when(io.uncache) {
//    dcache.io.uncache.read := io.read
//    dcache.io.uncache.write := io.write
//    dcache.io.uncache.addr := io.addr
//    dcache.io.uncache.byteEnable := byteEnable
//    dcache.io.uncache.wdata := wdata
//
//    io.stall := dcache.io.uncache.stall
//    rdata := dcache.io.uncache.rdata
//
//    dcache.io.cpu.read := False
//    dcache.io.cpu.write := False
//    dcache.io.cpu.addr.assignDontCare()
//    dcache.io.cpu.wdata.assignDontCare()
//    dcache.io.cpu.byteEnable.assignDontCare()
//  }.otherwise {
//    dcache.io.cpu.read := io.read
//    dcache.io.cpu.write := io.write
//    dcache.io.cpu.addr := io.addr
//    dcache.io.cpu.byteEnable := byteEnable
//    dcache.io.cpu.wdata := wdata
//
//    io.stall := dcache.io.cpu.stall
//    rdata := dcache.io.cpu.rdata
//
//    dcache.io.uncache.read := False
//    dcache.io.uncache.write := False
//    dcache.io.uncache.addr.assignDontCare()
//    dcache.io.uncache.wdata.assignDontCare()
//    dcache.io.uncache.byteEnable.assignDontCare()
//  }
}

object DCU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DCU(CacheRamConfig(), 8))
  }
}
