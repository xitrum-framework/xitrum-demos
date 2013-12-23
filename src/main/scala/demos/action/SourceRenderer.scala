package demos.action

import scala.util.control.NonFatal
import xitrum.util.Loader

object SourceRenderer {
  def render(path: String) = {
    val src =
      try {
        Loader.stringFromFile(path)
      } catch {
        case NonFatal(_) => "Could not load file: " + path
      }
    src
  }
}
