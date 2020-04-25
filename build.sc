import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

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

  object appJs extends WebJarJsModule {
    def scalaVersion = ScalaDefs.ScalaVersion
    def scalaJSVersion = ScalaJsDefs.ScalaJSVersion

    def ivyDeps = ScalaJsDefs.IvyDeps

    object test extends WebJarJsTests {
      def testFrameworks = ScalaDefs.TestFrameworks
    }
  }
}
