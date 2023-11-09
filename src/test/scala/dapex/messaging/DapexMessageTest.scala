package dapex.messaging

import dapex.messaging.Method.{DELETE, INSERT, PROCESS, RESPONSE, SELECT, UPDATE}
import dapex.test.DapexMessageFixture
import org.scalatest.OptionValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class DapexMessageTest extends AnyFlatSpec with DapexMessageFixture with OptionValues {

  it should "verify endpoint" in {
    DapexMessage.checkEndPointValidity(dapexMessage) shouldBe true
  }

  it should "fail endpoint verification when endpoint is empty" in {
    val badMessage = dapexMessage.copy(endpoint = dapexMessage.endpoint.copy(resource = ""))

    DapexMessage.checkEndPointValidity(badMessage) shouldBe false
  }

  it should "fail endpoint verification when endpoint is null" in {
    val badMessage = dapexMessage.copy(endpoint = dapexMessage.endpoint.copy(resource = null))

    DapexMessage.checkEndPointValidity(badMessage) shouldBe false
  }

  it should "fail endpoint verification when method is an empty string" in {
    val badMessage = dapexMessage.copy(endpoint = dapexMessage.endpoint.copy(method = ""))

    DapexMessage.checkEndPointValidity(badMessage) shouldBe false
  }

  it should "fail endpoint verification when method is null" in {
    val badMessage = dapexMessage.copy(endpoint = dapexMessage.endpoint.copy(method = null))

    DapexMessage.checkEndPointValidity(badMessage) shouldBe false
  }

  it should "verify select request" in {
    val select = getMessage(SELECT)

    DapexMessage.isValid(select) shouldBe true
  }

  it should "verify update request" in {
    val update = getMessage(UPDATE)

    DapexMessage.isValid(update) shouldBe true
  }

  it should "not verify update request when update section is empty" in {
    val update = getMessage(UPDATE).copy(update = Vector())

    DapexMessage.isValid(update) shouldBe false
  }

  it should "verify insert request" in {
    val insert = getMessage(INSERT)

    DapexMessage.isValid(insert) shouldBe true
  }

  it should "not verify insert request when insert section is empty" in {
    val insert = getMessage(INSERT).copy(insert = Vector())

    DapexMessage.isValid(insert) shouldBe false
  }

  it should "verify delete request" in {
    val delete = getMessage(DELETE)

    DapexMessage.isValid(delete) shouldBe true
  }

  it should "not verify delete request when criteria section is empty" in {
    val delete = getMessage(DELETE).copy(criteria = Vector())

    DapexMessage.isValid(delete) shouldBe false
  }

  it should "verify process request" in {
    val process = getMessage(PROCESS)

    DapexMessage.isValid(process) shouldBe true
  }

  it should "verify a response" in {
    val response = getMessage(RESPONSE)

    DapexMessage.isValid(response) shouldBe true
  }

  it should "not verify a response when it is not defined" in {
    val response = getMessage(RESPONSE).copy(response = None)

    DapexMessage.isValid(response) shouldBe false
  }

  it should "replace a criterion" in {
    val criterion = Criterion("password", "hash", "EQ")

    val result = authenticationRequest.replaceCriterion(criterion)

    result.criteria.size shouldBe 2
    val replaced = result.criteria.find(_.field == "password").value
    replaced shouldBe criterion
  }

  it should "extract username and password" in {
    val username = authenticationRequest.getUsername
    val password = authenticationRequest.getPassword

    username.isDefined shouldBe true
    password.isDefined shouldBe true
    username.value.value shouldBe "tester@test.com"
    password.value.value shouldBe "password1234"
  }

  it should "get authorization token" in {
    val select = getMessage(SELECT)
    val accessToken = select.getAuthorization

    accessToken shouldBe "securitytoken"
  }

  it should "get refresh token" in {
    val select = refreshTokenRequest
    val refreshToken = select.getRefreshToken

    refreshToken.isDefined shouldBe true
    refreshToken.value shouldBe "sometoken"
  }
}
