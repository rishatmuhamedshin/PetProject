package controllers

import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Action, Controller}

case class Foo(f: String)

object Foo{
  implicit val writer: Writes[Foo] = Json.writes[Foo]
}

object IndexController extends Controller{

  def action1 = Action{
    Ok
  }

  def action2 = Action{
    NotFound
  }

  def action3 = Action{
    BadRequest
  }

  def action4 = Action{
    InternalServerError("Ooops")
  }

  def action5 = Action{
    Status(488)("Hello 488")
  }

  def action6 = Action{
    Ok(Json.toJson(Foo("foo")))
  }

  def action7(number: Int) = Action{
    Ok(s"The number is $number")
  }

  def action8(number: Int) = Action{
    Ok(s"The number is $number")
  }
}

