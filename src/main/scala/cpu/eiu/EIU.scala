package cpu.eiu

import cpu.defs.ConstantVal._
import spinal.core._
import spinal.lib._

class EIU extends Component {
  //
  val E = in Bits (NUM_EXCEPTIONS bits)

  val w = new Bundle {
    val e = in Bool
    val rd_sel = in(EIU_RD_SEL)
    val rd_sel_v = in Bits (32 bits)
  }

  val r = new Bundle {
    val rd_sel = in(EIU_RD_SEL)
    val rd_sel_v = out Bits (32 bits)
  }

  val pcu = new Bundle {
    val pc = in UInt (32 bits)
    val in_slot = in Bool

    val new_pc = out UInt (32 bits)
  }

  //
  val BadVAddr = Reg(Bits(32 bits))
  val Count = Reg(Bits(32 bits))
  val Compare = Reg(Bits(32 bits))
  val Status = new Bundle {
    val reg = RegInit(B"0000_0_0_0_0_0_1_0_0_0_0_00_000000_00_000_00_1_0_0")

    def BEV(): Bool = {
      reg(22)
    }

    def EXL(): Bool = {
      reg(1)
    }

    def IE(): Bool = {
      reg(0)
    }
  }
  val Cause = new Bundle {
    val reg = RegInit(B"0_0_00_0_0_00_0_0_0_00000_000000_00_0_00000_00")

    def BD(): Bool = {
      reg(31)
    }

    def TI(): Bool = {
      reg(30)
    }

    def IPH(): Bits = {
      reg(15 downto 10)
    }

    def IPS(): Bits = {
      reg(9 downto 8)
    }

    def ExcCode: Bits = {
      reg(6 downto 2)
    }
  }
  val EPC = Reg(Bits(32 bits))

  //
  when(w.e) {
    switch(w.rd_sel) {
      is(EIU_RD_SEL.BadVAddr) {
        BadVAddr := w.rd_sel_v
      }
      is(EIU_RD_SEL.Count) {
        Count := w.rd_sel_v
      }
      is(EIU_RD_SEL.Compare) {
        Compare := w.rd_sel_v
      }
      is(EIU_RD_SEL.Status) {
        Status.reg := w.rd_sel_v
      }
      is(EIU_RD_SEL.Cause) {
        Cause.reg := w.rd_sel_v
      }
      is(EIU_RD_SEL.EPC) {
        EPC := w.rd_sel_v
      }
    }
  }

  switch(r.rd_sel) {
    is(EIU_RD_SEL.BadVAddr) {
      r.rd_sel_v := BadVAddr
    }
    is(EIU_RD_SEL.Count) {
      r.rd_sel_v := Count
    }
    is(EIU_RD_SEL.Compare) {
      r.rd_sel_v := Compare
    }
    is(EIU_RD_SEL.Status) {
      r.rd_sel_v := Status.reg
    }
    is(EIU_RD_SEL.Cause) {
      r.rd_sel_v := Cause.reg
    }
    is(EIU_RD_SEL.EPC) {
      r.rd_sel_v := EPC
    }
  }

  val exc = EXCEPTION()
  exc.assignFromBits(B(OHToUInt(OHMasking.first(E))))

  when(exc =/= EXCEPTION.None) {
    when(!Status.EXL) {
      EPC := B(pcu.in_slot ? (pcu.pc - 4) | pcu.pc)
      Cause.BD := pcu.in_slot
    }
    Status.EXL := True
  }
  pcu.new_pc := BigInt("BFC00380", 16)

  switch(exc) {
    is(EXCEPTION.Int) {
      Cause.ExcCode := 0x00
    }
    is(EXCEPTION.AdEIL) {
      Cause.ExcCode := 0x04
    }
    is(EXCEPTION.RI) {
      Cause.ExcCode := 0x0a
    }
    is(EXCEPTION.Sys) {
      Cause.ExcCode := 0x08
    }
    is(EXCEPTION.Bp) {
      Cause.ExcCode := 0x09
    }
    is(EXCEPTION.Ov) {
      Cause.ExcCode := 0x0c
    }
    is(EXCEPTION.AdEDL) {
      Cause.ExcCode := 0x04
    }
    is(EXCEPTION.AdEDS) {
      Cause.ExcCode := 0x05
    }
  }
}

object EIU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new EIU)
  }
}