package demos.action

import scala.concurrent.duration._

import xitrum.ActionActor
import xitrum.annotation.GET

@GET("actor")
class ActionActorDemo extends ActionActor with AppAction {
  def execute() {
    // See Akka doc about scheduler
    import context.dispatcher
    context.system.scheduler.scheduleOnce(3.seconds, self, System.currentTimeMillis)

    // See Akka doc about "become"
    context.become {
      case pastTime =>
        respondInlineView("It's " + pastTime + " Unix ms 3s ago.")
    }
  }
}
