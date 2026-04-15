package uk.gov.nationalarchives.tdr.common.utils.statuses

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.statuses.MetadataReviewStatus._

class MetadataReviewStatusSpec extends AnyWordSpec {
  "MetadataReviewStatus" should {
    "have the correct value" in {
      Requested.value should equal("Requested")
      Rejected.value should equal("Rejected")
      Approved.value should equal("Approved")
      Completed.value should equal("Completed")
    }
  }

  "MetadataReviewStatus constructor" should {
    "return the correct status for a given string" in {
      MetadataReviewStatus("Requested") shouldBe Requested
      MetadataReviewStatus("Rejected") shouldBe Rejected
      MetadataReviewStatus("Approved") shouldBe Approved
      MetadataReviewStatus("Completed") shouldBe Completed
    }

    "throw an exception when status is unrecognised" in {
      val exception = intercept[RuntimeException] {
        MetadataReviewStatus("someRandomValue")
      }
      exception.getMessage should equal("Invalid metadata review status: someRandomValue")
    }
  }
}
