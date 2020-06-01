package app.hello

import th.logz.LogZ

import app.db.DbService

import com.tersesystems.blindsight.LoggerFactory

object Layers {

  val logger = LoggerFactory.getLogger

  val dbLayers = {
    logger.debug("build dbLayers")
    LogZ.live ++ DbService.pgService
  }
}
