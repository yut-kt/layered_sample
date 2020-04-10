package domain.repository

import java.util.UUID

import domain.entity.UserEntity

trait UserRepository {

  def findByID(id: UUID): Option[UserEntity]
}
