package demos.action

import xitrum.Action
import xitrum.Config.xitrum.metrics

trait AppAction extends Action {
  override def layout: String = {
    at("metricsViewerApiKey") = metrics.get.apiKey
    renderViewNoLayout[AppAction]()
  }
}
