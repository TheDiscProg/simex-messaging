package simex.messaging

import io.circe.Decoder.Result
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe._
import simex.entities.ConversionError.ParsingJsonError
import thediscprog.slogic.Xor

/** Class to hold a single piece of data - hence datum
  * @param field - the name of the datum
  * @param check - Optional checks as determined by application
  * @param value - the value of the datum, either a single value or a vector of Datum
  */
case class Datum(
    field: String,
    check: Option[String],
    value: Xor[String, Vector[Datum]]
)

object Datum {

  private val status = Datum("status", None, Xor.applyLeft(""))
  private val message = Datum("message", None, Xor.applyLeft(""))

  val OkayStatus = status.copy(value = Xor.applyLeft("OK"))
  val NotFoundStatus = status.copy(value = Xor.applyLeft("Not Found"))
  val AuthenticationFailed = status.copy(value = Xor.applyLeft("Authentication failed"))

  val NotFoundMessage = message.copy(value = Xor.applyLeft("The requested data was not found"))
  val authenticationFailedMessage =
    message.copy(value = Xor.applyLeft("Either the username or the password did not match"))

  implicit val encoder: Encoder[Datum] = new Encoder[Datum] {
    override def apply(a: Datum): Json = Json.obj(
      ("field", Json.fromString(a.field)),
      ("check", a.check.fold(Json.Null)(c => Json.fromString(c))),
      (
        "value",
        if (a.value.isLeft)
          a.value.getLeft.fold(Json.Null)(f => Json.fromString(f))
        else {
          // this is a list of Datum
          a.value.getRight.fold(Json.Null)(v => v.asJson)
        }
      )
    )
  }

  implicit val decoder: Decoder[Datum] = new Decoder[Datum] {

    override def apply(c: HCursor): Result[Datum] = {
      val value = c.downField("value")
      for {
        f <- c.downField("field").as[String]
        c <- c.downField("check").as[Option[String]]
        v <-
          if (value.as[String].isRight) {
            value
              .as[String]
              .map(s => Xor.applyLeft(s): Xor[String, Vector[Datum]])
          } else {
            value
              .as[Seq[Datum]]
              .map(s => Xor.applyRight(s.toVector): Xor[String, Vector[Datum]])
          }
      } yield Datum(f, c, v)
    }
  }

  def decode(rst: Result[Datum]): Either[ParsingJsonError, Datum] =
    rst.fold(
      err => Left(ParsingJsonError(err.getMessage())),
      datum => Right(datum)
    )
}
