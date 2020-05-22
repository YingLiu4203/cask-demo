package app.hello

import zio.{Runtime, URIO, ZIO}

import app.db.{dbSetup, embeddedPg}

import app.hello.uapi.{ApiRoute, UIRoute, Util}

object App extends cask.Main {

  embeddedPg.start()
  createTableIfNotExists()

  val allRoutes = Seq(UIRoute(), ApiRoute())

  private def createTableIfNotExists() = {
    val run = dbSetup.createTableIfNotExists().provideLayer(Util.dbContextLayer)
    Runtime.default.unsafeRun(run)
  }
}
