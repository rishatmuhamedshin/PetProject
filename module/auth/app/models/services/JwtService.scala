package models.services
import models.dto.LoginDTO
import pdi.jwt.JwtAlgorithm.HS256
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import play.api.libs.json.Json

import java.time.Instant
import play.api.Play


object JwtService  {
  private val config = Play.current.configuration
  private val secretKey: String = config.getString("jwt.secret").getOrElse("")
  private val expirationTime: Long = config.getLong("jwt.expiration-time").getOrElse(0L)


  def createToken(dto: LoginDTO): String = {
    val claim = JwtClaim(
      content = Json.obj("email" -> dto.email,
                         "password" -> dto.password).toString(),
      expiration = Some(Instant.now.plusSeconds(expirationTime).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond)
    )
    Jwt.encode(claim , secretKey, HS256)
  }

  def validateToken(token: String) = {
    Jwt.decode(token, secretKey, Seq(JwtAlgorithm.HS256)).toOption
  }
}
