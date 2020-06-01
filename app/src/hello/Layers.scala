package app.hello

import app.db.DbService

object Layers {
  val dbLayers = DbService.pgService
}
