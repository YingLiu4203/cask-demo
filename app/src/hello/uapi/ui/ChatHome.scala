package app.hello.uapi

import zio.{Task, Runtime, URIO, ZIO}
import app.db.dbContext
import app.db.dbService
import app.db.dbService.DbService

import Util.messageList
import app.hello.Layers

import th.logz

object ChatHome {
  import scalatags.Text.all._

  // val log = logz.getLogger("ChatHome")

  def hello() = {

    val runnable = for {
      log <- logz.getLogger("ChatHome")
      _ <- log.info("get message list")
      messages <- messageList()
    } yield render(messages)

    runnable.provideLayer(Layers.dbLayers)
  }

  private def render(messages: Frag): String = {
    html(
      head(
        meta(charset := "utf-8"),
        link(
          rel := "stylesheet",
          href := "/static/css/bootstrap441.min.css"
        ),
        script(src := "/static/js/chat-form.js")
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
          form(onsubmit := "submitForm()")(
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
        )
      )
    ).render
  }

}
