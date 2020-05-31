package app.db

import io.getquill.{PostgresJdbcContext, LowerCase}
import zio.{Has, UIO, URIO, ZIO, URLayer, ZLayer}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import th.logz.{LogZ, Logger}
import com.typesafe.scalalogging.{Logger => Slog}

object dbContext {
  var count = 0
  val slog = Slog(dbContext.getClass)

  type PgContext = PostgresJdbcContext[LowerCase]
  type DbContext = Has[Service]

  trait Service {
    // the method type describe the effect thus the type is a ZIO effect
    def context: UIO[PgContext]
  }

  def context: URIO[DbContext, PgContext] = ZIO.accessM(_.get.context)

  val embeddedPg: URLayer[LogZ, DbContext] = ZLayer.fromService(logz =>
    new Service {
      // only executed once to constructor the context effect
      // but the context may execute multiple times
      val log = logz.getLogger("app.dbContext")
      slog.debug("create a new service of PgContext")

      val context: UIO[PgContext] = create(log)
    }
  )

  private def create(log: Logger): UIO[PgContext] = {

    log.info("create pgDataSoruce " + count) *> UIO {

      val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
      pgDataSource.setUser("postgres")

      val config = new HikariConfig()
      config.setDataSource(pgDataSource)

      // the two lines work differently if moved out of this UIO constructor
      count += 1
      slog.debug(s"!!!important code count: $count")

      slog.info("inside PGContext lowerCase")
      new PgContext(
        LowerCase,
        new HikariDataSource(config)
      )
    } <* log.info("created pgDataSource " + count)

  }
}
