package quickstart.action

import xitrum.annotation.GET

@GET("/")
class IndexAction extends AppAction {
  override def execute {
    renderView(
      <p>See:</p>
      <ul>
        <li>Xitrum: <a href="https://github.com/ngocdaothanh/xitrum">https://github.com/ngocdaothanh/xitrum</a></li>
        <li>This quickstart: <a href="https://github.com/ngocdaothanh/xitrum-quickstart">https://github.com/ngocdaothanh/xitrum-quickstart</a></li>
      </ul>
    )
  }
}
