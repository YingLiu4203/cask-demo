package app.hello.uapi

import zio.{Runtime, URIO, ZIO}

import scalatags.Text.all._

import app.db.dbService
import Util.{dbLayers, openConnections, messageList}
import cask.endpoints.{WsActor, WsChannelActor}
import scala.concurrent.ExecutionContext
import castor.Context
import cask.util.Logger

import app.db.dbService
import app.db.dbService.DbService

object Subscription {

  def handle(
      connection: WsChannelActor
  )(implicit ac: Context, log: Logger): WsActor = {

    def subscribe(msg: String) = {
      val run = runSubscribe(connection, msg).provideLayer(dbLayers)
      Runtime.default.unsafeRun(run)
    }

    def close() = openConnections -= connection

    cask.WsActor {
      case cask.Ws.Text(msg)   => subscribe(msg)
      case cask.Ws.Close(_, _) => close()
    }

  }

  private def runSubscribe(
      connection: WsChannelActor,
      msg: String
  ): URIO[DbService, Unit] = {
    def subscribe(messagesLength: Int, messageList: String) =
      if (msg.toInt < messagesLength) {
        connection.send(
          cask.Ws.Text(
            ujson
              .Obj(
                "index" -> messagesLength,
                "txt" -> messageList
              )
              .render()
          )
        )
      } else openConnections += connection

    for {
      messages <- dbService.messages
      messageList <- messageList()
    } yield subscribe(messages.length, messageList.render)
  }
}
