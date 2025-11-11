package io.github.thediscprog.simexmessaging.test

import io.github.thediscprog.simexmessaging.messaging.{Client, Datum, Endpoint, Method, Originator, Simex}
import io.github.thediscprog.simexmessaging.messaging.Simex._
import io.github.thediscprog.slogic.Xor

trait SimexTestFixture {

  val endpoint = Endpoint(
    resource = "service.auth",
    method = "select",
    entity = None,
    timestamp = Some("2023-01-01T00:00:00.000Z"),
    version = "v1"
  )

  val client = Client(
    clientId = "client1",
    requestId = "request1",
    sourceEndpoint = "client",
    authorization = "securitytoken"
  )

  val originator =
    Originator(
      clientId = "client1",
      requestId = "request1",
      sourceEndpoint = "client",
      originalToken = "security123",
      security = "1",
      messageTTL = Some(255L)
    )

  val simexMessage = Simex(
    destination = endpoint,
    client = client,
    originator = originator,
    data = Vector()
  )

  val authenticationRequest = simexMessage.copy(
    destination = simexMessage.destination.copy(entity = Some(AUTHENTICATION_ENTITY)),
    data = Vector(
      Datum("username", None, Xor.applyLeft("tester@test.com")),
      Datum("password", None, Xor.applyLeft("password1234"))
    )
  )

  val refreshTokenRequest = simexMessage.copy(
    destination = simexMessage.destination.copy(entity = Some(REFRESH_TOKEN_ENTITY)),
    data = Vector(
      Datum("refresh_token", None, Xor.applyLeft("sometoken"))
    )
  )

  def getMessage(method: Method, entity: Option[String], data: Vector[Datum]): Simex =
    method match {
      case Method.SELECT =>
        simexMessage.copy(
          destination = simexMessage.destination.copy(method = "select", entity = entity),
          data = data
        )
      case Method.UPDATE =>
        simexMessage.copy(
          destination = simexMessage.destination.copy(method = "update", entity = entity),
          data = data
        )
      case Method.INSERT =>
        simexMessage.copy(
          destination = simexMessage.destination.copy(method = "insert", entity = entity),
          data = data
        )
      case Method.DELETE =>
        simexMessage.copy(
          destination = simexMessage.destination.copy(method = "delete", entity = entity),
          data = data
        )
      case Method.PROCESS =>
        simexMessage.copy(
          destination = simexMessage.destination.copy(method = "process", entity = entity),
          data = data
        )
      case Method.RESPONSE =>
        simexMessage.copy(
          destination = simexMessage.destination.copy(method = "response", entity = entity),
          data = data
        )
      case _ =>
        simexMessage.copy(
          destination = simexMessage.destination.copy(method = "unsupported", entity = entity),
          data = data
        )
    }

  val badSimexJson =
    """
      |{
      |  "destination" : {
      |    "method" : "select"
      |  },
      |  "client" : {
      |    "clientId" : "client1",
      |    "requestId" : "request1",
      |    "sourceEndpoint" : "client",
      |    "authorization" : "securitytoken"
      |  },
      |  "originator" : {
      |    "clientId" : "client1",
      |    "requestId" : "request1",
      |    "sourceEndpoint" : "client",
      |    "originalToken" : "securitytoken",
      |    "security": "1"
      |  },
      |  "data" : [
      |    {
      |      "field" : "customerId",
      |      "check" : "eq",
      |      "value" : "1"
      |    }
      |  ]
      |}
      |""".stripMargin
}
