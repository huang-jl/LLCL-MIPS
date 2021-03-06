package tlb

import spinal.core._
import spinal.lib.{MuxOH, OHToUInt}
import cpu.defs.ConstantVal
import scala.language.postfixOps

//R1仅支持4KiB的页

object TLBConfig {
  // 常量
  val maskWidth      = 16
  val vpn2Width      = 19
  val asidWidth      = 8
  val pfnWidth       = ConstantVal.PABITS - 12
  val cacheAttrWidth = 3
  val tlbIndexWidth  = log2Up(ConstantVal.TLBEntryNum)
}

/** TLB翻译的结果 */
class TLBTranslationRes extends Bundle {

  /** 翻译得到的物理地址 */
  val paddr = UInt(32 bits)

  /** TLB Entry中对应的Cacheability and Coherency Attribute */
  val cacheAttr = Bits(TLBConfig.cacheAttrWidth bits)

  val miss, dirty, invalid = Bool

  //根据每一个表项hit的情况，来对TLBTranslationRes进行赋值
  def assignSomeFromEntry(evenOddBit: Bool, hitEntry: TLBEntry): Unit = {
    when(evenOddBit) {
      //evenOddBit为1，选择pfn1
      this.paddr(12, 20 bits) := hitEntry.pfn1(0, 20 bits).asUInt
      this.dirty := hitEntry.D1
      this.invalid := !hitEntry.V1
      this.cacheAttr := hitEntry.C1
    }.otherwise {
      //evenOddBit为0，选择pfn0
      this.paddr(12, 20 bits) := hitEntry.pfn0(0, 20 bits).asUInt
      this.dirty := hitEntry.D0
      this.invalid := !hitEntry.V0
      this.cacheAttr := hitEntry.C0
    }
  }

  def setDefault():Unit = {
    paddr(12, 20 bits) := 0
    cacheAttr := 0
    miss := True
    dirty := True
    invalid := True
  }
}

class TLBEntry extends Bundle {
  val mask              = Bits(TLBConfig.maskWidth bits)
  val vpn2              = Bits(TLBConfig.vpn2Width bits)
  val asid              = Bits(TLBConfig.asidWidth bits)
  val pfn0, pfn1        = Bits(TLBConfig.pfnWidth bits)
  val C0, C1            = Bits(TLBConfig.cacheAttrWidth bits)
  val V0, V1, D0, D1, G = Bool
}

/** @note 由于是全连接的TLB，直接使用寄存器存储，因为要同时读出来所有的TLB Entry并比较
  *       此外该TLB的查询是双端口，支持inst和data同时查询
  */
class TLB extends Component {
  val io = new Bundle {
    val asid      = in Bits (TLBConfig.asidWidth bits) //当前EntryHi对应的ASID
    val instVaddr = in UInt (32 bits)                  //指令对应虚拟地址
    val dataVaddr = in UInt (32 bits)                  //数据对应虚拟地址
    val instRes   = out(new TLBTranslationRes)
    val dataRes   = out(new TLBTranslationRes)

    //TLB指令相关的内容，包括tlbp, tlbr, tlbwi, tlbwr
    val index = in UInt (TLBConfig.tlbIndexWidth bits) //可能是Index，也可能是Random
    val wdata = in(new TLBEntry)                       //需要把要写入的TLB内容提前填充成一个Entry项
    val write = in Bool ()                             //是否把wdata写入TLB中
    val rdata = out(new TLBEntry)

    //针对tlbp的
    val probeVPN2  = in Bits (TLBConfig.vpn2Width bits)
    val probeASID  = in Bits (TLBConfig.asidWidth bits) //TODO: 这里的asid和上面的asid应该一直是一样的？
    val probeIndex = out Bits (32 bits)                 //这个值可以直接写回Index CP0寄存器
  }

  val addr = new Area {
    val instVPN2       = io.instVaddr(13, TLBConfig.vpn2Width bits).asBits //31 downto 13
    val dataVPN2       = io.dataVaddr(13, TLBConfig.vpn2Width bits).asBits
    val instEvenOddBit = io.instVaddr(12)
    val dataEvenOddBit = io.dataVaddr(12)
    val instOffset     = io.instVaddr(0, 12 bits)
    val dataOffset     = io.dataVaddr(0, 12 bits)
  }

  val entry = Vec(Reg(new TLBEntry), ConstantVal.TLBEntryNum)
  for (e <- entry) e init (new TLBEntry).getZero
  //读tlb 为tlbr服务
  io.rdata := entry(io.index)
  //写tlb 为tlbw服务
  val writeEntry = new TLBEntry
  when(io.write) {
    entry(io.index) := writeEntry
  }

  val default_ = new Area {
    io.instRes.paddr(0, 12 bits) := addr.instOffset
    io.dataRes.paddr(0, 12 bits) := addr.dataOffset
    io.instRes.setDefault()
    io.dataRes.setDefault()

    writeEntry := io.wdata
    io.probeIndex(31).assignFromBits(B"1'b1")
    io.probeIndex(0, 31 bits) := 0
  }
  for (i <- 0 until ConstantVal.TLBEntryNum) {
    when(TLB.entryMatch(entry(i), io.asid, addr.instVPN2)) {
      io.instRes.assignSomeFromEntry(addr.instEvenOddBit, entry(i))
      io.instRes.miss := False
    }
    when(TLB.entryMatch(entry(i), io.asid, addr.dataVPN2)) {
      io.dataRes.assignSomeFromEntry(addr.dataEvenOddBit, entry(i))
      io.dataRes.miss := False
    }
    when(TLB.entryMatch(entry(i), io.probeASID, io.probeVPN2)) {
      io.probeIndex(0, TLBConfig.tlbIndexWidth bits) := B(i)
      //probeIndex(31)为0表示命中TLB，为1表示未命中TLB
      io.probeIndex(31).assignFromBits(B"1'b0")
    }
  }

  //写入TLB内容的逻辑，会主动作用mask
  writeEntry.allowOverride
  writeEntry.vpn2 := TLB.getMaskedAddr(addr = io.wdata.vpn2, io.wdata.mask)
  writeEntry.pfn0 := TLB.getMaskedAddr(addr = io.wdata.pfn0, io.wdata.mask)
  writeEntry.pfn1 := TLB.getMaskedAddr(addr = io.wdata.pfn1, io.wdata.mask)
}

object TLB {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new TLB)
  }

  /** @param entry TLB中的一项
    * @param asid 当前entryHi中的ASID
    * @param vpn2 要查询虚拟地址的vpn2
    * @note 来判断一个表项是否匹配
    */
  def entryMatch(entry: TLBEntry, asid: Bits, vpn2: Bits): Bool =
    TLB.VPN2Match(entry, vpn2) & (entry.asid === asid | entry.G)

  /** 检测TLB entry的vpn2是否匹配vpn2 */
  def VPN2Match(entry: TLBEntry, vpn2: Bits): Bool =
    getMaskedAddr(entry.vpn2, entry.mask) === getMaskedAddr(vpn2, entry.mask)

  private def getMaskedAddr(addr: Bits, mask: Bits): Bits = {
    assert(mask.getBitsWidth == TLBConfig.maskWidth)
    addr & ~mask.resize(addr.getBitsWidth)
  }
}
