package simex.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

/** Identifies the service that generated this request
  * @param clientId - Unique across systems - can be hostname or some other way to identify the system
  * @param requestId - Request ID, unique when matched with client ID
  * @param sourceEndpoint - the resource/sourceEndpoint destination that generated the request
  * @param authorization - the authorisation string that authenticates that this is a genuine request
  */
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
