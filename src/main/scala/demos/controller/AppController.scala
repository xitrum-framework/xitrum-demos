package demos.controller

import xitrum.Controller

trait AppController extends Controller {
  override def layout = renderViewNoLayout(classOf[AppController])

  /** Used in layout */
  def sourceCodeLink = {
    val fullClassName = getClass.getName
    val className     = fullClassName.split("\\.").last
    val desc          = "Source code of %s.scala".format(className)
    val href          = "https://github.com/ngocdaothanh/xitrum-demos/tree/master/src/main/scala/" + fullClassName.replace(".", "/") + ".scala"
    <a href={href}>{desc}</a>
  }
}
