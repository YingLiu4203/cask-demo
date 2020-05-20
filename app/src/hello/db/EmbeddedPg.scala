package app.db

import com.opentable.db.postgres.embedded.EmbeddedPostgres

object embeddedPg {
  def start() =
    EmbeddedPostgres
      .builder()
      .setDataDirectory("data")
      .setCleanDataDirectory(false)
      .setPort(5432)
      .start()
}
