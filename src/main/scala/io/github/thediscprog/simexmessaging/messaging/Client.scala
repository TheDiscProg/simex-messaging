package io.github.thediscprog.simexmessaging.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

/** Identifies the service that generated this message.
  *
  * The client identifies the immediate sender of the current message and may therefore change as a
  * message is propagated between services.
  *
  * @param clientId
  *   unique identifier for the client/system that sends this message
  * @param requestId
  *   identifier of the current request
  * @param service
  *   service that generated the current message
  * @param authorization
  *   authentication credential for the current sender
  */
case class Client(
    clientId: String,
    requestId: String,
    service: String,
    authorization: String
)

object Client {
  implicit val encoder: Encoder[Client] = deriveEncoder
  implicit val decoder: Decoder[Client] = deriveDecoder
}
