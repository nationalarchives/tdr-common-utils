package uk.gov.nationalarchives.tdr.common.utils.statuses

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusActions._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

class StatusActionsSpec extends AnyWordSpec with MockitoSugar {
  "StatusActions" should {
    "return None for success statuses" in {
      action(FFIDType, SuccessValue) shouldBe None
      action(AntivirusType, SuccessValue) shouldBe None
      action(ChecksumMatchType, CompletedValue) shouldBe None
      action(ClientChecksumType, InProgressValue) shouldBe None
    }

    "return UserFixable for FFID failure reasons" in {
      action(FFIDType, NonJudgmentFormatValue) shouldBe Some(UserFixable)
      action(FFIDType, ZeroByteFileValue) shouldBe Some(UserFixable)
      action(FFIDType, MultipleFormatsValue) shouldBe Some(UserFixable)
      action(FFIDType, FailedValue) shouldBe Some(UserFixable)
    }

    "return UserFixable for antivirus failures" in {
      action(AntivirusType, VirusDetectedValue) shouldBe Some(UserFixable)
      action(AntivirusType, FailedValue) shouldBe Some(UserFixable)
    }

    "return UserFixable for checksum match failures" in {
      action(ChecksumMatchType, MismatchValue) shouldBe Some(UserFixable)
      action(ChecksumMatchType, FailedValue) shouldBe Some(UserFixable)
    }

    "return UserFixable for client check failures" in {
      action(ClientChecksumType, FailedValue) shouldBe Some(UserFixable)
      action(ClientFilePathType, FailedValue) shouldBe Some(UserFixable)
    }

    "have correct string values" in {
      UserFixable.value should equal("UserFixable")
      TNASupport.value should equal("TNASupport")
    }
  }
}
