package dapex.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Response(
    status: String,
    message: String,
    data: Vector[FieldValuePair]
)

object Response {

  implicit lazy val encoder: Encoder[Response] = deriveEncoder

  implicit lazy val decoder: Decoder[Response] = deriveDecoder
}
