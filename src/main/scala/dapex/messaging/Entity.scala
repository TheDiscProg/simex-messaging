package dapex.messaging

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Entity(name: String)

object Entity {
  implicit val encoder: Encoder[Entity] = deriveEncoder
  implicit val decoder: Decoder[Entity] = deriveDecoder
}
