package infrastructure.repository

import java.util.UUID

import domain.entity.UserEntity
import domain.repository.UserRepository
import infrastructure.dao.UserDAO

class UserRepositoryImpl extends UserRepository {
  override def findByID(id: UUID): Option[UserEntity] = {
    UserDAO.findByID(id)
  }
}
