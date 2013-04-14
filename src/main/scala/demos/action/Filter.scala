package demos.action

import xitrum.annotation.GET

@GET("filter/before")
class BeforeFilter extends AppAction {
  beforeFilter {
    logger.info("I run therefore I am")
    true
  }

  // This method is run after the above filters
  def execute() {
    respondInlineView("Before filters should have been run, please check the log")
  }
}

@GET("filter/after")
class AfterFilter extends AppAction {
  afterFilter {
    logger.info("Run at " + System.currentTimeMillis())
  }

  def execute() {
    respondInlineView("After filter should have been run, please check the log")
  }
}

@GET("filter/around")
class AroundFilter extends AppAction {
  aroundFilter { action =>
    val begin = System.currentTimeMillis()
    action()
    val end   = System.currentTimeMillis()
    logger.info("The action took " + (end - begin) + " [ms]")
  }

  def execute()  {
    respondInlineView("Around filter should have been run, please check the log")
  }
}
