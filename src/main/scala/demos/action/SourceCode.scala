package demos.action

import scala.util.control.NonFatal

import javassist.{ClassPool, ClassClassPath}
import xitrum.Config.xitrum.cache
import xitrum.util.Loader

object SourceCode {
  private val classPool = {
    val ret       = ClassPool.getDefault
    val classPath = new ClassClassPath(getClass)
    ret.insertClassPath(classPath)
    ret
  }

  def getSourceFileName(className: String) = {
    cache.get(className) match {
      case Some(fileName) =>
        fileName

      case None =>
        val ctClass   = classPool.get(className)
        val classFile = ctClass.getClassFile
        val fileName  = classFile.getSourceFile
        cache.put(className, fileName)
        fileName
    }
  }

  def getFileContent(path: String) = {
    cache.get(path) match {
      case Some(content) =>
        content.toString

      case None =>
        try {
          val content = Loader.stringFromFile(path)
          cache.put(path, content)
          content
        } catch {
          case NonFatal(_) =>
            "Could not load file: " + path
        }
    }
  }
}
