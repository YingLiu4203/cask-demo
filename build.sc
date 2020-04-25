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

  // // hacking, do not work after assembly
  // by default os.walk skip symlinks therefore we can use this trick
  // to run the app. This task is shared by both run and runBackground but not assembly
  override def forkWorkingDir = T {

    val publicFolder = os.Path(assetsPath(), millSourcePath)
    if (!os.exists(publicFolder)) os.makeDir(publicFolder)

    val symlinkFrom = publicFolder / "lib"
    if (os.exists(symlinkFrom)) os.remove(symlinkFrom)

    val webJarLib = os.Path(assetsPath(), webJarResources().path) / "lib"
    os.symlink(symlinkFrom, webJarLib)
    super.forkWorkingDir()
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
