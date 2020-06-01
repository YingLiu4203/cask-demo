package app.hello

import com.tersesystems.blindsight.LoggerFactory

import zio.{Runtime, URIO, ZIO}

import app.db.{dbSetup, embeddedPg}

import app.hello.uapi.{ApiRoute, UIRoute, Util}

object MyApp extends cask.Main {

  val logger = LoggerFactory.getLogger

  embeddedPg.start()
  logger.info("Embedded Pg started.")

  createTableIfNotExists()
  logger.debug("Tables are created if not exists.")

  val allRoutes = Seq(UIRoute(), ApiRoute())

  private def createTableIfNotExists() = {
    val run =
      dbSetup.createTableIfNotExists()
    Runtime.default.unsafeRun(run)
  }
}
