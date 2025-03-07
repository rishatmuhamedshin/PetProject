package controllers


import com.google.inject.Inject
import models.actions.AuthenticatedAction
import models.dto.LoginDTO
import models.services.JwtService
import play.api.data.Forms.{email, nonEmptyText}
import play.api.data.{Form, Forms, Mapping}
import play.api.mvc.{Action, Controller}


class AuthController extends Controller {


  val authenticatedAction = AuthenticatedAction
  val jwtService = JwtService

  println(s"AuthenticatedAction: $authenticatedAction")

  def secureEndpoint = authenticatedAction {
    Ok("You are authorized!")
  }


  val mappingForm: Mapping[LoginDTO] = Forms.mapping(
    "email" -> email,
    "password" -> nonEmptyText(minLength = 6)
  )(LoginDTO.apply)(LoginDTO.unapply)


  //  def login = Action(parse.json){request =>
  //    (request.body \ "email").asOpt[String] match {
  //      case Some(email) =>
  //        val token = jwtService.createToken(email)
  //        Ok(Json.obj("token" -> token))
  //      case None => BadRequest("Missing username")
  //    }
  //  }
  val form: Form[LoginDTO] = Form(mappingForm)

  def loginPage = Action{
    Ok(views.html.login(form))
  }

  def loginForSubmit() = Action{ implicit req =>
    form.bindFromRequest.fold(
    formWithErrors => BadRequest(views.html.login(formWithErrors)),
    dto =>{
        val token = jwtService.createToken(dto)
        Redirect(routes.AuthController.secureEndpoint())
          .withHeaders(("Authorization" -> s"Bearer $token"))
      }
    )
  }

}

