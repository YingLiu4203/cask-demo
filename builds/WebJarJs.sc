import $ivy.`com.lihaoyi::mill-contrib-playlib:$MILL_VERSION`
import mill.playlib.Static

import mill.scalajslib.ScalaJSModule
import mill.api.PathRef

trait WebJarJsModule extends ScalaJSModule with Static { outer =>

  // the public sources defined by [[Static]]
  private def publicSources = T.sources {
    os.Path(assetsPath(), webJarResources().path) / "lib"
  }

  private def libFiles(libPathRefs: Seq[PathRef], pattern: String) = {
    val libPaths = libPathRefs.map(_.path)
    val libFiles =
      libPaths
        .flatMap(os.walk(_))
        .filter(path => os.isFile(path) && path.last.matches(pattern))
    libFiles
  }

  /** Prepend the WebJar Js libs to the overriden output file
    *
    * @param outputFile the output file path, usually [[T.des/"output.js"]]
    * @param libPathRefs the path of WebJar libs
    * @param overriden the overriden output file
    */
  private def mergeWebJarJs(
      outputFile: os.Path,
      libPathRefs: Seq[PathRef],
      overriden: os.Path
  ): Unit = {
    val jsFiles = libFiles(libPathRefs, "[^\\.]*\\.min.js$")
    val allFiles = jsFiles :+ overriden
    val allContent = allFiles.map(os.read).mkString(";")

    // throw an exception if an error occurs
    os.write(outputFile, allContent)
  }

  def fastOpt = T {
    val outputFile = T.dest / "out.js"
    val taskFile = super.fastOpt().path

    // that's the playlib Static resource
    val libPath = os.Path(assetsPath(), webJarResources().path) / "lib"

    mergeWebJarJs(outputFile, publicSources(), taskFile)
    PathRef(outputFile)
  }

  def fullOpt = T {
    val outputFile = T.dest / "out.min.js"
    val taskFile = super.fullOpt().path

    mergeWebJarJs(outputFile, publicSources(), taskFile)
    PathRef(outputFile)
  }

  trait WebJarJsTests extends super.Tests {
    def fastOptTest = T {
      val taskFile = super.fastOptTest().path
      val outputFile = T.ctx.dest / "out.js"

      outer.mergeWebJarJs(outputFile, outer.publicSources(), taskFile)
      PathRef(outputFile)
    }
  }
}
