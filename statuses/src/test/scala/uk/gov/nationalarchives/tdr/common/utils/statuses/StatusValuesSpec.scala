package uk.gov.nationalarchives.tdr.common.utils.statuses

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

class StatusValuesSpec  extends AnyWordSpec with MockitoSugar {
  "StatusValues" should {
    "have the correct value" in {
      CompletedValue.value should equal("Completed")
      CompletedWithIssuesValue.value should equal("CompletedWithIssues")
      FailedValue.value should equal("Failed")
      InProgressValue.value should equal("InProgress")
    }
  }

  "StatusValue constructor" should {
    "return correct value type for given id" in {
      StatusValue.apply("Completed") shouldBe CompletedValue
      StatusValue.apply("CompletedWithIssues") shouldBe CompletedWithIssuesValue
      StatusValue.apply("Failed") shouldBe FailedValue
      StatusValue.apply("InProgress") shouldBe InProgressValue
    }

    "throw an exception when id is unrecognised" in {
      val exception = intercept[RuntimeException] {
        StatusValue.apply("someInvalidId")
      }
      exception.getMessage should equal("Invalid status value: someInvalidId")
    }
  }

}
