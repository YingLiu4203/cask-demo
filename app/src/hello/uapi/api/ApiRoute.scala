// package app.hello.uapi

// import Util.openConnections

// case class ApiRoute()(implicit val log: cask.Logger) extends cask.Routes {

//   @cask.websocket("/subscribe")
//   def subscribe() = cask.WsHandler(Subscription.handle)

//   @cask.postJson("/")
//   def postHello(name: String, msg: String) = Hello.postHello(name, msg)

//   initialize()
// }
