package cpu.defs

import spinal.core._
import spinal.lib.bus.amba4.axi._
import Mips32InstImplicits._

object ConstantVal {
  def INST_NOP: Mips32Inst = B"00000000000000000000000000100000"
  def INIT_PC = U"hBFC00000"

  val AXI_BUS_CONFIG = Axi4Config(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 4,
    useRegion = false,
    useQos = false
  )

  /** TLB相关配置 */
  val PABITS = 32 //至少应该大于等于32，表示支持的物理地址宽度
  val TLBEntryNum = 32  //TLB表项的个数, 2的幂
  val USE_TLB = false //是否开启真正的TLB

  /** Cache配置 */
  val IcacheLineSize = 32  //一个cacheLine的大小，单位是bytes, 2的幂
  val IcacheWayNum = 2  //icache中路数
  val IcacheFifoDepth = 8 //TODO ICache的victim cache深度

  val DcacheLineSize = 32  //一个cacheLine的大小，单位是bytes, 2的幂
  val DcacheWayNum = 2  //dcache中路数
  val DcacheFifoDepth = 4 //DCache的FIFO深度

  /** 时钟中断配置 */
  val TimeInterruptEnable = false

  /** 静态预测配置：是否预测跳转 */
  val AlwaysBranch = true
}
