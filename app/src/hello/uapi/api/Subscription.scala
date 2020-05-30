package app.hello.uapi

import zio.{Runtime, URIO, ZIO}

import scalatags.Text.all._

import app.db.dbService
import Util.{openConnections, createList}
import cask.endpoints.{WsActor, WsChannelActor}
import scala.concurrent.ExecutionContext
import castor.Context
import cask.util.Logger

import com.typesafe.scalalogging.{Logger => Slog}

import app.db.dbService
import app.db.dbService.DbService

import app.hello.Layers

object Subscription {

  val slog = Slog(Subscription.getClass)

  def handle(
      connection: WsChannelActor
  )(implicit ac: Context, log: Logger): WsActor = {

    def subscribe(msg: String) = {
      slog.debug(s"ZIO run websocket subscribe $msg")
      val run = runSubscribe(connection, msg).provideLayer(Layers.dbLayers)
      Runtime.default.unsafeRun(run)
    }

    cask.WsActor {
      case cask.Ws.Text(msg)   => subscribe(msg)
      case cask.Ws.Close(_, _) => openConnections -= connection
    }

  }

  private def runSubscribe(
      connection: WsChannelActor,
      msg: String
  ): URIO[DbService, Unit] = {
    def subscribe(messagesLength: Int, messageList: String) =
      if (msg.toInt < messagesLength) {
        val response = cask.Ws.Text(
          ujson
            .Obj(
              "index" -> messagesLength,
              "txt" -> messageList
            )
            .render()
        )
        slog.debug(s"connection.send ${messagesLength} messages")
        connection.send(response)
      } else {
        slog.debug("add a new connection")
        openConnections += connection
      }

    for {
      messages <- dbService.messages
    } yield subscribe(messages.length, createList(messages).render)
  }
}
