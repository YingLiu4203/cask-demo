package app.hello

import app.db.{dbSetup, embeddedPg}

// import app.hello.uapi.{ApiRoute, UIRoute}
import app.hello.uapi.UIRoute

object App extends cask.Main {

  embeddedPg.start()
  // dbService.createTableIfNotExists()
  // val allRoutes = Seq(UIRoute(), ApiRoute())
  val allRoutes = Seq(UIRoute())
}
