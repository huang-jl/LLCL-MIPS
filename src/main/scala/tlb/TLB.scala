package tlb

import spinal.core._
import spinal.lib.OHToUInt

import cpu.defs.ConstantVal

//R1仅支持4KiB的页

object TLBConfig {
  // 常量
  val maskWidth = 16
  val vpn2Width = 19
  val asidWidth = 8
  val pfnWidth = ConstantVal.PABITS - 12
  val cacheAttrWidth = 3
  val tlbIndexWidth = log2Up(ConstantVal.TLBEntryNum)
}

/** TLB翻译的结果 */
class TLBTranslationRes extends Bundle {
  /** 翻译得到的物理地址 */
  val paddr = UInt(32 bits)
  /** TLB Entry中对应的Cacheability and Coherency Attribute */
  val cacheAttr = Bits(TLBConfig.cacheAttrWidth bits)
  /** 是否查到TLB，dirty位是否是1，valid位是否是1 */
  val miss, dirty, valid = Bool
  /** 匹配到的TLB表项编号 */
  val index = UInt(TLBConfig.tlbIndexWidth bits)

  //根据每一个表项hit的情况，来对TLBTranslationRes进行赋值
  def assignFromHit(hit: Bits, evenOddBit: Bool, entry: Vec[TLBEntry]): Unit = {
    assert(entry.length == ConstantVal.TLBEntryNum)
    assert(hit.getBitsWidth == ConstantVal.TLBEntryNum)
    this.miss := hit.orR
    this.index := OHToUInt(hit)
    when(evenOddBit) {
      //evenOddBit为1，选择pfn1
      this.paddr(12, 20 bits) := entry(this.index).pfn1(0, 20 bits).asUInt
      this.dirty := entry(this.index).D1
      this.valid := entry(this.index).V1
      this.cacheAttr := entry(this.index).C1
    }.otherwise {
      //evenOddBit为0，选择pfn0
      this.paddr(12, 20 bits) := entry(this.index).pfn0(0, 20 bits).asUInt
      this.dirty := entry(this.index).D0
      this.valid := entry(this.index).V0
      this.cacheAttr := entry(this.index).C0
    }
  }
}

class TLBEntry(useMask: Boolean = true, init: Boolean = false) extends Bundle {
  val mask = if (useMask) {
    if (!init) Bits(TLBConfig.maskWidth bits) else B(0, TLBConfig.maskWidth bits)
  } else null
  val vpn2 = if (!init) Bits(TLBConfig.vpn2Width bits) else B(0, TLBConfig.vpn2Width bits)
  val asid = if (!init) Bits(TLBConfig.asidWidth bits) else B(0, TLBConfig.asidWidth bits)
  val pfn0, pfn1 = if (!init) Bits(TLBConfig.pfnWidth bits) else B(0, TLBConfig.pfnWidth bits)
  val C0, C1 = if (!init) Bits(TLBConfig.cacheAttrWidth bits) else B(0, TLBConfig.cacheAttrWidth bits)
  val V0, V1, D0, D1, G = if (!init) Bool else False
}

/**
 * @note 由于是全连接的TLB，因此不好采用XPM_Memory，因为要同时读出来所有的TLB Entry并比较
 * */
class TLB(useMask: Boolean) extends Component {
  val io = new Bundle {
    val asid = in Bits (TLBConfig.asidWidth bits) //当前EntryHi对应的ASID
    val instVaddr = in UInt (32 bits) //指令对应虚拟地址
    val dataVaddr = in UInt (32 bits) //数据对应虚拟地址
    val instRes = out(new TLBTranslationRes)
    val dataRes = out(new TLBTranslationRes)

    //TLB指令相关的内容，包括tlbp, tlbr, tlbwi, tlbwr
    val index = in UInt (TLBConfig.tlbIndexWidth bits)
    val wdata = in(new TLBEntry(useMask)) //需要把要写入的TLB内容提前填充成一个Entry项
    val write = in Bool //是否把wdata写入TLB中
    val rdata = out(new TLBEntry(useMask))

    //针对tlbp的
    val probeVPN2 = in Bits (TLBConfig.vpn2Width bits)
    val probeASID = in Bits (TLBConfig.asidWidth bits) //TODO: 这里的asid和上面的asid应该一直是一样的？
    val probeIndex = out Bits (32 bits) //这个值可以直接写回Index CP0寄存器
  }

