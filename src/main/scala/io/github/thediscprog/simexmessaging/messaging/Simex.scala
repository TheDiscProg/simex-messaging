package io.github.thediscprog.simexmessaging.messaging

import io.github.thediscprog.simexmessaging.entities.ConversionError.{ParsingJsonError, ParsingStringError}
import Method.UNSUPPORTED
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, parser}
import io.github.thediscprog.simexmessaging.entities.ConversionError

case class Simex(
    destination: Endpoint,
    client: Client,
    originator: Originator,
    data: Vector[Datum]
) {

  import Simex._

  /**
   * Gets the username by finding the "username"
   *
   * @return option of username
   */
  def getUsername: Option[String] =
    extractDatumByFieldname(USERNAME)
      .map(_.value)
      .flatMap(_.getLeft)

  /**
   * Gets the password by find the "password"
   *
   * @return option of password
   */
  def getPassword: Option[String] =
    extractDatumByFieldname(PASSWORD)
      .map(_.value)
      .flatMap(_.getLeft)

  /**
   * Gets the authorization token from the client section
   *
   * @return the authorization token from client
   */
  def getAuthorization: String = this.client.authorization

  /**
   * Gets the refresh token from the data field by matching "refresh_token
   * "
   * @return option of refresh token
   */
  def getRefreshToken: Option[String] =
    extractDatumByFieldname(REFRESH_TOKEN)
      .map(_.value)
      .flatMap(_.getLeft)

  /**
   * Replaces the data value in the data section
   * @param datum the data to match with new value
   * @return updated Simex
   */
  def replaceDatum(datum: Datum): Simex =
    this
      .copy(
        data = this.data.filter(!_.field.equalsIgnoreCase(datum.field)) :+ datum
      )

  /**
   * Gets the datum by matching the field name
   *
   * @param field the name to match
   * @return option of datum
   */
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
