package demos

import xitrum.handler.Server
import xitrum.routing.Routes

import demos.controller.{Errors, SockJsChatHandler}

object Boot {
  def main(args: Array[String]) {
    Routes.sockJs(classOf[SockJsChatHandler], "sockjs_chat_handler", websocket=true, cookieNeeded=false)
    Routes.error = classOf[Errors]
    Server.start()
  }
}
