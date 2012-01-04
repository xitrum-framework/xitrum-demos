package quickstart.controller

object Site extends Site

class Site extends AppController {
  val index = GET {
    respondView()
  }
}
