package app.db

import zio.UIO

object dbSetup {

  def createTableIfNotExists(): UIO[Unit] = {
    UIO(PgService.pgContext.executeAction("""
    CREATE TABLE IF NOT EXISTS message (
      name text,
      msg text
    );
  """))
  }
}
