package demos.action

import io.netty.handler.codec.http.HttpResponseStatus
import xitrum.{Action, SkipCsrfCheck}
import xitrum.annotation.{GET, POST, PATCH, DELETE, Swagger}

@Swagger(
  Swagger.Resource("articles", "Operations about articles"),
  Swagger.Produces("application/json")
)
trait Api extends Action with SkipCsrfCheck {
  beforeFilter {
    val apiKey  = param("api_key")
    if (apiKey != "123") {
      response.setStatus(HttpResponseStatus.UNAUTHORIZED)
      respondJson(Map("error" -> """Incorrect API key (please use "123" as api_key)"""))
    }
  }
}

@GET("api/articles")
@Swagger(
  Swagger.Nickname("index"),
  Swagger.Summary("List all articles")
)
class ApiArticlesIndex extends Api {
  def execute() {
    val articles = Article.findAll()
    respondJson(articles)
  }
}

@GET("api/articles/:id<[0-9]+>")
@Swagger(
  Swagger.Nickname("show"),
  Swagger.Summary("Show an article"),
  Swagger.IntPath("id", "ID of the article")
)
class ApiArticlesShow extends Api {
  def execute() {
    val id      = param[Int]("id")
    var article = Article.find(id)
    respondJson(article)
  }
}

@POST("api/articles")
@Swagger(
  Swagger.Nickname("create"),
  Swagger.Summary("Create a new article"),
  Swagger.StringForm("title"),
  Swagger.StringForm("content"),
  Swagger.Response(200, "ID of the newly created article will be returned")
)
class ApiArticlesCreate extends Api {
  def execute() {
    val title   = param("title")
    val content = param("content")
    val article = Article(title = title, content = content)
    article.validationMessage match {
      case None =>
        val id = Article.insert(article)
        respondJson(Map("id" -> id))
      case Some(msg) =>
        respondJson(Map("error" -> msg))
    }
  }
}

@PATCH("api/articles/:id")
@Swagger(
  Swagger.Nickname("update"),
  Swagger.Summary("Modify an article"),
  Swagger.IntPath("id", "ID of the article to modify"),
  Swagger.StringForm("title", "New title"),
  Swagger.StringForm("content", "New content")
)
class ApiArticlesUpdate extends Api {
  def execute() {
    val id      = param[Int]("id")
    val title   = param("title")
    val content = param("content")
    val article = Article(id, title, content)
    article.validationMessage match {
      case None =>
        Article.update(article)
        respondJson(Map("id" -> id))
      case Some(msg) =>
        respondJson(Map("error" -> msg))
    }
  }
}

@DELETE("api/articles/:id")
@Swagger(
  Swagger.Nickname("destroy"),
  Swagger.Summary("Delete an article"),
  Swagger.IntPath("id")
)
class ApiArticlesDestroy extends Api {
  def execute() {
    val id = param[Int]("id")
    Article.delete(id)
    respondJson(Map("id" -> id))
  }
}

@GET("swagger")
class SwaggerDemo extends AppAction {
  def execute() {
    respondView()
  }
}
