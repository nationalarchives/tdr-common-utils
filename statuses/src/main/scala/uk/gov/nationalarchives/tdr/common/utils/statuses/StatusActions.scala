package uk.gov.nationalarchives.tdr.common.utils.statuses

import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

object StatusActions {
  sealed trait StatusActionType {
    val value: String
  }

  case object UserFixable extends StatusActionType {
    val value: String = "UserFixable"
  }

  case object TNASupport extends StatusActionType {
    val value: String = "TNASupport"
  }

  case class StatusAction(actionType: StatusActionType, messageKey: String)

  def action(statusType: StatusType, reason: StatusValue): Option[StatusAction] =
    action(statusType, reason, None)

  def action(statusType: StatusType, reason: StatusValue, puid: Option[String]): Option[StatusAction] =
    (statusType, reason) match {
      case (_, SuccessValue)    => None
      case (_, CompletedValue)  => None
      case (_, InProgressValue) => None

      case (FFIDType, NonJudgmentFormatValue) => Some(StatusAction(UserFixable, ffidKey("nonJudgmentFormat", puid)))
      case (FFIDType, ZeroByteFileValue)      => Some(StatusAction(UserFixable, ffidKey("zeroByteFile", puid)))
      case (FFIDType, MultipleFormatsValue)   => Some(StatusAction(UserFixable, ffidKey("multipleFormats", puid)))
      case (FFIDType, FailedValue)            => Some(StatusAction(UserFixable, ffidKey("failed", puid)))

      case (AntivirusType, VirusDetectedValue) => Some(StatusAction(UserFixable, "antivirus.virusDetected"))
      case (AntivirusType, FailedValue)        => Some(StatusAction(UserFixable, "antivirus.failed"))

      case (ChecksumMatchType, MismatchValue) => Some(StatusAction(UserFixable, "checksumMatch.mismatch"))
      case (ChecksumMatchType, FailedValue)   => Some(StatusAction(UserFixable, "checksumMatch.failed"))

      case (ClientChecksumType, FailedValue)  => Some(StatusAction(UserFixable, "clientChecksum.failed"))
      case (ClientFilePathType, FailedValue)  => Some(StatusAction(UserFixable, "clientFilePath.failed"))

      case (RedactionType, SuccessValue)               => None
      case (RedactionType, NoOriginalFileValue)        => Some(StatusAction(UserFixable, "redaction.noOriginalFile"))
      case (RedactionType, AmbiguousOriginalFileValue) => Some(StatusAction(UserFixable, "redaction.ambiguousOriginalFile"))
      case (RedactionType, DuplicateFileNameValue)     => Some(StatusAction(UserFixable, "redaction.duplicateFileName"))
      case (RedactionType, _)                          => Some(StatusAction(UserFixable, "redaction.failed"))

      case (statusType, reason) => Some(StatusAction(UserFixable, s"${statusType.id.toLowerCase}.${reason.value.toLowerCase}"))
    }

  private def
  ffidKey(reason: String, puid: Option[String]): String =
    puid.map(p => s"ffid.$reason.$p").getOrElse(s"ffid.$reason")
}
