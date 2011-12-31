package quickstart.action

import xitrum.annotation.GET

@GET("/comet")
class CometAction extends AppAction {
  override def execute {
    renderScalateView()
  }
}
