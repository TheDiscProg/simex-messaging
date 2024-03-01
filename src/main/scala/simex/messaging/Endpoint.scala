package simex.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

/** The destination that handles the request, equivalent to the URL of an HTTP, the exchange/queue of messaging
  * @param resource - The unique identfier for routing messages to handlers/orchestrators
  * @param method - The method to apply - one of SELECT, UPDATE, INSERT, DELETE, PROCESS, RESPONSE - see Method
  * @param entity - a business object to which this request should be applied to
  */
case class Endpoint(
    resource: String,
    method: String,
    entity: Option[String]
)

object Endpoint {
  implicit val encoder: Encoder[Endpoint] = deriveEncoder
  implicit val decoder: Decoder[Endpoint] = deriveDecoder
}
