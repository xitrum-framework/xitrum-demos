package demos.action

import akka.actor.Actor

import xitrum.{SockJsActor, SockJsText, WebSocketActor, WebSocketText, WebSocketBinary, WebSocketPing, WebSocketPong}
import xitrum.annotation.{GET, SOCKJS, WEBSOCKET}
import xitrum.mq.{MessageQueue, QueueMessage}

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

case class MsgsFromQueue(msgs: Seq[String])

trait MessageQueueListener {
  this: Actor =>

  protected val TOPIC = "chat"

  protected val listener = (messages: Seq[QueueMessage]) => {
    val msgs = messages.map(_.body.toString)
    self ! MsgsFromQueue(msgs)

    // Tell MessageQueue not to unsubscribe this listener
    false
  }

  override def postStop() {
    MessageQueue.unsubscribe(TOPIC, listener)
  }
}

@SOCKJS("sockJsChat")
class SockJsChatActor extends SockJsActor with MessageQueueListener {
  def execute() {
    MessageQueue.subscribe(TOPIC, listener, 0)
    context.become {
      case MsgsFromQueue(msgs) =>
        msgs.foreach { msg => respondSockJsText(msg) }

      case SockJsText(text) =>
        MessageQueue.publish(TOPIC, text)
    }
  }
}

@WEBSOCKET("websocketChat")
class WebSocketChatActor extends WebSocketActor with MessageQueueListener {
  def execute() {
    MessageQueue.subscribe(TOPIC, listener, 0)
    context.become {
      case MsgsFromQueue(msgs) =>
        msgs.foreach { msg => respondWebSocketText(msg) }

      case WebSocketText(text) =>
        MessageQueue.publish(TOPIC, text)

      case WebSocketBinary(bytes) =>
      case WebSocketPing =>
      case WebSocketPong =>
    }
  }
}
