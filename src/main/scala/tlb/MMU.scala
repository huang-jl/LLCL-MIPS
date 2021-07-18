package tlb

import cpu.Utils
import cpu.defs.ConstantVal
import spinal.core._
import spinal.lib._
import scala.language.postfixOps

//TODO: 如果取指不经过cache怎么办
//TODO: CP0的某些寄存器如果是特殊值，可以让kesg0变成Uncached

/** @param useTLB 是否开启TLB转换
  * @note MMU进行地址翻译后得到的结果
  */
class MMUTranslationRes(useTLB: Boolean) extends Bundle {
  val paddr              = UInt(32 bits)
  val miss, dirty, valid = Bool
  val cached             = Bool
  val mapped             = Utils.instantiateWhen(Bool, useTLB)
}

class CPUMMUInterface extends Bundle with IMasterSlave {
  val asid      = Bits(TLBConfig.asidWidth bits) //当前EntryHi对应的ASID
  val instVaddr = UInt(32 bits)                  //指令对应虚拟地址
  val dataVaddr = UInt(32 bits)                  //数据对应虚拟地址
  val instRes   = new MMUTranslationRes(ConstantVal.FINAL_MODE)
  val dataRes   = new MMUTranslationRes(ConstantVal.FINAL_MODE)

  //TLB指令相关的内容，包括tlbp, tlbr, tlbwi, tlbwr
  /** 不是Index寄存器，而是Index寄存器（或者Random寄存器）中的值 */
  val index = Utils.instantiateWhen(UInt(TLBConfig.tlbIndexWidth bits), ConstantVal.FINAL_MODE)
  val wdata =
    Utils.instantiateWhen(new TLBEntry, ConstantVal.FINAL_MODE) //需要把要写入的TLB内容提前填充成一个Entry项
  val write = Utils.instantiateWhen(Bool, ConstantVal.FINAL_MODE) //是否把wdata写入TLB中
  val rdata = Utils.instantiateWhen(new TLBEntry, ConstantVal.FINAL_MODE)

  //针对tlbp的
  val probeVPN2 = Utils.instantiateWhen(Bits(TLBConfig.vpn2Width bits), ConstantVal.FINAL_MODE)
  val probeASID =
    Utils.instantiateWhen(
      Bits(TLBConfig.asidWidth bits),
      ConstantVal.FINAL_MODE
    ) //TODO: 这里的asid和上面的asid应该一直是一样的？
  val probeIndex =
    Utils.instantiateWhen(Bits(32 bits), ConstantVal.FINAL_MODE) //这个值可以直接写回Index CP0寄存器

  //其他信息
  val K0  = Utils.instantiateWhen(Bits(3 bits), ConstantVal.FINAL_MODE)
  val ERL = Utils.instantiateWhen(Bool, ConstantVal.FINAL_MODE)

  override def asMaster(): Unit = {
    in(instRes, dataRes)
    out(asid, instVaddr, dataVaddr)
    if (ConstantVal.FINAL_MODE) {
      in(rdata, probeIndex)
      out(index, wdata, write, probeVPN2, probeASID, K0, ERL)
    }
  }
}

