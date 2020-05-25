import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill.Agg
import mill.scalalib.{DepSyntax, ScalaModule}

object app extends ScalaModule {

  def scalaVersion = "2.13.1"

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.6.0",
    ivy"io.getquill::quill-jdbc:3.5.1",
    ivy"org.postgresql:postgresql:42.2.12",
    ivy"com.opentable.components:otj-pg-embedded:0.13.3",
    ivy"com.lihaoyi::scalatags:0.8.6",
    ivy"dev.zio::zio:1.0.0-RC19-2",
    ivy"com.lihaoyi::geny:0.6.0"
  )

  object test extends Tests {
    def testFrameworks = Seq("utest.runner.Framework")

    def ivyDeps = Agg(
      ivy"com.lihaoyi::utest::0.7.4",
      ivy"com.lihaoyi::requests::0.5.2"
    )
  }
}
