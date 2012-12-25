package demos.controller

object AfterFilterController extends AfterFilterController

class AfterFilterController extends AppController {
  def index = GET("filter/after") {
    respondInlineView("After filter should have been run, please check the log")
  }

  afterFilter {
    logger.info("Run at " + System.currentTimeMillis())
  }
}
