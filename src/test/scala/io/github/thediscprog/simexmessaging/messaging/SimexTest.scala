package io.github.thediscprog.simexmessaging.messaging

import io.github.thediscprog.simexmessaging.test.SimexTestFixture
import org.scalatest.OptionValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SimexTest extends AnyFlatSpec with SimexTestFixture with Matchers with OptionValues {

  it should "verify destination" in {
    Simex.checkEndPointValidity(simexMessage) shouldBe true
  }

  it should "fail destination verification when destination is empty" in {
    val badMessage = simexMessage.copy(destination = simexMessage.destination.copy(resource = ""))

    Simex.checkEndPointValidity(badMessage) shouldBe false
  }

  it should "fail destination verification when method is an empty string" in {
    val badMessage = simexMessage.copy(destination = simexMessage.destination.copy(method = ""))

    Simex.checkEndPointValidity(badMessage) shouldBe false
  }

  it should "fail destination verification when method is null" in {
    val badMessage = simexMessage.copy(destination = simexMessage.destination.copy(method = null))

    Simex.checkEndPointValidity(badMessage) shouldBe false
  }

  it should "extract username and password" in {
    val username = authenticationRequest.getUsername
    val password = authenticationRequest.getPassword

    (username.isDefined &&
      password.isDefined &&
    username.value == "tester@test.com" &&
    password.value == "password1234") shouldBe true
  }

  it should "get authorization token" in {
    val select = authenticationRequest
    val accessToken = select.getAuthorization

    accessToken shouldBe "securitytoken"
  }

  it should "get refresh token" in {
    val select = refreshTokenRequest
    val refreshToken = select.getRefreshToken

    (refreshToken.isDefined &&
    refreshToken.value == "sometoken") shouldBe true
  }
}
