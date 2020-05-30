package app.hello

import th.logz.LogZ

import app.db.dbContext
import app.db.dbService

object Layers {

  val logLayer = LogZ.live

  val dbContextLayer = logLayer >>> dbContext.embeddedPg
  val dbLayers = logLayer ++ (dbContextLayer >>> dbService.pgService)
}
