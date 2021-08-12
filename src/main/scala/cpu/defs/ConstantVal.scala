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
  val Multiply_Latency = 2
  require(Multiply_Latency > 1)

  /** TLB相关配置 */
  val PABITS = 32 //至少应该大于等于32，表示支持的物理地址宽度
  val TLBEntryNum = 32  //TLB表项的个数, 2的幂

  /** Cache配置 */
  val IcacheLineSize = 32  //一个cacheLine的大小，单位是bytes, 2的幂
  val IcacheWayNum = 2  //icache中路数
  val IcacheSetsPerWay = 128  //影响Config1中的IS字段
  val IcacheFifoDepth = 8 //TODO ICache的victim cache深度

  val DcacheLineSize = 32  //一个cacheLine的大小，单位是bytes, 2的幂
  val DcacheWayNum = 2  //dcache中路数
  val DcacheSetsPerWay = 128  //影响Config1中的IS字段
  val DcacheFifoDepth = 2 //DCache的FIFO深度

  /** 时钟中断配置 */
  val TimeInterruptEnable = true

  /** 是否开启全部指令，关闭的时候基本仅覆盖初赛的指令；
   * 不能控制TLB相关的指令和逻辑 */
  val FINAL_MODE = true
  val DISABLE_VIRT_UART = false
}
