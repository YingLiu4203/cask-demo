import $ivy.`com.lihaoyi::mill-contrib-playlib:$MILL_VERSION`
import mill.playlib.Static

import mill.scalajslib.ScalaJSModule
import mill.api.PathRef

import $file.ModuleDefs
import ModuleDefs.ScalaJsDefs

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
    * @param outputFile the output file path, usually [[T.des/"out.js"]]
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
    val allContent = allFiles.map(os.read).mkString(";\n")

    // throw an exception if an error occurs
    os.write(outputFile, allContent)
  }

  def fastOpt = T {
    val taskFile = super.fastOpt().path
    val outputFile = T.dest / taskFile.last

    mergeWebJarJs(outputFile, publicSources(), taskFile)
    PathRef(outputFile)
  }

  def fullOpt = T {
    val taskFile = super.fullOpt().path
    val outputFile = T.dest / taskFile.last

    mergeWebJarJs(outputFile, publicSources(), taskFile)
    PathRef(outputFile)
  }

  trait WebJarJsTests extends super.Tests {
    def fastOptTest = T {
      val taskFile = super.fastOptTest().path
      val outputFile = T.ctx.dest / taskFile.last

      outer.mergeWebJarJs(outputFile, outer.publicSources(), taskFile)
      PathRef(outputFile)
    }

    import mill.scalajslib.api.JsEnvConfig
    def jsEnvConfig: T[JsEnvConfig] = T { JsEnvConfig.JsDom() }

    def ivyDeps = ScalaJsDefs.TestIvyDeps
  }
}
