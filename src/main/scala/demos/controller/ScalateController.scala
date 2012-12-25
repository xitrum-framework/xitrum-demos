package demos.controller

object ScalateController extends ScalateController

class ScalateController extends AppController {
  def fromDB = GET("fromDB") {
    val template = "p This Jade template is loaded from DB"
    val string   = renderJadeString(template)
    respondInlineView(string)
  }

  def mustache = GET("mustache") {
    at("name")        = "Chris"
    at("value")       = 10000
    at("taxed_value") = 10000 - (10000 * 0.4)
    at("in_ca")       = true
    respondView("mustache")
  }
}
