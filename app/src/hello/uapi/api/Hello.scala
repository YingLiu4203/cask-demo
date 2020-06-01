package app.hello.uapi

import zio.{Has, Runtime, URIO, UIO, ZIO}

import app.db.DbService
import Util.{openConnections, createList}
import app.hello.Layers

import com.tersesystems.blindsight.LoggerFactory
import th.logz

object Hello {
  val logger = LoggerFactory.getLogger

  import scalatags.Text.all._

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
      runEffect(name, msg).provideLayer(Layers.dbLayers)
    }
  }

  private def runEffect(
      name: String,
      msg: String
  ): URIO[Has[DbService] with logz.LogZ, ujson.Obj] = {

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
      openConnections = Set.empty // synchronized clients will be added back
      ujson.Obj("success" -> true, "txt" -> messageList)
    }

    for {
      _ <- DbService.insertMessage(name, msg)
      messages <- DbService.messages
    } yield insert(messages.length, createList(messages).render)
  }

}
