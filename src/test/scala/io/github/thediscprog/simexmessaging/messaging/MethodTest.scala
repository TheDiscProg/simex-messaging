package io.github.thediscprog.simexmessaging.messaging

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Method.{DELETE, INSERT, PROCESS, RESPONSE, SELECT, UNSUPPORTED, UPDATE}

class MethodTest extends AnyFlatSpec with Matchers {

  it should "return unsupported for a null string" in {
    val result = Method.fromString(null)
    result shouldBe UNSUPPORTED
  }

  it should "return unsupported for an empty string" in {
    val result = Method.fromString("")
    result shouldBe UNSUPPORTED
  }

  it should "return non-recognised string" in {
    val result = Method.fromString("alj")
    result shouldBe UNSUPPORTED
  }

  it should "return select" in {
    val result = Method.fromString("sEleCT")
    result shouldBe SELECT
  }

  it should "return update" in {
    val result = Method.fromString("UpDAte")
    result shouldBe UPDATE
  }

  it should "return insert" in {
    val result = Method.fromString("InSErT")
    result shouldBe INSERT
  }

  it should "return delete" in {
    val result = Method.fromString("dELete")
    result shouldBe DELETE
  }

  it should "return process" in {
    val result = Method.fromString("proCESS")
    result shouldBe PROCESS
  }

  it should "return response" in {
    val result = Method.fromString("RESPONSe")
    result shouldBe RESPONSE
  }
}
