package cache

import spinal.core._
import spinal.core.sim.SimDataPimper
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi._

// Direct-Mapped Cache
case class ICacheConfig(
                         lineWidth: Int = 256,
                         cacheSize: Int = 16 * 256 //4KiB
                       ) {
  def lineNum: Int = cacheSize / lineWidth

  def wordNumPerLine: Int = lineWidth / 32

  def tagWidth: Int = {
    val addrWidth = 32
    addrWidth - byteOffsetWidth - wordOffsetWidth - lineOffsetWidth
  }

  def lineOffsetWidth: Int = log2Up(lineNum)

  def wordOffsetWidth: Int = log2Up(wordNumPerLine)

  def byteOffsetWidth: Int = 2
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
  val raddr = UInt(32 bits)
  val rdata = Bits(32 bits)
  val stall = Bool

  override def asMaster(): Unit = {
    out(read, raddr)
    in(rdata, stall)
  }
}


class ICache(config: ICacheConfig) extends Component {
  def willMem: Bool = {
    io.cpu.read && !comparison.hit
  }

  val io = new Bundle {
    val cpu = slave(new CPUICacheInterface())
    val axi = master(Axi4ReadOnly(ICache.axiConfig))
  }
  // Cache configuration
  val lines = Vec(new ICacheLine(
    CacheLineConfig(wordNum = config.wordNumPerLine, tagWidth = config.tagWidth)
  ), config.lineNum) simPublic()


  //Default Value
  //Constant AXI in ICache
  //ar
  io.axi.ar.payload.addr := (io.cpu.raddr(31 downto config.byteOffsetWidth) ## B"2'b00").asUInt
  io.axi.ar.payload.id := 0
  io.axi.ar.payload.lock := 0
  io.axi.ar.payload.cache := 0
  io.axi.ar.payload.prot := 0
  io.axi.ar.len := config.wordNumPerLine
  io.axi.ar.size := U"3'b101" //2^5 = 32
  io.axi.ar.burst := B"2'b01" //INCR
  io.axi.ar.valid := False
  //r
  io.axi.r.ready := False

  //Tag Comparison
  val tags = new Area {
    val addrTag = Bits(config.tagWidth bits)
    val lineTag = UInt(config.lineOffsetWidth bits) simPublic()
    val wordTag = UInt(config.wordOffsetWidth bits) simPublic()

    var start = 31
    addrTag := io.cpu.raddr(start downto start - config.tagWidth + 1).asBits
    //    println(s"addrTag = (${start} downto ${start - config.tagWidth + 1})")
    start = start - config.tagWidth
    lineTag := io.cpu.raddr(start downto start - config.lineOffsetWidth + 1)
    //    println(s"lineTag = (${start} downto ${start - config.lineOffsetWidth + 1})")
    start = start - config.lineOffsetWidth
    wordTag := io.cpu.raddr(start downto start - config.wordOffsetWidth + 1)
    //    println(s"wordTag = (${start} downto ${start - config.wordOffsetWidth + 1})")
  }
  val comparison = new Area {
    val hit: Bool = lines(tags.lineTag).tag === tags.addrTag
    val hitData = lines(tags.lineTag).datas(tags.wordTag)
  }

  //State Machine
  val iCacheFSM = new StateMachine {
    val read_cnt = Reg(UInt(config.wordOffsetWidth bits)) init (0) // The number of readed "Word"
    val IDLE: State = new State with EntryPoint {
      whenIsActive {
        when(willMem)(goto(AXI_READ_START))
      }
    }

    val AXI_READ_START: State = new State {
      whenIsActive {
        read_cnt := 0
        goto(AXI_READ)
      }
    }

    val AXI_READ: State = new State {
      whenIsActive {
        when(io.axi.ar.ready)(read_cnt := read_cnt + 1)
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
      io.axi.ar.addr := io.cpu.raddr
      io.axi.ar.valid := True
    }
  }

  val AXI_READ = new Area {
    when(iCacheFSM.isActive(iCacheFSM.AXI_READ)) {
      when(io.axi.r.valid) {
        io.axi.r.ready := True
        lines(tags.lineTag).datas(iCacheFSM.read_cnt) := io.axi.r.data
        lines(tags.lineTag).tag := tags.addrTag
      }
    }
  }

  val WRITE_CACHE = new Area {
    when(iCacheFSM.isActive(iCacheFSM.WRITE_CACHE)) {

    }
  }



  //CPU-Cache Interface
  io.cpu.rdata := comparison.hitData
  io.cpu.stall := willMem || !iCacheFSM.isActive(iCacheFSM.IDLE)
}