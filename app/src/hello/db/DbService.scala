package app.db

import zio.{Has, UIO, URLayer, URIO, ZIO, ZLayer}

import app.db.dbContext.{DbContext, PgContext}
import app.model.Message

import com.typesafe.scalalogging.Logger
import th.logz

object dbService {
  val lz = logz.getLogger("app.db.dbService")

  val log = Logger(dbService.getClass)

  type DbService = Has[Service]

  trait Service {
    def messages: UIO[List[(String, String)]]
    def insertMessage(name: String, msg: String): UIO[Long]
  }

  // accessor methods, the method signature is different:
  // 1. add the current Has[Service] as R
  // 2. remove the effect type of the return type A
  def messages: URIO[DbService with logz.LogZ, List[(String, String)]] =
    lz.flatMap(_.debug("in messages")) *>
      ZIO.accessM(_.get.messages)

  def insertMessage(
      name: String,
      msg: String
  ): URIO[DbService with logz.LogZ, Long] =
    lz.flatMap(_.debug("in insertMessage")) *>
      ZIO.accessM(_.get.insertMessage(name, msg))

  val pgService: URLayer[DbContext, DbService] =
    ZLayer.fromService(dbContext => {
      log.info("create PgService")
      PgService(dbContext.context)
    })
}
