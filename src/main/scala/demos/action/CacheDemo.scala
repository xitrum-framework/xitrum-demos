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
      <ul>
        <li>
          In production mode, this action is cached 10 seconds.
          (<b>System.currentTimeMillis()</b> below will not change in 10 seconds.)
        </li>
        <li>For action cache, filters are run. Please try refreshing and see the log.</li>
        <li>System.currentTimeMillis(): {System.currentTimeMillis()}</li>
      </ul>
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
      <ul>
        <li>
          In production mode, this page is cached 10 seconds.
          (<b>System.currentTimeMillis()</b> below will not change in 10 seconds).
        </li>
        <li>For page cache, filters are NOT run. Please try refreshing and see the log.</li>
        <li>System.currentTimeMillis(): {System.currentTimeMillis()}</li>
      </ul>
    )
  }
}
