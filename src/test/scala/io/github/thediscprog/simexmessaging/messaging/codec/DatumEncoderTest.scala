package io.github.thediscprog.simexmessaging.messaging.codec

import io.github.thediscprog.simexmessaging.codec.DatumEncoder
import io.github.thediscprog.slogic.Xor
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.deriving.Mirror

// Test classes
case class SimpleA(aString: String)
case class SimpleCC(anInt: Int, aString: String, aBoolean: Boolean, simpleA: SimpleA) derives DatumEncoder

/**
 * Encode: Case Class => Datum
 * Decode: Datum => Case Class
 */
class SimexCodecTest extends AnyFlatSpec with Matchers {
  it should "encode a simple case class" in {
    val simpleA = SimpleA("testing")
    val test = SimpleCC(1, "test", true, simpleA)
    
    val encoder = summon[DatumEncoder[SimpleCC]]
    val result = encoder.encode(test)

    result shouldBe Vector(Xor.applyLeft(1), Xor.applyLeft("test"))
  }
}

