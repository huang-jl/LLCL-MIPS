package ip

import ip.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

class ReadOnlyRamPort(dataWidth: Int, addrWidth: Int) extends Bundle with IMasterSlave {
  val en   = Bool.simPublic()
  val addr = UInt(addrWidth bits).simPublic()
  val dout = Bits(dataWidth bits)

  override def asMaster(): Unit = {
    in(dout)
    out(en, addr)
  }

  /** 仿真行为，参见 [[Pipeline]] */
  def handledBy(delayedPipeline: Pipeline, storage: Array[BigInt]) = {
    val doutPulled = pullFromOutside(dout)
    delayedPipeline
      .whenReset {
        doutPulled #= 0
      }
      .everyTick { schedule =>
        if (en.toBoolean) {
          val addrValue = addr.toInt
          schedule {
            if (doutPulled.toBigInt != storage(addrValue))
              doutPulled #= storage(addrValue)
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

class WriteOnlyRamPort(dataWidth: Int, addrWidth: Int) extends Bundle with IMasterSlave {
  val en   = Bool.simPublic()
  val we   = Bool.simPublic() //write enable
  val addr = UInt(addrWidth bits).simPublic()
  val din  = Bits(dataWidth bits).simPublic()

  override def asMaster(): Unit = {
    out(en, we, addr, din)
  }

  /** 仿真行为，参见 [[Pipeline]] */
  def handledBy(delayedPipeline: Pipeline, storage: Array[BigInt]) = {
    delayedPipeline
      .everyTick { schedule =>
        if (en.toBoolean) {
          val addrValue = addr.toInt
          if (we.toBoolean) {
            val dinValue = din.toBigInt
            schedule {
              val oldValue = storage(addrValue)
              storage(addrValue) = dinValue
            }
          }
        }
      }
  }
}

object WriteOnlyRamPort {
  def apply(dataWidth: Int, addrWidth: Int) = new WriteOnlyRamPort(dataWidth, addrWidth)
}

/** @param dataWidth 数据宽度，单位是bit
  * @param addrWidth ram地址宽度
  * @param writeMode 控制写入成功时 dout 返回值
  */
class RamPort(dataWidth: Int, addrWidth: Int, writeMode: WriteMode.Value)
    extends ReadOnlyRamPort(dataWidth, addrWidth) {
  val we  = Bool.simPublic() //write enable
  val din = Bits(dataWidth bits).simPublic()

  override def asMaster(): Unit = {
    super.asMaster()
    out(we, din)
  }

  /** 仿真行为，参见 [[Pipeline]] */
  override def handledBy(delayedPipeline: Pipeline, storage: Array[BigInt]) = {
    val doutPulled = pullFromOutside(dout)

    delayedPipeline
      .whenReset {
        doutPulled #= 0
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
                  if (doutPulled.toBigInt != dinValue)
                    doutPulled #= dinValue
                case WriteMode.ReadFirst =>
                  if (doutPulled.toBigInt != oldValue)
                    doutPulled #= oldValue
                case WriteMode.NoChange =>
                // Nothing.
              }
            }
          } else {
            schedule {
              if (doutPulled.toBigInt != storage(addrValue))
                doutPulled #= storage(addrValue)
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
  implicit class DelayedPipelineHandlesPort(delayedPipeline: Pipeline) {
    // 对 port 多态
    def handlePort(port: ReadOnlyRamPort, storage: Array[BigInt]): Pipeline =
      port.handledBy(delayedPipeline, storage)
  }
}
