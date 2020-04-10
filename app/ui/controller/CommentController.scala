package ui.controller

import application.services.CommentService
import domain.exception.DomainException.{ NotExistsPost, NotPostIDParam }
import domain.repository.{ PostRepository, UserRepository }
import infrastructure.repository.{ PostRepositoryImpl, UserRepositoryImpl }
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import ui.requests.PostRequest
import ui.dto.PostDTO

trait MixinCommentService {
  val commentService: CommentService = new CommentService {
    override val postRepository: PostRepository = new PostRepositoryImpl
    override val userRepository: UserRepository = new UserRepositoryImpl
  }
}

@Singleton
class CommentController @Inject()(
    cc: ControllerComponents,
) extends AbstractController(cc)
    with MixinCommentService {

  def show(postID: String) = Action {
    import scalikejdbc._

    implicit val session: AutoSession.type = AutoSession

    val comments = commentService.getComments(postID)

    val dto = comments.map { comment =>
      PostDTO(
        id = comment.id.toString,
        userID = comment.userID.toString,
        text = comment.text,
        parentPostID = comment.parentPostID.map(_.toString),
        commentCount = comment.commentCount,
        postedAt = comment.postedAt
      )
    }
    Ok(Json.obj("comments" -> dto))
  }

  def create(postID: String) = Action { implicit req =>
    PostRequest.form.bindFromRequest.fold(
      errors => BadRequest(errors.errors.map(_.message).mkString),
      form => {
        commentService.create(form.user_id, form.text, postID) match {
          case Right(_) => Ok(Json.obj("result" -> "OK"))
          case Left(ex) => ServiceUnavailable(ex.getMessage)
        }
      }
    )
  }
}
