package app.hello.uapi

import scalatags.Text.all._

import Util.messageList

object ChatHome {

  val ChatFormId = "chatForm"
  val ChatSubmitId = "chatSubmit"
  val MessageListId = "messageList"
  val ErrorDivId = "errorDiv"

  def hello(): String = {
    html(
      head(
        link(
          rel := "stylesheet",
          href := "/public/lib/bootstrap/css/bootstrap.min.css"
        )
      ),
      body(
        div(cls := "container")(
          h1("Scala Chat"),
          hr,
          div(id := MessageListId)(
            messageList()
          ),
          hr,
          div(id := ErrorDivId, color.red),
          form(id := ChatFormId)(
            input(
              `type` := "text",
              id := "nameInput",
              name := "name",
              placeholder := "User name",
              width := "20%"
            ),
            input(
              `type` := "text",
              id := "msgInput",
              name := "msg",
              placeholder := "Please write a message!",
              width := "60%"
            ),
            button(id := ChatSubmitId, `type` := "button", width := "20%")(
              "Submit"
            )
          ),
          script(src := "/public/js/chat-form.js"),
          script(src := "/public/js/out.js")
        )
      )
    ).render
  }

}
