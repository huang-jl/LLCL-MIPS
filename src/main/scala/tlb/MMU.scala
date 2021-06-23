package tlb

import spinal.core._
import spinal.lib._

//TODO: 如果取指不经过cache怎么办
//TODO: CP0的某些寄存器如果是特殊值，可以让kesg0变成Uncached

/** useTLB: 是否使用TLB进行地址转换，useMask：是否在TLB中使用mask（是否实现了PageMask） */
class CPUMMUInterface(useTLB: Boolean, useMask: Boolean) extends Bundle with IMasterSlave {
  val asid = Bits(TLBConfig.asidWidth bits) //当前EntryHi对应的ASID
  val instVaddr = UInt(32 bits) //指令对应虚拟地址
  val dataVaddr = UInt(32 bits) //数据对应虚拟地址
  val instRes = new TLBTranslationRes
  val dataRes = new TLBTranslationRes
  val instCached = Bool //inst取指是否需要经过cache
  val dataCached = Bool //data访存是否需要经过cache

  //TLB指令相关的内容，包括tlbp, tlbr, tlbwi, tlbwr
  /** 不是Index寄存器，而是Index寄存器（或者Random寄存器）中的值 */
  val index = if (useTLB) UInt(TLBConfig.tlbIndexWidth bits) else null
  val wdata = if (useTLB) new TLBEntry(useMask) else null //需要把要写入的TLB内容提前填充成一个Entry项
  val write = if (useTLB) Bool else null //是否把wdata写入TLB中
  val rdata = if (useTLB) new TLBEntry(useMask) else null

  //针对tlbp的
  val probeVPN2 = if (useTLB) Bits(TLBConfig.vpn2Width bits) else null
  val probeASID = if (useTLB) Bits(TLBConfig.asidWidth bits) else null //TODO: 这里的asid和上面的asid应该一直是一样的？
  val probeIndex = if (useTLB) Bits(32 bits) else null //这个值可以直接写回Index CP0寄存器

  override def asMaster(): Unit = {
    if (useTLB) {
      in(instRes, dataRes, rdata, probeIndex, instCached, dataCached)
      out(asid, instVaddr, dataVaddr, index, wdata, write, probeVPN2, probeASID)
    } else {
      in(instRes, dataRes, instCached, dataCached)
      out(asid, instVaddr, dataVaddr)
    }
  }
}

/**
 * useTLB: 是否使用TLB进行地址转换，useMask：是否在TLB中使用mask（是否实现了PageMask）
 * 开启TLB后支持useg, kseg0, kseg1, kseg3
 */
class MMU(useTLB: Boolean, useMask: Boolean) extends Component {
  val io = slave(new CPUMMUInterface(useTLB = useTLB, useMask = useMask))
  val instMapped, dataMapped = Bool //是否需要经过TLB
  if (useTLB) {
    val tlb = new TLB(useMask)
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
    io.instCached := !(MMU.isUncachedSection(io.instVaddr) | (instMapped & tlb.io.instRes.cacheAttr === 2))
    instMapped := !MMU.isUnmappedSection(io.instVaddr)
    io.dataCached := !(MMU.isUncachedSection(io.dataVaddr) | (dataMapped & tlb.io.dataRes.cacheAttr === 2))
    dataMapped := !MMU.isUnmappedSection(io.dataVaddr)
    when(!instMapped) {
      io.instRes.miss := False
      io.instRes.paddr := (B(0, 3 bits) ## io.instVaddr(0, 29 bits)).asUInt
    }
    when(!dataMapped) {
      io.dataRes.miss := False
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
    SpinalVerilog(new MMU(true, false))
  }

  def isUncachedSection(vaddr: UInt): Bool = {
    vaddr(29, 3 bits) === U"3'b101"
  }

  def isUnmappedSection(vaddr: UInt): Bool = {
    vaddr(30, 2 bits) === U"2'b10"
  }
}
