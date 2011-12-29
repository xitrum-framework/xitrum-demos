package quickstart.action

import scala.xml.NodeBuffer
import xitrum.imperatively.ImperativelyXitrum
import xitrum.imperatively.SessionHolder
import xitrum.annotation.GET

@GET("/boring")
class BoringAction extends AppAction {

  val boring = new Boring

  override def execute {
    SessionHolder.set(session)
    val params: Map[String, String] = textParams.filterNot(x => x._2.isEmpty).map(x => (x._1, x._2.head)).toMap
    renderView(boring.nextStep(params))
  }
}

class Boring extends ImperativelyXitrum {

  def workflow(): NodeBuffer @imp = {
    <h1>Boring</h1>
    <div>That was not very interesting.</div>
  }
}
