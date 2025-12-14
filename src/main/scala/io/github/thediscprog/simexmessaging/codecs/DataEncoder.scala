package io.github.thediscprog.simexmessaging.codecs

import scala.deriving.*
import scala.compiletime.*
import scala.annotation.nowarn
import io.github.thediscprog.simexmessaging.messaging.Datum
import io.github.thediscprog.slogic.Xor

trait DataEncoder[T] {
  def encode(check: Option[String], t: T): Vector[Datum]
}

object DataEncoder {

  inline def derived[T](using m: Mirror.Of[T]): DataEncoder[T] =
    inline m match
      case p: Mirror.ProductOf[T] => productEncoder(using p)
      case s: Mirror.SumOf[T] => sumEncoder(using s)

  inline def summonAll[T <: Tuple]: List[DataEncoder[?]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts) => summonInline[DataEncoder[t]] :: summonAll[ts]

  @nowarn
  inline given productEncoder[T](using m: Mirror.ProductOf[T]): DataEncoder[T] =
    new DataEncoder[T] {
      override def encode(check: Option[String] = None, t: T): Vector[Datum] =
        val typeName = constValue[m.MirroredLabel].asInstanceOf[String]
        val labels: List[String] = constValueTuple[m.MirroredElemLabels].toList
          .asInstanceOf[List[String]]
        val values: List[Any] = t.asInstanceOf[Product].productIterator.toList
        val encoders: List[DataEncoder[?]] = summonAll[m.MirroredElemTypes]
        val encodedValues = labels
          .zip(values.zip(encoders))
          .map { case (name, (value, enc)) =>
            val encoder = enc.asInstanceOf[DataEncoder[Any]]
            val encoded = encoder.encode(check, value)
            Datum(name, check, Xor.applyRight(encoded))
          }
        encodedValues.toVector
    }

  @nowarn
  inline given sumEncoder[T](using m: Mirror.SumOf[T]): DataEncoder[T] =
    new DataEncoder[T] {
      private val caseEncoders = summonAll[m.MirroredElemTypes]
      override def encode(check: Option[String] = None, t: T): Vector[Datum] = {
        val oridinal = m.ordinal(t)
        val encoder = caseEncoders(oridinal).asInstanceOf[DataEncoder[Any]]
        val caseName = constValueTuple[m.MirroredElemLabels]
          .productElement(oridinal)
          .asInstanceOf[String]

        encoder.encode(check, t)
      }

    }
}
