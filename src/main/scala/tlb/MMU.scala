package tlb

import cpu.Utils
import cpu.defs.ConstantVal
import spinal.core._
import spinal.lib._
import scala.language.postfixOps

//TODO: 如果取指不经过cache怎么办
//TODO: CP0的某些寄存器如果是特殊值，可以让kesg0变成Uncached
//TODO: 统一valid为invalid；统一在一个地方进行地址检查？

/** @param useTLB 是否开启TLB转换
  * @note MMU进行地址翻译后得到的结果
  */
class MMUTranslationRes(useTLB: Boolean) extends Bundle {
  val paddr                = UInt(32 bits)
  val miss, dirty, invalid = Bool
  val cached               = Bool
  val mapped               = Bool
  val illegal              = Bool // MMU会检查**虚拟地址**区域是否合法

  /**
   * @note 在关闭Final Mode时使用，这个设置下不会触发TLB的异常
   * */
  def setDefault():Unit = {
    miss := False
    dirty := True
    invalid := False
    illegal := False
    mapped := False
  }

  def tlbRefillException: Bool = mapped & miss
  def tlbInvalidException: Bool = mapped & invalid
  def tlbModException: Bool = mapped & !dirty
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
  val K0       = Utils.instantiateWhen(Bits(3 bits), ConstantVal.FINAL_MODE)
  val ERL      = Utils.instantiateWhen(Bool, ConstantVal.FINAL_MODE)
  val userMode = Utils.instantiateWhen(Bool, ConstantVal.FINAL_MODE)

  override def asMaster(): Unit = {
    in(instRes, dataRes)
    out(asid, instVaddr, dataVaddr)
    if (ConstantVal.FINAL_MODE) {
      in(rdata, probeIndex)
      out(index, wdata, write, probeVPN2, probeASID, K0, ERL, userMode)
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
    io.instRes.cached := !Utils.VAddr.isUncached(
      io.instVaddr,
      tlb.io.instRes.cacheAttr,
      io.K0,
      io.ERL
    )
    io.dataRes.cached := !Utils.VAddr.isUncached(
      io.dataVaddr,
      tlb.io.dataRes.cacheAttr,
      io.K0,
      io.ERL
    )
    io.instRes.mapped := !Utils.VAddr.isUnmapped(io.instVaddr, io.ERL)
    io.dataRes.mapped := !Utils.VAddr.isUnmapped(io.dataVaddr, io.ERL)
    when(Utils.VAddr.isUnmappedSection(io.instVaddr)) {
      io.instRes.paddr := (B(0, 3 bits) ## io.instVaddr(0, 29 bits)).asUInt
    }
    when(Utils.VAddr.isUnmappedSection(io.dataVaddr)) {
      io.dataRes.paddr := (B(0, 3 bits) ## io.dataVaddr(0, 29 bits)).asUInt
    }
    // 在用户态并且访问其他虚拟地址段的时候非法
    io.instRes.illegal := io.userMode & !Utils.VAddr.isUseg(io.instVaddr)
    io.dataRes.illegal := io.userMode & !Utils.VAddr.isUseg(io.dataVaddr)
  } else {
    io.instRes.setDefault()
    io.dataRes.setDefault()
    io.instRes.paddr := (B(0, 3 bits) ## io.instVaddr(0, 29 bits)).asUInt
    io.dataRes.paddr := (B(0, 3 bits) ## io.dataVaddr(0, 29 bits)).asUInt
    //不开启TLB的时候除了kseg1全是Cache的
    io.instRes.cached := !Utils.VAddr.isUncachedSection(io.instVaddr)
    io.dataRes.cached := !Utils.VAddr.isUncachedSection(io.dataVaddr)
  }
}

object MMU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MMU)
  }
}
