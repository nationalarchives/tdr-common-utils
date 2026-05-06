package uk.gov.nationalarchives.tdr.common.utils.statuses

import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

object StatusActions {
  sealed trait StatusAction {
    val value: String
  }

  case object UserFixable extends StatusAction {
    val value: String = "UserFixable"
  }

  case object TNASupport extends StatusAction {
    val value: String = "TNASupport"
  }

  def action(statusType: StatusType, reason: StatusValue): Option[StatusAction] =
    (statusType, reason) match {
      case (_, SuccessValue)      => None
      case (_, CompletedValue)    => None
      case (_, InProgressValue)   => None

      case (FFIDType, NonJudgmentFormatValue) => Some(UserFixable)
      case (FFIDType, ZeroByteFileValue)      => Some(UserFixable)
      case (FFIDType, MultipleFormatsValue)   => Some(UserFixable)
      case (FFIDType, FailedValue)            => Some(UserFixable)

      case (AntivirusType, VirusDetectedValue) => Some(UserFixable)
      case (AntivirusType, FailedValue)        => Some(UserFixable)

      case (ChecksumMatchType, MismatchValue) => Some(UserFixable)
      case (ChecksumMatchType, FailedValue)   => Some(UserFixable)

      case (ClientChecksumType, FailedValue)  => Some(UserFixable)
      case (ClientFilePathType, FailedValue)  => Some(UserFixable)

      case (RedactionType, SuccessValue) => None
      case (RedactionType, _)            => Some(UserFixable)

      case _ => Some(UserFixable)
    }
}
