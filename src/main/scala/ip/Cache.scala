package ip

import spinal.core._

class SDPRAM(numEntries: Int, numWays: Int, indexWidth: Int, entryWidth: Int) extends Component {
  val memGeneric = new xpm_memory_sdpram_generic
  memGeneric.ADDR_WIDTH_A = indexWidth
  memGeneric.ADDR_WIDTH_B = indexWidth
  memGeneric.BYTE_WRITE_WIDTH_A = numWays * entryWidth
  memGeneric.MEMORY_PRIMITIVE = "block"
  memGeneric.MEMORY_SIZE = numEntries * entryWidth
  memGeneric.READ_DATA_WIDTH_B = numWays * entryWidth
  memGeneric.READ_LATENCY_B = 1
  memGeneric.WRITE_DATA_WIDTH_A = numWays * entryWidth
  memGeneric.WRITE_MODE_B = "read_first"
  val mem = new xpm_memory_sdpram(memGeneric)
  mem.mapClockDomain(clock = mem.io.clka)
  mem.mapClockDomain(clock = mem.io.clkb)

  val io = new Bundle {
    val ena   = in Bool ()
    val addra = in UInt (indexWidth bits)
    val dina  = in Bits (numWays * entryWidth bits)

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
  mem.io.wea := B"1"

  mem.io.enb := True
  mem.io.addrb := io.enb ? io.addrb | currAddrb
  io.doutb := (io.ena & currAddrb === io.addra) ?
    io.dina | ((prevEna & currAddrb === prevAddra) ? prevDina | mem.io.doutb)

  def write(input: Bits, we: Bits, data: Bits): Bits = {
    val output = Bits(input.getBitsWidth bits)
    output := input
    output(0, entryWidth bits) := data
    for (i <- 1 until numWays) {
      when(we(i)) {
        output := input
        output(i * entryWidth, entryWidth bits) := data
      }
    }
    output
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
  val pMem    = Mem(Bits(numWays - 1 bits), numEntries / numWays) randBoot
  // Vec(Reg(Bits(numWays - 1 bits)) randBoot, numEntries / numWays)
  //  tagMem.prevEna.setAsComb := dataMem.prevEna
  //  tagMem.prevAddra.setAsComb := dataMem.prevAddra
  //  tagMem.currAddrb.setAsComb := dataMem.currAddrb

  val io = new Bundle {
    val w = new Bundle {
      val en       = in Bool ()
      val addr     = in UInt (addrWidth bits)
      val dataLine = in Bits (numWays * dataWidth bits)
      val tagLine  = in Bits (numWays * (1 + tagWidth) bits)

      val pEn = in Bool ()
      val p   = in Bits (numWays - 1 bits)
    }
    val r = new Bundle {
      val en       = in Bool ()
      val addr     = in UInt (addrWidth bits)
      val dataLine = out Bits (numWays * dataWidth bits)
      val tagLine  = out Bits (numWays * (1 + tagWidth) bits)

      val p = out Bits (numWays - 1 bits)
    }
  }

  val wIndex = io.w.addr(indexOffset, indexWidth bits)
  val rIndex = io.r.addr(indexOffset, indexWidth bits)

  //
  dataMem.io.ena := io.w.en
  dataMem.io.addra := wIndex
  dataMem.io.dina := io.w.dataLine

  dataMem.io.enb := io.r.en
  dataMem.io.addrb := rIndex
  io.r.dataLine := dataMem.io.doutb

  //
  tagMem.io.ena := io.w.en
  tagMem.io.addra := wIndex
  tagMem.io.dina := io.w.tagLine

  tagMem.io.enb := io.r.en
  tagMem.io.addrb := rIndex
  io.r.tagLine := tagMem.io.doutb

  //
  io.r.p := (io.w.pEn & tagMem.currAddrb === wIndex) ?
    io.w.p | pMem.readAsync(dataMem.currAddrb, writeFirst)
  pMem.write(wIndex, io.w.p, io.w.pEn)

  //
  def plru(p: Bits, i: Int = 1, v: Bool = True): Bits = {
    if (i < numWays) {
      plru(p, i << 1 | 1, v & !p(i - 1)) ## plru(p, i << 1, v & p(i - 1))
    } else {
      B(v)
    }
  }

  def getP(pIn: Bits, we: Bits, set: Bool, pOut: Bits, i: Int = 1): Bool = {
    if (i < numWays) {
      val l = getP(pIn, we, set, pOut, i << 1)
      val r = getP(pIn, we, set, pOut, i << 1 | 1)

      pOut(i - 1) := pIn(i - 1)
      when(l) {
        pOut(i - 1) := !set
      }
      when(r) {
        pOut(i - 1) := set
      }

      l | r
    } else {
      we(i % numWays)
    }
  }

  def getHitLineAndData(tagLine: Bits, dataLine: Bits, addr: UInt, hitLine: Bits, data: Bits) {
    for (i <- 0 until numWays) {
      hitLine(i) :=
        tagLine(i * (1 + tagWidth), 1 + tagWidth bits) === True ## addr(tagOffset, tagWidth bits)
    }

    data := dataLine(0, dataWidth bits)
    for (i <- 1 until numWays) {
      when(hitLine(i)) { data := dataLine(i * dataWidth, dataWidth bits) }
    }
  }
}
