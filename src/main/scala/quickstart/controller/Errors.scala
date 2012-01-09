package quickstart.controller

import xitrum.ErrorController

class Errors extends AppController with ErrorController {
  def error404 = errorAction {
    respondInlineView("This is custom 404 page")
  }

  def error500 = errorAction {
    respondInlineView("This is custom 500 page")
  }
}
