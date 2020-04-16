package app.db

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.getquill._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import app.model.{Message, MessageOps}

object EmbeddedPg extends MessageOps {

  val server = EmbeddedPostgres
    .builder()
    .setDataDirectory("data")
    .setCleanDataDirectory(false)
    .setPort(5432)
    .start()

  val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()

  pgDataSource.setUser("postgres")
  val config = new HikariConfig()
  config.setDataSource(pgDataSource)
  private val ctx =
    new PostgresJdbcContext(LowerCase, new HikariDataSource(config))
  ctx.executeAction("""
    CREATE TABLE IF NOT EXISTS message (
      name text,
      msg text
    );
  """)

  import ctx._

  def messages: List[(String, String)] =
    ctx.run(query[Message].map(m => (m.name, m.msg)))

  def insertMessage(name: String, msg: String): Long =
    ctx.run(query[Message].insert(lift(Message(name, msg))))

}
