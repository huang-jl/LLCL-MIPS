package ip

import cpu.Utils
import ip.sim._
import spinal.core._
import spinal.core.sim._

/** @param dataWidth  被除数和除数的宽度
  * @param detectZero 是否检测除0
  * @note Vivado的IP会自动把除数和被除数8字节对齐
  *       假设你的输入是33bit，那么输入会到40bit（文档没明确说怎么填充多余的位，估计是高7位填充0）
  *       此时输出的格式是B(0, 7 bits) ## 商(32 bit) ## B(0, 7 bits) ## 余数(32 bit)
  */
class DividerIP(dataWidth: Int = 32, detectZero: Boolean = false, name: String = "divider")
    extends SimulatedBlackBox {
  setDefinitionName(name)
  val alignedDataWidth = ((dataWidth + 7) / 8) * 8

  val io = new Bundle {
    val aclk = in Bool

    /** 被除数 */
    val dividend = new Bundle {
      setName("s_axis_dividend")
      val tdata  = in UInt (alignedDataWidth bits)
      val tvalid = in Bool
    }.simPublic()
    val divisor = new Bundle {
      setName("s_axis_divisor")
      val tdata  = in UInt (alignedDataWidth bits)
      val tvalid = in Bool
    }.simPublic()
    val dout = new Bundle {
      setName("m_axis_dout")
      val tdata        = out Bits (2 * alignedDataWidth bits)
      val tvalid       = out Bool
      val tuser        = Utils.instantiateWhen(out(Bool), detectZero)
      def divideByZero = tuser
    }
  }

  noIoPrefix()
  mapClockDomain(clock = io.aclk)

  val quotient  = UInt(alignedDataWidth bits)
  val remainder = UInt(alignedDataWidth bits)
  io.dout.tdata := quotient ## remainder

  override def createSimJob() = {
    val pulled = new Area {
      val valid        = pullFromOutside(io.dout.tvalid)
      val quotient     = pullFromOutside(DividerIP.this.quotient)
      val remainder    = pullFromOutside(DividerIP.this.remainder)
      val divideByZero = if (detectZero) pullFromOutside(io.dout.tuser) else null
    }

    Pipeline(20)
      .whenIdle {
        pulled.valid #= false
        pulled.quotient.randomize()
        pulled.remainder.randomize()
        if (detectZero) {
          pulled.divideByZero.randomize()
        }
      }
      .everyTick { schedule =>
        if (io.dividend.tvalid.toBoolean && io.divisor.tvalid.toBoolean) {
          val dividend = io.dividend.tdata.toBigInt
          val divisor  = io.divisor.tdata.toBigInt

          schedule {
            pulled.valid #= true
            val divideByZero = divisor == 0
            if (!divideByZero) {
              pulled.quotient #= dividend / divisor
              pulled.remainder #= dividend % divisor
            } else {
              pulled.quotient.randomize()
              pulled.remainder.randomize()
            }
            if (detectZero) {
              pulled.divideByZero #= divideByZero
            }
          }
        }
      }
      .toJob
  }
}

case class DividerJob(
    dividend: BigInt,
    divisor: BigInt
)

object Divider {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new Component {
      val divider = new DividerIP(32, true)
      divider.io.dividend.assignDontCare()
      divider.io.divisor.assignDontCare()
    })
  }
}