  val addr = new Area {
    val instVPN2 = io.instVaddr(13, TLBConfig.vpn2Width bits).asBits //31 downto 13
    val dataVPN2 = io.dataVaddr(13, TLBConfig.vpn2Width bits).asBits
    val instEvenOddBit = io.instVaddr(12)
    val dataEvenOddBit = io.dataVaddr(12)
    val instOffset = io.instVaddr(0, 12 bits)
    val dataOffset = io.dataVaddr(0, 12 bits)
  }

  val entry = Vec(Reg(new TLBEntry((useMask))) init (new TLBEntry(useMask, true)), ConstantVal.TLBEntryNum)
  //  val entryRam = Mem(new TLBEntry(useMask), ConstantVal.TLBEntryNum)
  //  val entry = Vec(new TLBEntry(useMask), ConstantVal.TLBEntryNum)
  //  if (ConstantVal.SIM) {
  //    entryRam.init(for (_ <- 0 until ConstantVal.TLBEntryNum) yield new TLBEntry(ConstantVal.USE_MASK, true))
  //  }
  //读端口
  //  for (i <- 0 until ConstantVal.TLBEntryNum) entry(i) := entryRam.readAsync(U(i, TLBConfig.tlbIndexWidth bits))
  //  io.rdata := entryRam.readAsync(io.index)
  io.rdata := entry(io.index)
  //写端口
  val writeEntry = new TLBEntry(useMask)
  //  entryRam.write(io.index, writeEntry, io.write)
  when(io.write) {
    entry(io.index) := writeEntry
  }

  val hit = new Area {
    val instHit, dataHit, probeHit = Bits(ConstantVal.TLBEntryNum bits)
    for (i <- 0 until ConstantVal.TLBEntryNum) {
      instHit(i) := TLB.VPN2Match(entry(i), addr.instVPN2, useMask) & (entry(i).asid === io.asid | entry(i).G)
      dataHit(i) := TLB.VPN2Match(entry(i), addr.dataVPN2, useMask) & (entry(i).asid === io.asid | entry(i).G)
      probeHit(i) := TLB.VPN2Match(entry(i), io.probeVPN2, useMask) & (entry(i).asid === io.probeASID | entry(i).G)
    }
  }

  val default_ = new Area {
    io.instRes.paddr(0, 12 bits) := addr.instOffset
    io.dataRes.paddr(0, 12 bits) := addr.dataOffset

    writeEntry := io.wdata
    io.probeIndex := 0
  }
  //地址翻译的逻辑
  io.instRes.assignFromHit(hit = hit.instHit, evenOddBit = addr.instEvenOddBit, entry = entry)
  io.dataRes.assignFromHit(hit = hit.dataHit, evenOddBit = addr.dataEvenOddBit, entry = entry)
  //写入TLB内容的逻辑
  if (useMask) {
    writeEntry.allowOverride
    writeEntry.vpn2 := TLB.getMaskedAddr(addr = io.wdata.vpn2, io.wdata.mask)
    writeEntry.pfn0 := TLB.getMaskedAddr(addr = io.wdata.pfn0, io.wdata.mask)
    writeEntry.pfn1 := TLB.getMaskedAddr(addr = io.wdata.pfn1, io.wdata.mask)
  }
  io.probeIndex(0, TLBConfig.tlbIndexWidth bits) := OHToUInt(hit.probeHit).asBits
  //probeIndex(31)为0表示命中TLB，为1表示未命中TLB
  io.probeIndex(31) := !hit.probeHit.orR
}


object TLB {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new TLB(false))
  }

  /** 检测TLB entry的vpn2是否匹配vpn2 */
  def VPN2Match(entry: TLBEntry, vpn2: Bits, useMask: Boolean): Bool = {
    if (useMask) {
      getMaskedAddr(entry.vpn2, entry.mask) === getMaskedAddr(vpn2, entry.mask)
    } else {
      entry.vpn2 === vpn2
    }
  }

  def getMaskedAddr(addr: Bits, mask: Bits): Bits = {
    assert(mask.getBitsWidth == TLBConfig.maskWidth)
    addr & ~mask.resize(addr.getBitsWidth)
  }
}
