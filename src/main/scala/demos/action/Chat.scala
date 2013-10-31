package demos.action

import akka.actor.Actor

import xitrum.{SockJsActor, SockJsText, WebSocketActor, WebSocketText, WebSocketBinary, WebSocketPing, WebSocketPong}
import xitrum.annotation.{GET, SOCKJS, WEBSOCKET}

@GET("sockJsChatDemo")
class SockJsChat extends AppAction {
  def execute() {
    respondView()
  }
}

@GET("websocketChatDemo")
class WebSocketChat extends AppAction {
  def execute() {
    respondView()
  }
}

@SOCKJS("sockJsChat")
class SockJsChatActor extends SockJsActor {
  def execute() {
    context.become {
      case SockJsText(msg) =>
        respondSockJsText(msg)
    }
  }
}

@WEBSOCKET("websocketChat")
class WebSocketChatActor extends WebSocketActor {
  def execute() {
    context.become {
      case WebSocketText(msg) =>
        respondWebSocketText(msg)

      case WebSocketBinary(bytes) =>
      case WebSocketPing =>
      case WebSocketPong =>
    }
  }
}
