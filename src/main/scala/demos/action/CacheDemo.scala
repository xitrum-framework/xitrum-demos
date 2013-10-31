package demos.action

import xitrum.annotation.{CacheActionSecond, CachePageSecond, GET}

@CacheActionSecond(10)
trait TenSecsCacheAction extends AppAction

@GET("cache/action")
class ActionCacheDemo extends TenSecsCacheAction {
  beforeFilter {
    log.info("Filter is run")
    true
  }

  def execute() {
    respondInlineView(
      "In production mode, this action is cached 10 seconds. " +
      "For action cache, filters are run. Please try refreshing and see the log. " +
      "System.currentTimeMillis(): " + System.currentTimeMillis()
    )
  }
}

@GET("cache/page")
@CachePageSecond(10)
class PageCacheDemo extends AppAction {
  beforeFilter {
    log.info("Filter is run")
    true
  }

  def execute() {
    respondInlineView(
      "In production mode, this page is cached 10 seconds. " +
      "For page cache, filters are NOT run. Please try refreshing and see the log. " +
      "System.currentTimeMillis(): " + System.currentTimeMillis()
    )
  }
}