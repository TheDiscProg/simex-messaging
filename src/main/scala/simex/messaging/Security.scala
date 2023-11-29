package simex.messaging

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed trait Security extends StringEnumEntry {
  def value: String
  def level: String
}

case object Security extends StringEnum[Security] with StringCirceEnum[Security] {

  case object BASIC extends Security {

    override val level: String = "1"

    override val value: String = "basic"
  }

  case object AUTHORIZED extends Security {
    override val level: String = "2"

    override val value: String = "authorized"
  }

  case object ORIGINAL_TOKEN extends Security {
    override val level: String = "3"

    override val value: String = "original_token"
  }

  case object FORBIDDEN extends Security {
    override val level: String = "0"

    override val value: String = "forbidden"
  }

  override def values: IndexedSeq[Security] = findValues

  def fromString(s: String): Security =
    Option(s) match {
      case Some(level) =>
        level.toLowerCase match {
          case "1" | "level 1" => BASIC
          case "2" | "level 2" => AUTHORIZED
          case "3" | "level 3" => ORIGINAL_TOKEN
          case _ => FORBIDDEN
        }
      case _ => FORBIDDEN
    }

  def determineHighestSecurity(sec1: Security, sec2: Security): Security = {
    val sec1Level = sec1.level.toInt
    val sec2Level = sec2.level.toInt
    if (sec1Level == 0 || sec2Level == 0)
      FORBIDDEN
    else {
      if (sec1Level > sec2Level)
        sec1
      else
        sec2
    }
  }

  def determineSecurityLevel(simex: Simex): Security =
    fromString(simex.originator.security)
}
