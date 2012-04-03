package demos.controller

object AfterFilter extends AfterFilter

class AfterFilter extends AppController {
  def index = GET("filter/after") {
    respondInlineView("After filter should have been run, please check the log")
  }

  afterFilter {
    logger.info("Run at " + System.currentTimeMillis())
  }
}
