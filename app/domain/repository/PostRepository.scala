package domain.repository

import java.util.UUID

import domain.entity.PostEntity

trait PostRepository {

  def find(id: UUID): Option[PostEntity]

  def findAll(): List[PostEntity]

  def create(postEntity: PostEntity): PostEntity

  def getComments(id: UUID): List[PostEntity]
}
