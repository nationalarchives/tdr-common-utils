package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import org.mockito.MockitoSugar
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.ObjectTypes.{Csv, Metadata, Record}

class ObjectTypesSpec extends AnyWordSpec with MockitoSugar {
  "ObjectTypes" should {
    "have the correct field values" in {
      Csv.id shouldEqual "csv"
      Metadata.id shouldEqual "metadata"
      Record.id shouldEqual "record"
    }

    "return the correct 'object categories' based on input string" in {
      ObjectTypes.toObjectType("csv") shouldBe Csv
      ObjectTypes.toObjectType("metadata") shouldBe Metadata
      ObjectTypes.toObjectType("record") shouldBe Record
    }

    "throw an exception when category is unrecognised" in {
      val exception = intercept[RuntimeException] {
        ObjectTypes.toObjectType("someRandomValue")
      }

      exception.getMessage should equal("Invalid object type: someRandomValue")
    }
  }
}
