package cpu

import spinal.core._
import scala.language.postfixOps

class ICU extends Component {
  val io = new Bundle {
    val offset = in UInt(2 bits)   //地址的末两位
    val addrValid = out Bool()
  }

  io.addrValid := io.offset(0, 2 bits) === U"2'b00"
}
