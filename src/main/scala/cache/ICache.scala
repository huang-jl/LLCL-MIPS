package cache

import spinal.core._
import spinal.core.sim.SimDataPimper
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi._

class CPUICacheInterface extends Bundle with IMasterSlave {
  val read = Bool
  val addr = UInt(32 bits)
  val data = Bits(32 bits)
  val stall = Bool

  override def asMaster(): Unit = {
    out(read, addr)
    in(data, stall)
  }

  def <<(that: CPUICacheInterface): Unit = that >> this

  def >>(that: CPUICacheInterface): Unit = {
    //this is master
    that.read := this.read
    that.addr := this.addr
    this.data := that.data
    this.stall := that.stall
  }
}

object ICache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ICache(CacheRamConfig()))
  }
}

class ICache(config: CacheRamConfig) extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface)
    val axi = master(new Axi4(Axi4Config(
      addressWidth = 32, dataWidth = 32, idWidth = 4,
      useRegion = false, useQos = false
    )))
  }

  val inputAddr = new Area {
    val tag: UInt = io.cpu.addr(config.offsetWidth + config.indexWidth, config.tagWidth bits)
    val index: UInt = io.cpu.addr(config.offsetWidth, config.indexWidth bits)
    val offset: UInt = io.cpu.addr(0, config.offsetWidth bits)
  }
  val cacheRam = new Area {
    val tags = Array.fill(config.wayNum)(Mem(Meta(config.tagWidth), 1 << config.indexWidth))
    val datas = Array.fill(config.wayNum)(Mem(Block(config.blockSize), 1 << config.indexWidth))
  }

  val hit = new Area {
    val hitPerWay = Bits(config.wayNum bits) //每一路是否命中
    for (i <- 0 until config.wayNum) {
      val meta = cacheRam.tags(i).readAsync(inputAddr.index)
      hitPerWay(i) := (meta.tag.asUInt === inputAddr.tag) & meta.valid
    }
    val hit: Bool = hitPerWay.orR & io.cpu.read
    val hitLine = MuxOH(hitPerWay, for (i <- 0 until config.wayNum) yield cacheRam.datas(i).readAsync(inputAddr.index))
  }
  io.cpu.stall := False
  io.cpu.data := 0

  //  Default value of AXI singal
  io.axi.ar.id := 0
  io.axi.ar.addr := io.cpu.addr
  io.axi.ar.lock := 0
  io.axi.ar.cache := 0
  io.axi.ar.prot := 0
  io.axi.ar.len := config.wordSize - 1
  io.axi.ar.size := U"3'b010" //2^2 = 4Bytes
  io.axi.ar.burst := B"2'b01" //INCR
  io.axi.ar.valid := False
  //r
  io.axi.r.ready := False
  //Write is always disabled for ICache
  //aw
  io.axi.aw.addr := 0
  io.axi.aw.id := 0
  io.axi.aw.lock := 0
  io.axi.aw.cache := 0
  io.axi.aw.prot := 0
  io.axi.aw.len := 0
  io.axi.aw.size := U"3'b010" //2^2 = 4Bytes
  io.axi.aw.burst := B"2'b01" //INCR
  io.axi.aw.valid := False
  //w
  io.axi.w.valid := False
  io.axi.w.last := False
  io.axi.w.strb := B"4'b1111"
  io.axi.w.data := 0
  //b
  io.axi.b.ready := False

  val counter = Counter(0 to config.wordSize)
  val ICacheFSM = new StateMachine {
    val waitAXIReady = new State
    val readMem = new State
    val done = new State
    disableAutoStart()

    stateBoot
      .whenIsActive {
        counter.clear()
        when(io.cpu.read & !hit.hit)(goto(waitAXIReady))
      }

    waitAXIReady
      .whenIsActive {
        io.axi.ar.valid := True
        counter.clear()
        when(io.axi.ar.ready)(goto(waitAXIReady))
      }

    readMem
      .whenIsActive {
        io.axi.r.ready := True
        when(io.axi.r.valid) {
          counter.increment()
          cacheRam.datas
        }
        when(io.axi.r.valid & io.axi.r.last)(goto(done))
      }

    done
      .whenIsActive(goto(stateBoot))
  }

  //更新
}

