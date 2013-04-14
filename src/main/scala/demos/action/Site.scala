package demos.action

import xitrum.ActionActor
import xitrum.annotation.GET

@GET("")
class SiteIndex extends AppAction {
  def execute() {
    respondView()
  }
}

@GET("forward")
class ForwardDemo extends AppAction {
  def execute() {
    forwardTo[ForwardedDemo]()
  }
}

@GET("forwarded")
class ForwardedDemo extends AppAction {
  def execute() {
    respondInlineView("This action could have been forwarded from ForwardDemo")
  }
}
