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

var socket = new WebSocket('ws://' + location.host + '/subscribe')
var eventIndex = 0
socket.onopen = function (ev) {
  socket.send('' + eventIndex)
}

socket.onmessage = function (ev) {
  var json = JSON.parse(ev.data)
  eventIndex = json.index
  socket.send('' + eventIndex)
  messageList.innerHTML = json.txt
}
