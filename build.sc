import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill.Agg
import mill.scalalib.{DepSyntax, ScalaModule}

object app extends ScalaModule {

  def scalaVersion = "2.13.1"

  import coursier.maven.MavenRepository

  def repositories = super.repositories ++ Seq(
    MavenRepository(
      "https://dl.bintray.com/tersesystems/maven"
    )
  )

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.6.0",
    ivy"io.getquill::quill-jdbc:3.5.1",
    ivy"org.postgresql:postgresql:42.2.12",
    ivy"com.opentable.components:otj-pg-embedded:0.13.3",
    ivy"com.lihaoyi::scalatags:0.8.6",
    ivy"dev.zio::zio:1.0.0-RC20",
    ivy"com.lihaoyi::geny:0.6.0",
    ivy"com.typesafe.scala-logging::scala-logging:3.9.2",
    ivy"com.tersesystems.blindsight::blindsight-logstash:1.0.1",
    ivy"ch.qos.logback:logback-classic:1.2.3"
  )

  def moduleDeps = Seq(logz)

  object test extends Tests {
    def testFrameworks = Seq("utest.runner.Framework")

    def ivyDeps = Agg(
      ivy"com.lihaoyi::utest::0.7.4",
      ivy"com.lihaoyi::requests::0.5.2"
    )
  }
}

object logz extends ScalaModule {

  def scalaVersion = "2.13.1"

  import coursier.maven.MavenRepository

  def repositories = super.repositories ++ Seq(
    MavenRepository(
      "https://dl.bintray.com/tersesystems/maven"
    )
  )

  def ivyDeps = Agg(
    ivy"dev.zio::zio:1.0.0-RC20",
    ivy"org.slf4j:slf4j-api:1.7.30",
    ivy"com.tersesystems.blindsight::blindsight-logstash:1.0.1"
  )
}
