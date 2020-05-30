package th

import com.typesafe.scalalogging.{Logger => UnderLying}

import zio._

package object logz {

  type LogZ = Has[LogZ.Service]

  object LogZ {

    trait Service {
      def getLogger(name: String): Logger
    }

    val live: ULayer[LogZ] = ZLayer.succeed(new Service {
      def getLogger(name: String) = new Logger(UnderLying(name))
    })

  }

  // access methods
  def getLogger(name: String): ZIO[LogZ, Nothing, Logger] =
    ZIO.access(_.get.getLogger(name))

  class Logger(logger: UnderLying) {
    def info(message: String): UIO[Unit] = UIO(logger.info(message))
    def debug(message: String): UIO[Unit] = UIO(logger.debug(message))
  }
}
