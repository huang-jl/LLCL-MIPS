package cache

import spinal.core._
import spinal.core.sim.SimDataPimper
import spinal.lib._
import spinal.lib.fsm._
import spinal.lib.bus.amba4.axi.{Axi4, Axi4Config}

//Write-back + Direct Mapped policy DCache
//CPU must keep addr and wdata until stall = False
case class DCacheConfig(
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
}

object DCache {
  val axiConfig = Axi4Config(
    addressWidth = 32, dataWidth = 32, idWidth = 4,
    useRegion = false, useQos = false
  )

  def main(args: Array[String]): Unit = {
    SpinalVerilog(new DCache(DCacheConfig()))
  }
}

class CPUDCacheInterface extends Bundle with IMasterSlave {
  val read = Bool
  val write = Bool
  val addr = UInt(32 bits)
  val rdata = Bits(32 bits)
  val wdata = Bits(32 bits)
  val byteEnable = Bits(2 bits)
  val stall = Bool

  override def asMaster(): Unit = {
    out(read, write, addr, byteEnable, wdata)
    in(rdata, stall)
  }

  def <<(that: CPUDCacheInterface): Unit = that >> this

  def >>(that: CPUDCacheInterface): Unit = {
    //This is master
    that.read := this.read
    that.write := this.write
    that.addr := this.addr
    that.wdata := this.wdata
    that.byteEnable := this.byteEnable
    this.rdata := that.rdata
    this.stall := that.stall
  }
}

class DCache(config: DCacheConfig) extends Component {
  val io = new Bundle {
    val cpu = slave(new CPUDCacheInterface)
    val axi = master(new Axi4(DCache.axiConfig))
  }

  def ARHandShake(axi: Axi4): Unit = {
    axi.ar.valid := True
    //Read from Mem always use the default addr define below
  }

  def AWHandShake(axi: Axi4): Unit = {
    axi.aw.valid := True
    //Write to Mem always use the default addr define below
  }

  val cacheData = new DCacheData(CacheLineConfig(wordNum = config.wordPerLine, tagWidth = config.tagWidth), config.lineNum)
  val writeCounter = Reg(UInt(config.wordIndexWidth bits)) init (0)
  val readCounter = Reg(UInt(config.wordIndexWidth bits)) init (0)
  val comparison = new Area {
    val tag = Bits(config.tagWidth bits)
    val lineIndex = UInt(config.lineIndexWidth bits) simPublic()
    val wordIndex = UInt(config.wordIndexWidth bits) simPublic()
    val byteIndex = UInt(config.byteIndexWidth bits) simPublic()

    var start = 31
    tag := io.cpu.addr(start downto start - config.tagWidth + 1).asBits
    start = start - config.tagWidth
    lineIndex := io.cpu.addr(start downto start - config.lineIndexWidth + 1)
    start = start - config.lineIndexWidth
    wordIndex := io.cpu.addr(start downto start - config.wordIndexWidth + 1)
    start = start - config.wordIndexWidth
    assert(start - config.byteIndexWidth + 1 == 0 && start == 1)
    byteIndex := io.cpu.addr(start downto start - config.byteIndexWidth + 1)

    val cacheTag = cacheData.readTag(lineIndex) //tag in the cache
    val hit: Bool = cacheData.hit(lineIndex, tag) simPublic()
    val hitData = cacheData.readData(lineIndex, wordIndex) simPublic()
    val dirty: Bool = cacheData.isDirty(lineIndex) simPublic()
  }
  val op = new Area {
    val writeMem: Bool = (io.cpu.read || io.cpu.write) & !comparison.hit & comparison.dirty simPublic()
    val readMem: Bool = (io.cpu.read || io.cpu.write) & !comparison.hit & !comparison.dirty
    val writeCache: Bool = io.cpu.write & comparison.hit
  }
  //Default Value of AXI
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
  //Write
  //aw
  io.axi.aw.addr := (comparison.cacheTag ## comparison.lineIndex ## U(0, config.lineOffset bits)).asUInt
  io.axi.aw.id := 0
  io.axi.aw.lock := 0
  io.axi.aw.cache := 0
  io.axi.aw.prot := 0
  io.axi.aw.len := config.wordPerLine - 1
  io.axi.aw.size := U"3'b010" //2^2 = 4Bytes
  io.axi.aw.burst := B"2'b01" //INCR
  io.axi.aw.valid := False
  //w
  io.axi.w.valid := False
  io.axi.w.last := (writeCounter === config.wordPerLine - 1)
  io.axi.w.strb := B"4'b1111"
  io.axi.w.data := cacheData.readData(comparison.lineIndex, writeCounter)
  //b
  io.axi.b.ready := True

  val dCacheFSM = new StateMachine {
    val IDLE: State = new State with EntryPoint {
      whenIsActive {
        when(op.writeMem)(goto(AXI_WRITE_START))
          .elsewhen(op.readMem)(goto(AXI_READ_START))
          .elsewhen(op.writeCache) {
            cacheData.writeDataByteEnable(lineIndex = comparison.lineIndex, wordIndex = comparison.wordIndex,
              byteIndex = comparison.byteIndex, data = io.cpu.wdata, byteEnable = io.cpu.byteEnable)
            cacheData.markDirty(comparison.lineIndex)
//            goto(DONE)
          }
      }
    }
    val AXI_READ_START: State = new State {
      whenIsActive {
        readCounter := 0
        ARHandShake(io.axi)
        when(io.axi.ar.ready)(goto(AXI_READ))
      }
    }
    val AXI_READ: State = new State {
      whenIsActive {
        when(io.axi.r.valid) {
          readCounter := readCounter + 1
          io.axi.r.ready := True
          cacheData.writeData(comparison.lineIndex, readCounter, io.axi.r.data)
        }
        when(io.axi.r.last & io.axi.r.valid) {
          cacheData.writeTag(comparison.lineIndex, comparison.tag)
          cacheData.validate(comparison.lineIndex)
          cacheData.clearDirty(comparison.lineIndex)
//          goto(DONE)
          goto(IDLE)
        }
      }
    }
    val AXI_WRITE_START: State = new State {
      whenIsActive {
        writeCounter := 0
        AWHandShake(io.axi)
        when(io.axi.aw.ready)(goto(AXI_WRITE))
      }
    }
    val AXI_WRITE: State = new State {
      whenIsActive {
        when(io.axi.w.ready)(writeCounter := writeCounter + 1)
        when(io.axi.w.last & io.axi.w.ready) {
          cacheData.clearDirty(comparison.lineIndex)
          goto(AXI_WRITE_WAIT_B)
        }
        io.axi.w.valid := True
      }
    }
    val AXI_WRITE_WAIT_B: State = new State {
      whenIsActive {
        //neglect the response
        when(io.axi.b.valid)(goto(IDLE))
      }
    }
//    val DONE: State = new State {
//      whenIsActive(goto(IDLE))
//    }
  }

  io.cpu.stall := !(dCacheFSM.isActive(dCacheFSM.IDLE) && !op.readMem && !op.writeMem)
  io.cpu.rdata := comparison.hitData
}
