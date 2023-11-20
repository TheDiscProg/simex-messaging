package simex.messaging

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import simex.messaging.Security.{AUTHORIZED, BASIC, FORBIDDEN, ORIGINAL_TOKEN}

class SecurityTest extends AnyFlatSpec with Matchers {

  it should "return forbidden when string is null" in {
    val result = Security.fromString(null)
    result shouldBe FORBIDDEN
  }
  it should "return forbidden when security level is empty" in {
    val result = Security.fromString("")
    result shouldBe FORBIDDEN
  }
  it should "return forbidden when security level cannot be determined" in {
    val result = Security.fromString("dfie")
    result shouldBe FORBIDDEN
  }
  it should "return basic when level 1" in {
    val result = Security.fromString("Level 1")
    result shouldBe BASIC
  }
  it should "return basic when 1" in {
    val result = Security.fromString("1")
    result shouldBe BASIC
  }
  it should "return authorized when level 2" in {
    val result = Security.fromString("leveL 2")
    result shouldBe AUTHORIZED
  }

  it should "return authorized when  2" in {
    val result = Security.fromString("2")
    result shouldBe AUTHORIZED
  }
  it should "return original token when level 3" in {
    val result = Security.fromString("level 3")
    result shouldBe ORIGINAL_TOKEN
  }
  it should "return original token when 3" in {
    val result = Security.fromString("3")
    result shouldBe ORIGINAL_TOKEN
  }
}
