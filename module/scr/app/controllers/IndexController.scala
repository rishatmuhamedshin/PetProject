package controllers

import controllers.Assets.Redirect
import models.actions.AuthenticatedAction
import models.dto.Users
import play.api.Play
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Action, Controller}

import scala.collection.mutable
import scala.util.Random

case class Foo(f: String)

object Foo {
  implicit val writer: Writes[Foo] = Json.writes[Foo]
}

object IndexController extends Controller {

  private val config = Play.current.configuration

  def action1 = Action {
    Ok
  }

  def action2 = Action {
    NotFound
  }

  def action3 = Action {
    BadRequest
  }

  def action4 = Action {
    InternalServerError("Ooops")
  }

  def action5 = Action {
    Status(488)("Hello 488")
  }

  def action6 = Action {
    Ok(Json.toJson(Foo("foo")))
  }

  def action7(number: Int) = Action {
    Ok(s"The number is $number")
  }

  def action8(number: Int) = Action {
    Ok(s"The number is $number")
  }


  def action10 = {
    //TODO: Просто попробовал поиграться с файлом, потом переделать с Try
    AuthenticatedAction {
      Action {
        Ok.sendFile(
          content = new java.io.File(config.getString("file.location").getOrElse("")),
          inline = true,
          fileName = _ => "SystemDesign.pdf"
        )
      }
    }
  }


  def action11 = Action {

    val mathRandom = Random.nextInt(3)
    val word = mathRandom match {
      case 1 => "Green"
      case 2 => "Blue"
      case _ => "Red"
    }

    Ok(views.html.Joke(word))
  }

}

