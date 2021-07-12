package cache

import spinal.core._
import spinal.lib.{OHToUInt, Reverse}

object LRUManegr {
  def main(args: Array[String]): Unit = {
//    SpinalVerilog(new LRUManegr(8))
    SpinalVerilog(new Component {
      val io = new Bundle {
        val x = in Bool ()
        val y = out Bits (10 bits)
      }
      val temp = Vec(Reg(Bits(1 bits)) init (0), 10)
      io.y := temp.asBits
    })
  }
}

class PLRU(bitLength: Int) extends Bundle {
  val prev = Vec(Reg(Bool) init (False), bitLength)
  val next = Vec(Bool, bitLength)
}

object LRUCalculator {
  def statusLength(wayNum: Int): Int = {
    wayNum match {
      case 2 => 1
      case 4 => 3
      case 8 => 7
    }
  }

}

/** @param wayNum 表示路数，仅支持2 4 8路组
  * @note 记录LRU需要的数据，cache采用的是Tree-based pseudo-LRU algorithm
  * @note 0表示上一半最近被使用
  *       1表示下一半最近被使用
  */
case class LRUCalculator(wayNum: Int) {
  assert(wayNum == 2 || wayNum == 4 || wayNum == 8)

  private def way2(status: Bits): UInt = {
    assert(status.getBitsWidth == 1)
    (!status(0)).asUInt
  }

  private def way4(status: Bits): UInt = {
    assert(status.getBitsWidth == 3)
    status(0) ? way2(status(1).asBits).resize(2) |
      U"1'b1" @@ way2(status(2).asBits)
  }

  private def way8(status: Bits): UInt = {
    assert(status.getBitsWidth == 7)
    status(0) ? way4(status(3 downto 1)).resize(3) |
      U"1'b1" @@ way4(status(6 downto 4))
  }

  def leastRecentUsedIndex(status: Bits): UInt = {
    assert(status.getBitsWidth == LRUCalculator.statusLength(wayNum))

    wayNum match {
      case 2 => way2(status.asBits)
      case 4 => way4(status.asBits)
      case 8 => way8(status.asBits)
    }
  }

  private def updateWay2(access: Bits, status: Bits): Bits = {
    assert(access.getBitsWidth == 1 && status.getBitsWidth == 1)
//    status(0) := access(0)
    access(0).asBits
  }

  private def updateWay4(access: Bits, status: Bits): Bits = {
    assert(access.getBitsWidth == 2 && status.getBitsWidth == 3)
//    status(0) := access(1)
//    when(access(1)) {
//      status(2) := access(0)
//    }.otherwise {
//      status(1) := access(0)
//    }
    val res = Bits(3 bits)
    when(access(1)) {
      res := access(0) ## status(1) ## access(1)
    }.otherwise {
      res := status(2) ## access(0) ## access(1)
    }
    res
  }

  private def updateWay8(access: Bits, status: Bits): Bits = {
    assert(access.getBitsWidth == 3 && status.getBitsWidth == 7)
    val res = Bits(7 bits)
    when(access(2)) {
      res := updateWay4(access(1 downto 0), status(6 downto 4)) ## status(3 downto 1) ## access(2)
    }.otherwise {
      res := status(6 downto 4) ## updateWay4(access(1 downto 0), status(3 downto 1)) ## access(2)
    }
    res
//    status(0) := access(2)
//    when(access(2)) {
//      status(2) := access(1)
//      when(access(1)) {
//        status(6) := access(0)
//      }.otherwise {
//        status(5) := access(0)
//      }
//    }.otherwise {
//      status(1) := access(1)
//      when(access(1)) {
//        status(4) := access(0)
//      }.otherwise {
//        status(3) := access(0)
//      }
//    }
  }

  def updateStatus(access: UInt, status: Bits): Bits = {
    assert(status.getBitsWidth == LRUCalculator.statusLength(wayNum))
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
/** @wayNum 路数
  * @setSize 有多少项
  * @note 每一项对应一个cache的set，一个端口读，一个端口写，并且内置数据前传
  */
class LRUManegr(wayNum: Int, setSize: Int) extends Component {
  val width: Int = LRUCalculator.statusLength(wayNum)
  val calculator = new LRUCalculator(wayNum)
  val io = new Bundle {
    val read = new Bundle {
      val addr = in UInt (log2Up(setSize) bits)
      val data = out Bits (width bits)
    }
    val write = new Bundle {
      val addr   = in UInt (log2Up(setSize) bits)
      val access = in UInt (log2Up(wayNum) bits) // 最近访问的编号
      val en     = in Bool ()                    //写使能
    }
  }
  val data  = Vec(Reg(Bits(width bits)) init 0, setSize)
  val wdata = calculator.updateStatus(io.write.access, data(io.write.addr))
  when(io.write.en & io.write.addr === io.read.addr) {
    io.read.data := wdata
  }.otherwise {
    io.read.data := data(io.read.addr)
  }
  when(io.write.en) {
    data(io.write.addr) := wdata
  }
}
