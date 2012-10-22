package demos.controller

import scala.collection.mutable.{Map => MMap}
import xitrum.{Controller, SockJsHandler}
import xitrum.sockjs.{MessageQueue, QueueMessage}

class SockJsChatHandler extends SockJsHandler {
  private val TOPIC = "chat"

  private val listener = (messages: Seq[QueueMessage]) => {
    messages.foreach { message =>
      sendMessage(message.body)
    }

    // Return false for Comet not to automatically unsubscribe this listener.
    // With SockJS sendMessage can be called to send SockJS frames many times.
    false
  }

  def onOpen(controller: Controller) {
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
