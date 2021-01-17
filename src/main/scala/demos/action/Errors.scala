package demos.action

import xitrum.annotation.{Error404, Error500}

@Error404
class NotFoundError extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}

@Error500
class ServerError extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}
