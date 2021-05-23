package cpu.defs

import spinal.core._
import spinal.lib.IMasterSlave

case class SramBusConfig(
  dataWidth: Int,
  addrWidth: Int,
  selWidth: Int
)

case class SramBus(config: SramBusConfig) extends Bundle with IMasterSlave{
  val req = Bool
  val wr = Bool
  val size = Bits(config.selWidth bits)
  val addr = UInt(config.addrWidth bits)
  val wdata = Bits(config.dataWidth bits)
  val addrOk = Bool
  val dataOk = Bool
  val rdata = Bits(config.dataWidth bits)

  override def asMaster(): Unit = {
    out(req, wr, wdata, size, addr)
    in(rdata, addrOk, dataOk)
  }
}
