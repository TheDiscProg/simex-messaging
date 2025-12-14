package io.github.thediscprog.simexmessaging.codecs

import io.github.thediscprog.simexmessaging.messaging.Datum
import scala.deriving.*
import scala.quoted.*

trait SimexDataEncoder[T] {
  def encode(check: Option[String], t: T): Vector[Datum]
}

object SimexDataEncoder {

  inline def apply[T](using enc: SimexDataEncoder[T]): SimexDataEncoder[T] = enc

  inline given derived[T](using m: Mirror.ProductOf[T]): SimexDataEncoder[T] =
    ${ derivedImpl[T]('m) }

  def derivedImpl[T: Type](
      mExpr: Expr[Mirror.ProductOf[T]]
  )(using q: Quotes): Expr[SimexDataEncoder[T]] = {
    import q.reflect.*

    val tpe = TypeRepr.of[T]
    val symbol = tpe.typeSymbol
    // Verify that this is a clase class
    if !symbol.flags.is(Flags.Case) then
      report.errorAndAbort(s"SimexDataEncoder only works with Scala Case Classes!")

    // Extract the property names
    val propertyNames = tpe.typeSymbol.caseFields.map(_.name).map(Expr(_))
    val properties = Expr.ofList(propertyNames)

    // Extract the field typs as TypeRepr
    val mirrorTpe = mExpr.asTerm.tpe

    // val mirrorSym = mirrorTpe.typeSymbol

    val elemTypesSym =
      mirrorTpe.typeSymbol.typeMembers
        .find(_.name == "MirroredElemTypes")
        .getOrElse {
          report.errorAndAbort(
            s"Mirror for ${Type.show[T]} does not define MirroredElemTypes"
          )
        }
    val elemTypesRepr = mirrorTpe.memberType(elemTypesSym)
    val elementTypes: List[TypeRepr] = elemTypesRepr match
      case AppliedType(_, args) => args
      case other =>
        report.errorAndAbort(s"Failed to extract field types for ${Type.show[T]}: ${other.show}")

    // Summmon encoders for each field but handle primitives
    val summonedFieldEncoders = elementTypes.map { ft =>
      ft.asType match
        case '[f] =>
          Expr.summon[SimexDataEncoder[f]] match
            case Some(enc) => '{ (c: Option[String], v: Any) => $enc.encode(c, v.asInstanceOf[f]) }
            case _ => '{ (c: Option[String], v: Any) => None }
    }
    val encoders = Expr.ofList(summonedFieldEncoders)

    '{
      new SimexDataEncoder[T] {
        def encode(check: Option[String], t: T): Vector[Datum] = {
          val names = $properties
          val encs = $encoders
          val values = t.asInstanceOf[Product].productIterator.toList
          names.zip(values.zip(encs)).map {
            case (label, (value, enc)) =>
              println(s"Checking class [$label][$value][$enc]")
            case (label, (value, _)) =>
              println(s"Checking Primitive [$label][$value]")
            case a =>
              throw new RuntimeException(s"Cannot handle [$a]")

          }

          Vector()
        }
      }
    }
  }
}
/*
scala.deriving.*
import scala.compiletime.{erasedValue, summonInline, constValue, constValueTuple, summonFrom}
import scala.Tuple.*

// ------------------------
// The Encoder trait
// ------------------------
trait SimexDataEncoder[T]:
  def encode(check: Option[String], t: T): Vector[String]

// ------------------------
// Companion object with derivation
// ------------------------
object SimexDataEncoder:

  // Summon encoder for a type T
  inline def apply[T](using enc: SimexDataEncoder[T]): SimexDataEncoder[T] = enc

  // ------------------------
  // Derive an encoder for any case class
  // ------------------------
  inline given derived[T](using m: Mirror.ProductOf[T]): SimexDataEncoder[T] =
    new SimexDataEncoder[T]:
      def encode(check: Option[String], t: T): Vector[String] =
        // Extract the field names at compile-time
        val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
        // Extract the values from the case class
        val values = t.asInstanceOf[Product].productIterator.toList
        // Summon encoders for each field type at compile-time
        val encs = summonAll[m.MirroredElemTypes]
        // Apply each encoder to its field
        labels.zip(values.zip(encs)).flatMap {
          case (label, (value, enc)) =>
            enc.encode(check, value)
        }.toVector

  // ------------------------
  // Summon a tuple of encoders for a tuple of types
  // ------------------------
  inline def summonAll[T <: Tuple]: List[SimexDataEncoder[?]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (h *: t)  =>
        summonInline[SimexDataEncoder[h]] :: summonAll[t]

  // ------------------------
  // Helper for primitive types
  // ------------------------
  given SimexDataEncoder[Long] with
    def encode(c: Option[String], l: Long): Vector[String] = Vector(s"Long($l)")

  given SimexDataEncoder[String] with
    def encode(c: Option[String], s: String): Vector[String] = Vector(s"String($s)")

 */
