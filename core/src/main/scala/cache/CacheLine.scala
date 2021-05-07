package cache

import spinal.core._

case class CacheLineConfig(
                            wordNum: Int,
                            tagWidth: Int
                          )

class CacheLineBase(config: CacheLineConfig) extends Bundle {
  val datas = Vec(Reg(Bits(32 bits)) init (0), config.wordNum)
  val tag = Reg(Bits(config.tagWidth bits)) init (0)
  val valid = Reg(Bool) init (false)
}

class ICacheLine(config: CacheLineConfig) extends CacheLineBase(config)

class DCacheLine(config: CacheLineConfig) extends CacheLineBase(config) {
  val dirty = Reg(Bool) init (false)
}

class CacheDataBase[T <: CacheLineBase](config: CacheLineConfig, lineNum: Int, factory: CacheLineConfig => T) extends Bundle {
  val lines = Vec(factory(config), lineNum)

  def writeData(lineIndex: UInt, wordIndex: UInt, data: Bits): Unit = {
    lines(lineIndex).datas(wordIndex) := data
  }

  def writeTag(lineIndex: UInt, tag: Bits): Unit = {
    lines(lineIndex).tag := tag
  }

  def readData(lineIndex: UInt, wordIndex: UInt): Bits = {
    lines(lineIndex).datas(wordIndex)
  }

  def readTag(lineIndex: UInt): Bits = {
    lines(lineIndex).tag
  }

  def hit(lineIndex: UInt, tag: Bits): Bool = {
    lines(lineIndex).tag === tag & lines(lineIndex).valid
  }

  def invalidate(lineIndex: UInt): Unit = (lines(lineIndex).valid := False)

  def validate(lineIndex: UInt): Unit = (lines(lineIndex).valid := True)
}

class ICacheData(config: CacheLineConfig, lineNum: Int) extends CacheDataBase[ICacheLine](config, lineNum, (x) => new ICacheLine(x))

class DCacheData(config: CacheLineConfig, lineNum: Int) extends CacheDataBase[DCacheLine](config, lineNum, (x) => new DCacheLine(x)) {
  def isDirty(lineIndex: UInt): Bool = (lines(lineIndex).dirty)

  def markDirty(lineIndex: UInt): Unit = (lines(lineIndex).dirty := True)

  def clearDirty(lineIndex: UInt): Unit = (lines(lineIndex).dirty := False)

  def writeDataByteEnable(lineIndex: UInt, wordIndex: UInt, byteIndex: UInt, data: Bits, byteEnable: Bits): Unit = {
    //byteEnable is 2 bits signal
    //0 ------> byteIndex*8+7 : byteIndex*8
    //1 ------> byteIndex*8+15: byteIndex*8
    //2 ------> whole word
    val hitData: Bits = lines(lineIndex).datas(wordIndex) //origin data in the cache
    var wdata = hitData //default to value in the cache
    switch(byteEnable) {
      is(0) {
        switch(byteIndex) {
          is(0) {
            wdata \= hitData(31 downto 8) ## data(7 downto 0)
          }
          is(1) {
            wdata \= hitData(31 downto 16) ## data(15 downto 8) ## hitData(7 downto 0)
          }
          is(2) {
            wdata \= hitData(31 downto 24) ## data(23 downto 16) ## hitData(15 downto 0)
          }
          is(3) {
            wdata \= data(31 downto 24) ## hitData(23 downto 0)
          }
        }
      }
      is(1) {
        switch(byteIndex) {
          is(0) {
            wdata \= hitData(31 downto 16) ## data(15 downto 0)
          }
          is(2) {
            wdata \= data(31 downto 16) ## hitData(15 downto 0)
          }
        }
      }
      is(2) {
        wdata \= data
      }
    }
    lines(lineIndex).datas(wordIndex) := wdata
  }
}