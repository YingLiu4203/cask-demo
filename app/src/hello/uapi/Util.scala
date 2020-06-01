package app.hello.uapi

import zio.{Has, Runtime, URIO, ZIO, Task}
import cask.Response
import cask.internal.Conversion

import app.db.DbService

import com.tersesystems.blindsight.LoggerFactory
import scalatags.Text.all._

object Util {

  val logger = LoggerFactory.getLogger

  var openConnections = Set.empty[cask.WsChannelActor]

  def messageList() =
    for {
      messageList <- DbService.messages
    } yield (createList(messageList))

  def createList(messageList: List[(String, String)]): Frag =
    frag(
      for ((name, msg) <- messageList)
        yield p(b(name), " ", msg)
    )

  class GetZ(override val path: String) extends cask.endpoints.get(path) {
    def convertToResultType(task: Task[String]): Response.Raw = {
      logger.debug("ZIO run for GetZ")
      val t = Runtime.default.unsafeRunTask(task)
      logger.debug("ZIO After run for GetZ")
      Response(t)
    }
  }

  class PostJsonZ(override val path: String)
      extends cask.endpoints.postJson(path) {
    def convertToResultType(task: Task[ujson.Obj]): Response.Raw = {
      logger.debug("ZIO run for PostJsonZ")
      val t = Runtime.default.unsafeRunTask(task)
      logger.debug("ZIO AFTER run for PostJsonZ")
      Response(t)
    }
  }
}