class MMU extends Component {
  val io = slave(new CPUMMUInterface)
  if (ConstantVal.FINAL_MODE) {
    val tlb = new TLB
    //tlb连线
    val tlbWire = new Area {
      tlb.io.asid := io.asid
      tlb.io.instVaddr := io.instVaddr
      tlb.io.dataVaddr := io.dataVaddr
      io.instRes.assignSomeByName(tlb.io.instRes)
      io.dataRes.assignSomeByName(tlb.io.dataRes)

      tlb.io.index := io.index
      tlb.io.wdata := io.wdata
      tlb.io.write := io.write
      io.rdata := tlb.io.rdata

      tlb.io.probeVPN2 := io.probeVPN2
      tlb.io.probeASID := io.probeASID
      io.probeIndex := tlb.io.probeIndex
    }
    //在Uncache区域或者TLB的C字段为2，那么就Uncache
    io.instRes.cached := !MMU.isUncached(io.instVaddr, tlb.io.instRes.cacheAttr, io.K0, io.ERL)
    io.dataRes.cached := !MMU.isUncached(io.dataVaddr, tlb.io.dataRes.cacheAttr, io.K0, io.ERL)
    io.instRes.mapped := !MMU.isUnmapped(io.instVaddr, io.ERL)
    io.dataRes.mapped := !MMU.isUnmapped(io.dataVaddr, io.ERL)
//    io.instRes.cached := !(MMU.isUncachedSection(
//      io.instVaddr
//    ) | (io.instRes.mapped & tlb.io.instRes.cacheAttr === 2))
//    io.instRes.mapped := !MMU.isUnmappedSection(io.instVaddr)
//    io.dataRes.cached := !(MMU.isUncachedSection(
//      io.dataVaddr
//    ) | (io.dataRes.mapped & tlb.io.dataRes.cacheAttr === 2))
//    io.dataRes.mapped := !MMU.isUnmappedSection(io.dataVaddr)
    when(MMU.isUnmappedSection(io.instVaddr)) {
//    when(!io.instRes.mapped) {
      io.instRes.paddr := (B(0, 3 bits) ## io.instVaddr(0, 29 bits)).asUInt
    }
    when(MMU.isUnmappedSection(io.dataVaddr)) {
//    when(!io.dataRes.mapped) {
      io.dataRes.paddr := (B(0, 3 bits) ## io.dataVaddr(0, 29 bits)).asUInt
    }
  } else {
    //miss已经默认为False了
    io.instRes.assignFromBits(B(0, io.instRes.getBitsWidth bits))
    io.dataRes.assignFromBits(B(0, io.dataRes.getBitsWidth bits))
    io.instRes.allowOverride
    io.dataRes.allowOverride
    io.instRes.paddr := (B(0, 3 bits) ## io.instVaddr(0, 29 bits)).asUInt
    io.dataRes.paddr := (B(0, 3 bits) ## io.dataVaddr(0, 29 bits)).asUInt
    //不开启TLB的时候除了kseg1全是Cache的
    io.instRes.cached := !MMU.isUncachedSection(io.instVaddr)
    io.dataRes.cached := !MMU.isUncachedSection(io.dataVaddr)
  }
}

object MMU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MMU)
  }

  def isUncachedSection(vaddr: UInt): Bool = {
    vaddr(29, 3 bits) === U"3'b101"
  }

  def isUnmappedSection(vaddr: UInt): Bool = {
    vaddr(30, 2 bits) === U"2'b10"
  }

  def isKseg0(vaddr: UInt): Bool = {
    vaddr(29, 3 bits) === U"100"
  }

  def isKseg1(vaddr: UInt): Bool = {
    vaddr(29, 3 bits) === U"101"
  }

  def isKuseg(vaddr: UInt): Bool = {
    vaddr(31).asUInt === U"0"
  }

  def isUncached(vaddr: UInt, cacheAttr: Bits, K0: Bits, ERL: Bool): Bool = {
    //Uncache的情况：
    //1. 落在Uncached区域(kseg1)
    //2. 在mapped区域并且tlb的cacheAttr为2
    //3. 在kseg0并且此时CP0.Config.K0 = 2
    //4. 在kuseg并且此时CP0.Status.ERL = 1
    MMU.isUncachedSection(vaddr) |
      (!MMU.isUnmappedSection(vaddr) & cacheAttr === 2) |
      (MMU.isKseg0(vaddr) & K0 === 2) |
      (MMU.isKuseg(vaddr) & ERL)
  }

  def isUnmapped(vaddr: UInt, ERL: Bool): Bool = {
    //Unmapped的情况
    //1. 在Unmapped区域
    //2. 在Kuseg并且CP0.Status.ERL = 1
    MMU.isUnmappedSection(vaddr) | (MMU.isKuseg(vaddr) & ERL)
  }
}
