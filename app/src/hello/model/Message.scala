package app.model

case class Message(name: String, msg: String)

trait MessageOps {
  def messages: List[(String, String)]
  def insertMessage(name: String, msg: String): Long
}
