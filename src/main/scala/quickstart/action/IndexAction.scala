package quickstart.action

import xitrum.annotation.GET

@GET("/")
class IndexAction extends AppAction {
  override def execute {
    renderScalateView()
  }
}
