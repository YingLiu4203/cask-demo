package app.hello.uapi

import zio.URIO

import scalatags.Text.all._
import app.db.dbContext
import app.db.dbService
import app.db.dbService.DbService

object Util {

  val dbContextLayer = dbContext.embeddedPg
  val dbLayers = dbContextLayer >>> dbService.pgService

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
}
