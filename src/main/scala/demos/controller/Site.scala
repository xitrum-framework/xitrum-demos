package demos.controller

object Site extends Site

class Site extends AppController {
  def index = GET {
    respondView()
  }

  def mustache = GET("mustache") {
    at("name")        = "Chris"
    at("value")       = 10000
    at("taxed_value") = 10000 - (10000 * 0.4)
    at("in_ca")       = true
    respondView("mustache")
  }
}
