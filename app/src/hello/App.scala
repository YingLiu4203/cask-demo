package app.hello

import app.db.{dbService, embeddedPg}

import app.hello.uapi.{ApiRoute, UIRoute}

object App extends cask.Main {

  embeddedPg.start()
  dbService.createTableIfNotExists()

  val allRoutes = Seq(UIRoute(), ApiRoute())
}
