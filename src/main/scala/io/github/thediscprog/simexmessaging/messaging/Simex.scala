package io.github.thediscprog.simexmessaging.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Simex[A](
    destination: Destination,
    client: Client,
    originator: Originator,
    timestamp: String,
    data: A
)

object Simex {
  implicit def encoder[A](implicit encoder: Encoder[A]): Encoder[Simex[A]] = deriveEncoder
  implicit def decoder[A](implicit decoder: Decoder[A]): Decoder[Simex[A]] = deriveDecoder
}
