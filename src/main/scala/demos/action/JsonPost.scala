package demos.action

import io.netty.handler.codec.http.HttpMethod
import xitrum.annotation.{GET, POST}

case class MyClass(x: Int, y: Int)

@GET("json")
@POST("json/:demo")
class JsonPost extends AppAction {
  def execute() {
    if (request.getMethod == HttpMethod.POST) {
      param("demo") match {
        case "1" =>
          // You can parse the JSON as a Map like this:
          val map = requestContentJson[Map[String, Int]]
          respondText(map)

        case _ =>
          // Or convert it to a (case) class instance
          val myClass = requestContentJson[MyClass]
          respondText(myClass)
      }
    } else {
      respondView()
    }
  }
}
