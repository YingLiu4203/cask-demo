package app.hello.uapi

import zio.{Runtime, URIO, ZIO}
import scalatags.Text.all._

import app.db.dbService
import Util.{dbLayers, openConnections, messageList}
import app.db.dbService
import app.db.dbService.DbService

object Hello {

  def postHello(name: String, msg: String): ujson.Obj = {
    if (name == "")
      ujson.Obj("success" -> false, "txt" -> "Name cannot be empty")
    else if (name.length >= 10)
      ujson.Obj(
        "success" -> false,
        "txt" -> "Name cannot be longer than 10 characters"
      )
    else if (msg == "")
      ujson.Obj("success" -> false, "txt" -> "Message cannot be empty")
    else if (msg.length >= 160)
      ujson.Obj(
        "success" -> false,
        "txt" -> "Message cannot be longer than 160 characters"
      )
    else {
      insertMessage(name, msg)
    }
  }

  private def insertMessage(name: String, msg: String): ujson.Obj = {
    val run = runEffect(name, msg).provideLayer(dbLayers)
    Runtime.default.unsafeRun(run)
  }

  private def runEffect(
      name: String,
      msg: String
  ): URIO[DbService, ujson.Obj] = {

    def insert(length: Long, messageList: String) = {
      val notification = cask.Ws.Text(
        ujson
          .Obj(
            "index" -> length,
            "txt" -> messageList
          )
          .render()
      )
      for (conn <- openConnections) conn.send(notification)
      openConnections = Set.empty
      ujson.Obj("success" -> true, "txt" -> messageList)
    }

    for {
      msgLength <- dbService.insertMessage(name, msg)
      messageList <- messageList()
    } yield insert(msgLength, messageList.render)
  }

}
