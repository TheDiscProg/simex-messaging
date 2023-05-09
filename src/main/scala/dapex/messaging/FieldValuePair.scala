package dapex.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class FieldValuePair(field: String, value: String)

object FieldValuePair {
  implicit val encoder: Encoder[FieldValuePair] = deriveEncoder
  implicit val decoder: Decoder[FieldValuePair] = deriveDecoder
}
