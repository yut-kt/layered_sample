package infrastructure.dao

import java.util.UUID

import domain.entity.UserEntity
import scalikejdbc._

case class UserDAO(id: UUID, name: String)

object UserDAO extends SQLSyntaxSupport[UserDAO] {

  def apply(u: SyntaxProvider[UserDAO])(rs: WrappedResultSet): UserDAO = apply(u.resultName)(rs)

  def apply(u: ResultName[UserDAO])(rs: WrappedResultSet): UserDAO = new UserDAO(
    id = UUID.fromString(rs.get(u.id)),
    name = rs.get(u.name)
  )

  override val tableName = "users"
  private val u = UserDAO.syntax("u")

  def findByID(id: UUID)(implicit session: DBSession = autoSession): Option[UserEntity] = {
    val user = withSQL {
      select.from(UserDAO as u).where.eq(u.id, id.toString)
    }.map(UserDAO(u)).single.apply()

    user.map(userDAO => UserEntity.apply(userDAO.id, userDAO.name))
  }
}
