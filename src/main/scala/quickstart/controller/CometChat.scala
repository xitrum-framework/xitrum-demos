package quickstart.controller

object CometChat extends CometChat

class CometChat extends AppController {
  def index = GET("comet_chat") {
    respondView()
  }
}
