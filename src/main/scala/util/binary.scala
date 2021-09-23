package util

package object binary {
  implicit class BigIntWithBinaryRepr(b: BigInt) {

    /** 转为小端序的定长字节数组 */
    def toBinary(length: Int): Array[Byte] = {
      val bytes  = b.toByteArray
      val padded = Array.fill[Byte](length - bytes.length)(0) ++ bytes
      padded.reverseIterator.take(length).toArray
    }
  }
}
