package infrastructure.dao

import java.util.UUID

import domain.entity.PostEntity
import scalikejdbc._

case class PostDAO(id: UUID,
                   userID: UUID,
                   text: String,
                   parentPostID: Option[UUID],
                   postedAt: String)

object PostDAO extends SQLSyntaxSupport[PostDAO] {

  def apply(u: SyntaxProvider[PostDAO])(rs: WrappedResultSet): PostDAO =
    apply(u.resultName)(rs)

  def apply(p: ResultName[PostDAO])(rs: WrappedResultSet): PostDAO = PostDAO(
    id = UUID.fromString(rs.get(p.id)),
    userID = UUID.fromString(rs.get(p.userID)),
    text = rs.get(p.text),
    parentPostID = rs.stringOpt(p.parentPostID).map(UUID.fromString),
    postedAt = rs.get(p.postedAt)
  )

  override val tableName = "posts"
  private val p = PostDAO.syntax("p")

  def find(id: UUID)(implicit session: DBSession = autoSession): Option[PostEntity] = {
    val post = withSQL {
      select
        .from(PostDAO as p)
        .where
        .eq(p.id, id.toString)
        .orderBy(p.postedAt)
    }.map(PostDAO(p)).single.apply()

    val posts = findAll()
    post.map(
      postDAO =>
        PostEntity.apply(
          postDAO.id,
          postDAO.userID,
          postDAO.text,
          postDAO.parentPostID,
          posts.count(_.parentPostID.contains(postDAO.id)),
          postDAO.postedAt
      ))
  }

  def findAll()(implicit session: DBSession = autoSession): List[PostEntity] = {
    val posts: List[PostDAO] = withSQL {
      select
        .from(PostDAO as p)
        .orderBy(p.postedAt)
    }.map(PostDAO(p)).list.apply()

    for {
      post <- posts
      count = posts.count(_.parentPostID.contains(post.id))
    } yield {
      PostEntity.apply(post.id, post.userID, post.text, post.parentPostID, count, post.postedAt)
    }
  }

  def create(postEntity: PostEntity)(implicit session: DBSession = autoSession): PostEntity = {
    withSQL {
      insert
        .into(PostDAO)
        .namedValues(
          column.id -> postEntity.id.toString,
          column.userID -> postEntity.userID.toString,
          column.text -> postEntity.text,
          column.parentPostID -> postEntity.parentPostID.map(_.toString)
        )
    }.execute.apply
    postEntity
  }

  def getComments(id: UUID)(implicit session: DBSession = autoSession): List[PostEntity] = {
    val comments: List[PostDAO] = withSQL {
      select
        .from(PostDAO as p)
        .where
        .eq(p.parentPostID, id.toString)
        .orderBy(p.postedAt)
    }.map(PostDAO(p)).list.apply()

    val posts = findAll()

    for {
      comment <- comments
      count = posts.count(_.parentPostID.contains(comment.id))
    } yield {
      PostEntity.apply(
        comment.id,
        comment.userID,
        comment.text,
        comment.parentPostID,
        count,
        comment.postedAt
      )
    }
  }
}
