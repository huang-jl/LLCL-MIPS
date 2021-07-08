package cpu

import cpu.confreg._

package object funcTest {
  val baseAddr = 0x1faf0000

  val FuncTestConfRegs = Seq(
    Switch(baseAddr | 0xf020, Some(baseAddr | 0xf02c)),
    IOSimu(baseAddr | 0xffec),
    OpenTrace(baseAddr | 0xfff8)
  )
}
