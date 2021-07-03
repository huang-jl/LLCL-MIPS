package tlb

import spinal.core._
import spinal.lib._

//TODO: 如果取指不经过cache怎么办
//TODO: CP0的某些寄存器如果是特殊值，可以让kesg0变成Uncached

/** @param useTLB: 是否使用TLB进行地址转换
  */
class CPUMMUInterface(useTLB: Boolean) extends Bundle with IMasterSlave {
  val asid       = Bits(TLBConfig.asidWidth bits) //当前EntryHi对应的ASID
  val instVaddr  = UInt(32 bits)                  //指令对应虚拟地址
  val dataVaddr  = UInt(32 bits)                  //数据对应虚拟地址
  val instRes    = new TLBTranslationRes
  val dataRes    = new TLBTranslationRes
  val instCached = Bool                           //inst取指是否需要经过cache
  val dataCached = Bool                           //data访存是否需要经过cache
  val instMapped = if (useTLB) Bool else null     //inst取指是否需要经过MMU
  val dataMapped = if (useTLB) Bool else null     //data取指是否需要经过MMU

  //TLB指令相关的内容，包括tlbp, tlbr, tlbwi, tlbwr
  /** 不是Index寄存器，而是Index寄存器（或者Random寄存器）中的值 */
  val index = if (useTLB) UInt(TLBConfig.tlbIndexWidth bits) else null //要读或者要写的index
  val wdata = if (useTLB) new TLBEntry else null                       //需要把要写入的TLB内容提前填充成一个Entry项
  val write = if (useTLB) Bool else null                               //是否把wdata写入TLB中
  val rdata = if (useTLB) new TLBEntry else null

  //针对tlbp的
  val probeVPN2 = if (useTLB) Bits(TLBConfig.vpn2Width bits) else null
  val probeASID =
    if (useTLB) Bits(TLBConfig.asidWidth bits) else null //TODO: 这里的asid和上面的asid应该一直是一样的？
  val probeIndex = if (useTLB) Bits(32 bits) else null //这个值可以直接写回Index CP0寄存器

  override def asMaster(): Unit = {
    in(instRes, dataRes, instCached, dataCached)
    out(asid, instVaddr, dataVaddr)
    if (useTLB) {
      in(rdata, probeIndex, instMapped, dataMapped)
      out(index, wdata, write, probeVPN2, probeASID)
    }
  }
}

/** @param useTLB 是否使用TLB进行地址转换
  *               开启TLB后支持useg, kseg0, kseg1, kseg3
  */
class MMU(useTLB: Boolean) extends Component {
  val io = slave(new CPUMMUInterface(useTLB = useTLB))
  if (useTLB) {
    val tlb = new TLB
    //tlb连线
    val tlbWire = new Area {
      tlb.io.asid := io.asid
      tlb.io.instVaddr := io.instVaddr
      tlb.io.dataVaddr := io.dataVaddr
      io.instRes := tlb.io.instRes
      io.dataRes := tlb.io.dataRes

      tlb.io.index := io.index
      tlb.io.wdata := io.wdata
      tlb.io.write := io.write
      io.rdata := tlb.io.rdata

      tlb.io.probeVPN2 := io.probeVPN2
      tlb.io.probeASID := io.probeASID
      io.probeIndex := tlb.io.probeIndex
    }
    //在Uncache区域或者TLB的C字段为2，那么就Uncache
    io.instCached := !(MMU.isUncachedSection(
      io.instVaddr
    ) | (io.instMapped & tlb.io.instRes.cacheAttr === 2))
    io.instMapped := !MMU.isUnmappedSection(io.instVaddr)
    io.dataCached := !(MMU.isUncachedSection(
      io.dataVaddr
    ) | (io.dataMapped & tlb.io.dataRes.cacheAttr === 2))
    io.dataMapped := !MMU.isUnmappedSection(io.dataVaddr)
    when(!io.instMapped) {
      io.instRes.paddr := (B(0, 3 bits) ## io.instVaddr(0, 29 bits)).asUInt
    }
    when(!io.dataMapped) {
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

    io.instCached := !MMU.isUncachedSection(io.instVaddr)
    io.dataCached := !MMU.isUncachedSection(io.dataVaddr)
  }
}

object MMU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MMU(true))
  }

  def isUncachedSection(vaddr: UInt): Bool = {
    vaddr(29, 3 bits) === U"3'b101"
  }

  def isUnmappedSection(vaddr: UInt): Bool = {
    vaddr(30, 2 bits) === U"2'b10"
  }
}
