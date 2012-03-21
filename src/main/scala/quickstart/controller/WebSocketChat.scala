package quickstart.controller

import scala.collection.mutable.ListBuffer
import xitrum.comet.{Comet, CometMessage}
import xitrum.scope.request.Params

object WebSocketChat extends WebSocketChat

class WebSocketChat extends AppController {
  private val TOPIC = "chat"

  def index = GET("websocket_chat") {
    respondView()
  }

  def webSocketEntry = WEBSOCKET("websocket_chat") {
    acceptWebSocket(new WebSocketHandler() {
      val listener = (messages: Seq[CometMessage]) => {
        messages.foreach { message =>
          respondWebSocket(message.body("chatInput").head)
        }

        // Return false for Comet not to automatically unsubscribe this listener.
        // With WebSocket the response can be sent many times.
        false
      }

      def onOpen() {
        Comet.subscribe(TOPIC, listener, 0)
      }

      def onMessage(text: String) {
        Comet.publish(TOPIC, scala.collection.mutable.Map("chatInput" -> List(text)))
      }

      def onClose() {
        Comet.unsubscribe(TOPIC, listener)
      }
    })
  }
}
