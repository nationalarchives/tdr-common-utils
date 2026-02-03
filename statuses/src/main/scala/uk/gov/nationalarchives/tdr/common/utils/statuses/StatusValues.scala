package uk.gov.nationalarchives.tdr.common.utils.statuses

object StatusValues {
  sealed trait StatusValue {
    val value: String
  }

  object StatusValue {
    def apply(id: String): StatusValue = id match {
      case CompletedWithIssuesValue.value => CompletedWithIssuesValue
      case CompletedValue.value           => CompletedValue
      case InProgressValue.value          => InProgressValue
      case FailedValue.value              => FailedValue
      case _                              => throw new RuntimeException(s"Invalid status value: $id")
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
}
