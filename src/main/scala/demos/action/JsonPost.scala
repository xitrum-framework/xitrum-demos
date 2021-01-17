package demos.action

import io.netty.handler.codec.http.HttpMethod
import xitrum.annotation.{GET, POST}
import xitrum.util.SeriDeseri

case class MyClass(x: Int, y: Int)

@GET("json")
@POST("json/:demo")
class JsonPost extends AppAction {
  def execute(): Unit = {
    if (request.method == HttpMethod.POST) {
      param("demo") match {
        case "1" =>
          // You can parse the JSON as a Map like this:
          val map = SeriDeseri.fromJValue[Map[String, Int]](requestContentJValue)
          respondText(map)

        case _ =>
          // Or convert it to a (case) class instance
          val myClass = SeriDeseri.fromJValue[MyClass](requestContentJValue)
          respondText(myClass)
      }
    } else {
      respondView()
    }
  }
}
