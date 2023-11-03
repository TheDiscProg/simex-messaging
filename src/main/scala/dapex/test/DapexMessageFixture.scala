package dapex.test

import dapex.messaging._

trait DapexMessageFixture {

  val endpoint = Endpoint(resource = "service.auth", method = "select")

  val client = Client(
    clientId = "client1",
    requestId = "request1",
    sourceEndpoint = "client",
    authorisation = "securitytoken"
  )

  val originator =
    Originator(
      clientId = "client1",
      requestId = "request1",
      sourceEndpoint = "client",
      originalToken = "security123"
    )

  val criteria = Vector(
    Criterion(field = "customerId", value = "1", operator = "eq")
  )

  val update = Vector(
    UpdateDatumValue(
      field = "firstName",
      oldValue = Some("marky"),
      newValue = "Mark"
    )
  )

  val insert = Vector(
    FieldValuePair("firstName", "John"),
    FieldValuePair("surname", "Smith")
  )

  val process = Vector(
    FieldValuePair("arg1", "1230")
  )

  val response = Response(
    status = "okay",
    message = "No message",
    data = Vector(
      FieldValuePair("firstName", "John"),
      FieldValuePair("surname", "Smith")
    )
  )

  val dapexMessage = DapexMessage(
    endpoint = endpoint,
    client = client,
    originator = originator,
    entity = None,
    criteria = criteria,
    update = update,
    insert = insert,
    process = process,
    response = Some(response)
  )

  val authenticationRequest = dapexMessage.copy(
    entity = Some(Entity("user")),
    criteria = Vector(
      Criterion("username", "tester@test.com", "EQ"),
      Criterion("password", "password1234", "EQ")
    )
  )

  val refreshTokenRequest = dapexMessage.copy(
    entity = Some(Entity("refresh_token")),
    criteria = Vector(
      Criterion("refresh_token", "sometoken", "EQ")
    )
  )

  def getMessage(method: Method): DapexMessage =
    method match {
      case Method.SELECT =>
        dapexMessage.copy(
          endpoint = dapexMessage.endpoint.copy(method = "select"),
          entity = Some(Entity("person")),
          update = Vector(),
          insert = Vector(),
          process = Vector(),
          response = None
        )
      case Method.UPDATE =>
        dapexMessage.copy(
          endpoint = dapexMessage.endpoint.copy(method = "update"),
          entity = Some(Entity("person")),
          criteria = criteria :+ Criterion(field = "customerId", value = "1", operator = "eq"),
          insert = Vector(),
          process = Vector(),
          response = None
        )
      case Method.INSERT =>
        dapexMessage.copy(
          endpoint = dapexMessage.endpoint.copy(method = "insert"),
          entity = Some(Entity("person")),
          criteria = Vector(),
          update = Vector(),
          process = Vector(),
          response = None
        )
      case Method.DELETE =>
        dapexMessage.copy(
          endpoint = dapexMessage.endpoint.copy(method = "delete"),
          entity = Some(Entity("person")),
          criteria = criteria :+ Criterion(field = "customerId", value = "1", operator = "eq"),
          update = Vector(),
          process = Vector(),
          response = None
        )
      case Method.PROCESS =>
        dapexMessage.copy(
          endpoint = dapexMessage.endpoint.copy(method = "process"),
          criteria = Vector(),
          update = Vector(),
          insert = Vector(),
          response = None
        )
      case Method.RESPONSE =>
        dapexMessage.copy(
          endpoint = dapexMessage.endpoint.copy(method = "response"),
          criteria = Vector(),
          update = Vector(),
          insert = Vector(),
          process = Vector()
        )
      case _ =>
        dapexMessage.copy(
          endpoint = dapexMessage.endpoint.copy(method = "unsupported"),
          criteria = Vector(),
          update = Vector(),
          insert = Vector(),
          process = Vector(),
          response = None
        )
    }

  val badDapexMessage =
    """
      |{
      |  "endpoint" : {
      |    "method" : "select"
      |  },
      |  "client" : {
      |    "clientId" : "client1",
      |    "requestId" : "request1",
      |    "sourceEndpoint" : "client",
      |    "authorisation" : "securitytoken"
      |  },
      |  "originator" : {
      |    "clientId" : "client1",
      |    "requestId" : "request1",
      |    "sourceEndpoint" : "client",
      |    "originalToken" : "securitytoken"
      |  },
      |  "entity" : {
      |    "name" : "person"
      |  },
      |  "criteria" : [
      |    {
      |      "field" : "customerId",
      |      "value" : "1",
      |      "operator" : "eq"
      |    }
      |  ],
      |  "update" : [
      |  ],
      |  "insert" : [
      |  ],
      |  "process" : [
      |  ],
      |  "response" : null
      |}
      |""".stripMargin
}
