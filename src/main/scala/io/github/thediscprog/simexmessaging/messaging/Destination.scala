package io.github.thediscprog.simexmessaging.messaging

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.github.thediscprog.simexmessaging.messaging.Operation.UNSUPPORTED

/** Describes the route/destination of the service that will handle this message
  * @param service - The unique identfier for routing messages to handlers/orchestrators
  * @param resource - a business object/resource to which this request should be applied to
  * @param resourceId - optional, but indicates the ID of the resource to which this should apply to
  * @param operation - The operation to apply to the resource - see Operation
  * @param version - the version of the call, defaults to v1
  */
case class Destination(
    service: String,
    resource: String,
    resourceId: Option[String],
    operation: Operation,
    version: String = "v1"
)

object Destination {
  implicit val encoder: Encoder[Destination] = deriveEncoder
  implicit val decoder: Decoder[Destination] = deriveDecoder

  def isDestinationValid[A](destination: Destination): Boolean = {
    val isServiceDefined = destination.service.nonEmpty
    val isResourceDefined = destination.resource.nonEmpty
    val isOperationDefined = destination.operation != UNSUPPORTED

    isServiceDefined && isResourceDefined && isOperationDefined
  }
}
