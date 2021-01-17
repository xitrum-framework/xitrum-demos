package demos.action

import xitrum.annotation.{GET, POST}

@GET("get_post", "get_post_another_route")
@POST("get_post")
class GetPost extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}
