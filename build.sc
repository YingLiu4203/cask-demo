import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

import mill.scalalib.ScalaModule

import $ivy.`com.lihaoyi::mill-contrib-playlib:$MILL_VERSION`
import mill.playlib.Static

import $file.builds.ModuleDefs
import ModuleDefs.{ScalaDefs, ScalaJsDefs}

import $file.builds.WebJarJs
import WebJarJs.WebJarJsModule

object app extends ScalaModule with Static {

  def scalaVersion = ScalaDefs.ScalaVersion
  def ivyDeps = ScalaDefs.IvyDeps

  object apiTest extends Tests {
    def testFrameworks = ScalaDefs.TestFrameworks
    def ivyDeps = ScalaDefs.TestIvyDeps
  }

  // to make it runnable and be consistent with assembly output
  // 1) In app source code, put all js/css files in : [[millSourcePath / assetsPath()]], it is app/public
  // 2) create a runPublicAssets Task that copy all files in staticAssets and webJarResources to "T.dest/../.." folder
  // 3) call runPublicAssets from forkWorkingDir and set it to "T.dest/../../
  // 4) in code, resolve the statci assets to public folder, webjars in public/lib folder. This is consistent to the assembly output

  def runWorkingDir = T { T.dest / os.up / os.up }

  def runPublicAssets() = T.command {
    val runPublic = runWorkingDir() / assetsPath()

    val publicStatic = staticAssets().path / assetsPath()
    if (os.exists(publicStatic)) {
      os.list(publicStatic)
        .map(path => os.copy.over(path, runPublic / path.last))
    }

    val webJarPublic = webJarResources().path / assetsPath()
    if (os.exists(webJarPublic))
      os.list(webJarPublic)
        .map(path => os.copy.over(path, runPublic / path.last))

    PathRef(T.dest)
  }

  override def forkWorkingDir = T {
    runPublicAssets()
    runWorkingDir()
  }
}

object appJs extends WebJarJsModule {
  def scalaVersion = ScalaDefs.ScalaVersion
  def scalaJSVersion = ScalaJsDefs.ScalaJSVersion
  def ivyDeps = ScalaJsDefs.IvyDeps

  object test extends WebJarJsTests {

    def testFrameworks = ScalaDefs.TestFrameworks

    import mill.scalajslib.api.JsEnvConfig
    def jsEnvConfig: T[JsEnvConfig] = T { JsEnvConfig.JsDom() }

    def ivyDeps = ScalaJsDefs.TestIvyDeps
  }
}
