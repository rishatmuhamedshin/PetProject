package controllers

import models.actions.AuthenticatedAction
import play.api.Play
import play.api.mvc.{Action, Controller}

object IndexController extends Controller {

  private val config = Play.current.configuration


  def homePage =
    AuthenticatedAction (
      Action {
        Ok(views.html.homePage())
      }
  )
}

