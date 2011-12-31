package quickstart.action

import xitrum.Action

trait AppAction extends Action {
  override def layout = renderScalateTemplateToString(classOf[AppAction])

  /** Used in layout */
  def sourceCodeLink = {
    val fullClassName = getClass.getName
    val className     = fullClassName.split("\\.").last
    val desc          = "Source code of %s.scala".format(className)
    val href          = "https://github.com/ngocdaothanh/xitrum-quickstart/tree/master/src/main/scala/" + fullClassName.replace(".", "/") + ".scala"
    <a href={href}>{desc}</a>
  }
}
