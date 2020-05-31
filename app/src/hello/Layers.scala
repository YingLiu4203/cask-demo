package app.hello

import th.logz.LogZ

import app.db.DbService

import com.typesafe.scalalogging.{Logger => Slog}

object Layers {

  val slog = Slog(Layers.getClass)

  val dbLayers = {
    slog.debug("build dbLayers")
    LogZ.live ++ DbService.pgService
  }
}
