package app.hello.uapi

import scalatags.Text.all._

import app.db.dbService
import Util.{openConnections, messageList}
import cask.endpoints.{WsActor, WsChannelActor}
import scala.concurrent.ExecutionContext
import castor.Context
import cask.util.Logger

object Subscription {

  def handle(
      connection: WsChannelActor
  )(implicit ac: Context, log: Logger): WsActor = {

    def subscribe(msg: String) =
      if (msg.toInt < dbService.messages.length) {
        connection.send(
          cask.Ws.Text(
            ujson
              .Obj(
                "index" -> dbService.messages.length,
                "txt" -> messageList().render
              )
              .render()
          )
        )
      } else openConnections += connection

    def close() = openConnections -= connection

    cask.WsActor {
      case cask.Ws.Text(msg)   => subscribe(msg)
      case cask.Ws.Close(_, _) => close()
    }
  }
}
