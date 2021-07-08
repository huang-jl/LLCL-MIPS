package ip

import spinal.core._
import spinal.lib._

class SDPRAM(numEntries: Int, numWays: Int, indexWidth: Int, entryWidth: Int) extends Component {
  val memGeneric = new xpm_memory_sdpram_generic
  memGeneric.ADDR_WIDTH_A = indexWidth
  memGeneric.ADDR_WIDTH_B = indexWidth
  memGeneric.BYTE_WRITE_WIDTH_A = entryWidth
  memGeneric.MEMORY_PRIMITIVE = "block"
  memGeneric.MEMORY_SIZE = numEntries * entryWidth
  memGeneric.READ_DATA_WIDTH_B = numWays * entryWidth
  memGeneric.READ_LATENCY_B = 1;
  memGeneric.WRITE_DATA_WIDTH_A = numWays * entryWidth
  memGeneric.WRITE_MODE_B = "write_first";
  val mem = new xpm_memory_sdpram(memGeneric)
  mem.mapClockDomain(clock = mem.io.clka)
  mem.mapClockDomain(clock = mem.io.clkb)

  val io = new Bundle {
    val ena   = in Bool ()
    val addra = in UInt (indexWidth bits)
    val dina  = in Bits (numWays * entryWidth bits)
    val wea   = in Bits (numWays bits)

    val enb   = in Bool ()
    val addrb = in UInt (indexWidth bits)
    val doutb = out Bits (numWays * entryWidth bits)
  }

  val prevEna   = RegNext(mem.io.ena)
  val prevAddra = RegNext(mem.io.addra)
  val prevDina  = RegNext(mem.io.dina)

  val currAddrb = out(RegNext(mem.io.addrb))

  mem.io.ena := io.ena
  mem.io.addra := io.addra
  mem.io.dina := io.dina
  mem.io.wea := io.wea

  mem.io.enb := True
  mem.io.addrb := io.enb ? io.addrb | currAddrb
  io.doutb := (mem.io.ena & currAddrb === mem.io.addra) ?
    mem.io.dina | ((prevEna & currAddrb === prevAddra) ? prevDina | mem.io.doutb)

  def write(data: Bits, we: Bits, din: Bits): Bits = {
    val dout = Bits(data.getBitsWidth bits)
    dout := data
    for (i <- 0 until numWays) {
      when(we(i)) {
        dout(i * entryWidth, entryWidth bits) := din
      }
    }
    dout
  }
}

class Cache(
    numEntries: Int,
    numWays: Int,
    addrWidth: Int,
    indexOffset: Int,
    indexWidth: Int,
    tagOffset: Int,
    tagWidth: Int,
    dataWidth: Int
) extends Component {
  val dataMem = new SDPRAM(numEntries, numWays, indexWidth, dataWidth)
  val tagMem  = new SDPRAM(numEntries, numWays, indexWidth, 1 + tagWidth)
  val pMem    = Vec(Reg(Bits(numWays - 1 bits)) randBoot, numEntries / numWays)
  //  tagMem.prevEna.setAsComb := dataMem.prevEna
  //  tagMem.prevAddra.setAsComb := dataMem.prevAddra
  //  tagMem.currAddrb.setAsComb := dataMem.currAddrb

  val io = new Bundle {
    val w = new Bundle {
      val en       = in Bool ()
      val addr     = in UInt (addrWidth bits)
      val dataLine = in Bits (numWays * dataWidth bits)
      val tagLine  = in Bits (numWays * (1 + tagWidth) bits)
      val wea      = in Bits (numWays bits)
      val p        = in Bits (numWays - 1 bits)

      val pEn = in Bool () default False
    }
    val r = new Bundle {
      val en       = in Bool ()
      val addr     = in UInt (addrWidth bits)
      val dataLine = out Bits (numWays * dataWidth bits)
      val tagLine  = out Bits (numWays * (1 + tagWidth) bits)
      val p        = out Bits (numWays - 1 bits)
    }
  }
  //
  dataMem.io.ena := io.w.en
  dataMem.io.addra := io.w.addr(indexOffset, indexWidth bits)
  dataMem.io.dina := io.w.dataLine
  dataMem.io.wea := io.w.wea

  dataMem.io.enb := io.r.en
  dataMem.io.addrb := io.r.addr(indexOffset, indexWidth bits)
  io.r.dataLine := dataMem.io.doutb

  //
  tagMem.io.ena := io.w.en
  tagMem.io.addra := io.w.addr(indexOffset, indexWidth bits)
  tagMem.io.dina := io.w.tagLine
  tagMem.io.wea := io.w.wea

  tagMem.io.enb := io.r.en
  tagMem.io.addrb := io.r.addr(indexOffset, indexWidth bits)
  io.r.tagLine := tagMem.io.doutb

  //
  io.r.p := pMem(dataMem.currAddrb)
  when(io.w.pEn) {
    pMem(io.w.addr(indexOffset, indexWidth bits)) := io.w.p
    when(tagMem.currAddrb === io.w.addr(indexOffset, indexWidth bits)) {
      io.r.p := io.w.p
    }
  }

  //
  def plru(i: Int, p: Bits, v: Bool): Bits = {
    if (i < numWays) {
      plru(i << 1, p, v & p(i - 1)) ## plru(i << 1 | 1, p, v & !p(i - 1))
    } else {
      B(v)
    }
  }

  def getP(i: Int, we: Bits, set: Bool, p: Bits): Bool = {
    if (i < numWays) {
      val l = getP(i << 1, we, set, p)
      p(i - 1) := l ^ set

      l | getP(i << 1 | 1, we, set, p)
    } else {
      we(i % numWays)
    }
  }

  def getHitLineAndData(tagLine: Bits, dataLine: Bits, addr: UInt, hitLine: Bits, data: Bits) {
    data.clearAll
    for (i <- 0 until numWays) {
      hitLine(i) :=
        tagLine(i * (1 + tagWidth), 1 + tagWidth bits) === True ## addr(tagOffset, tagWidth bits)
      when(hitLine(i)) {
        data := dataLine(i * dataWidth, dataWidth bits)
      }
    }
  }
}
