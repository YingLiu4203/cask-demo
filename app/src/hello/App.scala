package app.hello

import zio.{Runtime, URIO, ZIO}

import app.db.{dbSetup, embeddedPg}

import app.hello.uapi.{ApiRoute, UIRoute, Util}

import th.logz.StrictLog

object MyApp extends cask.Main with StrictLog {

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
