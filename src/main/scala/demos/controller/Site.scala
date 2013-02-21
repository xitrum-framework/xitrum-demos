package demos.controller

object Site extends Site

class Site extends AppController {
  def index = GET {
    respondView()
  }

  def forward = GET("forward") {
    forwardTo(forwarded)
  }

  def forwarded = GET("forwarded") {
    respondInlineView("This action could have been forwarded from Site#forward")
  }
}
