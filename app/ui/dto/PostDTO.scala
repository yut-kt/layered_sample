package ui.dto

import play.api.libs.json.{ Json, Writes }

case class PostDTO(id: String,
                   userID: String,
                   text: String,
                   parentPostID: Option[String],
                   commentCount: Int,
                   postedAt: String)

object PostDTO {
  implicit val jsonWrites: Writes[PostDTO] = (p: PostDTO) =>
    Json.obj(
      "id" -> p.id,
      "user_id" -> p.userID,
      "text" -> p.text,
      "parent_post_id" -> p.parentPostID,
      "comment_count" -> p.commentCount,
      "posted_at" -> p.postedAt.toString,
  )
}
