package uk.gov.nationalarchives.tdr.common.utils.statuses

import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

object StatusActions {
  def action(statusType: StatusType, reason: StatusValue): Option[StatusAction] =
    (statusType, reason) match {
      case (_, SuccessValue) => None
      case (_, CompletedValue) => None
      case (_, InProgressValue) => None

      case (FFIDType, NonJudgmentFormatValue) => Some(StatusAction(UserFixable, "ffid.nonJudgmentFormat"))
      case (FFIDType, ZeroByteFileValue) => Some(StatusAction(UserFixable, "ffid.zeroByteFile"))
      case (FFIDType, MultipleFormatsValue) => Some(StatusAction(TNASupport, "ffid.multipleFormats"))
      case (FFIDType, FailedValue) => Some(StatusAction(TNASupport, "ffid.failed"))
      // Custom values come fro disallowed Puids in da-metadata-schema
      case (FFIDType, CustomValue("Unidentified")) => Some(StatusAction(TNASupport, "ffid.unidentified"))
      case (FFIDType, CustomValue("Zip")) => Some(StatusAction(UserFixable, s"ffid.zip"))
      case (FFIDType, CustomValue("PasswordProtected")) => Some(StatusAction(UserFixable, s"ffid.passwordProtected"))
      case (FFIDType, CustomValue("Executable")) => Some(StatusAction(UserFixable, s"ffid.executable"))
      case (FFIDType, CustomValue("Template")) => Some(StatusAction(UserFixable, s"ffid.template"))
      case (FFIDType, CustomValue("Shortcut")) => Some(StatusAction(UserFixable, s"ffid.shortcut"))
      case (FFIDType, CustomValue("OperatingSystemMac")) => Some(StatusAction(UserFixable, s"ffid.operatingSystemMac"))
      case (FFIDType, CustomValue(reason)) => Some(StatusAction(TNASupport, s"ffid.$reason"))

      case (AntivirusType, VirusDetectedValue) => Some(StatusAction(TNASupport, "antivirus.virusDetected"))
      case (AntivirusType, FailedValue) => Some(StatusAction(TNASupport, "antivirus.failed"))
      case (ChecksumMatchType, MismatchValue) => Some(StatusAction(TNASupport, "checksumMatch.mismatch"))
      case (ChecksumMatchType, FailedValue) => Some(StatusAction(TNASupport, "checksumMatch.failed"))
      case (ClientChecksumType, FailedValue) => Some(StatusAction(TNASupport, "clientChecksum.failed"))
      case (ClientFilePathType, FailedValue) => Some(StatusAction(TNASupport, "clientFilePath.failed"))

      case (RedactionType, SuccessValue) => None
      case (RedactionType, NoOriginalFileValue) => Some(StatusAction(UserFixable, "redaction.noOriginalFile"))
      case (RedactionType, AmbiguousOriginalFileValue) => Some(StatusAction(UserFixable, "redaction.ambiguousOriginalFile"))
      case (RedactionType, DuplicateFileNameValue) => Some(StatusAction(UserFixable, "redaction.duplicateFileName"))
      case (RedactionType, _) => Some(StatusAction(TNASupport, "redaction.failed"))

      case (statusType, reason) => Some(StatusAction(TNASupport, s"${statusType.id.toLowerCase}.${reason.value.toLowerCase}"))
    }

  sealed trait StatusActionType {
    val value: String
  }

  case class StatusAction(actionType: StatusActionType, messageKey: String)

  case object UserFixable extends StatusActionType {
    val value: String = "UserFixable"
  }

  case object TNASupport extends StatusActionType {
    val value: String = "TNASupport"
  }
}
