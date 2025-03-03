package models.actions

import models.services.JwtService
import play.api.mvc._

import scala.concurrent.Future


object AuthenticatedAction extends ActionBuilder[Request]{
  val jwtService = JwtService
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
    request.headers.get("Authorization") match {
      case Some(token) if token startsWith "Bearer " =>
        jwtService.validateToken(token.stripPrefix("Bearer ")) match {
          case Some(_) => block(request)
          case None => Future.successful(Results.Unauthorized("Invalid Token"))
        }
      case None => Future.successful(Results.Unauthorized("Missing token"))
    }
}

