package quickstart.controller

object AfterFilter extends AfterFilter

class AfterFilter extends AppController {
  val index = GET("after_filter") {
    respondInlineView("After filter should have been run, please check the log")
  }

  afterFilter {
    logger.info("Run at " + System.currentTimeMillis())
  }
}
