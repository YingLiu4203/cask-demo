package app.db

import zio.{Has, UIO, URLayer, URIO, ZIO, ZLayer}

import app.db.dbContext.{DbContext, PgContext}
import app.model.Message

object dbService {

  type DbService = Has[Service]

  trait Service {
    def messages: UIO[List[(String, String)]]
    def insertMessage(name: String, msg: String): UIO[Long]
  }

  // accessor methods, the method signature is different:
  // 1. add the current Has[Service] as R
  // 2. remove the effect type of the return type A
  def messages: URIO[DbService, List[(String, String)]] =
    ZIO.accessM(_.get.messages)
  def insertMessage(name: String, msg: String): URIO[DbService, Long] =
    ZIO.accessM(_.get.insertMessage(name, msg))

  val pgService: URLayer[DbContext, DbService] =
    ZLayer.fromService(dbContext => PgService(dbContext.context))
}
