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

    "return UserFixable with correct message key for FFID failure reasons zero byte and non Judgment " in {
      action(FFIDType, NonJudgmentFormatValue) shouldBe Some(StatusAction(UserFixable, "ffid.nonJudgmentFormat"))
      action(FFIDType, ZeroByteFileValue) shouldBe Some(StatusAction(UserFixable, "ffid.zeroByteFile"))
    }

    "return TNA with correct message key for FFID failure reasons multiple formats and failure " in {
      action(FFIDType, MultipleFormatsValue) shouldBe Some(StatusAction(TNASupport, "ffid.multipleFormats"))
      action(FFIDType, FailedValue) shouldBe Some(StatusAction(TNASupport, "ffid.failed"))
    }

    "return UserFixable when FFID PasswordProtected" in {
      action(FFIDType, CustomValue("PasswordProtected")) shouldBe Some(StatusAction(UserFixable, "ffid.passwordProtected"))
    }

    "return TNASupport when FFID Unidentified" in {
      action(FFIDType, Unidentified) shouldBe Some(StatusAction(TNASupport, "ffid.unidentified"))
    }

    "return UserFixable when FFID Executable" in {
      action(FFIDType, CustomValue("Executable")) shouldBe Some(StatusAction(UserFixable, "ffid.executable"))
    }

    "return UserFixable when FFID OperatingSystemMac" in {
      action(FFIDType, CustomValue("OperatingSystemMac")) shouldBe Some(StatusAction(UserFixable, "ffid.operatingSystemMac"))
    }

    "return UserFixable when FFID Shortcut" in {
      action(FFIDType, CustomValue("Shortcut")) shouldBe Some(StatusAction(UserFixable, "ffid.shortcut"))
    }

    "return UserFixable when FFID Zip" in {
      action(FFIDType, CustomValue("Zip")) shouldBe Some(StatusAction(UserFixable, "ffid.zip"))
    }

    "return UserFixable when FFID Template" in {
      action(FFIDType, CustomValue("Template")) shouldBe Some(StatusAction(UserFixable, "ffid.template"))
    }

    "return TNASupport with correct message key for antivirus failures" in {
      action(AntivirusType, VirusDetectedValue) shouldBe Some(StatusAction(TNASupport, "antivirus.virusDetected"))
      action(AntivirusType, FailedValue) shouldBe Some(StatusAction(TNASupport, "antivirus.failed"))
    }

    "return TNASupport with correct message key for checksum match failures" in {
      action(ChecksumMatchType, MismatchValue) shouldBe Some(StatusAction(TNASupport, "checksumMatch.mismatch"))
      action(ChecksumMatchType, FailedValue) shouldBe Some(StatusAction(TNASupport, "checksumMatch.failed"))
    }

    "return TNASupport with correct message key for client check failures" in {
      action(ClientChecksumType, FailedValue) shouldBe Some(StatusAction(TNASupport, "clientChecksum.failed"))
      action(ClientFilePathType, FailedValue) shouldBe Some(StatusAction(TNASupport, "clientFilePath.failed"))
    }

    "return UserFixable for redaction failures with specific message keys" in {
      action(RedactionType, NoOriginalFileValue) shouldBe Some(StatusAction(UserFixable, "redaction.noOriginalFile"))
      action(RedactionType, AmbiguousOriginalFileValue) shouldBe Some(StatusAction(UserFixable, "redaction.ambiguousOriginalFile"))
      action(RedactionType, DuplicateFileNameValue) shouldBe Some(StatusAction(UserFixable, "redaction.duplicateFileName"))
    }

    "return TNASupport for unknown redaction failures" in {
      action(RedactionType, FailedValue) shouldBe Some(StatusAction(TNASupport, "redaction.failed"))
      action(RedactionType, CompletedWithIssuesValue) shouldBe Some(StatusAction(TNASupport, "redaction.failed"))
    }

    "return None for redaction success" in {
      action(RedactionType, SuccessValue) shouldBe None
    }

    "return TNASupport if server checksum Failed" in {
      val result = action(ServerChecksumType, FailedValue)
      result shouldBe Some(StatusAction(TNASupport, "serverChecksum.failed"))
    }

    "have correct string values for action types" in {
      UserFixable.value should equal("UserFixable")
      TNASupport.value should equal("TNASupport")
    }
  }
}
