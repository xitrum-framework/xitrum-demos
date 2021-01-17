package demos.action

import java.io.{BufferedWriter, FileWriter}
import java.nio.file.{Path, Paths}

import xitrum.annotation.{GET, SOCKJS}
import xitrum.Config.xitrum.tmpDir
import xitrum.SockJsAction
import xitrum.util.FileMonitor

@GET("fileMonitor")
class FileMonitorEvents extends AppAction {
  def execute(): Unit = {
    respondView()
  }
}

@SOCKJS("fileMonitorSocket")
class FileMonitorEventsSocket extends SockJsAction {
  def execute(): Unit = {
    val targetDirPath  = tmpDir.toPath.toAbsolutePath
    val targetFilePath = Paths.get(targetDirPath.toString + "/created_" + System.currentTimeMillis()).toAbsolutePath

    // Start monitoring create event in config dir
    FileMonitor.monitor(targetDirPath) { (event, path, stop) =>
      event match {
        case FileMonitor.CREATE => onCreate(path)
        case FileMonitor.MODIFY => onModify(path)
        case FileMonitor.DELETE => onDelete(path, stop)
      }
    }

    respondSockJsText(s"[Registered]: File monitor is registered to: $targetDirPath")

    // Kick off file create event
    Thread.sleep(1000)
    targetFilePath.toFile.createNewFile()

    def onCreate(path: Path): Unit = {
      respondSockJsText(s"[Created]: $path")

      // Kick off file modify event
      Thread.sleep(1000)
      val writer = new BufferedWriter(new FileWriter(path.toFile))
      writer.write("There's text in here wee!!")
      writer.close()
    }

    def onModify(path: Path): Unit = {
      respondSockJsText(s"[Modified]: $path")

      // Kick off file delete event
      Thread.sleep(1000)
      path.toFile.delete()
    }

    def onDelete(path: Path, stop: FileMonitor.Stop): Unit = {
      respondSockJsText(s"[Deleted]: $path")
      stop()
    }
  }
}
