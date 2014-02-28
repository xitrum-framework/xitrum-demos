package demos.action

import java.io.BufferedWriter
import java.io.FileWriter
import java.nio.file.Path

import xitrum.WebSocketAction
import xitrum.annotation.{GET, WEBSOCKET}
import xitrum.util.{FileMonitor => xFileMonitor}

@GET("fileMonitor")
class FileMonitor extends AppAction {
  def execute() {
    respondView()
  }
}

@WEBSOCKET("fileMonitor")
class FileMonitorSocket extends WebSocketAction {
  def execute() {
    def respondToClient(body: Any) {
      respondWebSocketText(body)
    }

    def deleteCB(unmonitorPath: Path): (Path => Unit) = { deletedFilePath: Path =>
      respondToClient(s"[Deleted]: $deletedFilePath")
      xFileMonitor.unmonitor(xFileMonitor.DELETE, unmonitorPath)
    }

    def modifyCB(unmonitorPath:Path): (Path => Unit) = { modifiedFile: Path =>
      respondToClient(s"[Modified]: $modifiedFile")
      xFileMonitor.unmonitor(xFileMonitor.MODIFY, unmonitorPath)

      // Start monitoring delete event on modified file
      xFileMonitor.monitor(xFileMonitor.DELETE, modifiedFile, deleteCB(modifiedFile))
      respondToClient("[Registered]: File delete monitor is registered to:")
      respondToClient(modifiedFile)

      // Delete file
      modifiedFile.toFile.delete()
    }

    def createCB(unmonitorPath: Path): (Path => Unit) = { createdFilePath: Path =>
      respondToClient(s"[Created]: $createdFilePath")
      xFileMonitor.unmonitor(xFileMonitor.CREATE, unmonitorPath)

      // Start monitoring modify event on created file.
      xFileMonitor.monitor(xFileMonitor.MODIFY, createdFilePath, modifyCB(createdFilePath))
      respondToClient("[Registered]: File modification monitor is registered to:")
      respondToClient(createdFilePath)

      // Modify file
      val writer = new BufferedWriter(new FileWriter(createdFilePath.toFile))
      writer.write("There's text in here wee!!")
      writer.close()
    }

    val targetDirPath  = xFileMonitor.pathFromString("config")
    val targetFilePath = xFileMonitor.pathFromString("config/created_"+System.currentTimeMillis())
    // Stop current monitor if exists
    xFileMonitor.unmonitor(xFileMonitor.CREATE, targetDirPath)

    // Start monitoring create event in config dir
    xFileMonitor.monitor(xFileMonitor.CREATE, targetDirPath, createCB(targetDirPath))
    respondToClient("[Registered]: File creation monitor is registered to:")
    respondToClient(targetDirPath)

    // Create File
    targetFilePath.toFile.createNewFile()
  }
}
