package application.services

import java.util.UUID

import domain.entity.PostEntity
import domain.factory.PostFactory
import domain.repository.{ PostRepository, UserRepository }
import domain.exception.DomainException
import domain.exception.DomainException._

trait PostService {
  val postRepository: PostRepository
  val userRepository: UserRepository

  def getPosts(): List[PostEntity] = {
    postRepository.findAll()
  }

  def create(userID: String, text: String): Either[MicroBlogException, PostEntity] = {
    userRepository
      .findByID(UUID.fromString(userID))
      .fold[Either[MicroBlogException, PostEntity]](Left(DomainException.NotExistsUser)) { _ =>
        PostFactory.createPost(userID, text, None).map(postRepository.create)
      }
  }
}
