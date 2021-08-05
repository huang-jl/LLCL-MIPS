package cpu

import lib.Optional
import spinal.core._
import spinal.lib._

import scala.language.postfixOps

object EIU_RD_SEL extends SpinalEnum {
  val BadVAddr, Count, Compare, Status, Cause, EPC = newElement()
  defaultEncoding = SpinalEnumEncoding("staticEncoding")(
    BadVAddr -> 0x40,
    Count    -> 0x48,
    Compare  -> 0x58,
    Status   -> 0x60,
    Cause    -> 0x68,
    EPC      -> 0x70
  )
}

object ExcCode extends SpinalEnum {
  val overflow, reservedInstruction,syscall, break, copUnusable,
  storeAddrError, loadAddrError, storeTLBError, loadTLBError,
  tlbModError, trapError = newElement()

  defaultEncoding = SpinalEnumEncoding("ExcCode")(
    tlbModError  -> 0x01,
    loadTLBError -> 0x02,
    storeTLBError -> 0x03,
    loadAddrError -> 0x04,
    storeAddrError -> 0x05,
    reservedInstruction   -> 0x0a,
    syscall  -> 0x08,
    break   -> 0x09,
    copUnusable  -> 0x0b,
    overflow   -> 0x0c,
    trapError   -> 0x0d
  )

  def addrRelated(error:SpinalEnumCraft[ExcCode.this.type]): Bool = {
    Utils.equalAny(error, loadAddrError, storeAddrError)
  }

  def tlbRelated(error:SpinalEnumCraft[ExcCode.this.type]): Bool = {
    Utils.equalAny(error, loadTLBError, storeTLBError, tlbModError)
  }

  /** Interrupt对应的ExcCode */
  def Int: Bits = B"5'h0"
}

class EXCEPTION extends Bundle {
  val instFetch = Bool
  val excCode = Optional(ExcCode())
  val tlbRefill = Bool
}
