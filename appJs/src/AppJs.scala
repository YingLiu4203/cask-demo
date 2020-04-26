import io.udash.wrappers.jquery.jQ

import io.udash.wrappers.jquery.{EventName, JQueryEvent}
import org.scalajs.dom.{Element, XMLHttpRequest}
import scala.scalajs.js.JSON
import scala.scalajs.js
import io.udash.wrappers.jquery.JQueryAjaxSettings
import io.udash.wrappers.jquery.JQueryXHR
import org.scalajs.dom.ext.Ajax
import scala.util.Success
import scala.util.Failure

object AppJs {
  def main(args: Array[String]): Unit = {
    jQ(() => init())
    println("Hi from Scalajs 11")
  }

  def init() {
    jQ("#chatSubmit").on(EventName.click, postMessage)
  }

  def postMessage(element: Element, event: JQueryEvent): Unit = {
    def done(response: XMLHttpRequest) = {
      val json = ujson.read(response.responseText)

      if (json("success").bool) {
        jQ("#messageList").html(json("txt").str)
        jQ("#chatForm").trigger("reset")
        jQ("#errorDiv").text("")
      } else {
        jQ("#errorDiv").text(json("txt").str)
      }
    }

    val name = jQ("#nameInput").value().toString
    val msg = jQ("#msgInput").value().toString
    val jsonData = ujson.Obj("name" -> ujson.Str(name), "msg" -> ujson.Str(msg))

    import scala.concurrent.ExecutionContext.Implicits.global
    Ajax
      .post(
        url = "/",
        data = jsonData.toString,
        headers = Map("contentType" -> "application/json")
      )
      .onComplete {
        case Success(value) => done(value)
        case Failure(e)     => println(s"fail: ${e.toString}")
      }
  }
}

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
