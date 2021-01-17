package demos.action

import xitrum.annotation.GET

@GET("")
class SiteIndex extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}
