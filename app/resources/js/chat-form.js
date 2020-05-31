const chatForm = document.getElementById('chatForm')
chatForm.addEventListener('submit', submitForm)

function submitForm(event) {
  const value = { name: nameInput.value, msg: msgInput.value }
  const body = JSON.stringify(value)
  console.log('On submitForm')

  fetch('/', {
    method: 'POST',
    body,
  })
    .then((response) => response.json())
    .then((json) => {
      console.log(`submitForm fetch result: ${json.txt}`)
      if (json.success) {
        msgInput.value = ''
        errorDiv.innerText = ''
      } else {
        errorDiv.innerText = json.txt
      }
    })

  event.preventDefault()
}

var socket = new WebSocket('ws://' + location.host + '/subscribe')
var eventIndex = 0
socket.onopen = function (ev) {
  console.log(`socket.onopen eventIndex ${eventIndex}`)
  socket.send('' + eventIndex)
}

socket.onmessage = function (ev) {
  var json = JSON.parse(ev.data)
  console.log(`socket.onmessage  ${ev.data}`)
  eventIndex = json.index
  socket.send('' + eventIndex) // either get refreshed messages or subscribe
  messageList.innerHTML = json.txt
}
