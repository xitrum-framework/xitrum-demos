package quickstart.action

import xitrum.annotation.GET

@GET("/around_filter")
class AroundFilterAction extends AppAction {
  aroundFilter { executeOrPostback =>
    val begin = System.currentTimeMillis
    executeOrPostback()
    val end   = System.currentTimeMillis
    logger.info("The action took " + (end - begin) + " [ms]")
  }

  override def execute {
    renderView("Around filter should have been run, please check the log")
  }
}
