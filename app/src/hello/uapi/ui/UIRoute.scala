package app.hello.uapi

import scalatags.Text.all._

import Util.messageList

case class UIRoute()(implicit val log: cask.Logger) extends cask.Routes {

  @cask.get("/")
  def hello() = ChatHome.hello()

  @cask.decorators.compress
  @cask.staticFiles("/public/js")
  def staticFileJs() = "app/resources/js"

  // hacked in build.sc, but not work after assembly
  @cask.staticFiles("/public/lib", headers = Seq("Content-Type" -> "text/css"))
  def staticFileCss() = "app/public/lib"

  initialize()
}
