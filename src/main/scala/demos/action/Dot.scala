package demos.action

import xitrum.annotation.GET

@GET("articles/:id<[0-9]+>.:format")
class ArticlesDotShow extends AppAction {
  def execute() {
    val id     = param[Int]("id")
    val format = param("format")
    respondInlineView(
      <code>
        URL = {absUrl[ArticlesDotShow]("id" -> 1, "format" -> "foo")}<br />
        id = {id}<br />
        format = {format}
      </code>
    )
  }
}
