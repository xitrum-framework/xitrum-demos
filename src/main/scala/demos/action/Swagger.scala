package demos.action

import io.netty.handler.codec.http.HttpResponseStatus
import xitrum.{Action, SkipCsrfCheck}
import xitrum.annotation.{GET, POST, PATCH, DELETE, Swagger}

@Swagger(
  Swagger.Tags("Operations about articles"),
  Swagger.Produces("application/json")
)
trait Api extends Action with SkipCsrfCheck

@GET("api/articles")
@Swagger(
  Swagger.Summary("List all articles")
)
class ApiArticlesIndex extends Api {
  def execute(): Unit = {
    val articles = Article.findAll()
    respondJson(articles)
  }
}

@GET("api/articles/:id<[0-9]+>")
@Swagger(
  Swagger.Summary("Show an article"),
  Swagger.IntPath("id", "ID of the article")
)
class ApiArticlesShow extends Api {
  def execute(): Unit = {
    val id = param[Int]("id")
    Article.find(id) match {
      case None =>
        response.setStatus(HttpResponseStatus.NOT_FOUND)
        respondText("Article not found")

      case Some(article) =>
        respondJson(article)
    }
  }
}

@POST("api/articles")
@Swagger(
  Swagger.Summary("Create a new article"),
  Swagger.Consumes("application/x-www-form-urlencoded"),
  Swagger.StringForm("title"),
  Swagger.StringForm("content"),
  Swagger.Response(200, "ID of the newly created article will be returned")
)
class ApiArticlesCreate extends Api {
  def execute(): Unit = {
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
  Swagger.Summary("Modify an article"),
  Swagger.IntPath("id", "ID of the article to modify"),
  Swagger.Consumes("application/x-www-form-urlencoded"),
  Swagger.StringForm("title", "New title"),
  Swagger.StringForm("content", "New content")
)
class ApiArticlesUpdate extends Api {
  def execute(): Unit = {
    val id      = param[Int]("id")
    val title   = param("title")
    val content = param("content")
    val article = Article(id, title, content)
    article.validationMessage match {
      case None =>
        Article.update(article)
        respondJson(Map("id" -> id))

      case Some(msg) =>
        response.setStatus(HttpResponseStatus.BAD_REQUEST)
        respondJson(Map("error" -> msg))
    }
  }
}

@DELETE("api/articles/:id")
@Swagger(
  Swagger.Summary("Delete an article"),
  Swagger.IntPath("id")
)
class ApiArticlesDestroy extends Api {
  def execute(): Unit = {
    val id = param[Int]("id")
    if (id == 1) {
      response.setStatus(HttpResponseStatus.BAD_REQUEST)
      respondJson(Map("error" -> "This article is for demo, can't be deleted"))
    } else {
      Article.delete(id)
      respondJson(Map("id" -> id))
    }
  }
}

@GET("swagger")
class SwaggerDemo extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}
