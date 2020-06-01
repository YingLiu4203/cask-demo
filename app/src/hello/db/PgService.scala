package app.db

import zio.UIO
import com.tersesystems.blindsight.LoggerFactory

import app.model.Message

import io.getquill.{PostgresJdbcContext, LowerCase}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

final case object PgService extends DbService {

  val logger = LoggerFactory.getLogger

  type PgContext = PostgresJdbcContext[LowerCase]

  // db setup neeeds this
  val pgContext: PgContext = {

    val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
    pgDataSource.setUser("postgres")

    val config = new HikariConfig()
    config.setDataSource(pgDataSource)

    // the two lines work differently if moved out of this UIO constructor
    logger.info("!!!Important PgContext creation")

    new PgContext(
      LowerCase,
      new HikariDataSource(config)
    )
  }

  import pgContext._

  def messages(): UIO[List[(String, String)]] = UIO {
    logger.debug("db messages")
    pgContext.run(query[Message].map(m => (m.name, m.msg)))
  }

  def insertMessage(name: String, msg: String): UIO[Long] = UIO {
    logger.debug(s"db insert ${name} ${msg}")
    pgContext.run(query[Message].insert(lift(Message(name, msg))))
  }

}
