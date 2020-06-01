package th.logz

import com.tersesystems.blindsight.{LoggerFactory, Logger}

import zio.{Has, UIO, ZIO, ULayer, ZLayer}

case class ZLogger(logger: Logger) {
  def info(message: String): UIO[Unit] = UIO(logger.info(message))
  def debug(message: String): UIO[Unit] = UIO(logger.debug(message))
}

/**
  * Defines `logger` as a lazy value initialized with an underlying `org.slf4j.Logger`
  * named according to the class into which this trait is mixed.
  */
trait LazyLog {
  @transient
  protected lazy val logger: Logger = LoggerFactory.getLogger(getClass)
}

/**
  *  This one needs a temp to work
  * Defines `logger` as a value initialized with an underlying `org.slf4j.Logger`
  * named according to the class into which this trait is mixed.
  */
trait StrictLog {
  protected val logger: Logger = {
    val temp = LoggerFactory.getLogger(getClass)
    temp
  }
}

/**
  * Defines `logger` as a lazy value initialized with an underlying `org.slf4j.Logger`
  * named according to the class into which this trait is mixed.
  */
trait LazyLogZ {
  @transient
  lazy val zLog: ZLogger = {
    val logger = LoggerFactory.getLogger(getClass)
    ZLogger(logger)
  }
}

/**
  * Defines `logger` as a value initialized with an underlying `org.slf4j.Logger`
  * named according to the class into which this trait is mixed.
  */
trait StrictLogZ {
  protected val zLog: ZLogger = {
    val logger = LoggerFactory.getLogger(getClass)
    ZLogger(logger)
  }
}

// defined a service to customize logger name
class LogZ {
  def getLogger(name: String): ZLogger = ZLogger(LoggerFactory.getLogger(name))
}

object LogZ {
  val live: ULayer[Has[LogZ]] = ZLayer.succeed(new LogZ)
  // access methods
  def getLogger(name: String): ZIO[Has[LogZ], Nothing, ZLogger] =
    ZIO.access(_.get.getLogger(name))
}
