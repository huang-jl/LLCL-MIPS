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
    val cpu = new Bundle {
      val cached = slave(new CPUDCacheInterface)
      val uncached = slave(new CPUDCacheInterface)
    }
    val axi = new Bundle {
      val cached = master(new Axi4(DCache.axiConfig))
      val uncached = master(new Axi4(DCache.axiConfig))
    }
  }

  def translateByteEnable(byteEnable: Bits, byteIndex: UInt): Bits = {
    var res = B"4'b1111"
    switch(byteEnable) {
      is(0){res \= B"4'b0001"}
      is(1) {
        switch(byteIndex) {
          is(0)(res \= B"4'b0011")
          is(2)(res \= B"4'b1100")
        }
      }
      is(2)(res \= B"4'b1111")
    }
    res
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
  val recv = Reg(Bits(32 bits)) init (0) // register for data from axi bus
  val comparison = new Area {
    val tag = Bits(config.tagWidth bits)
    val lineIndex = UInt(config.lineIndexWidth bits) simPublic()
    val wordIndex = UInt(config.wordIndexWidth bits) simPublic()
    val byteIndex = UInt(config.byteIndexWidth bits) simPublic()

    var start = 31
    tag := io.cpu.cached.addr(start downto start - config.tagWidth + 1).asBits
    start = start - config.tagWidth
    lineIndex := io.cpu.cached.addr(start downto start - config.lineIndexWidth + 1)
    start = start - config.lineIndexWidth
    wordIndex := io.cpu.cached.addr(start downto start - config.wordIndexWidth + 1)
    start = start - config.wordIndexWidth
    assert(start - config.byteIndexWidth + 1 == 0 && start == 1)
    byteIndex := io.cpu.cached.addr(start downto start - config.byteIndexWidth + 1)

    val cacheTag = cacheData.readTag(lineIndex) //tag in the cache
    val hit: Bool = cacheData.hit(lineIndex, tag) simPublic()
    val hitData = cacheData.readData(lineIndex, wordIndex) simPublic()
    val dirty: Bool = cacheData.isDirty(lineIndex) simPublic()
  }
  val op = new Area {
    val cached = new Area {
      val writeMem: Bool = (io.cpu.cached.read || io.cpu.cached.write) & !comparison.hit & comparison.dirty simPublic()
      val readMem: Bool = (io.cpu.cached.read || io.cpu.cached.write) & !comparison.hit & !comparison.dirty
      val writeCache: Bool = io.cpu.cached.write & comparison.hit
    }
    val uncached = new Area {
      val writeMem: Bool = io.cpu.uncached.write
      val readMem: Bool = io.cpu.uncached.read
    }
  }
  //Default Value of AXI
  val cachedDefault = new Area {
    //ar
    io.axi.cached.ar.addr := (comparison.tag ## comparison.lineIndex ## U(0, config.lineOffset bits)).asUInt
    io.axi.cached.ar.id := 0
    io.axi.cached.ar.lock := 0
    io.axi.cached.ar.cache := 0
    io.axi.cached.ar.prot := 0
    io.axi.cached.ar.len := config.wordPerLine - 1
    io.axi.cached.ar.size := U"3'b010" //2^2 = 4Bytes
    io.axi.cached.ar.burst := B"2'b01" //INCR
    io.axi.cached.ar.valid := False
    //r
    io.axi.cached.r.ready := False
    //Write
    //aw
    io.axi.cached.aw.addr := (comparison.cacheTag ## comparison.lineIndex ## U(0, config.lineOffset bits)).asUInt
    io.axi.cached.aw.id := 0
    io.axi.cached.aw.lock := 0
    io.axi.cached.aw.cache := 0
    io.axi.cached.aw.prot := 0
    io.axi.cached.aw.len := config.wordPerLine - 1
    io.axi.cached.aw.size := U"3'b010" //2^2 = 4Bytes
    io.axi.cached.aw.burst := B"2'b01" //INCR
    io.axi.cached.aw.valid := False
    //w
    io.axi.cached.w.valid := False
    io.axi.cached.w.last := (writeCounter === config.wordPerLine - 1)
    io.axi.cached.w.strb := B"4'b1111"
    io.axi.cached.w.data := cacheData.readData(comparison.lineIndex, writeCounter)
    //b
    io.axi.cached.b.ready := True
  }
  val uncachedDefault = new Area {
    //ar
    io.axi.uncached.ar.addr := (io.cpu.uncached.addr(31 downto config.byteIndexWidth) ## B(0, config.byteIndexWidth bits)).asUInt
    io.axi.uncached.ar.id := 0
    io.axi.uncached.ar.lock := 0
    io.axi.uncached.ar.cache := 0
    io.axi.uncached.ar.prot := 0
    io.axi.uncached.ar.len := 0 // burst length is one for uncached transfer
    io.axi.uncached.ar.size := U"3'b010" //2^2 = 4Bytes
    io.axi.uncached.ar.burst := B"2'b01" //INCR
    io.axi.uncached.ar.valid := False
    //r
    io.axi.uncached.r.ready := False
    //Write
    //aw
    io.axi.uncached.aw.addr := (io.cpu.uncached.addr(31 downto config.byteIndexWidth) ## B(0, config.byteIndexWidth bits)).asUInt
    io.axi.uncached.aw.id := 0
    io.axi.uncached.aw.lock := 0
    io.axi.uncached.aw.cache := 0
    io.axi.uncached.aw.prot := 0
    io.axi.uncached.aw.len := 0 // burst length is one for uncached transfer
    io.axi.uncached.aw.size := U"3'b010" //2^2 = 4Bytes
    io.axi.uncached.aw.burst := B"2'b01" //INCR
    io.axi.uncached.aw.valid := False
    //w
    io.axi.uncached.w.valid := False
    io.axi.uncached.w.last := False
    io.axi.uncached.w.strb := translateByteEnable(io.cpu.uncached.byteEnable, comparison.byteIndex)
    io.axi.uncached.w.data := io.cpu.uncached.wdata
    //b
    io.axi.uncached.b.ready := True
  }

  val dCacheFSM = new StateMachine {
    val IDLE: State = new State with EntryPoint {
      whenIsActive {
        when(op.uncached.writeMem)(goto(UNCACHED_WRITE_START))
          .elsewhen(op.uncached.readMem)(goto(UNCACHED_READ_START))
          .elsewhen(op.cached.writeMem)(goto(AXI_WRITE_START))
          .elsewhen(op.cached.readMem)(goto(AXI_READ_START))
          .elsewhen(op.cached.writeCache) {
            cacheData.writeDataByteEnable(lineIndex = comparison.lineIndex, wordIndex = comparison.wordIndex,
              byteIndex = comparison.byteIndex, data = io.cpu.cached.wdata, byteEnable = io.cpu.cached.byteEnable)
            cacheData.markDirty(comparison.lineIndex)
            goto(DONE)
          }
      }
    }
    val AXI_READ_START: State = new State {
      whenIsActive {
        readCounter := 0
        ARHandShake(io.axi.cached)
        when(io.axi.cached.ar.ready)(goto(AXI_READ))
      }
    }
    val AXI_READ: State = new State {
      whenIsActive {
        when(io.axi.cached.r.valid) {
          readCounter := readCounter + 1
          io.axi.cached.r.ready := True
          cacheData.writeData(comparison.lineIndex, readCounter, io.axi.cached.r.data)
        }
        when(io.axi.cached.r.last & io.axi.cached.r.valid) {
          cacheData.writeTag(comparison.lineIndex, comparison.tag)
          cacheData.validate(comparison.lineIndex)
          cacheData.clearDirty(comparison.lineIndex)
          goto(DONE)
        }
      }
    }
    val AXI_WRITE_START: State = new State {
      whenIsActive {
        writeCounter := 0
        AWHandShake(io.axi.cached)
        when(io.axi.cached.aw.ready)(goto(AXI_WRITE))
      }
    }
    val AXI_WRITE: State = new State {
      whenIsActive {
        when(io.axi.cached.w.ready)(writeCounter := writeCounter + 1)
        when(io.axi.cached.w.last & io.axi.cached.w.ready) {
          cacheData.clearDirty(comparison.lineIndex)
          goto(AXI_WRITE_WAIT_B)
        }
        io.axi.cached.w.valid := True
      }
    }
    val AXI_WRITE_WAIT_B: State = new State {
      whenIsActive {
        //neglect the response
        when(io.axi.cached.b.valid)(goto(IDLE))
      }
    }
    val UNCACHED_READ_START: State = new State {
      whenIsActive {
        ARHandShake(io.axi.uncached)
        when(io.axi.uncached.ar.ready)(goto(UNCACHED_READ))
      }
    }
    val UNCACHED_READ: State = new State {
      whenIsActive {
        when(io.axi.uncached.r.last & io.axi.uncached.r.valid) {
          io.axi.uncached.r.ready := True
          recv := io.axi.uncached.r.data
          goto(DONE)
        }
      }
    }
    val UNCACHED_WRITE_START: State = new State {
      whenIsActive {
        AWHandShake(io.axi.uncached)
        when(io.axi.uncached.aw.ready)(goto(UNCACHED_WRITE))
      }
    }
    val UNCACHED_WRITE: State = new State {
      whenIsActive {
        io.axi.uncached.w.last := True
        io.axi.uncached.w.valid := True
        when(io.axi.uncached.w.ready)(goto(UNCACHED_WAIT_B))
      }
    }
    val UNCACHED_WAIT_B: State = new State {
      whenIsActive {
        when(io.axi.uncached.b.valid)(goto(DONE))
      }
    }
    val DONE: State = new State {
      whenIsActive(goto(IDLE))
    }
  }

  io.cpu.cached.stall := !(dCacheFSM.isActive(dCacheFSM.IDLE) && !op.cached.readMem && !op.cached.writeMem)
  io.cpu.cached.rdata := comparison.hitData

  //cpu send uncached request have to wait one cycle
  io.cpu.uncached.stall := !(dCacheFSM.isActive(dCacheFSM.DONE) ||
    (dCacheFSM.isActive(dCacheFSM.IDLE) && !op.uncached.readMem && !op.uncached.writeMem))
  io.cpu.uncached.rdata := recv
}
