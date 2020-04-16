package app.hello

import app.hello.uapi.{ApiRoute, UIRoute}

object App extends cask.Main {
  val allRoutes = Seq(UIRoute(), ApiRoute())
}
