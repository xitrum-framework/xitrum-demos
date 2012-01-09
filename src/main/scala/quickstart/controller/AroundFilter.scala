package quickstart.controller

object AroundFilter extends AroundFilter

class AroundFilter extends AppController {
  aroundFilter { action =>
    val begin = System.currentTimeMillis()
    action()
    val end   = System.currentTimeMillis()
    logger.info("The action took " + (end - begin) + " [ms]")
  }

  def index = GET("filter/around") {
    respondInlineView("Around filter should have been run, please check the log")
  }
}
