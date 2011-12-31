package quickstart.action

import xitrum.annotation.GET

@GET("/before_filter")
class BeforeFilterAction extends AppAction {
  val myFilter = { () =>
    logger.info("Run at " + System.currentTimeMillis)
    true
  }

  // Add this filter
  beforeFilter(myFilter)

  // Filter can be anonymous
  // This filter is run after the above
  beforeFilter { () =>
    logger.info("I run therefore I am")
    true
  }

  // This method is run after the above filters
  override def execute {
    renderView("Before filters should have been run, please check the log")
  }
}
