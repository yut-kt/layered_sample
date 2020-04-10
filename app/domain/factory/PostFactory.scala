package domain.factory

import java.util.UUID

import domain.entity.PostEntity
import domain.exception.DomainException
import domain.exception.DomainException.MicroBlogException

object PostFactory {
  def createPost(userID: String,
                 text: String,
                 parentPostID: Option[String]): Either[MicroBlogException, PostEntity] = {

    if (text.length < 1 && 100 < text.length) {
      Left(DomainException.InvalidText)
      //      throw DomainException.InvalidText
    } else {
      Right(
        PostEntity.apply(
          UUID.randomUUID(),
          UUID.fromString(userID),
          text,
          parentPostID.map(UUID.fromString),
          0,
          ""
        )
      )
    }
  }
}
