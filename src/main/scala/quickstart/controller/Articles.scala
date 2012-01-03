package quickstart.controller

object Articles extends Articles

class Articles extends AppController {
  pathPrefix = "articles"

  val index = GET() {
    renderView()
  }

  val show = GET(":id") {
    renderView()
  }

  // Set "first" for this route to have higher matching priority than "show" above
  val niw = first.GET("new") {
    renderView()
  }

  val create = POST() {
    renderView(niw)
  }

  val edit = GET(":id/edit") {
    renderView()
  }

  val update = PUT(":id") {
    renderView(edit)
  }

  val destroy = DELETE(":id") {
    redirectTo(index)
  }
}
