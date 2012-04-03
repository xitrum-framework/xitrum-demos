package demos.controller

object BeforeFilter extends BeforeFilter

class BeforeFilter extends AppController {
  beforeFilter {
    logger.info("I run therefore I am")
    true
  }

  // This method is run after the above filters
  def index = GET("filter/before") {
    respondInlineView("Before filters should have been run, please check the log")
  }
}
