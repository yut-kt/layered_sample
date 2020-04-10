package ui.controller

import ui.dto.PostDTO
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import application.services.PostService
import domain.repository.{ PostRepository, UserRepository }
import ui.requests.PostRequest
import infrastructure.repository.{ PostRepositoryImpl, UserRepositoryImpl }

trait MixinPostService {
  val postService: PostService = new PostService {
    override val postRepository: PostRepository = new PostRepositoryImpl
    override val userRepository: UserRepository = new UserRepositoryImpl
  }
}

@Singleton
class PostController @Inject()(
    cc: ControllerComponents
) extends AbstractController(cc)
    with MixinPostService {

  def index() = Action {
    import scalikejdbc._

    implicit val session: AutoSession.type = AutoSession

    val posts = postService.getPosts()

    val dto = posts.map { post =>
      PostDTO(
        id = post.id.toString,
        userID = post.userID.toString,
        text = post.text,
        parentPostID = post.parentPostID.map(_.toString),
        commentCount = post.commentCount,
        postedAt = post.postedAt
      )
    }

    Ok(Json.obj("posts" -> dto))
  }

  def create() = Action { implicit req =>
    PostRequest.form.bindFromRequest.fold(
      errors => BadRequest(errors.errors.map(_.message).mkString),
      form => {
        postService.create(form.user_id, form.text) match {
          case Right(_) => Ok(Json.obj("result" -> "OK"))
          case Left(ex) => ServiceUnavailable(ex.getMessage)
        }
      }
    )
  }
}
