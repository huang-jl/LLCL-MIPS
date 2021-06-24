package cpu.defs

import spinal.core._
import spinal.lib.bus.amba4.axi._
import Mips32InstImplicits._

object ConstantVal {
  def INST_NOP: Mips32Inst = B"00000000000000000000000000100000"
  def INIT_PC = U"hBFC00000"

  val SRAM_BUS_CONFIG =
    SramBusConfig(dataWidth = 32, addrWidth = 32, selWidth = 2)
  val AXI_BUS_CONFIG = Axi4Config(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 4,
    useRegion = false,
    useQos = false
  )

  val SIM = false  //是否仿真，会生成RAM的初始化文件

  /** TLB相关配置 */
  val PABITS = 32 //至少应该大于等于32，表示支持的物理地址宽度
  val TLBEntryNum = 16  //TLB表项的个数, 2的幂
  val USE_TLB = true //是否开启真正的TLB
  val USE_MASK = false  //是否开启TLB的Mask，和PageMask寄存器

  /** Cache配置 */
  val IcacheLineSize = 32  //bytes, 2的幂
  val IcacheIndexWIdth = 7  //icache中index的宽度，对应2**DcacheIndexWidth个组
  val IcacheWayNum = 2  //icache中路数
  val IcacheFifoDepth = 8 //TODO ICache的victim cache深度

  val DcacheLineSize = 32  //bytes, 2的幂
  val DcacheIndexWIdth = 7  //dcache中index的宽度，对应2**DcacheIndexWidth个组
  val DcacheWayNum = 2  //dcache中路数
  val DcacheFifoDepth = 8 //DCache的FIFO深度
}
