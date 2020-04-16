package app.hello.uapi

import scalatags.Text.all._

import Util.messageList

object ChatHome {

  def hello(): String = {
    html(
      head(
        link(
          rel := "stylesheet",
          href := "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        ),
        script(
          raw("""
          function submitForm(){
            fetch(
              "/",
              {method: "POST", body: JSON.stringify({name: nameInput.value, msg: msgInput.value})}
            ).then(response => response.json())
             .then(json => {
              if (json.success) {
                messageList.innerHTML = json.txt
                msgInput.value = ""
                errorDiv.innerText = ""
              } else {
                errorDiv.innerText = json.txt
              }
            })
          }
          var socket = new WebSocket("ws://" + location.host + "/subscribe");
          var eventIndex = 0
          socket.onopen = function(ev){ socket.send("" + eventIndex) }
          socket.onmessage = function(ev){
            var json = JSON.parse(ev.data)
            eventIndex = json.index
            socket.send("" + eventIndex)
            messageList.innerHTML = json.txt
          }
        """)
        )
      ),
      body(
        div(cls := "container")(
          h1("Scala Chat!"),
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
