package quickstart.controller

import scala.collection.mutable.ArrayBuffer
import xitrum.RequestVar
import xitrum.validator.Required

// Controller ------------------------------------------------------------------

// Request vars for passing data from action to Scalate view
object RVArticle  extends RequestVar[Article]
object RVArticles extends RequestVar[Seq[Article]]

// For use in AppController.jade
object Articles extends Articles

class Articles extends AppController {
  pathPrefix = "articles"

  val index = GET {
    val articles = Article.findAll()
    RVArticles.set(articles)
    respondView()
  }

  val show = GET(":id") {
    val id      = param[Int]("id")
    var article = Article.find(id)
    RVArticle.set(article)
    respondView()
  }

  // "first" for this route to have higher routing priority than "show" above
  val niw = first.GET("new") {
    val article = new Article()
    RVArticle.set(article)
    respondView()
  }

  val create = POST {
    val title   = param("title")
    val body    = param("body")
    val article = Article(title = title, body = body)
    article.v match {
      case None =>
        val id = Article.insert(article)
        flash("Article has been saved")
        redirectTo(show, "id" -> id)
      case Some(msg) =>
        RVArticle.set(article)
        flash(msg)
        respondView(niw)
    }
  }

  val edit = GET(":id/edit") {
    val id      = param[Int]("id")
    var article = Article.find(id)
    RVArticle.set(article)
    respondView()
  }

  val update = PUT(":id") {
    val id      = param[Int]("id")
    val title   = param("title")
    val body    = param("body")
    val article = Article(id, title, body)
    article.v match {
      case None =>
        Article.update(article)
        flash("Article has been saved")
        redirectTo(show, "id" -> id)
      case Some(msg) =>
        RVArticle.set(article)
        flash(msg)
        respondView(edit)
    }
  }

  val destroy = DELETE(":id") {
    val id = param[Int]("id")
    Article.delete(id)
    flash("Article has been deleted")
    redirectTo(index)
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
