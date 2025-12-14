package io.github.thediscprog.simexmessaging.codecs

import io.github.thediscprog.simexmessaging.messaging.Datum
import io.github.thediscprog.slogic.Xor

trait DatumEncoder[T] {
  def encode(field: String, check: Option[String], t: T): Datum
}

object DatumEncoder {

  given DatumEncoder[Boolean] with {
    override def encode(field: String, check: Option[String], t: Boolean): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }

  given DatumEncoder[Byte] with {
    override def encode(field: String, check: Option[String], t: Byte): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }

  given DatumEncoder[Short] with {
    override def encode(field: String, check: Option[String], t: Short): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }
  given DatumEncoder[Int] with {
    override def encode(field: String, check: Option[String], t: Int): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }

  given DatumEncoder[Long] with {
    override def encode(field: String, check: Option[String], t: Long): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }

  given DatumEncoder[Float] with {
    override def encode(field: String, check: Option[String], t: Float): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }

  given DatumEncoder[Double] with {
    override def encode(field: String, check: Option[String], t: Double): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }

  given DatumEncoder[BigInt] with {
    override def encode(field: String, check: Option[String], t: BigInt): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }

  given DatumEncoder[BigDecimal] with {
    override def encode(field: String, check: Option[String], t: BigDecimal): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }

  given DatumEncoder[String] with {
    override def encode(field: String, check: Option[String], t: String): Datum =
      Datum(field, check, Xor.applyLeft(t))
  }

  given DatumEncoder[Char] with {
    override def encode(field: String, check: Option[String], t: Char): Datum =
      Datum(field, check, Xor.applyLeft(t.toString))
  }
}
