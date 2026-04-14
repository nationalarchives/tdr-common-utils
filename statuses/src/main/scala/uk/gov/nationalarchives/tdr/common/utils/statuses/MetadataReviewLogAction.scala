package uk.gov.nationalarchives.tdr.common.utils.statuses

object MetadataReviewLogAction {
  sealed trait MetadataReviewLogAction {
    val value: String
  }

  case object Submission extends MetadataReviewLogAction {
    val value: String = "Submission"
  }

  case object Rejection extends MetadataReviewLogAction {
    val value: String = "Rejection"
  }

  case object Approval extends MetadataReviewLogAction {
    val value: String = "Approval"
  }

  case object Confirmation extends MetadataReviewLogAction {
    val value: String = "Confirmation"
  }

  object MetadataReviewLogAction {
    def apply(s: String): MetadataReviewLogAction = s match {
      case Submission.value   => Submission
      case Rejection.value    => Rejection
      case Approval.value     => Approval
      case Confirmation.value => Confirmation
      case _                  => throw new RuntimeException(s"Invalid metadata review log action: $s")
    }
  }
}
