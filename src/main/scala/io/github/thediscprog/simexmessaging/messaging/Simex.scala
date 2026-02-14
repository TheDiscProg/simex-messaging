package io.github.thediscprog.simexmessaging.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.github.thediscprog.simexmessaging.messaging.Method.UNSUPPORTED

case class Simex[A](
    destination: Endpoint,
    client: Client,
    originator: Originator,
    data: A
)

object Simex {
  implicit def encoder[A](implicit encoder: Encoder[A]): Encoder[Simex[A]] = deriveEncoder
  implicit def decoder[A](implicit decoder: Decoder[A]): Decoder[Simex[A]] = deriveDecoder

  def checkEndPointValidity[A](message: Simex[A]): Boolean = {
    val isResourceDefined = message.destination.resource.nonEmpty
    val isMethodDefined = Method.fromString(message.destination.method) !=
      UNSUPPORTED

    isResourceDefined && isMethodDefined
  }
}
