package app.hello.uapi

import zio.{Runtime, URIO, ZIO, Task}
import cask.Response
import cask.internal.Conversion

import app.db.dbContext
import app.db.dbService
import app.db.dbService.DbService

import com.typesafe.scalalogging.{Logger => Slog}
import th.logz

object Util {
  import scalatags.Text.all._

  val slog = Slog(Util.getClass)

  var openConnections = Set.empty[cask.WsChannelActor]

  def messageList(): URIO[DbService with logz.LogZ, Frag] = {
    slog.debug("call messageList()")
    for {
      messageList <- dbService.messages
    } yield (createList(messageList))
  }

  def createList(messageList: List[(String, String)]) =
    frag(
      for ((name, msg) <- messageList)
        yield p(b(name), " ", msg)
    )

  class GetZ(override val path: String) extends cask.endpoints.get(path) {
    def convertToResultType(task: Task[String]): Response.Raw = {
      slog.debug("ZIO run for GetZ")
      val t = Runtime.default.unsafeRunTask(task)
      slog.debug("ZIO After run for GetZ")
      Response(t)
    }
  }

  class PostJsonZ(override val path: String)
      extends cask.endpoints.postJson(path) {
    def convertToResultType(task: Task[ujson.Obj]): Response.Raw = {
      slog.debug("ZIO run for PostJsonZ")
      val t = Runtime.default.unsafeRunTask(task)
      slog.debug("ZIO AFTER run for PostJsonZ")
      Response(t)
    }
  }
}
