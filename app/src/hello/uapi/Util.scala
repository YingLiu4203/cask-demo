package app.hello.uapi

import scalatags.Text.all._

import app.db.EmbeddedPg

object Util {

  var openConnections = Set.empty[cask.WsChannelActor]

  def messageList(): Frag =
    frag(
      for ((name, msg) <- EmbeddedPg.messages)
        yield p(b(name), " ", msg)
    )
}
