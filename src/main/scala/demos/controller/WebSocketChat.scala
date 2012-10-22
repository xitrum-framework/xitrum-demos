package demos.controller

import scala.collection.mutable.{Map => MMap}
import xitrum.sockjs.{MessageQueue, QueueMessage}

object WebSocketChat extends WebSocketChat

class WebSocketChat extends AppController {
  private val TOPIC = "chat"

  def index = GET("websocket_chat") {
    respondView()
  }

  def webSocketEntry = WEBSOCKET("websocket_chat") {
    acceptWebSocket(new WebSocketHandler() {
      private val listener = (messages: Seq[QueueMessage]) => {
        messages.foreach { message =>
          respondWebSocket(message.body)
        }

        // Return false for Comet not to automatically unsubscribe this listener.
        // With WebSocket respondWebSocket can be called to send WebSocket frames many times.
        false
      }

      def onOpen() {
        MessageQueue.subscribe(TOPIC, listener, 0)
      }

      def onMessage(message: String) {
        MessageQueue.publish(TOPIC, message)
      }

      def onClose() {
        MessageQueue.unsubscribe(TOPIC, listener)
      }
    })
  }
}
