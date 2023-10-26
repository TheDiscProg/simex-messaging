package dapex.entities

import enumeratum.{Enum, EnumEntry}

sealed trait ConversionError extends EnumEntry {
  val message: String
}

case object ConversionError extends Enum[ConversionError] {

  case class ParsingStringError(message: String) extends ConversionError

  case class ParsingJsonError(message: String) extends ConversionError

  override def values: IndexedSeq[ConversionError] = findValues
}
