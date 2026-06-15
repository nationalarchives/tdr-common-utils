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
      SuccessValue.value should equal("Success")
      VirusDetectedValue.value should equal("VirusDetected")
      NonJudgmentFormatValue.value should equal("NonJudgmentFormat")
      MismatchValue.value should equal("Mismatch")
      ZeroByteFileValue.value should equal("ZeroByteFile")
      MultipleFormatsValue.value should equal("MultipleFormats")
      NoOriginalFileValue.value should equal("NoOriginalFile")
      AmbiguousOriginalFileValue.value should equal("AmbiguousOriginalFile")
      DuplicateFileNameValue.value should equal("DuplicateFileName")
      SkippedValue.value should equal("Skipped")
    }
  }

  "StatusValue constructor" should {
    "return correct value type for given id" in {
      StatusValue.apply("Completed") shouldBe CompletedValue
      StatusValue.apply("CompletedWithIssues") shouldBe CompletedWithIssuesValue
      StatusValue.apply("Failed") shouldBe FailedValue
      StatusValue.apply("InProgress") shouldBe InProgressValue
      StatusValue.apply("Success") shouldBe SuccessValue
      StatusValue.apply("VirusDetected") shouldBe VirusDetectedValue
      StatusValue.apply("NonJudgmentFormat") shouldBe NonJudgmentFormatValue
      StatusValue.apply("Mismatch") shouldBe MismatchValue
      StatusValue.apply("ZeroByteFile") shouldBe ZeroByteFileValue
      StatusValue.apply("MultipleFormats") shouldBe MultipleFormatsValue
      StatusValue.apply("NoOriginalFile") shouldBe NoOriginalFileValue
      StatusValue.apply("AmbiguousOriginalFile") shouldBe AmbiguousOriginalFileValue
      StatusValue.apply("DuplicateFileName") shouldBe DuplicateFileNameValue
      StatusValue.apply("Skipped") shouldBe SkippedValue
    }

    "create Custom StatusValue from String" in {
      val result = StatusValue.apply("SomeDynamicReason")
      result shouldBe CustomValue("SomeDynamicReason")
      result.value should equal("SomeDynamicReason")
    }
  }

}
