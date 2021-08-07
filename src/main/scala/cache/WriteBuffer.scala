package cache

import spinal.core._
import spinal.lib._
import scala.language.postfixOps

//depth: Fifo的大小
case class WriteBufferConfig(blockSize: Int, tagWidth: Int, depth: Int = 16) {
  def dataWidth: Int = blockSize * 8 //数据的位宽

  def counterWidth: Int = log2Up(depth) + 1 //log2(depth) + 1

  def memAddrWidth: Int = log2Up(depth)

  def wordIndexWidth: Int = log2Up(blockSize / 4)
}

object WriteBuffer {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new WriteBuffer(WriteBufferConfig(32, 27, 8)))
  }
}

/** @note 第一个周期组合逻辑查询，第二个周期出数据或者给出写入数据
  *       内置前传逻辑
  */
class WriteBufferInterface(config: WriteBufferConfig) extends Bundle with IMasterSlave {
  val query = new Bundle {
    val tag: Bits   = Bits(config.tagWidth bits) //要查询的tag
    val hit: Bool   = Bool
    val rdata: Bits = Bits(32 bits)
  }
  val merge = new Bundle {
    //要查询和写合并的信号
    val write: Bool      = Bool         //是否把queryWData写入对应命中的cache中
    val byteEnable: Bits = Bits(4 bits) //要写入FIFO数据的byteEnable信号，长度为一个cache line的byte数
    val wdata: Word      = Word()       //要写入FIFO对应的数据
    val hit: Bool        = Bool
  }
  val index: UInt = UInt(config.wordIndexWidth bits) //query或者merge对应的字偏移
  //要压入的数据
  val pushTag: Bits = Bits(config.tagWidth bits)
  val pushData      = Block(config.blockSize)
  //要弹出的数据
  val popTag: Bits = Bits(config.tagWidth bits)
  val popData      = Block(config.blockSize)
  val push: Bool   = Bool //是否压入数据
  val pop: Bool    = Bool //是否弹出数据

  val empty: Bool = Bool
  val full: Bool  = Bool

  override def asMaster(): Unit = {
    in(query.rdata, query.hit, popTag, merge.hit, popData, empty, full)
    out(
      query.tag,
      merge.write,
      merge.byteEnable,
      merge.wdata,
      index,
      pushTag,
      pushData,
      push,
      pop
    )
  }
}

//一个先进先出队列，由于是全连接，不适合使用XPM_MEMORY，直接用Reg存
//用来缓存要写入内存的数据
//blockSize: byte，一个cache block 的大小
//tagWidth:表示队列中要存储的tag宽度，应该是cache中tag+index
//
//如果当某个数据进入Fifo后，再次被命中，那么该数据在Fifo中的位置不会改变（但是对应的数据会被加入cache中去）
//
//如果某个数据这个周期被请求pop，那么这个周期的写不会命中，因为下个周期才会被写入，但是数据已经不是Fifo中的有效数据了
class WriteBuffer(config: WriteBufferConfig) extends Component {
  val io = slave(new WriteBufferInterface(config))

  /*
   * Signal and Reg Definition
   */
  val fifo = new Area {
    val tag   = Vec(Reg(Bits(config.tagWidth bits)), config.depth)
    val data  = Vec(Reg(Block(config.blockSize)), config.depth)
    val valid = Vec(Reg(Bool) init False, config.depth)
  }
  val popPtr          = Counter(config.depth)
  val pushPtr         = Counter(config.depth)
  val ptrMatch        = popPtr === pushPtr
  val risingOccupancy = RegInit(False)
  when(io.push =/= io.pop)(risingOccupancy := io.push)
  io.full := risingOccupancy & ptrMatch
  io.empty := !risingOccupancy & ptrMatch
  /*
   * Default Value
   */
  io.popTag := fifo.tag(popPtr.value)
  io.popData := fifo.data(popPtr.value)
  //logic of query read
  io.query.rdata.assignDontCare()
  io.query.hit := False
  io.merge.hit := False

  /*
   * Logic
   */
  val writeData = Word()
  for (i <- 0 until config.depth) {
    //如果当前第i个是head并且正在pop，那么不命中，应该考虑去WB中寻找这个数据
    val hit = fifo.tag(i) === io.query.tag & fifo.valid(i)
    when(hit) {
      io.query.hit := True
      io.query.rdata := fifo.data(i)(io.index)
    }
    when(hit & !(io.pop & !io.empty & popPtr === i)) {
      io.merge.hit := True
    }
    when(hit & !(io.pop & !io.empty & popPtr === i) & io.merge.write) {
      fifo.data(i).banks(io.index) := writeData.asBits
    }
  }
  //logic of push and pop
  when(io.push & !io.full) {
    fifo.tag(pushPtr.value) := io.pushTag
    fifo.data(pushPtr.value).assignFromBits(io.pushData.asBits)
    fifo.valid(pushPtr.value) := True
    pushPtr.increment()
  }
  when(io.pop & !io.empty) {
    fifo.valid(popPtr.value) := False //把对应的数据无效了
    popPtr.increment()
  }

  //logic of write merge
  writeData := io.query.rdata
  for (j <- 0 until 4) {
    when(io.merge.byteEnable(j))(writeData(j) := io.merge.wdata(j))
  }
}

case class Word(wordSize: Int = 4) extends Bundle {
  val data                    = Vec(Bits(8 bits), wordSize)
  def apply(idx: Int): Bits   = data(idx)
  def apply(addr: UInt): Bits = data(addr)
  def :=(that: Bits): Unit    = data.assignFromBits(that)
}