//
//// Direct-Mapped Cache
//case class ICacheConfig(
//                         lineSize: Int = 256, //256B = 64word
//                         cacheSize: Int = 16 * 256 //4KiB
//                       ) {
//  def lineNum: Int = cacheSize / lineSize
//
//  def wordPerLine: Int = lineSize / 4
//
//  def lineOffset: Int = log2Up(lineSize)
//
//  def tagWidth: Int = {
//    val addrWidth = 32
//    addrWidth - byteIndexWidth - wordIndexWidth - lineIndexWidth
//  }
//
//  def lineIndexWidth: Int = log2Up(lineNum)
//
//  def wordIndexWidth: Int = log2Up(wordPerLine)
//
//  def byteIndexWidth: Int = 2
//}
//
//object ICache {
//  val axiConfig = Axi4Config(
//    addressWidth = 32, dataWidth = 32, idWidth = 4,
//    useRegion = false, useQos = false
//  )
//
//  def main(args: Array[String]): Unit = {
//    SpinalVerilog(new ICache(ICacheConfig()))
//  }
//}
//
//
//
//class ICache(config: ICacheConfig) extends Component {
//  val io = new Bundle {
//    val cpu = slave(new CPUICacheInterface())
//    val axi = master(Axi4ReadOnly(ICache.axiConfig))
//  }
//  // Cache configuration
//  val cacheData = new ICacheData(CacheLineConfig(config.wordPerLine, config.tagWidth), config.lineNum) simPublic()
//  val readCounter = Reg(UInt(config.wordIndexWidth bits)) init (0) // The number of readed "Word"
//  //Tag Comparison
//  val comparison = new Area {
//    val tag = Bits(config.tagWidth bits)
//    val lineIndex = UInt(config.lineIndexWidth bits) simPublic()
//    val wordIndex = UInt(config.wordIndexWidth bits) simPublic()
//
//    var start = 31
//    tag := io.cpu.addr(start downto start - config.tagWidth + 1).asBits
//    //    println(s"addrTag = (${start} downto ${start - config.tagWidth + 1})")
//    start = start - config.tagWidth
//    lineIndex := io.cpu.addr(start downto start - config.lineIndexWidth + 1)
//    //    println(s"lineTag = (${start} downto ${start - config.lineOffsetWidth + 1})")
//    start = start - config.lineIndexWidth
//    wordIndex := io.cpu.addr(start downto start - config.wordIndexWidth + 1)
//    //    println(s"wordTag = (${start} downto ${start - config.wordOffsetWidth + 1})")
//    val hit: Bool = cacheData.hit(lineIndex, tag) simPublic()
//    val hitData = cacheData.readData(lineIndex, wordIndex)
//    val willMem = io.cpu.read && !hit
//  }
//
//  //Default Value
//  //Constant AXI in ICache
//  //ar
//  io.axi.ar.addr := (comparison.tag ## comparison.lineIndex ## U(0, config.lineOffset bits)).asUInt
//  io.axi.ar.id := 0
//  io.axi.ar.lock := 0
//  io.axi.ar.cache := 0
//  io.axi.ar.prot := 0
//  io.axi.ar.len := config.wordPerLine - 1
//  io.axi.ar.size := U"3'b010" //2^2 = 4Bytes
//  io.axi.ar.burst := B"2'b01" //INCR
//  io.axi.ar.valid := False
//  //r
//  io.axi.r.ready := False
//
//
//  //State Machine
//  val iCacheFSM = new StateMachine {
//    val IDLE: State = new State with EntryPoint {
//      whenIsActive {
//        when(comparison.willMem)(goto(AXI_READ_START))
//      }
//    }
//
//    val AXI_READ_START: State = new State {
//      whenIsActive {
//        readCounter := 0
//        io.axi.ar.valid := True
//        when(io.axi.ar.ready)(goto(AXI_READ))
//      }
//    }
//
//    val AXI_READ: State = new State {
//      whenIsActive {
//        when(io.axi.r.valid) {
//          readCounter := readCounter + 1
//          io.axi.r.ready := True
//          cacheData.writeData(lineIndex = comparison.lineIndex, wordIndex = readCounter, data = io.axi.r.data)
//        }
//        when(io.axi.r.last & io.axi.r.valid) {
//          cacheData.validate(comparison.lineIndex)
//          cacheData.writeTag(lineIndex = comparison.lineIndex, tag = comparison.tag)
//          goto(IDLE)
//        }
//      }
//    }
//
////    val WRITE_CACHE: State = new State {
////      //Use for the last word to write back cache
////      whenIsActive(goto(IDLE))
////    }
//  }
//
//  //CPU-Cache Interface
//  io.cpu.data := comparison.hitData
//  io.cpu.stall := comparison.willMem || !iCacheFSM.isActive(iCacheFSM.IDLE)
//}