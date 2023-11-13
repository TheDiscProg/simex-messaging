package simex.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Client(
    clientId: String,
    requestId: String,
    sourceEndpoint: String,
    authorization: String
)

object Client {
  implicit val encoder: Encoder[Client] = deriveEncoder
  implicit val decoder: Decoder[Client] = deriveDecoder
}
