package io.github.thediscprog.simexmessaging.messaging

import io.circe.parser
import io.circe.syntax._
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.github.thediscprog.slogic.Xor

/** These tests are for testing the JSON serializing/deserializing as there is
  * a recursive XOR in the Datum.
  */
class DatumTest extends AnyFlatSpec with Matchers with EitherValues {

  val basic = Datum("name", None, Xor.applyLeft("John"))

  val basicJsonStr =
    """{
      |  "field" : "name",
      |  "check" : null,
      |  "value" : "John"
      |}""".stripMargin

  val basicWithCheck = basic.copy(check = Some("EQUALS"))

  val basicWithCheckStr =
    """{
      |  "field" : "name",
      |  "check" : "EQUALS",
      |  "value" : "John"
      |}""".stripMargin

  val forename = Datum("forename", None, Xor.applyLeft("John"))
  val surname = Datum("surname", None, Xor.applyLeft("Smith"))
  val house = Datum("address", None, Xor.applyLeft("1 The Street"))
  val town = Datum("town", None, Xor.applyLeft("The Town"))
  val postcode = Datum("postcode", None, Xor.applyLeft("PC01"))
  val address = Datum("address", None, Xor.applyRight(Vector(house, town, postcode)))

  val person = Datum(
    field = "person",
    check = None,
    value = Xor.applyRight(Vector(forename, surname, address))
  )

  val personJson =
    """{
      |  "field" : "person",
      |  "check" : null,
      |  "value" : [
      |    {
      |      "field" : "forename",
      |      "check" : null,
      |      "value" : "John"
      |    },
      |    {
      |      "field" : "surname",
      |      "check" : null,
      |      "value" : "Smith"
      |    },
      |    {
      |      "field" : "address",
      |      "check" : null,
      |      "value" : [
      |        {
      |          "field" : "address",
      |          "check" : null,
      |          "value" : "1 The Street"
      |        },
      |        {
      |          "field" : "town",
      |          "check" : null,
      |          "value" : "The Town"
      |        },
      |        {
      |          "field" : "postcode",
      |          "check" : null,
      |          "value" : "PC01"
      |        }
      |      ]
      |    }
      |  ]
      |}""".stripMargin

  it should "give a JSON string for a very basic Datum" in {
    val json = basic.asJson.toString()

    json shouldBe basicJsonStr
  }

  it should "return Datum for the basic JSON string" in {
    val datumJson = parser.parse(basicJsonStr)
    val datumResult = datumJson.map(json => json.as[Datum])
    val datum = Datum.decode(datumResult.value)

    (datum.isRight && 
    datum.value == basic) shouldBe true
  }

  it should "give a JSON string for basic Datum with check" in {
    val json = basicWithCheck.asJson.toString()

    json shouldBe basicWithCheckStr
  }

  it should "return Datum for the basic JSON with check" in {
    val datumJson = parser.parse(basicWithCheckStr)
    val datumResult = datumJson.map(json => json.as[Datum])
    val datum = Datum.decode(datumResult.value)

    (datum.isRight &&
    datum.value == basicWithCheck) shouldBe true
  }

  it should "return JSON for Datum with vector value" in {
    val json = person.asJson.toString()

    json shouldBe personJson
  }

  it should "return Datum with recursive vector" in {
    val datumJson = parser.parse(personJson)
    val datumResult = datumJson.map(json => json.as[Datum])
    val datum = Datum.decode(datumResult.value)

    (datum.isRight && datum.value == person) shouldBe true
  }

}
