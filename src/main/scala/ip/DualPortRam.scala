package ip

import ip.RamPortDelayedPipelineImplicits._
import ip.sim._
import cpu.Utils
import spinal.core._
import spinal.lib._

/** @param dataWidth 数据宽度，单位是bit
  * @param size      ram总的大小，单位是bit
  * @param latency   读端口的延迟
  */
case class BRamIPConfig(
    dataWidth: Int = 32,
    size: Int = 4 * 1024 * 8,
    latency: Int = 1,
    writeModeA: WriteMode.Value = WriteMode.NoChange,
    writeModeB: WriteMode.Value = WriteMode.NoChange
) { def depth = size / dataWidth }

/** @note A端口主要写，B端口主要读，内置从A向B的前传
  * @note A的读端口是默认的行为
  * @note 使用Bram默认其读端口延迟为1
  *  @note 目前仅针对cache使用
  */
class DualPortBram(config: BRamIPConfig) extends Component {
  val io = new Bundle {
    val clk = in Bool
    val rst = in Bool
    val portA = slave(RamPort(config.dataWidth, log2Up(config.depth)))
    val portB = slave(RamPort(config.dataWidth, log2Up(config.depth)))
  }
  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
  noIoPrefix()
  addPrePopTask { () => {
    for (bt <- io.flatten) {
      val name = bt.getName()
      if (name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
      if (name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
    }
  }
  }
  addRTLPath("./rtl/dual_port_ram.v")
}

/** @param dataWidth 数据宽度，单位是bit
  * @param size      ram总的大小，单位是bit
  */
case class LutRamIPConfig(
    dataWidth: Int = 32,
    size: Int = 4 * 1024 * 8
) {
  def depth = size / dataWidth
}

/** @note A端口主要写，B端口主要读，已经内置跨端口写优先
  * @note 使用LRURam默认其读延迟为0
  * @note A的读端口是默认行为
  *  @note 目前仅针对cache使用
  */
class DualPortLutram(config: LutRamIPConfig) extends Component {
  val io = new Bundle {
    val clk = in Bool
    val rst = in Bool
    // TODO: Is it no change?
    val portA = slave(RamPort(dataWidth, log2Up(size / dataWidth)))
    val portB = slave(RamPort(dataWidth, log2Up(size / dataWidth), readOnly = true))
  }

  val param = new xpm_memory_dpdistram_generic
  param.WRITE_DATA_WIDTH_A = config.dataWidth
  param.READ_DATA_WIDTH_A = config.dataWidth
  param.READ_DATA_WIDTH_B = config.dataWidth
  param.BYTE_WRITE_WIDTH_A = config.dataWidth
  param.ADDR_WIDTH_A = log2Up(config.depth)
  param.ADDR_WIDTH_B = log2Up(config.depth)
  param.READ_LATENCY_A = 0
  param.READ_LATENCY_B = 0
  param.MEMORY_SIZE = config.size

  val mem = new xpm_memory_dpdistram(param)
  mem.io.ena := io.portA.en
  mem.io.dina := io.portA.din
  mem.io.addra := io.portA.addr
  mem.io.wea := io.portA.we.asBits
  io.portA.dout := mem.io.douta

  mem.io.enb := True
  mem.io.addrb := io.portB.addr

//  val prevAddrb = RegNext(mem.io.addrb) init 0
//  mem.io.addrb := io.portB.en ? io.portB.addr | prevAddrb

  when(io.portA.we & io.portA.addr === io.portB.addr) {
    // 内置跨端口的写优先
    io.portB.dout := io.portA.din
  }.otherwise {
    io.portB.dout := mem.io.doutb
  }
}

/** @note A端口只写，B端口只读，已经内置前传
  * @note 使用SDP_RAM默认其读延迟为1
  *  @note 目前仅针对cache使用
  */
class SimpleDualPortBram(config: BRamIPConfig) extends Component {
  val io = new Bundle {
    val portA = slave(RamPort(config.dataWidth, log2Up(config.depth), writeOnly = true))
    val portB = slave(RamPort(config.dataWidth, log2Up(config.depth), readOnly = true))
  }

  val param = new xpm_memory_sdpram_generic
  param.WRITE_DATA_WIDTH_A = config.dataWidth
  param.READ_DATA_WIDTH_B = config.dataWidth
  param.BYTE_WRITE_WIDTH_A = config.dataWidth
  param.ADDR_WIDTH_A = log2Up(config.depth)
  param.ADDR_WIDTH_B = log2Up(config.depth)
  param.READ_LATENCY_B = 1
  param.MEMORY_SIZE = config.size
  param.MEMORY_PRIMITIVE = "block"

  val mem = new xpm_memory_sdpram(param)
  // A
  mem.io.ena := io.portA.en
  mem.io.dina := io.portA.din
  mem.io.addra := io.portA.addr
  mem.io.wea := io.portA.we.asBits
  // B
  mem.io.enb := True

  // Cache第一阶段会使用portB读；第二阶段会使用portA写
  // 下面是进行前传需要的寄存器
  val prevWriteA = RegNext(io.portA.we) init False
  val prevAddrA  = RegNext(io.portA.addr) init 0
  val prevWDataA = RegNext(io.portA.din) init 0
  // 当前周期读出的dout对应的地址
  val currAddrB = RegNext(mem.io.addrb) init 0

  mem.io.addrb := io.portB.en ? io.portB.addr | currAddrB

  when(prevWriteA & prevAddrA === currAddrB) {
    // 需要进行前传
    io.portB.dout := prevWDataA
  }.otherwise {
    io.portB.dout := mem.io.doutb
  }
}

///** @param dataWidth 数据宽度，单位是bit
// * @param size      ram总的大小，单位是bit
// * @param latency   读端口的延迟
// */
//case class BRamIPConfig(
//                         dataWidth: Int = 32,
//                         size: Int = 4 * 1024 * 8,
//                         latency: Int = 1,
//                         writeModeA: WriteMode.Value = WriteMode.NoChange,
//                         writeModeB: WriteMode.Value = WriteMode.NoChange
//                       ) { def depth = size / dataWidth }
//
//class DualPortBRam(config: BRamIPConfig) extends SimulatedBlackBox {
//  setDefinitionName("dual_port_bram")
//  val generic = new Generic {
//    val DATA_WIDTH       = config.dataWidth
//    val DEPTH            = config.depth
//    val MEMORY_PRIMITIVE = "block"
//    val LATENCY          = config.latency
//    val WRITE_MODE_A     = config.writeModeA.toString
//    val WRITE_MODE_B     = config.writeModeB.toString
//  }
//
//  val io = new Bundle {
//    val clk   = in Bool
//    val rst   = in Bool
//    val portA = slave(RamPort(config.dataWidth, log2Up(config.depth), config.writeModeA))
//    val portB = slave(RamPort(config.dataWidth, log2Up(config.depth), config.writeModeB))
//  }
//
//  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
//  noIoPrefix()
//  addPrePopTask { () =>
//  {
//    for (bt <- io.flatten) {
//      val name = bt.getName()
//      if (name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
//      if (name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
//    }
//  }
//  }
//
//  addRTLPath("./rtl/dual_port_ram.v")
//
//  override def createSimJob() = {
//    val storage = new Array[BigInt](config.size / config.dataWidth)
//
//    Pipeline(config.latency)
//      .whenReset {
//        for (i <- storage.indices) {
//          storage(i) = 0
//        }
//      }
//      .handlePort(io.portA, storage)
//      .handlePort(io.portB, storage)
//      .toJob
//  }
//}
//
///** @param dataWidth 数据宽度，单位是bit
// * @param size      ram总的大小，单位是bit
// * @param latency   读端口的延迟
// */
//class DualPortLutRam(dataWidth: Int = 32, size: Int = 4 * 1024 * 8, latency: Int = 1)
//  extends SimulatedBlackBox {
//  setDefinitionName("dual_port_lutram")
//  val generic = new Generic {
//    val DATA_WIDTH = dataWidth
//    val DEPTH      = size / dataWidth
//    val LATENCY    = latency
//  }
//
//  val io = new Bundle {
//    val clk = in Bool
//    val rst = in Bool
//    // TODO: Is it no change?
//    val portA = slave(RamPort(dataWidth, log2Up(size / dataWidth), WriteMode.NoChange))
//    val portB = slave(ReadOnlyRamPort(dataWidth, log2Up(size / dataWidth)))
//  }
//
//  mapClockDomain(clock = io.clk, reset = io.rst, resetActiveLevel = HIGH)
//  noIoPrefix()
//  addPrePopTask { () =>
//  {
//    for (bt <- io.flatten) {
//      val name = bt.getName()
//      if (name.startsWith("portA")) bt.setName(name.substring(name.indexOf('_') + 1) + 'a')
//      if (name.startsWith("portB")) bt.setName(name.substring(name.indexOf('_') + 1) + 'b')
//    }
//  }
//  }
//
//  addRTLPath("./rtl/dual_port_ram.v")
//
//  override def createSimJob() = {
//    val storage = new Array[BigInt](size / dataWidth)
//
//    Pipeline(latency)
//      .whenReset {
//        for (i <- storage.indices) {
//          storage(i) = 0
//        }
//      }
//      .handlePort(io.portA, storage)
//      .handlePort(io.portB, storage)
//      .toJob
//  }
//}
//
