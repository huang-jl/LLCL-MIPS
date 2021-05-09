package cpu

import spinal.core._
import spinal.core.sim._
import spinal.sim._
import cpu.du._

class DUTest extends Component {

}

object DUTest {
  def main(args: Array[String]): Unit = {
    SimConfig.withWave.compile(new DU).doSim("Decode Instructions")(du => {
      du.inst #= BigInt("00000000001000110011100000100000", 2) // add
      sleep(1)
      du.inst #= BigInt("00100000001000110000000000000111", 2) // addi
      sleep(1)
      du.inst #= BigInt("00000000001000110011100000100001", 2) // addu
      sleep(1)
      du.inst #= BigInt("00100100001000110000000000000111", 2) // addiu
      sleep(1)
      du.inst #= BigInt("00000000001000110011100000100010", 2) // sub
      sleep(1)
      du.inst #= BigInt("00000000001000110011100000100011", 2) // subu
      sleep(1)
      du.inst #= BigInt("00000000001000110011100000101010", 2) // slt
      sleep(1)
      du.inst #= BigInt("00101000001000110000000000000111", 2) // slti
      sleep(1)
      du.inst #= BigInt("00000000001000110011100000101011", 2) // sltu
      sleep(1)
      du.inst #= BigInt("00101100001000110000000000000111", 2) // sltiu
      sleep(1)


      du.inst #= BigInt("00010000001000110000000000000111", 2) // beq
      sleep(1)
      du.inst #= BigInt("00010100001000110000000000000111", 2) // bne
      sleep(1)
      du.inst #= BigInt("00000100001000010000000000000011", 2) // bgez
      sleep(1)
      du.inst #= BigInt("00011100001000000000000000000011", 2) // bgtz
      sleep(1)
      du.inst #= BigInt("00011000001000000000000000000011", 2) // blez
      sleep(1)
      du.inst #= BigInt("00000100001000000000000000000011", 2) // bltz
      sleep(1)
      du.inst #= BigInt("00000100001100010000000000000011", 2) // bgezal
      sleep(1)
      du.inst #= BigInt("00000100001100000000000000000011", 2) // bltzal
      sleep(1)
      du.inst #= BigInt("00001000000000000000000000000001", 2) // j
      sleep(1)
      du.inst #= BigInt("00001100000000000000000000000001", 2) // jal
      sleep(1)
      du.inst #= BigInt("00000000001000000000000000001000", 2) // jr
      sleep(1)
      du.inst #= BigInt("00000000001000000001100000001001", 2) // jalr
      sleep(1)

      du.inst #= BigInt("10000000001000110000000000000111", 2) // lb
      sleep(1)
      du.inst #= BigInt("10010000001000110000000000000111", 2) // lbu
      sleep(1)
      du.inst #= BigInt("10000100001000110000000000000111", 2) // lh
      sleep(1)
      du.inst #= BigInt("10010100001000110000000000000111", 2) // lhu
      sleep(1)
      du.inst #= BigInt("10001100001000110000000000000111", 2) // lw
      sleep(1)
      du.inst #= BigInt("10100000001000110000000000000111", 2) // sb
      sleep(1)
      du.inst #= BigInt("10100100001000110000000000000111", 2) // sh
      sleep(1)
      du.inst #= BigInt("10101100001000110000000000000111", 2) // sw
      sleep(1)
      //      du.inst #= BigInt("00101100001000110000000000000111", 2) // andi
      //      sleep(1)
      //      du.inst #= BigInt("00101100001000110000000000000111", 2) // lui
      //      sleep(1)
      simSuccess()
    })
  }
}