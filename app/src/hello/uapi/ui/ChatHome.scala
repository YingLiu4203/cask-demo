package app.hello.uapi

import zio.{Task, Runtime, URIO, ZIO}

import app.db.DbService

import app.hello.Layers

// use the LogZ service to customize the log name
import th.logz.LogZ

object ChatHome {
  import scalatags.Text.all._

  def hello() = {

    val runnable = for {
      zLog <- LogZ.getLogger("app.hello.uapi")
      _ <- zLog.debug("in hello()")
      messages <- Util.messageList()
    } yield render(messages)

    runnable.provideLayer(LogZ.live ++ Layers.dbLayers)
  }

  private def render(messages: Frag): String = {
    html(
      head(
        meta(charset := "utf-8"),
        link(
          rel := "stylesheet",
          href := "/static/css/bootstrap441.min.css"
        )
      ),
      body(
        div(cls := "container")(
          h1("Scala Chat 聊天"),
          hr,
          div(id := "messageList")(
            messages
          ),
          hr,
          div(id := "errorDiv", color.red),
          form(id := "chatForm")(
            input(
              `type` := "text",
              id := "nameInput",
              placeholder := "User name",
              width := "20%"
            ),
            input(
              `type` := "text",
              id := "msgInput",
              placeholder := "Please write a message!",
              width := "60%"
            ),
            input(`type` := "submit", width := "20%")
          )
        ),
        script(src := "/static/js/chat-form.js")
      )
    ).render
  }

}
