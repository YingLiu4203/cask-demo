// function submitForm() {
//   fetch('/', {
//     method: 'POST',
//     body: JSON.stringify({ name: nameInput.value, msg: msgInput.value }),
//   })
//     .then((response) => response.json())
//     .then((json) => {
//       if (json.success) {
//         messageList.innerHTML = json.txt
//         msgInput.value = ''
//         errorDiv.innerText = ''
//       } else {
//         errorDiv.innerText = json.txt
//       }
//     })
// }

import io.udash.wrappers.jquery.jQ

// import app.hello.uapi.ChatHome
import io.udash.wrappers.jquery.{EventName, JQueryEvent}
import org.scalajs.dom.Element
import scala.scalajs.js.JSON
import scala.scalajs.js
import io.udash.wrappers.jquery.JQueryAjaxSettings
import io.udash.wrappers.jquery.JQueryXHR

object AppJs {
  def main(args: Array[String]): Unit = {
    jQ(() => init())
    println("Hi from Scalajs 3")
  }

  def init() {
    jQ("#chatSubmit").on(EventName.click, postMessage)
  }

  def postMessage(element: Element, event: JQueryEvent): Unit = {
    println("Clicked")

    def done(response: js.Any, textStatus: String, jqXhr: JQueryXHR) = {
      println(response.toString)
      jQ("#messageList").html(response.toString)
      jQ("#chatForm").trigger("reset")
    }

    def fail(jqXhr: JQueryXHR, textStatus: String, response: js.Any) = {
      println(response.toString)
      jQ("#errorDiv").text(response.toString)
    }

    val formData = jQ("#chatForm").serializeArray()
    val jsonData = JSON.stringify(formData)
    println(jsonData)
    val ajaxSetting = JQueryAjaxSettings(
      url = "/",
      method = "POST",
      contentType = "application/json",
      data = jsonData
    )

    jQ.ajax(
        ajaxSetting
      )
      .then(done, fail)
  }
}

// var socket = new WebSocket('ws://' + location.host + '/subscribe')
// var eventIndex = 0
// socket.onopen = function (ev) {
//   socket.send('' + eventIndex)
// }

// socket.onmessage = function (ev) {
//   var json = JSON.parse(ev.data)
//   eventIndex = json.index
//   socket.send('' + eventIndex)
//   messageList.innerHTML = json.txt
// }
