package app.hello

import th.logz.LogZ

import app.db.dbContext
import app.db.dbService

import com.typesafe.scalalogging.{Logger => Slog}

object Layers {

  val slog = Slog(Layers.getClass)

  val logLayer = LogZ.live

  val dbContextLayer = {
    slog.debug("build dbContextLayer")
    logLayer >>> dbContext.embeddedPg
  }
  val dbLayers = {
    slog.debug("build dbLayers")
    logLayer ++ (dbContextLayer >>> dbService.pgService)
  }
}
