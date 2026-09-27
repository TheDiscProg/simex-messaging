package io.github.thediscprog.simexmessaging.messaging

import io.circe.syntax._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DestinationTest extends AnyWordSpec with Matchers {

  private val validDestination = Destination(
    service = "test-service",
    resource = "test-resource",
    resourceId = Some("12345"),
    operation = Operation.SELECT
  )

  "Destination.isDestinationValid" should {

    "return true when the destination is valid" in {
      Destination.isDestinationValid(validDestination) shouldBe true
    }

    "return true when resourceId is None" in {
      val destination = validDestination.copy(
        resourceId = None
      )

      Destination.isDestinationValid(destination) shouldBe true
    }

    "return true for each supported operation" in
      Operation.values
        .filterNot(_ == Operation.UNSUPPORTED)
        .foreach { operation =>
          val destination = validDestination.copy(
            operation = operation
          )

          Destination.isDestinationValid(destination) shouldBe true
        }

    "return false when service is empty" in {
      val destination = validDestination.copy(
        service = ""
      )

      Destination.isDestinationValid(destination) shouldBe false
    }

    "return false when resource is empty" in {
      val destination = validDestination.copy(
        resource = ""
      )

      Destination.isDestinationValid(destination) shouldBe false
    }

    "return false when operation is unsupported" in {
      val destination = validDestination.copy(
        operation = Operation.UNSUPPORTED
      )

      Destination.isDestinationValid(destination) shouldBe false
    }

    "return false when service and resource are empty" in {
      val destination = validDestination.copy(
        service = "",
        resource = ""
      )

      Destination.isDestinationValid(destination) shouldBe false
    }

    "return false when service is empty and operation is unsupported" in {
      val destination = validDestination.copy(
        service = "",
        operation = Operation.UNSUPPORTED
      )

      Destination.isDestinationValid(destination) shouldBe false
    }

    "return false when resource is empty and operation is unsupported" in {
      val destination = validDestination.copy(
        resource = "",
        operation = Operation.UNSUPPORTED
      )

      Destination.isDestinationValid(destination) shouldBe false
    }

    "return false when all required fields are invalid" in {
      val destination = validDestination.copy(
        service = "",
        resource = "",
        operation = Operation.UNSUPPORTED
      )

      Destination.isDestinationValid(destination) shouldBe false
    }

    "encode and decode a destination" in {
      val json = validDestination.asJson

      json.as[Destination] shouldBe Right(validDestination)
    }

    "encode an operation using its string value" in {
      val json = validDestination.asJson

      json.hcursor.get[String]("operation") shouldBe Right("select")
    }

    "encode a None resourceId as null" in {
      val destination = validDestination.copy(resourceId = None)

      val json = destination.asJson

      json.hcursor.get[Option[String]]("resourceId") shouldBe Right(None)
    }
  }
}
