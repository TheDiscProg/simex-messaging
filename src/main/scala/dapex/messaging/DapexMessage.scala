package dapex.messaging

import dapex.entities.ConversionError
import dapex.messaging.Method.UNSUPPORTED
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, parser}

case class DapexMessage(
    endpoint: Endpoint,
    client: Client,
    originator: Originator,
    criteria: Vector[Criterion],
    update: Vector[UpdateDatumValue],
    insert: Vector[FieldValuePair],
    process: Vector[FieldValuePair],
    response: Option[Response]
)

object DapexMessage {
  implicit val encoder: Encoder[DapexMessage] = deriveEncoder
  implicit val decoder: Decoder[DapexMessage] = deriveDecoder

  def serializeToString(msg: DapexMessage): String = msg.asJson.toString()

  def deSerializeFromString(jsonString: String): Either[ConversionError, DapexMessage] =
    parser
      .parse(jsonString)
      .fold(
        err => Left(ConversionError(err.getMessage())),
        json =>
          json
            .as[DapexMessage]
            .fold(e => Left(ConversionError(e.getMessage())), msg => Right(msg))
      )

  def isValid(message: DapexMessage): Boolean = {
    val method = Method.fromString(message.endpoint.method)
    method match {
      case Method.SELECT =>
        checkSelectValidity(message)
      case Method.UPDATE =>
        checkUpdateValidity(message)
      case Method.INSERT =>
        checkInsertValidity(message)
      case Method.PROCESS =>
        checkProcessValidity(message)
      case Method.RESPONSE =>
        checkResponseValidity(message)
      case Method.UNSUPPORTED =>
        false
    }
  }

  def checkSelectValidity(message: DapexMessage): Boolean =
    checkEndPointValidity(message)

  def checkUpdateValidity(message: DapexMessage): Boolean =
    checkEndPointValidity(message) &&
      message.update.nonEmpty

  def checkInsertValidity(message: DapexMessage): Boolean =
    checkEndPointValidity(message) &&
      message.insert.nonEmpty

  def checkProcessValidity(message: DapexMessage): Boolean =
    checkEndPointValidity(message)

  def checkResponseValidity(message: DapexMessage): Boolean =
    checkEndPointValidity(message) &&
      message.response.isDefined

  def checkEndPointValidity(message: DapexMessage): Boolean = {
    val isResourceDefined = Option(message.endpoint.resource) match {
      case Some(value) => value.nonEmpty
      case _ => false
    }

    val isMethodDefined = Method.fromString(message.endpoint.method) !=
      UNSUPPORTED

    isResourceDefined && isMethodDefined
  }
}
