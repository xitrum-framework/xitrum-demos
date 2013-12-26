package demos.action

import akka.actor.{Actor, ActorRef, Props, Terminated}
import glokka.Registry

import xitrum.{Config, Log, SockJsActor, SockJsText, WebSocketActor, WebSocketText, WebSocketBinary, WebSocketPing, WebSocketPong}
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
class SockJsChatActor extends SockJsActor with LookupOrCreateChatRoom {
  def execute() {
    joinChatRoom()
  }

  def onJoinChatRoom(chatRoom: ActorRef) = {
    case SockJsText(msg) =>
      chatRoom ! ChatRoom.Msg(msg)

    case ChatRoom.Msg(body) =>
      respondSockJsText(body)
  }
}

@WEBSOCKET("websocketChat")
class WebSocketChatActor extends WebSocketActor with LookupOrCreateChatRoom {
  def execute() {
    joinChatRoom()
  }

  def onJoinChatRoom(chatRoom: ActorRef) = {
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
// See https://github.com/ngocdaothanh/glokka

object ChatRoom {
  val MAX_MSGS   = 20
  val ROOM_NAME  = "chatRoom"
  val PROXY_NAME = "chatRoomProxy"

  case object Join
  case class  Msg(body: String)

  val registry = Registry.start(Config.actorSystem, PROXY_NAME)
}

trait LookupOrCreateChatRoom {
  this: Actor =>

  import ChatRoom._

  def onJoinChatRoom(chatRoom: ActorRef): Actor.Receive

  def joinChatRoom() {
    registry ! Registry.LookupOrCreate(ROOM_NAME)
    context.become(waitForLookupResult)
  }

  private def waitForLookupResult(): Actor.Receive = {
    case Registry.LookupResultOk(_, chatRoom) =>
      chatRoom ! Join
      context.become(onJoinChatRoom(chatRoom))

    case Registry.LookupResultNone(_) =>
      val tmpChatRoom = Config.actorSystem.actorOf(Props[ChatRoom])
      registry ! Registry.Register(ROOM_NAME, tmpChatRoom)
      context.become(waitForRegisterResult(tmpChatRoom))
  }

  private def waitForRegisterResult(tmpChatRoom: ActorRef): Actor.Receive = {
    case Registry.RegisterResultOk(_, chatRoom) =>
      chatRoom ! Join
      context.become(onJoinChatRoom(chatRoom))

    case Registry.RegisterResultConflict(_, chatRoom) =>
      Config.actorSystem.stop(tmpChatRoom)
      chatRoom ! Join
      context.become(onJoinChatRoom(chatRoom))
  }
}

class ChatRoom extends Actor with Log {
  import ChatRoom._

  private var subscribers = Seq.empty[ActorRef]
  private var msgs        = Seq.empty[String]

  def receive = {
    case Join =>
      val subscriber = sender
      subscribers = subscribers :+ sender
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
