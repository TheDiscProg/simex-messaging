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
}
