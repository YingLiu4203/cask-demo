package app.hello.uapi

import zio.{Runtime, URIO, ZIO}
import app.db.dbContext
import app.db.dbService
import app.db.dbService.DbService

import Util.{dbLayers, messageList}

object ChatHome {
  import scalatags.Text.all._

  def hello(): String = {

    val run = runHello().provideLayer(dbLayers)
    Runtime.default.unsafeRun(run)
  }

  def runHello(): URIO[DbService, String] = {
    for {
      messages <- messageList()
    } yield render(messages)
  }

  private def render(messages: Frag): String = {
    html(
      head(
        link(
          rel := "stylesheet",
          href := "/static/css/bootstrap441.min.css"
        ),
        script(src := "/static/js/chat-form.js")
      ),
      body(
        div(cls := "container")(
          h1("Scala Chat"),
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
