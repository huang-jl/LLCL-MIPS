package util

import com.typesafe.config.ConfigFactory

import java.io.File

object VivadoConf {
  lazy val config = ConfigFactory.parseFile(new File("vivado.conf"))

  def vivadoPath = config.getString("vivado.path")
}
