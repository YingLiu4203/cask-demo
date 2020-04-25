package app.hello.uapi

import scalatags.Text.all._

import Util.messageList

object ChatHome {

  def hello(): String = {
    html(
      head(
        link(
          rel := "stylesheet",
          href := "/public/lib/bootstrap/css/bootstrap.min.css"
        ),
        script(src := "/public/js/chat-form.js")
      ),
      body(
        div(cls := "container")(
          h1("Scala Chat"),
          hr,
          div(id := "messageList")(
            messageList()
          ),
          hr,
          div(id := "errorDiv", color.red),
          form(onsubmit := "submitForm(); return false")(
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
