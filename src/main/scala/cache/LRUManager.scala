package cache

import spinal.core._
import spinal.lib.{OHToUInt, Reverse}

object LRUManegr {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new LRUManegr(8))
  }
}

//记录LRU需要的数据，理论上cache采用的是伪LRU
//0表示上一半最近被使用
//1表示下一半最近被使用
class LRUManegr(wayNum: Int) extends Component {
  val io = new Bundle {
    val use = in Bits (wayNum bits) //对应cache中的hitPerWays
    val update = in Bool() //是否更新LRU的记录
    val next = out(UInt(log2Up(wayNum) bits)) //应该替换的路的编号
  }
  val status = Array.tabulate(log2Up(wayNum))(i => {
    Reg(Bits((1 << i) bits)) init (0)
  })
  val useIndex = Reverse(OHToUInt(io.use))


  when(io.update) {
    for (i <- 0 until log2Up(wayNum)) {
      val statusIndex: UInt = if (i == 0) U(0) else useIndex(i - 1 downto 0)
      status(i)(statusIndex) := useIndex(i)
    }
  }

  for (i <- 0 until log2Up(wayNum)) {
//    io.next(log2Up(wayNum) - i - 1) \= ~status(i)(0)
    //search for the unused ways
  }
}
