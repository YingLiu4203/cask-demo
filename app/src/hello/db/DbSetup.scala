package app.db

import zio.{URIO, ZIO}
import app.db.dbContext
import app.db.dbContext.DbContext

object dbSetup {

  def createTableIfNotExists(): URIO[DbContext, Unit] = {
    val zpgContext = dbContext.context
    zpgContext.map(_.executeAction("""
    CREATE TABLE IF NOT EXISTS message (
      name text,
      msg text
    );
  """))
  }
}
