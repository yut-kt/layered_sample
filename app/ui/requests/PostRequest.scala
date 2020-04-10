package ui.requests

import play.api.data.{ Form, Forms }
import play.api.data.Forms._

case class PostRequest(user_id: String, text: String)

object PostRequest {
  val form = Form(
    mapping(
      "user_id" -> Forms.text,
      "text" -> Forms.text
    )(PostRequest.apply)(PostRequest.unapply)
  )
}
