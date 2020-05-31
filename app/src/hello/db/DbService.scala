package app.db

import zio.{Has, UIO, URLayer, URIO, ZIO, ULayer, ZLayer}

import io.getquill.{PostgresJdbcContext, LowerCase}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import app.model.Message

import th.logz

trait DbService {
  def messages(): UIO[List[(String, String)]]
  def insertMessage(name: String, msg: String): UIO[Long]
}

object DbService {

  val lz = logz.getLogger("app.db.DbService")

  // accessor methods, the method signature is different:
  // 1. add the current Has[Service] as R
  // 2. remove the effect type of the return type A
  def messages(): URIO[Has[DbService] with logz.LogZ, List[(String, String)]] =
    lz.flatMap(_.debug("in messages")) *>
      ZIO.accessM(_.get.messages())

  def insertMessage(
      name: String,
      msg: String
  ): URIO[Has[DbService] with logz.LogZ, Long] =
    lz.flatMap(_.debug("in insertMessage")) *>
      ZIO.accessM(_.get.insertMessage(name, msg))

  val pgService: ULayer[Has[DbService]] =
    ZLayer.succeed(PgService)
}
