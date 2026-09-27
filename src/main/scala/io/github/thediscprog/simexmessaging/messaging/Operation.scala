package io.github.thediscprog.simexmessaging.messaging

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed trait Operation extends StringEnumEntry {

  def value: String
}

case object Operation extends StringEnum[Operation] with StringCirceEnum[Operation] {

  case object SELECT extends Operation {
    val value: String = "select"
  }

  case object UPDATE extends Operation {
    val value: String = "update"
  }

  case object INSERT extends Operation {
    val value: String = "insert"
  }

  case object DELETE extends Operation {
    val value: String = "delete"
  }

  case object PROCESS extends Operation {
    val value: String = "process"
  }

  case object RESPONSE extends Operation {
    val value: String = "response"
  }

  case object UNSUPPORTED extends Operation {
    val value: String = "unsupported"
  }

  val values = findValues

}
