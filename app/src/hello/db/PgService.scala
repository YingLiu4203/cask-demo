package app.db

import zio.UIO

import app.db.dbContext.PgContext
import app.db.dbService.Service
import app.model.Message

private[db] final case class PgService(zpgContext: UIO[PgContext])
    extends Service {

  def messages: UIO[List[(String, String)]] =
    zpgContext.map { pgContext =>
      import pgContext._
      pgContext.run(query[Message].map(m => (m.name, m.msg)))
    }

  def insertMessage(name: String, msg: String): UIO[Long] =
    zpgContext.map { pgContext =>
      import pgContext._
      pgContext.run(query[Message].insert(lift(Message(name, msg))))
    }
}
