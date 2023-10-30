package dapex.messaging

import dapex.entities.ConversionError.{ParsingJsonError, ParsingStringError}
import dapex.messaging.Method.SELECT
import dapex.test.DapexMessageFixture
import io.circe.syntax._
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DapexMessageJsonTest
    extends AnyFlatSpec
    with DapexMessageFixture
    with Matchers
    with EitherValues {

  val authRequest: DapexMessage = getMessage(SELECT)
  val testJson = authRequest.asJson
  val testString = testJson.toString()

  it should "serialize a Dapex message" in {
    val serializedMsg = DapexMessage.serializeToString(authRequest)

    serializedMsg.nonEmpty shouldBe true
    serializedMsg shouldBe testString
  }

  it should "deserialize a Dapex message" in {
    val deserialisedMsg = DapexMessage.deSerializeFromString(testString)

    deserialisedMsg shouldBe Right(authRequest)
  }

  it should "handle string to Json conversion errors" in {
    val errorStr = s"$testString { "

    val error = DapexMessage.deSerializeFromString(errorStr)

    error.isLeft shouldBe true
    error.left.value shouldBe ParsingStringError(
      "expected whitespace or eof got '{ ' (line 35, column 3)"
    )
  }

  it should "handle json to dapex conversion error" in {
    val error = DapexMessage.deSerializeFromString(badDapexMessage)

    error.isLeft shouldBe true
    error.left.value shouldBe ParsingJsonError(
      "DecodingFailure at .resource: Missing required field"
    )
  }
}
