package app.hello.uapi

import scalatags.Text.all._

import Util.messageList

case class UIRoute()(implicit val log: cask.Logger) extends cask.Routes {

  @cask.get("/")
  def hello() = ChatHome.hello()

  initialize()
}
