package demos.action

import akka.actor.{Actor, ActorRef, Props, Terminated}
import glokka.Registry

import xitrum.{Config, Log, SockJsAction, SockJsText, WebSocketAction, WebSocketText, WebSocketBinary, WebSocketPing, WebSocketPong}
import xitrum.annotation.{GET, SOCKJS, WEBSOCKET}

@GET("sockJsChatDemo")
class SockJsChat extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}

@GET("websocketChatDemo")
class WebSocketChat extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}

@SOCKJS("sockJsChat")
class SockJsChatActor extends SockJsAction with LookupOrCreateChatRoom {
  def execute(): Unit = {
    joinChatRoom()
  }

  def onJoinChatRoom(chatRoom: ActorRef): Receive = {
    case SockJsText(msg) =>
      chatRoom ! ChatRoom.Msg(msg)

    case ChatRoom.Msg(body) =>
      respondSockJsText(body)
  }
}

@WEBSOCKET("websocketChat")
class WebSocketChatActor extends WebSocketAction with LookupOrCreateChatRoom {
  def execute(): Unit = {
    joinChatRoom()
  }

  def onJoinChatRoom(chatRoom: ActorRef): Receive = {
    case WebSocketText(msg) =>
      chatRoom ! ChatRoom.Msg(msg)

    case ChatRoom.Msg(body) =>
      respondWebSocketText(body)

    case WebSocketBinary(bytes) =>
    case WebSocketPing =>
    case WebSocketPong =>
  }
}

//------------------------------------------------------------------------------
// See https://github.com/xitrum-framework/glokka

object ChatRoom {
  val MAX_MSGS   = 20
  val ROOM_NAME  = "chatRoom"
  val PROXY_NAME = "chatRoomProxy"

  // Subscribers:
  // * To join a chat room, send Join
  // * When there's a chat message, receive Msg
  case object Join
  case class  Msg(body: String)

  // Registry is used for looking up chat room actor by name.
  // For simplicity, this demo uses only one chat room (lobby chat room).
  // If you want many chat rooms, create more chat rooms with different names.
  val registry: ActorRef = Registry.start(Config.actorSystem, PROXY_NAME)
}

/** Subclasses should implement onJoinChatRoom and call joinChatRoom. */
trait LookupOrCreateChatRoom {
  this: Actor =>

  import ChatRoom._

  def onJoinChatRoom(chatRoom: ActorRef): Actor.Receive

  def joinChatRoom(): Unit = {
    registry ! Registry.Register(ROOM_NAME, Props[ChatRoom]())
    context.become(waitForRegisterResult)
  }

  private def waitForRegisterResult: Actor.Receive = {
    case msg: Registry.FoundOrCreated =>
      val chatRoom = msg.ref
      chatRoom ! Join
      context.become(onJoinChatRoom(chatRoom))
  }
}

class ChatRoom extends Actor with Log {
  import ChatRoom._

  private var subscribers = Seq.empty[ActorRef]
  private var msgs        = Seq.empty[String]

  def receive: Receive = {
    case Join =>
      val subscriber = sender()
      subscribers = subscribers :+ subscriber
      context.watch(subscriber)
      msgs foreach (subscriber ! Msg(_))
      log.debug("Joined chat room: " + subscriber)

    case m @ Msg(body) =>
      msgs = msgs :+ body
      if (msgs.length > MAX_MSGS) msgs = msgs.drop(1)
      subscribers foreach (_ ! m)

    case Terminated(subscriber) =>
      subscribers = subscribers.filterNot(_ == subscriber)
      log.debug("Left chat room: " + subscriber)
  }
}
