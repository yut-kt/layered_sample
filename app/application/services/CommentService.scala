package application.services

import java.util.UUID

import domain.entity.PostEntity
import domain.exception.DomainException
import domain.exception.DomainException.MicroBlogException
import domain.factory.PostFactory
import domain.repository.{ PostRepository, UserRepository }

trait CommentService {
  val postRepository: PostRepository
  val userRepository: UserRepository

  def getComments(id: String): List[PostEntity] = {
    postRepository.getComments(UUID.fromString(id))
  }

  def create(userID: String,
             text: String,
             postID: String): Either[MicroBlogException, PostEntity] = {
    userRepository
      .findByID(UUID.fromString(userID))
      .fold[Either[MicroBlogException, PostEntity]](Left(DomainException.NotExistsUser)) { _ =>
        PostFactory.createPost(userID, text, Some(postID)) match {
          case Right(postEntity) =>
            postEntity.parentPostID
              .fold[Either[MicroBlogException, PostEntity]](Left(DomainException.NotPostIDParam)) {
                parentPostID =>
                  postRepository
                    .find(parentPostID)
                    .fold[Either[MicroBlogException, PostEntity]](
                      Left(DomainException.NotExistsPost)) { _ =>
                      Right(postRepository.create(postEntity))
                    }
              }
          case Left(ex) => Left(ex)
        }
      }
  }
}
