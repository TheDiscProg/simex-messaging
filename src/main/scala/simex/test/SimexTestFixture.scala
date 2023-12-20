package simex.test

import simex.messaging._
import simex.messaging.Simex._

trait SimexTestFixture {

  val endpoint = Endpoint(
    resource = "service.auth",
    method = "select",
    entity = None
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
    endpoint = endpoint,
    client = client,
    originator = originator,
    data = Vector()
  )

  val authenticationRequest = simexMessage.copy(
    endpoint = simexMessage.endpoint.copy(entity = Some(AUTHENTICATION_ENTITY)),
    data = Vector(
      Datum("username", "tester@test.com", None),
      Datum("password", "password1234", None)
    )
  )

  val refreshTokenRequest = simexMessage.copy(
    endpoint = simexMessage.endpoint.copy(entity = Some(REFRESH_TOKEN_ENTITY)),
    data = Vector(
      Datum("refresh_token", "sometoken", None)
    )
  )

  def getMessage(method: Method, entity: Option[String], data: Vector[Datum]): Simex =
    method match {
      case Method.SELECT =>
        simexMessage.copy(
          endpoint = simexMessage.endpoint.copy(method = "select", entity = entity),
          data = data
        )
      case Method.UPDATE =>
        simexMessage.copy(
          endpoint = simexMessage.endpoint.copy(method = "update", entity = entity),
          data = data
        )
      case Method.INSERT =>
        simexMessage.copy(
          endpoint = simexMessage.endpoint.copy(method = "insert", entity = entity),
          data = data
        )
      case Method.DELETE =>
        simexMessage.copy(
          endpoint = simexMessage.endpoint.copy(method = "delete", entity = entity),
          data = data
        )
      case Method.PROCESS =>
        simexMessage.copy(
          endpoint = simexMessage.endpoint.copy(method = "process", entity = entity),
          data = data
        )
      case Method.RESPONSE =>
        simexMessage.copy(
          endpoint = simexMessage.endpoint.copy(method = "response", entity = entity),
          data = data
        )
      case _ =>
        simexMessage.copy(
          endpoint = simexMessage.endpoint.copy(method = "unsupported", entity = entity),
          data = data
        )
    }

  val badSimexJson =
    """
      |{
      |  "endpoint" : {
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
      |      "value" : "1",
      |      "check" : "eq"
      |    }
      |  ]
      |}
      |""".stripMargin
}
