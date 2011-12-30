package quickstart.action

import xitrum.Action
import xitrum.view.DocType

trait AppAction extends Action {
  override def layout = DocType.html5(
    <html>
      <head>
        {antiCSRFMeta}
        {xitrumCSS}

        <meta content="text/html; charset=utf-8" http-equiv="content-type" />
        <title>Welcome to Xitrum</title>

        <link type="image/vnd.microsoft.icon" rel="shortcut icon" href={urlForPublic("../favicon.ico")} />

        <link type="text/css" rel="stylesheet" media="all" href={urlForPublic("css/960/reset.css")} />
        <link type="text/css" rel="stylesheet" media="all" href={urlForPublic("css/960/text.css")} />
        <link type="text/css" rel="stylesheet" media="all" href={urlForPublic("css/960/960.css")} />
        <link type="text/css" rel="stylesheet" media="all" href={urlForPublic("css/app.css")} />
      </head>
      <body>
        <div class="container_12">
          <h1><a href={urlFor[IndexAction]}>Welcome to Xitrum</a></h1>

          <div class="grid_8">
            <p>{sourceCodeLink}</p>
            <div id="flash">{jsFlash}</div>
            {renderedView}
          </div>

          <div class="grid_4">
            <h3>Samples</h3>
            <ul>
              <li><a href={urlFor[CometAction]}>Comet chat</a></li>
              <li><a href={urlFor[BoringAction]}>Boring workflow</a></li>
              <li><a href={urlFor[GreeterAction]}>Greeter workflow</a></li>
              <li><a href={urlFor[TodoAction]}>Todo list</a></li>
            </ul>
          </div>
        </div>
        {jsAtBottom}
      </body>
    </html>
  )

  private def sourceCodeLink = {
    val fullClassName = getClass.getName
    val className     = fullClassName.split("\\.").last
    val desc          = "Source code of %s.scala".format(className)
    val href          = "https://github.com/ngocdaothanh/xitrum-quickstart/tree/master/src/main/scala/" + fullClassName.replace(".", "/") + ".scala"
    <a href={href}>{desc}</a>
  }
}
