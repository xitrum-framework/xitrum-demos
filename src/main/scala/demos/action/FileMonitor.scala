package demos.action

import java.io.{BufferedWriter, FileWriter}
import java.nio.file.{Path, Paths}

import xitrum.annotation.{GET, SOCKJS}
import xitrum.Config.xitrum.tmpDir
import xitrum.SockJsAction
import xitrum.util.{FileMonitor => xFileMonitor}

@GET("fileMonitor")
class FileMonitor extends AppAction {
  def execute() {
    respondView()
  }
}

@SOCKJS("fileMonitorSocket")
class FileMonitorSocket extends SockJsAction {
  def execute() {
    def respondToClient(body: String) {
      respondSockJsText(body)
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
      respondToClient(modifiedFile.toString)

      // Delete file
      Thread.sleep(1000)
      modifiedFile.toFile.delete()
    }

    def createCB(unmonitorPath: Path): (Path => Unit) = { createdFilePath: Path =>
      respondToClient(s"[Created]: $createdFilePath")
      xFileMonitor.unmonitor(xFileMonitor.CREATE, unmonitorPath)

      // Start monitoring modify event on created file.
      xFileMonitor.monitor(xFileMonitor.MODIFY, createdFilePath, modifyCB(createdFilePath))
      respondToClient("[Registered]: File modification monitor is registered to:")
      respondToClient(createdFilePath.toString)

      // Modify file
      Thread.sleep(1000)
      val writer = new BufferedWriter(new FileWriter(createdFilePath.toFile))
      writer.write("There's text in here wee!!")
      writer.close()
    }

    val targetDirPath  = tmpDir.toPath.toAbsolutePath
    val targetFilePath = Paths.get(targetDirPath + "/created_" + System.currentTimeMillis()).toAbsolutePath
    // Stop current monitor if exists
    xFileMonitor.unmonitor(xFileMonitor.CREATE, targetDirPath)

    // Start monitoring create event in config dir
    xFileMonitor.monitor(xFileMonitor.CREATE, targetDirPath, createCB(targetDirPath))
    respondToClient("[Registered]: File creation monitor is registered to:")
    respondToClient(targetDirPath.toString)

    // Create File
    Thread.sleep(1000)
    targetFilePath.toFile.createNewFile()
  }
}
