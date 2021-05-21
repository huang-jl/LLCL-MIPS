package cache

import spinal.core._
import spinal.lib.{OHToUInt, Reverse}

object LRUManegr {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new LRUManegr(8))
  }
}

//记录LRU需要的数据，cache采用的是Tree-based pseudo-LRU algorithm
//0表示上一半最近被使用
//1表示下一半最近被使用
class LRUManegr(wayNum: Int) extends Component {
  val io = new Bundle {
    val access = in Bits (wayNum bits) //对应cache中的hitPerWays
    val update = in Bool() //是否更新LRU的记录
    val next = out(UInt(log2Up(wayNum) bits)) //应该替换的路的编号
  }
  val status = Array.tabulate(log2Up(wayNum))(i => {
    Reg(Bits((1 << i) bits)) init (0)
  })
  val accessIndex = OHToUInt(io.access) //把one-hot的access转为index
  val used = Bits(wayNum bits) //是否最近被使用，取反后应该是one-hot形式，例如1110表示第0way最近没有被使用


  when(io.update) {
    //当要更新的时候，根据accessIndex依次找
    //例如8-way，当accessIndex为101的时候
    //status(0)(0) = 101的最高位1
    //status(1)(101的最高位1) = 101的次高位0
    //status(2)(101的最高两位10，即2) = 101的最低位1
    for (i <- 0 until log2Up(wayNum)) {
      val statusIndex: UInt = if (i == 0) U(0) else accessIndex(log2Up(wayNum) - i, i bits)
      status(i)(statusIndex) := accessIndex(log2Up(wayNum) - 1 - i)
    }
  }

  //根据当前的树状status查看使用情况
  //对于第i路而言，检查
  // status(0)(0) === i最高位
  // status(1)(i最高位) === i次高位
  // status(2)(i最高两位构成的index) === i次次高位
  // ...
  //只要有一个为1，就表明最近使用过
  for (i <- 0 until wayNum) {
    val temp = Bits(log2Up(wayNum) bits)
    for (j <- 0 until log2Up(wayNum)) {
      temp(j) := (status(j)(i >> (log2Up(wayNum) - j)) === B(i, log2Up(wayNum) bits)(log2Up(wayNum) - 1 - j))
    }
    used(i) := temp.orR
  }
  io.next := OHToUInt(~used)
}
