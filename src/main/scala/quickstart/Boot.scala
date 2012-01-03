package quickstart

import xitrum.handler.Server
import xitrum.routing.Routes

import quickstart.controller.Errors

object Boot {
  def main(args: Array[String]) {
    Routes.fromCacheFileOrRecollect()
    Routes.error404 = Errors.error404
    Routes.error500 = Errors.error500
    Server.start()
  }
}
