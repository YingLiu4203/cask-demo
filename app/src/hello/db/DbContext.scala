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
      val log = logz.getLogger("embeddedPg")
      def context: UIO[PgContext] = create(log)
    }
  )

  private def create(log: Logger): UIO[PgContext] = {

    count += 1
    log.info("create pgDataSoruce " + count) *> UIO {

      val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
      pgDataSource.setUser("postgres")

      val config = new HikariConfig()
      config.setDataSource(pgDataSource)

      slog.info("inside PGContext lowerCase")
      new PgContext(
        LowerCase,
        new HikariDataSource(config)
      )
    } <* log.info("created pgDataSource " + count)

  }
}
