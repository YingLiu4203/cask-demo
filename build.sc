import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

import mill.Module
import mill.scalalib.ScalaModule

import $ivy.`com.lihaoyi::mill-contrib-playlib:$MILL_VERSION`
import mill.playlib.Static

import $file.builds.ModuleDefs
import ModuleDefs.{ScalaDefs, ScalaJsDefs}

import $file.builds.WebJar
import WebJar.RunWebJar

import $file.builds.WebJarJs
import WebJarJs.WebJarJsModule

object app extends ScalaModule with RunWebJar {

  def scalaVersion = ScalaDefs.ScalaVersion
  def ivyDeps = ScalaDefs.IvyDeps

  object test extends Tests {
    def testFrameworks = ScalaDefs.TestFrameworks
    def ivyDeps = ScalaDefs.TestIvyDeps
  }
}

object appJs extends WebJarJsModule {

  def scalaVersion = ScalaDefs.ScalaVersion
  def scalaJSVersion = ScalaJsDefs.ScalaJSVersion

  def ivyDeps = ScalaJsDefs.IvyDeps

  object test extends WebJarJsTests {
    def testFrameworks = ScalaDefs.TestFrameworks
  }
}

object ci extends Module {
  def mododuleDeps = Seq(app, appJs)

  // copy js fullOpt to
  def sync = T {
    val runAssetsPath = app.runPublicAssets().path
    val jsFullOptPath = appJs.fullOpt().path

    os.copy.over(jsFullOptPath, runAssetsPath / "js" / jsFullOptPath.last)
    PathRef(T.dest)
  }

  // to stop, have to clean the original runBackground
  def runBackground(args: String*) = T.command {
    sync()
    app.runBackground(args: _*)()
  }

  def stopBackground() = T.command {
    os.proc("mill", "clean", "ci.runBackground", "app.runBackground").call()
  }
}
