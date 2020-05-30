package app.db

import zio.UIO
import com.typesafe.scalalogging.{Logger => Slog}

import app.db.dbContext.PgContext
import app.db.dbService.Service
import app.model.Message

private[db] final case class PgService(zpgContext: UIO[PgContext])
    extends Service {

  val slog = Slog[PgService]

  def messages: UIO[List[(String, String)]] =
    zpgContext.map { pgContext =>
      slog.debug("db messages")
      import pgContext._
      pgContext.run(query[Message].map(m => (m.name, m.msg)))
    }

  def insertMessage(name: String, msg: String): UIO[Long] =
    zpgContext.map { pgContext =>
      slog.debug(s"db insert ${name} ${msg}")
      import pgContext._
      pgContext.run(query[Message].insert(lift(Message(name, msg))))
    }
}
