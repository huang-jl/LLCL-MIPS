package cache

import spinal.core._
import spinal.lib.{OHToUInt, Reverse}

object LRUManegr {
  def main(args: Array[String]): Unit = {
//    SpinalVerilog(new LRUManegr(8))
    SpinalVerilog(new Component {
      val io = new Bundle {
        val x = in Bool
        val y = out Bits (10 bits)
      }
      val temp = Vec(Reg(Bits(1 bits)) init(0), 10)
      io.y := temp.asBits
    })
  }
}

class PLRU(bitLength: Int) extends Bundle {
  val prev = Vec(Reg(Bool) init(False), bitLength)
  val next = Vec(Bool, bitLength)
}

/** @param wayNum 表示路数，仅支持2 4 8路组
  * @note 记录LRU需要的数据，cache采用的是Tree-based pseudo-LRU algorithm
  * @note 0表示上一半最近被使用
  *       1表示下一半最近被使用
  */
case class LRUCalculator(wayNum: Int) {
  assert(wayNum == 2 || wayNum == 4 || wayNum == 8)

  def statusLength: Int = {
    wayNum match {
      case 2 => 1
      case 4 => 3
      case 8 => 7
    }
  }

  private def way2(status:Bits): UInt = {
    assert(status.getBitsWidth == 1)
    (!status(0)).asUInt
  }

  private def way4(status:Bits): UInt = {
    assert(status.getBitsWidth == 3)
    status(0) ? way2(status(1).asBits).resize(2) |
      U"1'b1" @@ way2(status(2).asBits)
  }

  private def way8(status:Bits): UInt = {
    assert(status.getBitsWidth == 7)
    status(0) ? way4(status(4) ## status(3) ## status(1)).resize(3) |
      U"1'b1" @@ way4(status(6) ## status(5) ## status(2))
  }

  def leastRecentUsedIndex(status:Vec[Bool]): UInt = {
    assert(status.getBitsWidth == statusLength)

    wayNum match {
      case 2 => way2(status.asBits)
      case 4 => way4(status.asBits)
      case 8 => way8(status.asBits)
    }
  }

  private def updateWay2(access: Bits, status:Vec[Bool]): Unit = {
    assert(access.getBitsWidth == 1 && status.getBitsWidth == 1)
    status(0) := access(0)
  }

  private def updateWay4(access: Bits, status: Vec[Bool]): Unit = {
    assert(access.getBitsWidth == 2 && status.getBitsWidth == 3)
    status(0) := access(1)
    when(access(1)) {
      status(2) := access(0)
    }.otherwise {
      status(1) := access(0)
    }
  }

  private def updateWay8(access: Bits, status:Vec[Bool]): Unit = {
    assert(access.getBitsWidth == 3 && status.getBitsWidth == 7)
    status(0) := access(2)
    when(access(2)) {
      status(2) := access(1)
      when(access(1)) {
        status(6) := access(0)
      }.otherwise {
        status(5) := access(0)
      }
    }.otherwise {
      status(1) := access(1)
      when(access(1)) {
        status(4) := access(0)
      }.otherwise {
        status(3) := access(0)
      }
    }
  }

  def updateStatus(access: UInt, status:Vec[Bool]): Unit = {
    assert(status.getBitsWidth == statusLength)
    wayNum match {
      case 2 => updateWay2(access.asBits, status)
      case 4 => updateWay4(access.asBits, status)
      case 8 => updateWay8(access.asBits, status)
    }
  }
}

//记录LRU需要的数据，cache采用的是Tree-based pseudo-LRU algorithm
//0表示上一半最近被使用
//1表示下一半最近被使用
class LRUManegr(wayNum: Int) extends Component {
  val io = new Bundle {
    val access = in Bits (wayNum bits)          //对应cache中的hitPerWays
    val update = in Bool ()                     //是否更新LRU的记录
    val next   = out(UInt(log2Up(wayNum) bits)) //应该替换的路的编号
  }
  val status = Array.tabulate(log2Up(wayNum))(i => {
    Reg(Bits((1 << i) bits)) init (0)
  })
  val accessIndex = OHToUInt(io.access) //把one-hot的access转为index
  val used        = Bits(wayNum bits)   //是否最近被使用，取反后应该是one-hot形式，例如1110表示第0way最近没有被使用

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
      temp(j) := (status(j)(i >> (log2Up(wayNum) - j)) === B(i, log2Up(wayNum) bits)(
        log2Up(wayNum) - 1 - j
      ))
    }
    used(i) := temp.orR
  }
  io.next := OHToUInt(~used)
}
