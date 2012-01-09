package quickstart.controller

object Errors extends Errors

class Errors extends AppController {
  val error404 = indirectAction {
    respondInlineView("This is custom 404 page")
  }

  val error500 = indirectAction {
    respondInlineView("This is custom 500 page")
  }
}
