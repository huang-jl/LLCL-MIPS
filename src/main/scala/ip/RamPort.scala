package ip

import spinal.core._
import spinal.core.sim._
import spinal.lib._

class ReadOnlyRamPort(dataWidth: Int, addrWidth: Int) extends Bundle with IMasterSlave {
  val en   = Bool
  val addr = UInt(addrWidth bits)
  val dout = out Bits (dataWidth bits)

  override def asMaster(): Unit = {
    in(dout)
    out(en, addr)
  }

  /** 仿真行为，参见 [[simUtils.DelayedPipeline]] */
  def handledBy(delayedPipeline: simUtils.DelayedPipeline, storage: Array[BigInt]) = {
    delayedPipeline
      .whenReset {
        dout #= 0
      }
      .everyTick { schedule =>
        if (en.toBoolean) {
          val addrValue = addr.toInt
          schedule {
            dout #= storage(addrValue)
          }
        }
      }
  }
}

object ReadOnlyRamPort {
  def apply(dataWidth: Int, addrWidth: Int) = new ReadOnlyRamPort(dataWidth, addrWidth)
}

object WriteMode extends Enumeration {

  /** 先写再读，读出写的值 */
  val WriteFirst = Value("write_first")

  /** 先读再写，读出写之前的值 */
  val ReadFirst = Value("read_first")

  /** 读出值不改变，保持之前的结果 */
  val NoChange = Value("no_change")
}

/** @param dataWidth 数据宽度，单位是bit
  * @param addrWidth ram地址宽度
  * @param writeMode 控制写入成功时 dout 返回值
  */
class RamPort(dataWidth: Int, addrWidth: Int, writeMode: WriteMode.Value)
    extends ReadOnlyRamPort(dataWidth, addrWidth) {
  val we  = Bool //write enable
  val din = in Bits (dataWidth bits)

  override def asMaster(): Unit = {
    super.asMaster()
    out(we, din)
  }

  /** 仿真行为，参见 [[simUtils.DelayedPipeline]] */
  override def handledBy(delayedPipeline: simUtils.DelayedPipeline, storage: Array[BigInt]) = {
    delayedPipeline
      .whenReset {
        dout #= 0
      }
      .everyTick { schedule =>
        if (en.toBoolean) {
          val addrValue = addr.toInt
          if (we.toBoolean) {
            val dinValue = din.toBigInt
            schedule {
              val oldValue = storage(addrValue)
              storage(addrValue) = dinValue

              writeMode match {
                case WriteMode.WriteFirst =>
                  dout #= dinValue
                case WriteMode.ReadFirst =>
                  dout #= oldValue
                case WriteMode.NoChange =>
                // Nothing.
              }
            }
          } else {
            schedule {
              dout #= storage(addrValue)
            }
          }
        }
      }
  }
}

object RamPort {
  def apply(dataWidth: Int, addrWidth: Int, writeMode: WriteMode.Value) =
    new RamPort(dataWidth, addrWidth, writeMode)
}

object RamPortDelayedPipelineImplicits {
  implicit class DelayedPipelineHandlesPort(delayedPipeline: simUtils.DelayedPipeline) {
    // 对 port 多态
    def handlePort(port: ReadOnlyRamPort, storage: Array[BigInt]): simUtils.DelayedPipeline =
      port.handledBy(delayedPipeline, storage)
  }
}
