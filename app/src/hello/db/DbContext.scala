package app.db

import io.getquill.{PostgresJdbcContext, LowerCase}
import zio.{Has, UIO, URIO, ZIO, ULayer, ZLayer}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

object dbContext {

  type PgContext = PostgresJdbcContext[LowerCase]
  type DbContext = Has[Service]

  trait Service {
    // the method type describe the effect thus the type is a ZIO effect
    def context: UIO[PgContext]
  }

  def context: URIO[DbContext, PgContext] = ZIO.accessM(_.get.context)

  val embeddedPg: ULayer[DbContext] = ZLayer.succeed(
    new Service {
      def context: UIO[PgContext] = create()
    }
  )

  private def create() = UIO.effectTotal {
    val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
    pgDataSource.setUser("postgres")
    val config = new HikariConfig()
    config.setDataSource(pgDataSource)
    new PostgresJdbcContext[LowerCase](LowerCase, new HikariDataSource(config))
  }
}
