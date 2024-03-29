package simex.messaging

import simex.entities.ConversionError
import simex.entities.ConversionError.{ParsingJsonError, ParsingStringError}
import simex.messaging.Method.UNSUPPORTED
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, parser}

case class Simex(
    destination: Endpoint,
    client: Client,
    originator: Originator,
    data: Vector[Datum]
) {
  import Simex._
  def getUsername: Option[String] =
    extractDatumByFieldname(USERNAME)
      .map(_.value)
      .flatMap(_.getLeft)

  def getPassword: Option[String] =
    extractDatumByFieldname(PASSWORD)
      .map(_.value)
      .flatMap(_.getLeft)

  def getAuthorization: String = this.client.authorization

  def getRefreshToken: Option[String] =
    extractDatumByFieldname(REFRESH_TOKEN)
      .map(_.value)
      .flatMap(_.getLeft)

  def replaceDatum(datum: Datum): Simex =
    this
      .copy(
        data = this.data.filter(!_.field.equalsIgnoreCase(datum.field)) :+ datum
      )

  def extractDatumByFieldname(field: String): Option[Datum] =
    this.data.find(_.field.equalsIgnoreCase(field))
}

object Simex {
  implicit val encoder: Encoder[Simex] = deriveEncoder
  implicit val decoder: Decoder[Simex] = deriveDecoder

  val USERNAME = "username"
  val PASSWORD = "password"
  // Entity Definitions
  val AUTHENTICATION_ENTITY = "authentication"
  val REFRESH_TOKEN_ENTITY = "refresh"
  val REGISTRATION_ENTITY = "registration"

  // data field definitions
  val AUTHORIZATION = "authorization"
  val REFRESH_TOKEN = "refresh_token"

  def serializeToString(msg: Simex): String = msg.asJson.toString()

  def deSerializeFromString(jsonString: String): Either[ConversionError, Simex] =
    parser
      .parse(jsonString)
      .fold(
        err => Left(ParsingStringError(err.getMessage())),
        json =>
          json
            .as[Simex]
            .fold(e => Left(ParsingJsonError(e.getMessage())), msg => Right(msg))
      )

  def checkEndPointValidity(message: Simex): Boolean = {
    val isResourceDefined = message.destination.resource.nonEmpty
    val isMethodDefined = Method.fromString(message.destination.method) !=
      UNSUPPORTED

    isResourceDefined && isMethodDefined
  }

  def setReturnToSender(msg: Simex): Simex = ???
}
