package io.github.thediscprog.simexmessaging.messaging

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class OperationTest extends AnyWordSpec with Matchers {

  "Operation" should {

    "contain all supported operations" in {
      Operation.values should contain theSameElementsAs Seq(
        Operation.SELECT,
        Operation.UPDATE,
        Operation.INSERT,
        Operation.DELETE,
        Operation.PROCESS,
        Operation.RESPONSE,
        Operation.UNSUPPORTED
      )
    }

    "have the correct string values" in {
      Operation.SELECT.value shouldBe "select"
      Operation.UPDATE.value shouldBe "update"
      Operation.INSERT.value shouldBe "insert"
      Operation.DELETE.value shouldBe "delete"
      Operation.PROCESS.value shouldBe "process"
      Operation.RESPONSE.value shouldBe "response"
      Operation.UNSUPPORTED.value shouldBe "unsupported"
    }
  }
}
