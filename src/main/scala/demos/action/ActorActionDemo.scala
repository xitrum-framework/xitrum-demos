package demos.action

import scala.concurrent.duration._

import xitrum.ActorAction
import xitrum.annotation.GET

@GET("actor")
class ActorActionDemo extends ActorAction with AppAction {
  def execute(): Unit = {
    // See Akka doc about scheduler
    context.system.scheduler.scheduleOnce(3.seconds, self, System.currentTimeMillis)

    // See Akka doc about "become"
    context.become {
      case pastTime =>
        respondInlineView("It's " + pastTime + " Unix ms 3s ago.")
    }
  }
}
