package demos.action

import scala.collection.mutable.ArrayBuffer

import xitrum.RequestVar
import xitrum.annotation.{First, GET, POST, PUT, DELETE}
import xitrum.validator.Required

// Actions ---------------------------------------------------------------------

// Request vars for passing data from action to Scalate view
object RVArticle  extends RequestVar[Article]
object RVArticles extends RequestVar[Seq[Article]]

@GET("articles")
class ArticlesIndex extends AppAction {
  def execute() {
    val articles = Article.findAll()
    RVArticles.set(articles)
    respondView()
  }
}

@GET("articles/:id<[0-9]+>")
class ArticlesShow extends AppAction {
  def execute() {
    val id      = param[Int]("id")
    var article = Article.find(id)
    RVArticle.set(article)
    respondView()
  }
}

@First  // This route has higher priority than "ArticlesShow" above
@GET("articles/new")
class ArticlesNew extends AppAction {
  def execute() {
    val article = new Article()
    RVArticle.set(article)
    respondView()
  }
}

@POST("articles")
class ArticlesCreate extends AppAction {
  def execute() {
    val title   = param("title")
    val body    = param("body")
    val article = Article(title = title, body = body)
    article.v match {
      case None =>
        val id = Article.insert(article)
        flash("Article has been saved")
        redirectTo[ArticlesShow]("id" -> id)
      case Some(msg) =>
        RVArticle.set(article)
        flash(msg)
        respondView(classOf[ArticlesNew])
    }
  }
}

@GET("articles/:id/edit")
class ArticlesEdit extends AppAction {
  def execute() {
    val id      = param[Int]("id")
    var article = Article.find(id)
    RVArticle.set(article)
    respondView()
  }
}

@PUT("articles/:id")
class ArticlesUpdate extends AppAction {
  def execute() {
    val id      = param[Int]("id")
    val title   = param("title")
    val body    = param("body")
    val article = Article(id, title, body)
    article.v match {
      case None =>
        Article.update(article)
        flash("Article has been saved")
        redirectTo[ArticlesShow]("id" -> id)
      case Some(msg) =>
        RVArticle.set(article)
        flash(msg)
        respondView(classOf[ArticlesEdit])
    }
  }
}

@DELETE("articles/:id")
class ArticlesDestroy extends AppAction {
  def execute() {
    val id = param[Int]("id")
    Article.delete(id)
    flash("Article has been deleted")
    redirectTo[ArticlesIndex]()
  }
}

// Model -----------------------------------------------------------------------

case class Article(id: Int = 0, title: String = "", body: String = "") {
  // Returns Some(error message) or None
  def v =
    Required.v("Title", title) orElse
    Required.v("Body",  body)
}

object Article {
  val storage = ArrayBuffer[Article]()
  insert(Article(1, "Title 1", "Body 1"))
  insert(Article(2, "Title 2", "Body 2"))

  def findAll() = storage

  def find(id: Int) = storage(id - 1)

  def insert(article: Article): Int = synchronized {
    val article2 = Article(storage.length + 1, article.title, article.body)
    storage.append(article2)
    article2.id
  }

  def update(article: Article) = synchronized {
    storage(article.id - 1) = article
  }

  def delete(id: Int) = synchronized {
    storage.remove(id - 1)
  }
}
