package ip

import spinal.core._

class SDPRAM(numEntries: Int, indexWidth: Int, entryWidth: Int) extends Component {
  val memGeneric = new xpm_memory_sdpram_generic
  memGeneric.ADDR_WIDTH_A = indexWidth
  memGeneric.ADDR_WIDTH_B = indexWidth
  memGeneric.BYTE_WRITE_WIDTH_A = entryWidth
  memGeneric.MEMORY_PRIMITIVE = "block"
  memGeneric.MEMORY_SIZE = numEntries * entryWidth
  memGeneric.READ_DATA_WIDTH_B = entryWidth
  memGeneric.READ_LATENCY_B = 1
  memGeneric.WRITE_DATA_WIDTH_A = entryWidth
  memGeneric.WRITE_MODE_B = "read_first"
  val mem = new xpm_memory_sdpram(memGeneric)

  val io = new Bundle {
    val ena   = in Bool ()
    val addra = in UInt (indexWidth bits)
    val dina  = in Bits (entryWidth bits)

    val enb   = in Bool ()
    val addrb = in UInt (indexWidth bits)
    val doutb = out Bits (entryWidth bits)
  }

  val prevDina  = RegNext(mem.io.dina)
  val currAddrb = RegNext(mem.io.addrb)
  val conflict  = RegNext(io.ena & (io.addra === currAddrb)) init False

  mem.io.ena := io.ena
  mem.io.addra := io.addra
  mem.io.dina := B(io.dina)
  mem.io.wea := B"1"

  mem.io.enb := True
  mem.io.addrb := io.enb ? io.addrb | currAddrb
  io.doutb.assignFromBits(conflict ? prevDina | mem.io.doutb)
}

class Cache(entriesPerWay: Int, numWays: Int, indexWidth: Int, metaWidth: Int, dataWidth: Int) extends Component {
  val metaMem = Seq.fill(numWays)(new SDPRAM(entriesPerWay, indexWidth, metaWidth))
  val dataMem = Seq.fill(numWays)(new SDPRAM(entriesPerWay, indexWidth, dataWidth))
  val pMem    = Mem(Bits(numWays - 1 bits), entriesPerWay).randBoot()

  val io = new Bundle {
    val w = new Bundle {
      val en    = in Bool ()
      val oh    = in Bits (numWays bits)
      val index = in UInt (indexWidth bits)
      val data  = in Bits (dataWidth bits)
      val meta  = in Bits (metaWidth bits)
      val p     = in Bits (numWays - 1 bits)
    }
    val r = new Bundle {
      val en       = in Bool ()
      val index    = in UInt (indexWidth bits)
      val dataLine = out Vec (Bits(dataWidth bits), numWays)
      val metaLine = out Vec (Bits(metaWidth bits), numWays)
      val p        = out Bits (numWays - 1 bits)
    }
  }

  for (i <- 0 until numWays) {
    val we = io.w.en & io.w.oh(i)
    /**/
    dataMem(i).io.ena := we
    dataMem(i).io.addra := io.w.index
    dataMem(i).io.dina := io.w.data

    dataMem(i).io.enb := io.r.en
    dataMem(i).io.addrb := io.r.index
    io.r.dataLine(i) := dataMem(i).io.doutb
    /**/
    metaMem(i).io.ena := we
    metaMem(i).io.addra := io.w.index
    metaMem(i).io.dina := io.w.meta

    metaMem(i).io.enb := io.r.en
    metaMem(i).io.addrb := io.r.index
    io.r.metaLine(i) := metaMem(i).io.doutb
  }

  //
  io.r.p := pMem(io.r.index)
  when(io.w.en) { pMem(io.w.index) := io.w.p }

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
}

object Cache {
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new Cache(1024, 4, 10, 10, 10))
  }
}
