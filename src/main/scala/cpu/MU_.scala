//package cpu
//
//import cpu.defs.ConstantVal._
//import spinal.core._
//
//class MU extends Component {
//  // in
//  val offset = in UInt (2 bits)
//  val data_in = in Bits (32 bits)
//  val be = in UInt (2 bits)
//  val ex = in(MU_EX)
//
//  // out
//  val data_out = out Bits (32 bits)
//
//  //
//  val ex_s = ex === MU_EX.s
//  data_out := be.mux(
//    U"00" ->
//      (ex_s ?
//        offset.mux(
//          U"00" -> S(data_in(7 downto 0), 32 bits).asBits,
//          U"01" -> S(data_in(15 downto 8), 32 bits).asBits,
//          U"10" -> S(data_in(23 downto 16), 32 bits).asBits,
//          U"11" -> S(data_in(31 downto 24), 32 bits).asBits) |
//        offset.mux(
//          U"00" -> U(data_in(7 downto 0), 32 bits).asBits,
//          U"01" -> U(data_in(15 downto 8), 32 bits).asBits,
//          U"10" -> U(data_in(23 downto 16), 32 bits).asBits,
//          U"11" -> U(data_in(31 downto 24), 32 bits).asBits)),
//    U"01" ->
//      (ex_s ?
//        (offset(1) ?
//          S(data_in(31 downto 16), 32 bits).asBits |
//          S(data_in(15 downto 0), 32 bits).asBits) |
//        (offset(1) ?
//          U(data_in(31 downto 16), 32 bits).asBits |
//          U(data_in(15 downto 0), 32 bits).asBits)),
//    default -> data_in
//  )
//}
//
//object MU {
//  def main(args: Array[String]): Unit = {
//    SpinalVerilog(new MU)
//  }
//}