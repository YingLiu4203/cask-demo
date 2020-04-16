package app.hello.uapi

import scalatags.Text.all._

import app.db.EmbeddedPg
import Util.{openConnections, messageList}

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
      EmbeddedPg.insertMessage(name, msg)
      val notification = cask.Ws.Text(
        ujson
          .Obj(
            "index" -> EmbeddedPg.messages.length,
            "txt" -> messageList().render
          )
          .render()
      )
      for (conn <- openConnections) conn.send(notification)
      openConnections = Set.empty
      ujson.Obj("success" -> true, "txt" -> messageList().render)
    }
  }
}
