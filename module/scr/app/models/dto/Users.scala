package models.dto

import play.api.data.Form
import play.api.data.Forms.{email, mapping, nonEmptyText, number}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.QueryStringBindable

case class Users(name: String, surname: String, age: Int, gender: String)


object Users {
  val registrationNewUsers: Form[Users] = Form(
    mapping(
      "name" -> nonEmptyText,
      "surname" -> nonEmptyText(minLength = 3),
      "age" -> number(min = 0, max = 100),
      "gender" -> nonEmptyText
    )(Users.apply)(Users.unapply) verifying("Нельзя выбирать такое имя", field => field match{
      case users => validate(users).isDefined
    })
  )

  private def validate(users: Users) = {
    users.name match {
      case "admin" => None
      case _ => Some(users)
    }
  }

  implicit val writes: Writes[Users] = (
    (__ \ "name").write[String] and
      (JsPath \ "surname").write[String] and
      (__ \ "age").write[Int] and
      (__ \ "gender").write[String]
  )(unlift(Users.unapply))
  implicit val reads: Reads[Users] = (
      (__ \ "name").read[String] and
      (JsPath \ "surname").read[String] and
      (__ \ "age").read[Int] and
      (__ \ "gender").read[String]
    )(Users.apply _)


  implicit val format: Format[Users] = (
    (__ \ "name").format[String] and
      (__ \ "surname").format[String] and
      (__ \ "age").format[Int] and
      (__ \ "gender").format[String]
  )(Users.apply, unlift(Users.unapply))

  implicit def quryBind(implicit intBinder: QueryStringBindable[Int], stringBinder: QueryStringBindable[String]): QueryStringBindable[Users] =
    new QueryStringBindable[Users] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Users]] = ???


      override def unbind(key: String, value: Users): String = ???
    }
}