package cache

import spinal.core._
import spinal.core.sim.SimDataPimper
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi._

// Direct-Mapped Cache
case class ICacheConfig(
                         lineSize: Int = 256, //256B = 64word
                         cacheSize: Int = 16 * 256 //4KiB
                       ) {
  def lineNum: Int = cacheSize / lineSize

  def wordPerLine: Int = lineSize / 4

  def lineOffset: Int = log2Up(lineSize)

  def tagWidth: Int = {
    val addrWidth = 32
    addrWidth - byteIndexWidth - wordIndexWidth - lineIndexWidth
  }

  def lineIndexWidth: Int = log2Up(lineNum)

  def wordIndexWidth: Int = log2Up(wordPerLine)

  def byteIndexWidth: Int = 2
  //  def cacheTagRange: Range = {
  //    (31 downto (32 - tagWidth))
  //  }
  //  def lineTagRange: Range = {
  //    val start = (32 - tagWidth)  - 1
  //    (start downto (start - log2Up(lineNum) + 1))
  //  }
  //  def wordTagRange: Range = {
  //    val wordOffsetWidth = log2Up(lindWidth / 32)
  //    (2 + wordOffsetWidth - 1 downto 2)
  //  }
}

object ICache {
  val axiConfig = Axi4Config(
    addressWidth = 32, dataWidth = 32, idWidth = 4,
    useRegion = false, useQos = false
  )

  def main(args: Array[String]): Unit = {
    SpinalVerilog(new ICache(ICacheConfig()))
  }
}

class CPUICacheInterface extends Bundle with IMasterSlave {
  val read = Bool
  val addr = UInt(32 bits)
  val data = Bits(32 bits)
  val stall = Bool

  override def asMaster(): Unit = {
    out(read, addr)
    in(data, stall)
  }
}


class ICache(config: ICacheConfig) extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface())
    val axi = master(Axi4ReadOnly(ICache.axiConfig))
  }
  // Cache configuration
  val cacheData = new ICacheData(CacheLineConfig(config.wordPerLine, config.tagWidth), config.lineNum) simPublic()
  val readCounter = Reg(UInt(config.wordIndexWidth bits)) init (0) // The number of readed "Word"
  //Tag Comparison
  val comparison = new Area {
    val tag = Bits(config.tagWidth bits)
    val lineIndex = UInt(config.lineIndexWidth bits) simPublic()
    val wordIndex = UInt(config.wordIndexWidth bits) simPublic()

    var start = 31
    tag := io.cpu.addr(start downto start - config.tagWidth + 1).asBits
    //    println(s"addrTag = (${start} downto ${start - config.tagWidth + 1})")
    start = start - config.tagWidth
    lineIndex := io.cpu.addr(start downto start - config.lineIndexWidth + 1)
    //    println(s"lineTag = (${start} downto ${start - config.lineOffsetWidth + 1})")
    start = start - config.lineIndexWidth
    wordIndex := io.cpu.addr(start downto start - config.wordIndexWidth + 1)
    //    println(s"wordTag = (${start} downto ${start - config.wordOffsetWidth + 1})")
    val hit: Bool = cacheData.hit(lineIndex, tag) simPublic()
    val hitData = cacheData.readData(lineIndex, wordIndex)
    val willMem = io.cpu.read && !hit
  }

  //Default Value
  //Constant AXI in ICache
  //ar
  io.axi.ar.addr := (comparison.tag ## comparison.lineIndex ## U(0, config.lineOffset bits)).asUInt
  io.axi.ar.id := 0
  io.axi.ar.lock := 0
  io.axi.ar.cache := 0
  io.axi.ar.prot := 0
  io.axi.ar.len := config.wordPerLine - 1
  io.axi.ar.size := U"3'b010" //2^2 = 4Bytes
  io.axi.ar.burst := B"2'b01" //INCR
  io.axi.ar.valid := False
  //r
  io.axi.r.ready := False


  //State Machine
  val iCacheFSM = new StateMachine {
    val IDLE: State = new State with EntryPoint {
      whenIsActive {
        when(comparison.willMem)(goto(AXI_READ_START))
      }
    }

    val AXI_READ_START: State = new State {
      whenIsActive {
        readCounter := 0
        when(io.axi.ar.ready)(goto(AXI_READ))
      }
    }

    val AXI_READ: State = new State {
      whenIsActive {
        when(io.axi.r.valid)(readCounter := readCounter + 1)
        when(io.axi.r.last)(goto(WRITE_CACHE))
      }
    }

    val WRITE_CACHE: State = new State {
      //Use for the last word to write back cache
      whenIsActive(goto(IDLE))
    }
  }


  val AXI_READ_START = new Area {
    when(iCacheFSM.isActive(iCacheFSM.AXI_READ_START)) {
      io.axi.ar.valid := True
    }
  }

  val AXI_READ = new Area {
    when(iCacheFSM.isActive(iCacheFSM.AXI_READ)) {
      when(io.axi.r.valid) {
        io.axi.r.ready := True
        cacheData.writeData(lineIndex = comparison.lineIndex, wordIndex = readCounter, data = io.axi.r.data)
      }
      when(io.axi.r.last & io.axi.r.valid) {
        cacheData.validate(comparison.lineIndex)
        cacheData.writeTag(lineIndex = comparison.lineIndex, tag = comparison.tag)
      }
    }
  }

  //CPU-Cache Interface
  io.cpu.data := comparison.hitData
  io.cpu.stall := comparison.willMem || !iCacheFSM.isActive(iCacheFSM.IDLE)
}