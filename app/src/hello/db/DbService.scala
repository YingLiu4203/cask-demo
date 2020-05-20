package app.db

import io.getquill._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import app.model.{Message, MessageOps}

object dbService extends MessageOps {

  private val dbContext: PostgresJdbcContext[LowerCase.type] = createDbContext()
  import dbContext._

  def messages: List[(String, String)] =
    dbContext.run(query[Message].map(m => (m.name, m.msg)))

  def insertMessage(name: String, msg: String): Long =
    dbContext.run(query[Message].insert(lift(Message(name, msg))))

  private def createDbContext(): PostgresJdbcContext[LowerCase.type] = {
    val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
    pgDataSource.setUser("postgres")
    val config = new HikariConfig()
    config.setDataSource(pgDataSource)
    new PostgresJdbcContext(LowerCase, new HikariDataSource(config))
  }

  def createTableIfNotExists() =
    dbContext.executeAction("""
    CREATE TABLE IF NOT EXISTS message (
      name text,
      msg text
    );
  """)
}
