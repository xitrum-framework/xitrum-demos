package demos.action

import xitrum.Action

trait AppAction extends Action {
  override def layout = {
    // stack(0) is AppAction, stack(1) is current extended Action
    val stack = new RuntimeException("").getStackTrace()
    at("currentActionFileName") = stack(1).getFileName()
    renderViewNoLayout(classOf[AppAction])
  }
}
