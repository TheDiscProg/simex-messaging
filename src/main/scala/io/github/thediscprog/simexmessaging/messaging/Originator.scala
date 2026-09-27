package io.github.thediscprog.simexmessaging.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

/** Identifies the originator of the original request.
  *
  * The originator is established by the original requester and must not change as the request is
  * propagated between services. The originator's requestId is used to correlate the eventual
  * response with the original request.
  *
  * @param clientId
  *   unique identifier of the original client/system that sent the message
  * @param requestId
  *   identifier of the original request
  * @param service
  *   service that generated the original request
  * @param originalToken
  *   authentication token supplied with the original request
  */
case class Originator(
    clientId: String,
    requestId: String,
    service: String,
    originalToken: String
)

object Originator {
  implicit val encoder: Encoder[Originator] = deriveEncoder
  implicit val decoder: Decoder[Originator] = deriveDecoder
}
