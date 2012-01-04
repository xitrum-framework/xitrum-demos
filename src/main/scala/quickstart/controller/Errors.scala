package quickstart.controller

object Errors extends Errors

class Errors extends AppController {
  val error404 = indirectRoute {
    respondInlineView("This is custom 404 page")
  }

  val error500 = indirectRoute {
    respondInlineView("This is custom 500 page")
  }
}
