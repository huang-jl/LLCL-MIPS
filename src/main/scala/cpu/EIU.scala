package cpu

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

object EXCEPTION extends SpinalEnum {
  val fetchAddrError, fetchTLBError, overflow, reservedInstruction,
  syscall, break, storeAddrError, loadAddrError, storeTLBError, loadTLBError,
  tlbModError, trapError = newElement()

  def instFetch(error:SpinalEnumCraft[EXCEPTION.this.type]): Bool = {
    Utils.equalAny(error, fetchAddrError, fetchTLBError)
  }

  def addrRelated(error:SpinalEnumCraft[EXCEPTION.this.type]): Bool = {
    Utils.equalAny(error, fetchAddrError, loadAddrError, storeAddrError)
  }

  def tlbRelated(error:SpinalEnumCraft[EXCEPTION.this.type]): Bool = {
    Utils.equalAny(error, fetchTLBError, loadTLBError, storeTLBError, tlbModError)
  }

  def code(error: SpinalEnumCraft[EXCEPTION.this.type]): SpinalEnumCraft[ExcCode.type] = {
    import ExcCode._
    error.mux(
      fetchAddrError -> AdEL,
      fetchTLBError -> TLBL,
      overflow -> Ov,
      reservedInstruction -> RI,
      syscall -> Sys,
      break -> Bp,
      storeAddrError -> AdES,
      loadAddrError -> AdEL,
      storeTLBError -> TLBS,
      loadTLBError -> TLBL,
      tlbModError -> Mod,
      trapError -> Tr
    )
  }
}

object ExcCode extends SpinalEnum {
  val Int,        // Interrupt
  AdEL, AdES,     // Address Error
  RI,             // Instruction Validity Exceptions
  Sys, Bp, Ov, Tr, // Execution Exception
  TLBL, TLBS, Mod, // TLBException
  CpU             // Coprocessor Unusable
  = newElement()

  defaultEncoding = SpinalEnumEncoding("ExcCode")(
    Int  -> 0x00,
    Mod  -> 0x01,
    TLBL -> 0x02,
    TLBS -> 0x03,
    AdEL -> 0x04,
    AdES -> 0x05,
    RI   -> 0x0a,
    Sys  -> 0x08,
    Bp   -> 0x09,
    CpU  -> 0x0b,
    Ov   -> 0x0c,
    Tr   -> 0x0d
  )
}