package simex.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Datum(
    field: String,
    value: String,
    check: Option[String]
)

object Datum {
  implicit val encoder: Encoder[Datum] = deriveEncoder
  implicit val decoder: Decoder[Datum] = deriveDecoder

  private val status = Datum("status", "", None)
  private val message = Datum("message", "", None)

  val OkayStatus = status.copy(value = "OK")
  val NotFoundStatus = status.copy(value = "Not Found")
  val AuthenticationFailed = status.copy(value = "Authentication failed")

  val NotFoundMessage = message.copy(value = "The requested data was not found")
  val authenticationFailedMessage =
    message.copy(value = "Either the username or the password did not match")

}
