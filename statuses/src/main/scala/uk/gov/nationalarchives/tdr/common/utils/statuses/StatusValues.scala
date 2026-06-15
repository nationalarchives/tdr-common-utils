package uk.gov.nationalarchives.tdr.common.utils.statuses

object StatusValues {
  sealed trait StatusValue {
    val value: String
  }

  object StatusValue {
    def apply(id: String): StatusValue = id match {
      case CompletedWithIssuesValue.value   => CompletedWithIssuesValue
      case CompletedValue.value             => CompletedValue
      case InProgressValue.value            => InProgressValue
      case FailedValue.value                => FailedValue
      case SuccessValue.value               => SuccessValue
      case VirusDetectedValue.value         => VirusDetectedValue
      case NonJudgmentFormatValue.value     => NonJudgmentFormatValue
      case MismatchValue.value              => MismatchValue
      case ZeroByteFileValue.value          => ZeroByteFileValue
      case MultipleFormatsValue.value       => MultipleFormatsValue
      case NoOriginalFileValue.value        => NoOriginalFileValue
      case AmbiguousOriginalFileValue.value => AmbiguousOriginalFileValue
      case DuplicateFileNameValue.value     => DuplicateFileNameValue
      case SkippedValue.value               => SkippedValue
      case other                            => CustomValue(other)
    }
  }

  case object CompletedValue extends StatusValue {
    val value: String = "Completed"
  }

  case object CompletedWithIssuesValue extends StatusValue {
    val value: String = "CompletedWithIssues"
  }

  case object InProgressValue extends StatusValue {
    val value: String = "InProgress"
  }

  case object FailedValue extends StatusValue {
    val value: String = "Failed"
  }

  case object SuccessValue extends StatusValue {
    val value: String = "Success"
  }

  case object VirusDetectedValue extends StatusValue {
    val value: String = "VirusDetected"
  }

  case object NonJudgmentFormatValue extends StatusValue {
    val value: String = "NonJudgmentFormat"
  }

  case object MismatchValue extends StatusValue {
    val value: String = "Mismatch"
  }

  case object ZeroByteFileValue extends StatusValue {
    val value: String = "ZeroByteFile"
  }

  case object MultipleFormatsValue extends StatusValue {
    val value: String = "MultipleFormats"
  }

  case object NoOriginalFileValue extends StatusValue {
    val value: String = "NoOriginalFile"
  }

  case object AmbiguousOriginalFileValue extends StatusValue {
    val value: String = "AmbiguousOriginalFile"
  }

  case object DuplicateFileNameValue extends StatusValue {
    val value: String = "DuplicateFileName"
  }

  case object SkippedValue extends StatusValue {
    val value: String = "Skipped"
  }

  case object Unidentified extends StatusValue {
    val value: String = "Unidentified"
  }

  case class CustomValue(value: String) extends StatusValue
}
