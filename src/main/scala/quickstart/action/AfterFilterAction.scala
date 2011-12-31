package quickstart.action

import xitrum.annotation.GET

@GET("/after_filter")
class AfterFilterAction extends AppAction {
  override def execute {
    renderView("After filter should have been run, please check the log")
  }

  afterFilter { () =>
    logger.info("Run at " + System.currentTimeMillis)
  }
}
