package demos.action

import xitrum.Action

trait AppAction extends Action {
  override def layout = renderViewNoLayout(classOf[AppAction])
}
