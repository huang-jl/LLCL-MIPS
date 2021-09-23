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
case class MMUTranslationRes() extends Bundle {
  val paddr                = UInt(32 bits)
  val cached               = Bool
}

class CPUMMUInterface extends Bundle with IMasterSlave {
  val instVaddr = UInt(32 bits) //指令对应虚拟地址
  val dataVaddr = UInt(32 bits) //数据对应虚拟地址
  val instRes   = MMUTranslationRes()
  val dataRes   = MMUTranslationRes()

  override def asMaster(): Unit = {
    in(instRes, dataRes)
    out(instVaddr, dataVaddr)
  }
}

class MMU extends Component {
  val io = slave(new CPUMMUInterface)
  io.instRes.paddr := (B(0, 3 bits) ## io.instVaddr(0, 29 bits)).asUInt
  io.dataRes.paddr := (B(0, 3 bits) ## io.dataVaddr(0, 29 bits)).asUInt
  //不开启TLB的时候除了kseg1全是Cache的
  io.instRes.cached := !Utils.VAddr.isUncachedSection(io.instVaddr)
  io.dataRes.cached := !Utils.VAddr.isUncachedSection(io.dataVaddr)
}

object MMU {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new MMU)
  }
}
