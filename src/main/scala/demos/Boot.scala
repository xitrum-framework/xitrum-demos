package demos

import xitrum.handler.Server
import xitrum.routing.Routes

import demos.controller.Errors

object Boot {
  def main(args: Array[String]) {
    Routes.error = classOf[Errors]
    Server.start()
  }
}
