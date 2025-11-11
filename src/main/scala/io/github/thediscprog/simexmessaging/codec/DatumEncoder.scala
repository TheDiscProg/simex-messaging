package io.github.thediscprog.simexmessaging.codec

import io.github.thediscprog.simexmessaging.messaging.Datum
import io.github.thediscprog.slogic.Xor
import deriving.Mirror
import compiletime.*
import scala.collection.immutable.Map

trait DatumEncoder[A] {
  def encode(a: A): Vector[Datum]
}

object DatumEncoder {
  
   inline def derived[A <: Product](using mirror: Mirror.ProductOf[A]): DatumEncoder[A] =
     new DatumEncoder[A]:
       override def encode(a: A): Vector[Datum] =
         extractToXor[mirror.MirroredElemTypes, mirror.MirroredElemLabels](Tuple.fromProductTyped(a), Map.empty: Map[String, String])
  /**
   * Given these values for a case class, it will return a Vector of Datum, where each Datum represents
   * the fields or attributes of a class.
   * In the example, case class(age: Int, name: String, active: Boolean) is used to convert to
   * a Datum:
   * case class Datum(
   * field: String,
   * check: Option[String],
   * value: Xor[String, Vector[Datum]]
   * )
   *
   * @param elements: the tuple of values of a case class, i.e. (25, "John Smith", true)
   * @tparam E: Tuple of element types, i.e. (Int, String, Boolean)
   * @tparam L: Tuple of element labels, i.e. (age,  name, active)
   * @return List of Datum
   */
  private inline def extractToXor[E <: Tuple, L <: Tuple](elements: E, map: Map[String, String]): Vector[Datum] =
    inline val map = (elements, erasedValue[L]) match {
      case (EmptyTuple, EmptyTuple) => Vector()
      case (el: (eh *: et), lab: (lh *: lt)) =>
        val (headValue *: tail) = el // h = first value
        val (labelHead *: labelTail) = lab
        extractToXor(tail, map + (labelHead.toString -> headValue.toString))
    }
}
