package domain.exception

object DomainException {

  sealed abstract class MicroBlogException extends Exception

  case object InvalidText extends MicroBlogException {
    override def getMessage: String = "invalid text size"
  }

  case object NotExistsUser extends MicroBlogException {
    override def getMessage: String = "invalid user id"
  }

  case object NotPostIDParam extends MicroBlogException {
    override def getMessage: String = "not get parameter post id"
  }

  case object NotExistsPost extends MicroBlogException {
    override def getMessage: String = "invalid post id"
  }
}
