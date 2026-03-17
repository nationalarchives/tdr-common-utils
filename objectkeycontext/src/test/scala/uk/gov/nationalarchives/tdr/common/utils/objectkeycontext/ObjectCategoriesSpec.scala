package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.ObjectCategories.{DryRunMetadata, Metadata, Records}

class ObjectCategoriesSpec extends AnyWordSpec with MockitoSugar {
  "ObjectCategories" should {
    "have the correct field values" in {
      DryRunMetadata.id shouldEqual "dryrunmetadata"
      Metadata.id shouldEqual "metadata"
      Records.id shouldEqual "records"
    }

    "return the correct 'object categories' based on input string" in {
      ObjectCategories.toObjectCategory("dryrunmetadata") shouldBe DryRunMetadata
      ObjectCategories.toObjectCategory("metadata") shouldBe Metadata
      ObjectCategories.toObjectCategory("records") shouldBe Records
    }

    "throw an exception when category is unrecognised" in {
      val exception = intercept[RuntimeException] {
        ObjectCategories.toObjectCategory("someRandomValue")
      }

      exception.getMessage should equal("Invalid object category: someRandomValue")
    }
  }
}
