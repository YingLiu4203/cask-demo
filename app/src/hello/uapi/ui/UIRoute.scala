package app.hello.uapi

import scalatags.Text.all._

import zio.Task
import Util.messageList

case class UIRoute()(implicit val log: cask.Logger) extends cask.Routes {

  @Util.GetZ("/")
  def hello(): Task[String] = ChatHome.hello()

  @cask.decorators.compress
  @cask.staticFiles("/static/js")
  def staticFileJs() = "app/resources/js"

  @cask.staticFiles("/static/css", headers = Seq("Content-Type" -> "text/css"))
  def staticFileCss() = "app/resources/css"

  initialize()
}
