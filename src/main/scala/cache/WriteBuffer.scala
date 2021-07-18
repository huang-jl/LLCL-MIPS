package cache

import spinal.core._
import spinal.lib._
import scala.language.postfixOps

//depth: Fifo的大小
case class WriteBufferConfig(blockSize: Int, tagWidth: Int, depth: Int = 16) {
  def dataWidth: Int = blockSize * 8 //数据的位宽

  def beWidth: Int = blockSize //byteEnable信号的宽度

  def counterWidth: Int = log2Up(depth) + 1 //log2(depth) + 1

  def memAddrWidth: Int = log2Up(depth)
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
    val tag: Bits     = Bits(config.tagWidth bits)      //要查询的tag
    val hit: Bool     = Bool
    val hitAddr: UInt = UInt(log2Up(config.depth) bits) //命中FIFO的位置
    val rdata: Bits   = Bits(config.dataWidth bits)     //读出的FIFO数据
  }
  val merge = new Bundle {
    //要查询和写合并的信号
    val write: Bool      = Bool                            //是否把queryWData写入对应命中的cache中
    val byteEnable: Bits = Bits(config.beWidth bits)       //要写入FIFO数据的byteEnable信号，长度为一个cache line的byte数
    val wdata: Bits      = Bits(config.dataWidth bits)     //要写入FIFO对应的数据
    val waddr: UInt      = UInt(log2Up(config.depth) bits) //要写入的FIFO位置
  }
  //要压入的数据
  val pushTag: Bits  = Bits(config.tagWidth bits)
  val pushData: Bits = Bits(config.dataWidth bits)
  //要弹出的数据
  val popTag: Bits  = Bits(config.tagWidth bits)
  val popData: Bits = Bits(config.dataWidth bits)
  val push: Bool    = Bool //是否压入数据
  val pop: Bool     = Bool //是否弹出数据

  val empty: Bool = Bool
  val full: Bool  = Bool

  override def asMaster(): Unit = {
    in(query.rdata, query.hit, query.hitAddr, popTag, popData, empty, full)
    out(
      query.tag,
      merge.write,
      merge.byteEnable,
      merge.wdata,
      merge.waddr,
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
    val tag   = Vec(Reg(Bits(config.tagWidth bits)) init (0), config.depth)
    val data  = Vec(Reg(Bits(config.dataWidth bits)) init (0), config.depth)
    val valid = Vec(Reg(Bool) init (False), config.depth)
  }
  val counter = new Area {
    val head = Counter(0 until config.depth)
    val tail = Counter(0 until config.depth)
    val cnt  = Reg(UInt(config.counterWidth bits)) init (0)
  }
//  val queryReadHit = Bits(config.depth bits)
//  val queryWriteHit = Bits(config.depth bits)
  /*
   * Default Value
   */
  io.popTag := fifo.tag(counter.head.value)
  io.popData := fifo.data(counter.head.value)
  //logic of query read
  io.query.rdata := fifo.data(io.query.hitAddr)

  io.empty := counter.cnt === 0
  io.full := counter.cnt === config.depth

  /*
   * Logic
   */
  val hitPerWay = Bits(config.depth bits)
  for (i <- 0 until config.depth) {
//    queryReadHit(i) := (fifo.tag(i) === io.query.tag) & fifo.valid(i)
//    queryWriteHit(i) := (io.pop & counter.head.value === i) ? False | queryReadHit(i)
    //如果当前第i个是head并且正在pop，那么不命中，应该考虑去WB中寻找这个数据
    hitPerWay(i) := (fifo.tag(i) === io.query.tag) & fifo.valid(
      i
    ) & !(io.pop & !io.empty & counter.head.value === i)
  }
  io.query.hit := hitPerWay.orR
  io.query.hitAddr := OHToUInt(hitPerWay)
  //logic of push and pop
  when(io.push & !io.full) {
    fifo.tag(counter.tail.value) := io.pushTag
    fifo.data(counter.tail.value) := io.pushData
    fifo.valid(counter.tail.value) := True
    counter.tail.increment()
    counter.cnt := counter.cnt + 1
  }
  when(io.pop & !io.empty) {
//    io.popTag := fifo.tag(counter.head.value)
//    io.popData := fifo.data(counter.head.value)
    fifo.valid(counter.head.value) := False //把对应的数据无效了
    counter.head.increment()
    counter.cnt := counter.cnt - 1
  }
  when(io.pop & io.push & !io.full & !io.empty)(counter.cnt := counter.cnt) //同一时间pop和push，计数器不变

  //logic of write merge
  val writeData = B(fifo.data(io.merge.waddr), config.dataWidth bits)
  for (j <- 0 until config.beWidth) {
    when(io.merge.byteEnable(j))(writeData(j << 3, 8 bits) := io.merge.wdata(j << 3, 8 bits))
  }
  // 下面是当发生write merge时的前传逻辑
  when(io.merge.write) {

    /** 1. 前传1 ： 从wdata到pop
      * 写的逻辑实际上是Cache第二个阶段发生的，这个时候可能对应位置正在被pop，这个时候需要把数据直接前传过去
      * 并且此时不需要写入fifo了
      */
    when(io.pop & !io.empty & counter.head.value === io.merge.waddr) {
      io.popData := writeData
    }.otherwise {
      fifo.data(io.merge.waddr) := writeData
    }
    // 2. 前传2. 从wdata到rdata
    when(io.query.hitAddr === io.merge.waddr) {
      io.query.rdata := writeData
    }
  }
  //logic of query read
//  io.readHit := queryReadHit.orR
//  for (i <- 0 until config.depth) {
//    when(queryReadHit(i))(io.queryRData := fifo.data(i))
//  }
  //logic of query write : write merge
//  io.writeHit := queryWriteHit.orR
//  for (i <- 0 until config.depth) {
//    val writeData = B(fifo.data(i), config.dataWidth bits)
//    //当byte enable置1时，writeData对应的byte为queryWData，否则为fifoData(i)
//    for (j <- 0 until config.beWidth) {
//      when(io.queryBE(j))(writeData(j * 8, 8 bits) := io.queryWData(j * 8, 8 bits))
//    }
//    when(io.write & queryWriteHit(i)) {
//      fifo.data(i) := writeData
//    }
//  }
}
