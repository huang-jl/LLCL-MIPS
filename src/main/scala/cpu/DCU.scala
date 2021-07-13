package cpu

import spinal.core._
import Utils.{signExtend, zeroExtend}

import scala.language.postfixOps

// 帮助ME阶段进行地址检查和byteEnable转换
class DCU1 extends Component {
  val io = new Bundle {
    val input = new Bundle {
      val paddr      = in UInt (32 bits) //stage1
      val byteEnable = in UInt (2 bits)  //stage1
    }
    val output = new Bundle {
      val byteEnable = out Bits(4 bits) //stage1
      val addrValid  = out Bool()         //stage1
    }
  }

  //used by stage1
  switch(io.input.byteEnable) {
    is(0) {
      io.output.addrValid := True
    }
    is(1) {
      io.output.addrValid := io.input.paddr(0, 1 bits) === 0
    }
    is(3) {
      io.output.addrValid := io.input.paddr(0, 2 bits) === 0
    }
    default {
      io.output.addrValid := False
    }
  }

  val byteOffset = io.input.paddr(0, 2 bits)
  switch(io.input.byteEnable) {
    is(0) {
      io.output.byteEnable := (B"1'b1" << byteOffset).resize(4)
    }
    is(1) {
      io.output.byteEnable := (B"2'b11" << byteOffset).resize(4)
    }
    is(3) {
      io.output.byteEnable := B"4'b1111"
    }
    default {
      io.output.byteEnable := B"4'b1111"
    }
  }
}

class DCU2 extends Component {
  val io = new Bundle {
    val input = new Bundle {
      val byteEnable = in Bits (4 bits) //这个byteEnable应该是经过DCU1转换过的
      val rdata      = in Bits (32 bits)
      val wdata      = in Bits (32 bits)
      val extend     = in(MU_EX())
    }
    val output = new Bundle {
      val wdata = out Bits (32 bits)  //stage2
      val rdata = out Bits (32 bits) //stage2
    }
  }
  val signExt   = Bits(32 bits)
  val unsignExt = Bits(32 bits)

  io.output.wdata := 0
  switch(io.input.byteEnable) {
    is(B"4'b0001") {
      io.output.wdata(0, 8 bits) := io.input.wdata(0, 8 bits)
      signExt := signExtend(io.input.rdata(0, 8 bits))
      unsignExt := zeroExtend(io.input.rdata(0, 8 bits))
    }
    is(B"4'b0010") {
      io.output.wdata(8, 8 bits) := io.input.wdata(0, 8 bits)
      signExt := signExtend(io.input.rdata(8, 8 bits))
      unsignExt := zeroExtend(io.input.rdata(8, 8 bits))
    }
    is(B"4'b0100") {
      io.output.wdata(16, 8 bits) := io.input.wdata(0, 8 bits)
      signExt := signExtend(io.input.rdata(16, 8 bits))
      unsignExt := zeroExtend(io.input.rdata(16, 8 bits))
    }
    is(B"4'b1000") {
      io.output.wdata(24, 8 bits) := io.input.wdata(0, 8 bits)
      signExt := signExtend(io.input.rdata(24, 8 bits))
      unsignExt := zeroExtend(io.input.rdata(24, 8 bits))
    }
    is(B"4'b0011") {
      io.output.wdata(0, 16 bits) := io.input.wdata(0, 16 bits)
      signExt := signExtend(io.input.rdata(0, 16 bits))
      unsignExt := zeroExtend(io.input.rdata(0, 16 bits))
    }
    is(B"4'b1100") {
      io.output.wdata(16, 16 bits) := io.input.wdata(0, 16 bits)
      signExt := signExtend(io.input.rdata(16, 16 bits))
      unsignExt := zeroExtend(io.input.rdata(16, 16 bits))
    }
    default {
      //is(B"4'b1111")
      io.output.wdata := io.input.wdata
      signExt := io.input.rdata
      unsignExt := io.input.rdata
    }
  }
  io.output.rdata := (io.input.extend === MU_EX.s) ? signExt | unsignExt
}