package app.hello.uapi

import zio.{Has, Runtime, URIO, ZIO}

import Util.{openConnections, createList}
import cask.endpoints.{WsActor, WsChannelActor}
import scala.concurrent.ExecutionContext
import castor.Context
import cask.util.Logger

import com.typesafe.scalalogging.{Logger => Slog}

import app.db.DbService

import app.hello.Layers
import th.logz

object Subscription {
  import scalatags.Text.all._

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
  ): URIO[Has[DbService] with logz.LogZ, Unit] = {
    def subscribe0(messagesLength: Int, messageList: String) =
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
      messages <- DbService.messages()
    } yield subscribe0(messages.length, createList(messages).render)
  }
}
