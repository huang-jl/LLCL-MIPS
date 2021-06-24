package cache

import spinal.core._
import spinal.lib._

//depth: Fifo的大小
case class WriteBufferConfig(blockSize: Int, tagWidth: Int, depth: Int = 16, sim: Boolean) {
  def dataWidth: Int = blockSize * 8 //数据的位宽

  def beWidth: Int = blockSize //byteEnable信号的宽度

  def counterWidth: Int = log2Up(depth) + 1 //log2(depth) + 1

  def memAddrWidth: Int = log2Up(depth)
}

object WriteBuffer {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new WriteBuffer(WriteBufferConfig(32, 27, 8, false)))
  }
}

class WriteBufferInterface(config: WriteBufferConfig) extends Bundle with IMasterSlave {
  val write: Bool = Bool //是否把queryWData写入对应命中的cache中
  val queryTag: Bits = Bits(config.tagWidth bits) //要查询的tag
  val queryBE: Bits = Bits(config.beWidth bits) //要写入FIFO数据的byteEnable信号，长度为一个cache line的byte数量
  val queryWData: Bits = Bits(config.dataWidth bits) //要写入FIFO对应的数据
  val queryRData: Bits = Bits(config.dataWidth bits) //读出的FIFO数据
  val readHit: Bool = Bool //查询读是否命中
  val writeHit: Bool = Bool //查询写是否命中
  //要压入的数据
  val pushTag: Bits = Bits(config.tagWidth bits)
  val pushData: Bits = Bits(config.dataWidth bits)
  //要弹出的数据
  val popTag: Bits = Bits(config.tagWidth bits)
  val popData: Bits = Bits(config.dataWidth bits)
  val push: Bool = Bool //是否压入数据
  val pop: Bool = Bool //是否弹出数据

  val empty: Bool = Bool
  val full: Bool = Bool

  override def asMaster(): Unit = {
    in(queryRData, readHit, writeHit, popTag, popData, empty, full)
    out(write, queryTag, queryBE, queryWData, pushTag, pushData, push, pop)
  }
}

//一个先进先出队列，由于是全连接，不适合使用XPM_MEMORY
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
  val mem = new Area {
    val tag = Mem(Bits(config.tagWidth bits), config.depth)
    val data = Mem(Bits(config.dataWidth bits), config.depth)
    val valid = Mem(Bool, config.depth)
    if (config.sim) {
      tag.init(for (i <- 0 until config.depth) yield B(0))
      data.init(for (i <- 0 until config.depth) yield B(0))
      valid.init(for (i <- 0 until config.depth) yield False)
    }
    val fifoTag = Vec(Bits(config.tagWidth bits), config.depth)
    val fifoData = Vec(Bits(config.dataWidth bits), config.depth)
    val fifoValid = Vec(Bool, config.depth)
    for (i <- 0 until config.depth) {
      fifoTag(i) := tag.readAsync(U(i, config.memAddrWidth bits))
      fifoData(i) := data.readAsync(U(i, config.memAddrWidth bits))
      fifoValid(i) := valid.readAsync(U(i, config.memAddrWidth bits))
    }
  }
  val counter = new Area {
    val head = Counter(0 until config.depth)
    val tail = Counter(0 until config.depth)
    val cnt = Reg(UInt(config.counterWidth bits)) init (0)
  }
  val queryReadHit = Bits(config.depth bits)
  val queryWriteHit = Bits(config.depth bits)
  /*
   * Default Value
   */
  io.popTag := 0
  io.popData := 0
  io.queryRData := 0

  /*
   * Logic
   */
  io.empty := counter.cnt === 0
  io.full := counter.cnt === config.depth
  for (i <- 0 until config.depth) {
    queryReadHit(i) := (mem.fifoTag(i) === io.queryTag) & mem.fifoValid(i)
    //如果当前第i个是head并且正在pop，那么写不命中
    queryWriteHit(i) := Mux(io.pop & counter.head.value === i, False, queryReadHit(i))
  }
  //logic of push and pop
  when(io.push & !io.full) {
    mem.tag.write(counter.tail.value, io.pushTag)
    mem.data.write(counter.tail.value, io.pushData)
    mem.valid.write(counter.tail.value, True)
    counter.tail.increment()
    counter.cnt := counter.cnt + 1
  }
  when(io.pop & !io.empty) {
    io.popTag := mem.fifoTag(counter.head.value)
    io.popData := mem.fifoData(counter.head.value)
    mem.valid.write(counter.head.value, False) //把对应的数据无效了
    counter.head.increment()
    counter.cnt := counter.cnt - 1
  }
  when(io.pop & io.push & !io.full & !io.empty)(counter.cnt := counter.cnt) //同一时间pop和push，计数器不变
  //logic of query read
  io.readHit := queryReadHit.orR
  for (i <- 0 until config.depth) {
    when(queryReadHit(i))(io.queryRData := mem.fifoData(i))
  }
  //logic of query write
  io.writeHit := queryWriteHit.orR
  for (i <- 0 until config.depth) {
    val writeData = B(mem.fifoData(i), config.dataWidth bits)
    //当byte enable置1时，writeData对应的byte为queryWData，否则为fifoData(i)
    for (j <- 0 until config.beWidth) {
      when(io.queryBE(j))(writeData(j * 8, 8 bits) := io.queryWData(j * 8, 8 bits))
    }
    mem.data.write(i, writeData, io.write & queryWriteHit(i))
  }
}
