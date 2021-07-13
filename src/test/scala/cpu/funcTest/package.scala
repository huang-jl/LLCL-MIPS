package cpu

import cpu.confreg._
import cpu.display._

package object funcTest {
  val baseAddr = 0x1faf0000

  case class ConfRegs() {
    val cr = (0 until 8) map { i => Register(baseAddr | 0x8000 | (4 * i)) }

    val switch    = Switch(baseAddr | 0xf020, Some(baseAddr | 0xf02c))
    val ioSimu    = IOSimu(baseAddr | 0xffec)
    val openTrace = OpenTrace(baseAddr | 0xfff8)
    val simuFlag  = Constant(baseAddr | 0xfff4, value = 1)

    val timer = Timer(baseAddr | 0xe000)

    val led    = RepaintingRegister(baseAddr | 0xf000)
    val ledRg0 = RepaintingRegister(baseAddr | 0xf004)
    val ledRg1 = RepaintingRegister(baseAddr | 0xf008)
    val num    = RepaintingRegister(baseAddr | 0xf010)

    val textUart = TextUART(baseAddr | 0xfff0)

    def addTo(simulator: Simulator, displayerConfig: Option[DisplayerConfig] = None) = {
      for (reg <- cr) {
        simulator.addPlugin(reg)
      }

      simulator
        .addPlugin(switch)
        .addPlugin(ioSimu)
        .addPlugin(openTrace)
        .addPlugin(simuFlag)
        .addPlugin(timer)
        .addPlugin(textUart)

      for (config <- displayerConfig) {
        simulator
          .addPlugin(led)
          .addPlugin(ledRg0)
          .addPlugin(ledRg1)
          .addPlugin(num)
          .addPlugin(new DisplayerPlugin(config) {
            override def paint() = {
              val ledString    = confreg.printLed(led.value)
              val ledRg0String = confreg.printLedRg(ledRg0.value)
              val ledRg1String = confreg.printLedRg(ledRg1.value)
              val numString    = confreg.printNum(num.value)

              println(s"|  $ledString  |  $ledRg0String $ledRg1String  |  $numString  |")
            }
          })
      }
    }
  }
}
