package cpu

import spinal.core._
import spinal.lib.{master, slave}
import spinal.lib.bus.amba4.axi.Axi4

import cache.{CPUICacheInterface, ICache, CacheRamConfig}
import defs.ConstantVal
import lib.Optional

//暂时把ICache包了一层
//class ICU(config: CacheRamConfig) extends Component {
//  val io = new Bundle {
//    val ibus = slave(new CPUICacheInterface)
//    val axi = master(new Axi4(ConstantVal.AXI_BUS_CONFIG))
//    val exception = out(Optional(EXCEPTION()))
//  }
//
//  val icache = new ICache(config)
//
//  val addrValid = Bool
//  addrValid := io.ibus.addr(0, 2 bits).asBits === B"2'b00"
//  io.exception := None
//  when(!addrValid)(io.exception := EXCEPTION.AdEL)
//
//  io.ibus >> icache.io.cpu
//  io.axi <> icache.io.axi
//}
//
//object ICU {
//  def main(args: Array[String]): Unit = {
//    SpinalVerilog(new ICU(CacheRamConfig(sim = true)))
//  }
//}
