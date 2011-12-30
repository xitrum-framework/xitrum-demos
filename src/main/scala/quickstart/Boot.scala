package quickstart

import xitrum.handler.Server
import xitrum.routing.Routes

object Boot {
  def main(args: Array[String]) {
    Routes.fromCacheFileOrAnnotations()
    Server.start()
  }
}
