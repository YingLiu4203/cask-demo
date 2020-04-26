import mill.Agg
import mill.scalalib.DepSyntax

object Common {
  def IvyTestFramework = ivy"com.lihaoyi::utest::0.7.4"
  def IvyLog =
    ivy"org.apache.logging.log4j:log4j-slf4j-impl:2.13.2" // for slf4j
}

object ScalaDefs {

  def ScalaVersion = "2.13.1"
  def IvyDeps = Agg(
    ivy"com.lihaoyi::cask:0.5.7",
    ivy"io.getquill::quill-jdbc:3.5.1",
    ivy"org.postgresql:postgresql:42.2.12",
    ivy"com.opentable.components:otj-pg-embedded:0.13.3",
    ivy"com.lihaoyi::scalatags:0.8.6",
    ivy"org.webjars:bootstrap:4.4.1-1",
    Common.IvyLog
  )

  def TestIvyDeps = Agg(
    Common.IvyTestFramework,
    ivy"com.lihaoyi::requests::0.5.2",
    Common.IvyLog
  )

  def TestFrameworks = Seq("utest.runner.Framework")
}

object ScalaJsDefs {
  def ScalaJSVersion = "1.0.1"
  def IvyDeps = Agg(
    ivy"com.lihaoyi::scalatags_sjs1:0.9.0",
    ivy"org.webjars:jquery:3.4.1",
    ivy"org.scala-js:scalajs-dom_sjs1_2.13:1.0.0",
    ivy"io.udash::udash-jquery_sjs1:3.0.4",
    ivy"com.lihaoyi:ujson_sjs1_2.13:1.1.0"
  )

  def TestIvyDeps = IvyDeps ++ Agg(
    Common.IvyTestFramework,
    ivy"org.scala-js::scalajs-env-jsdom-nodejs:1.0.0"
  )
}
