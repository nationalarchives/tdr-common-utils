package uk.gov.nationalarchives.tdr.common.utils.statuses

import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

object StatusActions {
  private val wordPattern = "[A-Z]+(?=[A-Z][a-z]|\\b)|[A-Z]?[a-z]+|\\d+".r
  private val ffidUserFixableCustomReasons = Set("Zip", "PasswordProtected", "Executable", "Template", "Shortcut", "NonRecord",
    "OperatingSystemMac", "LicenseContainsRegistrationInformation", "Backup")

  private def toCamelCase(value: String): String = {
    val words = wordPattern.findAllIn(value).toList
    words match {
      case Nil => value
      case head :: tail => head.toLowerCase + tail.map(word => word.toLowerCase.capitalize).mkString
    }
  }

  private def messageKey(statusType: StatusType, reason: StatusValue): String =
    s"${toCamelCase(statusType.id)}.${toCamelCase(reason.value)}"

  private def statusAction(actionType: StatusActionType, statusType: StatusType, reason: StatusValue): Some[StatusAction] =
    Some(StatusAction(actionType, messageKey(statusType, reason)))

  def action(statusType: StatusType, reason: StatusValue): Option[StatusAction] =
    (statusType, reason) match {
      case (_, SuccessValue) => None
      case (_, CompletedValue) => None
      case (_, InProgressValue) => None
      case (ClientChecksType, _) => None

      case (FFIDType, NonJudgmentFormatValue) => statusAction(UserFixable, FFIDType, NonJudgmentFormatValue)
      case (FFIDType, ZeroByteFileValue) => statusAction(UserFixable, FFIDType, ZeroByteFileValue)
      case (FFIDType, MultipleFormatsValue) => statusAction(TNASupport, FFIDType, MultipleFormatsValue)
      case (FFIDType, FailedValue) => statusAction(TNASupport, FFIDType, FailedValue)
      case (FFIDType, Unidentified) => statusAction(TNASupport, FFIDType, Unidentified)
      // Custom values come from disallowed Puids in da-metadata-schema
      case (FFIDType, customValue @ CustomValue(reason)) if ffidUserFixableCustomReasons.contains(reason) => statusAction(UserFixable, FFIDType, customValue)
      case (FFIDType, customValue @ CustomValue(_)) => statusAction(TNASupport, FFIDType, customValue)

      case (AntivirusType, VirusDetectedValue) => statusAction(TNASupport, AntivirusType, VirusDetectedValue)
      case (AntivirusType, FailedValue) => statusAction(TNASupport, AntivirusType, FailedValue)
      case (ChecksumMatchType, MismatchValue) => statusAction(TNASupport, ChecksumMatchType, MismatchValue)
      case (ChecksumMatchType, FailedValue) => statusAction(TNASupport, ChecksumMatchType, FailedValue)
      case (ClientChecksumType, FailedValue) => statusAction(TNASupport, ClientChecksumType, FailedValue)
      case (ClientFilePathType, FailedValue) => statusAction(TNASupport, ClientFilePathType, FailedValue)

      case (RedactionType, NoOriginalFileValue) => statusAction(UserFixable, RedactionType, NoOriginalFileValue)
      case (RedactionType, AmbiguousOriginalFileValue) => statusAction(UserFixable, RedactionType, AmbiguousOriginalFileValue)
      case (RedactionType, DuplicateFileNameValue) => statusAction(UserFixable, RedactionType, DuplicateFileNameValue)
      case (RedactionType, _) => statusAction(TNASupport, RedactionType, FailedValue)

      case (statusType, reason) => statusAction(TNASupport, statusType, reason)
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
