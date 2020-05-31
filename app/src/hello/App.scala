package app.hello

import com.typesafe.scalalogging.{Logger, LazyLogging}

import zio.{Runtime, URIO, ZIO}

import app.db.{dbSetup, embeddedPg}

import app.hello.uapi.{ApiRoute, UIRoute, Util}

object MyApp extends cask.Main with LazyLogging {

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
