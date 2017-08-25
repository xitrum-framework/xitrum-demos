package demos

import xitrum.Server

object Boot {
  def main(args: Array[String]) {
    Server.start()
    Server.stopAtShutdown()
  }
}
