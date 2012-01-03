package quickstart.controller

object CometChat extends CometChat

class CometChat extends AppController {
  val index = GET("/comet_chat") {
    renderView()
  }
}
