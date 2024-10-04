package io.github.thediscprog.simexmessaging.messaging

import io.circe.syntax._
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.github.thediscprog.simexmessaging.entities.ConversionError.{ParsingJsonError, ParsingStringError}
import io.github.thediscprog.simexmessaging.test.SimexTestFixture

class SimexJsonTest extends AnyFlatSpec with SimexTestFixture with Matchers with EitherValues {

  val authRequest: Simex = authenticationRequest
  val testJson = authRequest.asJson
  val testString = testJson.toString()

  it should "serialize a Simex message" in {
    val serializedMsg = Simex.serializeToString(authRequest)

    serializedMsg.nonEmpty shouldBe true
    serializedMsg shouldBe testString
  }

  it should "deserialize a Simex message" in {
    val deserialisedMsg = Simex.deSerializeFromString(testString)

    deserialisedMsg shouldBe Right(authRequest)
    deserialisedMsg.value.destination.version shouldBe "v1"
  }

  it should "handle string to Json conversion errors" in {
    val errorStr = s"$testString { "

    val error = Simex.deSerializeFromString(errorStr)

    error.isLeft shouldBe true
    error.left.value shouldBe ParsingStringError(
      "expected whitespace or eof got '{ ' (line 34, column 3)"
    )
  }

  it should "handle json to simex conversion error" in {
    val error = Simex.deSerializeFromString(badSimexJson)

    error.isLeft shouldBe true
    error.left.value shouldBe ParsingJsonError(
      "DecodingFailure at .resource: Missing required field"
    )
  }
}
