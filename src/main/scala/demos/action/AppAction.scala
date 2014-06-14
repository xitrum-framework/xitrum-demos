package demos.action

import xitrum.Action

trait AppAction extends Action {
  override def layout = {
    // Get file path of the current action by creating an exception.
    // This is slow but OK for demos.
    //
    // stack(0) is AppAction, stack(1) is current extended Action
    val stack = new RuntimeException("").getStackTrace()
    at("currentActionFileName") = stack(1).getFileName()
    renderViewNoLayout[AppAction]()
  }
}
