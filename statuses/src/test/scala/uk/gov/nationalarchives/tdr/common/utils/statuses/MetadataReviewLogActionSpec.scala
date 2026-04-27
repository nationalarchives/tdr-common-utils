package uk.gov.nationalarchives.tdr.common.utils.statuses

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.statuses.MetadataReviewLogAction._
import uk.gov.nationalarchives.tdr.common.utils.statuses.MetadataReviewStatus._

class MetadataReviewLogActionSpec extends AnyWordSpec {
  "MetadataReviewLogAction" should {
    "have the correct value" in {
      Submission.value should equal("Submission")
      Rejection.value should equal("Rejection")
      Approval.value should equal("Approval")
      Confirmation.value should equal("Confirmation")
    }
    "have the correct reviewStatus" in {
      Submission.reviewStatus shouldBe Requested
      Rejection.reviewStatus shouldBe Rejected
      Approval.reviewStatus shouldBe Approved
      Confirmation.reviewStatus shouldBe Transferred
    }
  }
  "MetadataReviewLogAction constructor" should {
    "return the correct action for a given string" in {
      MetadataReviewLogAction("Submission") shouldBe Submission
      MetadataReviewLogAction("Rejection") shouldBe Rejection
      MetadataReviewLogAction("Approval") shouldBe Approval
      MetadataReviewLogAction("Confirmation") shouldBe Confirmation
    }
    "throw an exception when action is unrecognised" in {
      val exception = intercept[RuntimeException] {
        MetadataReviewLogAction("someRandomValue")
      }
      exception.getMessage should equal("Invalid metadata review log action: someRandomValue")
    }
  }
}
