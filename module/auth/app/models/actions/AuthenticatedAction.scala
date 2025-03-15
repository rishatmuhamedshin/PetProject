package models.actions

import controllers.Assets.Redirect
import models.services.JwtService
import play.api.mvc._

import scala.concurrent.Future

case class AuthenticatedAction[A](action: Action[A]) extends Action[A] {


  override def apply(request: Request[A]): Future[Result] = {
    Helper.checkPermission(request, action.apply)
  }
  lazy val parser = action.parser


}

object AuthenticatedAction extends ActionBuilder[Request] {


  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
    Helper.checkPermission(request, block)

  override protected def composeAction[A](action: Action[A]): Action[A] = AuthenticatedAction(action)

}

object Helper {
  val jwtService = JwtService

  def checkPermission[A](request: Request[A], action: Request[A] => Future[Result]) = {
    request.session.get("Authorization") match {
      case Some(token) if token startsWith "Bearer " =>
        jwtService.validateToken(token.stripPrefix("Bearer ")) match {
          case Some(_) => action(request)
          case None => Future.successful(Redirect(request.uri))
        }
      case None => Future.successful(Redirect(request.uri))
    }
  }
}


