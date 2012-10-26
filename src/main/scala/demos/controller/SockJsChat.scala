package demos.controller

import scala.collection.mutable.{Map => MMap}
import xitrum.SockJsHandler
import xitrum.sockjs.{MessageQueue, QueueMessage}

object SockJsChatHandler extends SockJsChatHandler
class SockJsChatHandler extends SockJsHandler {
  private val TOPIC = "chat"

  private val listener = (messages: Seq[QueueMessage]) => {
    messages.foreach { message =>
      send(message.body)
    }

    // Return false for MessageQueue not to automatically unsubscribe this listener.
    // With SockJS sendMessage can be called to send SockJS frames many times.
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
}

object SockJsChat extends SockJsChat
class SockJsChat extends AppController {
  def index = GET("sockjs_chat") {
    respondView()
  }
}
