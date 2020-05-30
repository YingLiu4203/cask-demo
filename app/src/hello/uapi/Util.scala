package app.hello.uapi

import zio.{Runtime, URIO, ZIO, Task}
import cask.Response
import cask.internal.Conversion

import scalatags.Text.all._
import app.db.dbContext
import app.db.dbService
import app.db.dbService.DbService

object Util {

  var openConnections = Set.empty[cask.WsChannelActor]

  def messageList(): URIO[DbService, Frag] = {
    for {
      messageList <- dbService.messages
    } yield (createList(messageList))
  }

  private def createList(messageList: List[(String, String)]) =
    frag(
      for ((name, msg) <- messageList)
        yield p(b(name), " ", msg)
    )

  class GetZ(override val path: String) extends cask.endpoints.get(path) {
    def convertToResultType(task: Task[String]): Response.Raw = {
      val t = Runtime.default.unsafeRunTask(task)
      Response(t)
    }
  }

  class PostJsonZ(override val path: String)
      extends cask.endpoints.postJson(path) {
    def convertToResultType(task: Task[ujson.Obj]): Response.Raw = {
      val t = Runtime.default.unsafeRunTask(task)
      Response(t)
    }
  }
}
