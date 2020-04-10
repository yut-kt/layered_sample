package domain.entity

import java.util.UUID

case class PostEntity(id: UUID,
                      userID: UUID,
                      text: String,
                      parentPostID: Option[UUID],
                      commentCount: Int,
                      postedAt: String)
