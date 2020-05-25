package app.hello.uapi

import zio.{Runtime, URIO, UIO, ZIO}
import scalatags.Text.all._

import app.db.dbService
import Util.{dbLayers, openConnections, messageList}
import app.db.dbService
import app.db.dbService.DbService

object Hello {

  def postHello(name: String, msg: String): UIO[ujson.Obj] = {
    if (name == "")
      UIO(ujson.Obj("success" -> false, "txt" -> "Name cannot be empty"))
    else if (name.length >= 10)
      UIO(
        ujson.Obj(
          "success" -> false,
          "txt" -> "Name cannot be longer than 10 characters"
        )
      )
    else if (msg == "")
      UIO(ujson.Obj("success" -> false, "txt" -> "Message cannot be empty"))
    else if (msg.length >= 160)
      UIO(
        ujson.Obj(
          "success" -> false,
          "txt" -> "Message cannot be longer than 160 characters"
        )
      )
    else {
      runEffect(name, msg).provideLayer(dbLayers)
    }
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
