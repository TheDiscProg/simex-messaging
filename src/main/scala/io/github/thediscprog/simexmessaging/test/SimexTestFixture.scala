package io.github.thediscprog.simexmessaging.test

import io.github.thediscprog.simexmessaging.messaging._

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

  def getMessage[A](method: Method, entity: Option[String], data: A): Simex[A] =
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

}
