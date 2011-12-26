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

        var escaped = $('<div/>').text(body.chatInput[0]).html();
        $('#chatOutput').append('- ' + escaped + '<br />');

        if (wasScrollAtBottom) xitrum.scrollToBottom('#chatOutput');
      }
    """)

    renderView(
      <h2>Chat</h2>
      <div id="chatOutput"></div>
      <form data-postback="submit" action={urlForPostback[CometPublishAction]} data-after="function() { $('#chatInput').attr('value', '') }">
        {<input type="hidden" name="channel" value="chat" /> :: Validated}
        {<input type="text" id="chatInput" name="chatInput" /> :: Required}
      </form>
    )
  }
}
