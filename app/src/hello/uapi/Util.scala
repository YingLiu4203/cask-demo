package app.hello.uapi

import scalatags.Text.all._

import app.db.dbService

object Util {

  var openConnections = Set.empty[cask.WsChannelActor]

  def messageList(): Frag =
    frag(
      for ((name, msg) <- dbService.messages)
        yield p(b(name), " ", msg)
    )
}
