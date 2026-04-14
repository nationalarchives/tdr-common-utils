package uk.gov.nationalarchives.tdr.common.utils.statuses

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.statuses.MetadataReviewStatus._

class MetadataReviewStatusSpec extends AnyWordSpec with MockitoSugar {
  "MetadataReviewStatus" should {
    "have the correct value" in {
      Requested.value should equal("Requested")
      Rejected.value should equal("Rejected")
      Approved.value should equal("Approved")
      Completed.value should equal("Completed")
    }
  }

  "toMetadataReviewStatus" should {
    "return the correct status for a given string" in {
      toMetadataReviewStatus("Requested") shouldBe Requested
      toMetadataReviewStatus("Rejected") shouldBe Rejected
      toMetadataReviewStatus("Approved") shouldBe Approved
      toMetadataReviewStatus("Completed") shouldBe Completed
    }

    "throw an exception when status is unrecognised" in {
      val exception = intercept[RuntimeException] {
        toMetadataReviewStatus("someRandomValue")
      }
      exception.getMessage should equal("Invalid metadata review status: someRandomValue")
    }
  }
}
