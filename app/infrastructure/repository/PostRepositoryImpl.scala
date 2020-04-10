package infrastructure.repository

import java.util.UUID

import domain.entity.PostEntity
import infrastructure.dao.PostDAO
import domain.repository.PostRepository

class PostRepositoryImpl extends PostRepository {

  override def find(id: UUID): Option[PostEntity] = {
    PostDAO.find(id)
  }

  override def findAll(): List[PostEntity] = {
    PostDAO.findAll()
  }

  override def create(postEntity: PostEntity): PostEntity = {
    PostDAO.create(postEntity)
  }

  override def getComments(id: UUID): List[PostEntity] = {
    PostDAO.getComments(id)
  }
}
