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

    "return UserFixable with correct message key for FFID failure reasons" in {
      action(FFIDType, NonJudgmentFormatValue) shouldBe Some(StatusAction(UserFixable, "ffid.nonJudgmentFormat"))
      action(FFIDType, ZeroByteFileValue) shouldBe Some(StatusAction(UserFixable, "ffid.zeroByteFile"))
      action(FFIDType, MultipleFormatsValue) shouldBe Some(StatusAction(UserFixable, "ffid.multipleFormats"))
      action(FFIDType, FailedValue) shouldBe Some(StatusAction(UserFixable, "ffid.failed"))
    }

    "include puid in FFID message key when provided" in {
      action(FFIDType, NonJudgmentFormatValue, Some("fmt/123")) shouldBe Some(StatusAction(UserFixable, "ffid.nonJudgmentFormat.fmt/123"))
      action(FFIDType, ZeroByteFileValue, Some("x-fmt/111")) shouldBe Some(StatusAction(UserFixable, "ffid.zeroByteFile.x-fmt/111"))
      action(FFIDType, MultipleFormatsValue, Some("fmt/456")) shouldBe Some(StatusAction(UserFixable, "ffid.multipleFormats.fmt/456"))
      action(FFIDType, FailedValue, Some("fmt/789")) shouldBe Some(StatusAction(UserFixable, "ffid.failed.fmt/789"))
    }

    "exclude puid from FFID message key when not provided" in {
      action(FFIDType, NonJudgmentFormatValue, None) shouldBe Some(StatusAction(UserFixable, "ffid.nonJudgmentFormat"))
    }

    "return UserFixable with correct message key for antivirus failures" in {
      action(AntivirusType, VirusDetectedValue) shouldBe Some(StatusAction(UserFixable, "antivirus.virusDetected"))
      action(AntivirusType, FailedValue) shouldBe Some(StatusAction(UserFixable, "antivirus.failed"))
    }

    "return UserFixable with correct message key for checksum match failures" in {
      action(ChecksumMatchType, MismatchValue) shouldBe Some(StatusAction(UserFixable, "checksumMatch.mismatch"))
      action(ChecksumMatchType, FailedValue) shouldBe Some(StatusAction(UserFixable, "checksumMatch.failed"))
    }

    "return UserFixable with correct message key for client check failures" in {
      action(ClientChecksumType, FailedValue) shouldBe Some(StatusAction(UserFixable, "clientChecksum.failed"))
      action(ClientFilePathType, FailedValue) shouldBe Some(StatusAction(UserFixable, "clientFilePath.failed"))
    }

    "return UserFixable for redaction failures regardless of reason" in {
      action(RedactionType, FailedValue) shouldBe Some(StatusAction(UserFixable, "redaction.failed"))
      action(RedactionType, CompletedWithIssuesValue) shouldBe Some(StatusAction(UserFixable, "redaction.failed"))
    }

    "return None for redaction success" in {
      action(RedactionType, SuccessValue) shouldBe None
    }

    "generate message key for unmapped combinations" in {
      val result = action(ServerChecksumType, FailedValue)
      result shouldBe Some(StatusAction(UserFixable, "serverchecksum.failed"))
    }

    "have correct string values for action types" in {
      UserFixable.value should equal("UserFixable")
      TNASupport.value should equal("TNASupport")
    }
  }
}
