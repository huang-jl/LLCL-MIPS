package cpu

import cpu.defs.ConstantVal
import spinal.core._
import Utils.{signExtend, zeroExtend}

import scala.language.postfixOps

object ME_TYPE extends SpinalEnum {
  val normal, lwl, lwr, swl, swr = ConstantVal.FINAL_MODE generate newElement()
}

// 帮助ME阶段进行地址检查和byteEnable转换
class DCU1 extends Component {
  val io = new Bundle {
    val input = new Bundle {
      val byteOffset = in UInt (2 bits)  //stage1
      val byteEnable = in UInt (2 bits)  //stage1
      val wdata      = in Bits (32 bits) //此时输入的wdata就是rtValue
      val meType     = Utils.instantiateWhen(in(ME_TYPE()), ConstantVal.FINAL_MODE)
      val userMode   = Utils.instantiateWhen(in(Bool), ConstantVal.FINAL_MODE)
    }
    val output = new Bundle {
      val wdata      = out Bits (32 bits) //stage2
      val byteEnable = out Bits (4 bits)  //stage1，只对写寄存器有用
      val addrValid  = out Bool ()        //stage1
    }
  }

  val byteOffset = io.input.byteOffset
  //used by stage1
  switch(io.input.byteEnable) {
    is(0) {
      io.output.addrValid := True
    }
    is(1) {
      io.output.addrValid := byteOffset(0, 1 bits) === 0
    }
    is(3) {
      io.output.addrValid := byteOffset(0, 2 bits) === 0
    }
    default {
      io.output.addrValid := False
    }
  }

  switch(io.input.byteEnable) {
    is(0) {
      io.output.wdata := Vec.fill(4)(io.input.wdata(0, 8 bits)).asBits
      io.output.byteEnable := (B"1'b1" << byteOffset).resize(4)
    }
    is(1) {
      io.output.wdata := Vec.fill(2)(io.input.wdata(0, 16 bits)).asBits
      io.output.byteEnable := (B"2'b11" << byteOffset).resize(4)
    }
    default {
//      is(3)
      io.output.wdata := io.input.wdata
      io.output.byteEnable := B"4'b1111"
    }
  }

  if (ConstantVal.FINAL_MODE) {
    when(io.input.meType =/= ME_TYPE.normal) {
      io.output.addrValid := True
    }
    switch(io.input.meType) {
      is(ME_TYPE.swl) {
        switch(byteOffset) {
          is(U"2'b00")(io.output.byteEnable := B"4'b0001")
          is(U"2'b01")(io.output.byteEnable := B"4'b0011")
          is(U"2'b10")(io.output.byteEnable := B"4'b0111")
          is(U"2'b11")(io.output.byteEnable := B"4'b1111")
        }
      }
      is(ME_TYPE.swr) {
        switch(byteOffset) {
          is(U"2'b00")(io.output.byteEnable := B"4'b1111")
          is(U"2'b01")(io.output.byteEnable := B"4'b1110")
          is(U"2'b10")(io.output.byteEnable := B"4'b1100")
          is(U"2'b11")(io.output.byteEnable := B"4'b1000")
        }
      }
    }
  }
}

class DCU2 extends Component {
  val io = new Bundle {
    val input = new Bundle {
      val byteEnable = in Bits (4 bits) //这个byteEnable应该是经过DCU1转换过的
      val rdata      = in Bits (32 bits)
      val extend     = in(MU_EX())
      val rtValue    = ConstantVal.FINAL_MODE generate in Bits (32 bits)
      val meType     = ConstantVal.FINAL_MODE generate in(ME_TYPE())
      val byteOffset = ConstantVal.FINAL_MODE generate in UInt (2 bits)
    }
    val output = new Bundle {
      val rdata = out Bits (32 bits) //stage2
    }
  }
  val signExt   = Bits(32 bits)
  val unsignExt = Bits(32 bits)

  switch(io.input.byteEnable) {
    is(B"4'b0001") {
      signExt := signExtend(io.input.rdata(0, 8 bits))
      unsignExt := zeroExtend(io.input.rdata(0, 8 bits))
    }
    is(B"4'b0010") {
      signExt := signExtend(io.input.rdata(8, 8 bits))
      unsignExt := zeroExtend(io.input.rdata(8, 8 bits))
    }
    is(B"4'b0100") {
      signExt := signExtend(io.input.rdata(16, 8 bits))
      unsignExt := zeroExtend(io.input.rdata(16, 8 bits))
    }
    is(B"4'b1000") {
      signExt := signExtend(io.input.rdata(24, 8 bits))
      unsignExt := zeroExtend(io.input.rdata(24, 8 bits))
    }
    is(B"4'b0011") {
      signExt := signExtend(io.input.rdata(0, 16 bits))
      unsignExt := zeroExtend(io.input.rdata(0, 16 bits))
    }
    is(B"4'b1100") {
      signExt := signExtend(io.input.rdata(16, 16 bits))
      unsignExt := zeroExtend(io.input.rdata(16, 16 bits))
    }
    default {
      //is(B"4'b1111")
      signExt := io.input.rdata
      unsignExt := io.input.rdata
    }
  }
  io.output.rdata := (io.input.extend === MU_EX.s) ? signExt | unsignExt
  if (ConstantVal.FINAL_MODE) {
    val rtValue = io.input.rtValue
    switch(io.input.meType) {
      is(ME_TYPE.lwl) {
        switch(io.input.byteOffset) {
          is(U"2'b00")(io.output.rdata := io.input.rdata(0, 8 bits) ## rtValue(0, 24 bits))
          is(U"2'b01")(io.output.rdata := io.input.rdata(0, 16 bits) ## rtValue(0, 16 bits))
          is(U"2'b10")(io.output.rdata := io.input.rdata(0, 24 bits) ## rtValue(0, 8 bits))
          is(U"2'b11")(io.output.rdata := io.input.rdata)
        }
      }
      is(ME_TYPE.lwr) {
        switch(io.input.byteOffset) {
          is(U"2'b00")(io.output.rdata := io.input.rdata)
          is(U"2'b01")(io.output.rdata := rtValue(24, 8 bits) ## io.input.rdata(8, 24 bits))
          is(U"2'b10")(io.output.rdata := rtValue(16, 16 bits) ## io.input.rdata(16, 16 bits))
          is(U"2'b11")(io.output.rdata := rtValue(8, 24 bits) ## io.input.rdata(24, 8 bits))
        }
      }
    }
  }
}
