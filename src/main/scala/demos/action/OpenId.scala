package demos.action

import scala.util.control.NonFatal

import org.openid4java.consumer.ConsumerManager
import org.openid4java.discovery.DiscoveryInformation
import org.openid4java.message.ParameterList

import xitrum.annotation.{GET, POST}

// See https://code.google.com/p/openid4java/wiki/QuickStart
//
// OpenID4Java dependency has been added to build.sbt:
// libraryDependencies += "org.openid4java" % "openid4java" % "0.9.8"

object OpenId {
  val manager     = new ConsumerManager
  val SESSION_KEY = "openIdDiscovered"
}

@GET("openid/login")
class OpenIdLogin extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}

@POST("openid/redirect")
class OpenIdRedirect extends AppAction {
  def execute(): Unit = {
    try {
      val openId         = param("openId")
      val discoveries    = OpenId.manager.discover(openId)
      val discovered     = OpenId.manager.associate(discoveries)
      val returnUrl      = absUrl[OpenIdVerify]  // Must be http(s)://...
      val authReq        = OpenId.manager.authenticate(discovered, returnUrl)
      val destinationUrl = authReq.getDestinationUrl(true)

      session(OpenId.SESSION_KEY) = discovered
      redirectTo(destinationUrl)
    } catch {
      case NonFatal(e) =>
        log.warn("OpenID error", e)
        flash("Could not redirect to remote OpenID provider. Your OpenID may be wrong.")
        redirectTo[OpenIdLogin]()
    }
  }
}

@GET("openid/verify")
class OpenIdVerify extends AppAction {
  def execute(): Unit = {
    try {
      val queryString  = request.uri.substring(request.uri.indexOf("?") + 1)
      val openIdResp   = ParameterList.createFromQueryString(queryString)
      val discovered   = session(OpenId.SESSION_KEY).asInstanceOf[DiscoveryInformation]
      val receivingUrl = absUrl[OpenIdVerify] + "?" + queryString
      val verification = OpenId.manager.verify(receivingUrl, openIdResp, discovered)
      val verifiedId   = verification.getVerifiedId

      session.clear()
      if (verifiedId != null)
        flash("OpenID login success: " + verifiedId)
      else
        flash("OpenID login failed")
    } catch {
      case NonFatal(e) =>
        log.warn("OpenID error", e)
        flash("OpenID login failed")
    }
    redirectTo[OpenIdLogin]()
  }
}
