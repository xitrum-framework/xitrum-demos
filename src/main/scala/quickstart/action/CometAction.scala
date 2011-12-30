package quickstart.action

import xitrum.annotation.GET
import xitrum.comet.CometPublishAction
import xitrum.validation.{Required, Validated}

@GET("/comet")
class CometAction extends AppAction {
  override def execute {
    jsCometGet("chat", """
      function(channel, timestamp, body) {
        var wasScrollAtBottom = xitrum.isScrollAtBottom('#chatOutput');
        $('#chatOutput').append('- ' + xitrum.escapeHtml(body.chatInput[0]) + '<br />');
        if (wasScrollAtBottom) xitrum.scrollToBottom('#chatOutput');
      }
    """)

    renderView(
      <h1>Chat</h1>
      <div id="chatOutput"></div>
      <form data-postback="submit" action={urlForPostback[CometPublishAction]} data-after="function() { $('#chatInput').attr('value', '') }">
        {<input type="hidden" name="channel" value="chat" /> :: Validated}
        {<input type="text" id="chatInput" name="chatInput" /> :: Required}
      </form>
    )
  }
}
