package ip

import spinal.core._

/**
 * @param dataWidth  被除数和除数的宽度
 * @param detectZero 是否检测除0
 * @note Vivado的IP会自动把除数和被除数8字节对齐
 *       假设你的输入是33bit，那么输入会到40bit（文档没明确说怎么填充多余的位，估计是高7位填充0）
 *       此时输出的格式是B(0, 7 bits) ## 商(32 bit) ## B(0, 7 bits) ## 余数(32 bit)
 * */
class Divider(dataWidth: Int = 32, detectZero: Boolean = false, name: String = "divider") extends BlackBox {
  setDefinitionName(name)
  val alignedDataWidth = ((dataWidth + 7) / 8) * 8
  val io = new Bundle {
    val aclk = in Bool
    /** 被除数 */
    val dividend = new Bundle {
      val tdata = in UInt (alignedDataWidth bits)
      val tvalid = in Bool
    }
    val divisor = new Bundle {
      val tdata = in UInt (alignedDataWidth bits)
      val tvalid = in Bool
    }
    val dout = new Bundle {
      val tdata = out UInt (2 * alignedDataWidth bits)
      val tvalid = out Bool
      val divideByZero = if (detectZero) out(Bool) else null
    }
  }

  noIoPrefix()
  addPrePopTask { () => {
    for (bt <- io.flatten) {
      val name = bt.getName()
      if (name.startsWith("divi")) bt.setName("s_axis_" + name)
      else if (name.startsWith("dout")) bt.setName("m_axis_" + name)
      if (name.toLowerCase.contains("zero")) bt.setName("m_axis_dout_tuser")
    }
  }
  }
  mapClockDomain(clock = io.aclk)
}

object Divider {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new Component {
      val divider = new Divider(32, true)
      divider.io.dividend.assignDontCare()
      divider.io.divisor.assignDontCare()
    })
  }
}
