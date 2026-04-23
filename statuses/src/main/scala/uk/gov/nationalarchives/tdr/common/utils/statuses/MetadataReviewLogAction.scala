package uk.gov.nationalarchives.tdr.common.utils.statuses

import uk.gov.nationalarchives.tdr.common.utils.statuses.MetadataReviewStatus._

object MetadataReviewLogAction {
  sealed trait MetadataReviewLogAction {
    val value: String
    val reviewStatus: MetadataReviewStatus
  }

  case object Submission extends MetadataReviewLogAction {
    val value: String = "Submission"
    val reviewStatus: MetadataReviewStatus = Requested
  }

  case object Rejection extends MetadataReviewLogAction {
    val value: String = "Rejection"
    val reviewStatus: MetadataReviewStatus = Rejected
  }

  case object Approval extends MetadataReviewLogAction {
    val value: String = "Approval"
    val reviewStatus: MetadataReviewStatus = Approved
  }

  case object Confirmation extends MetadataReviewLogAction {
    val value: String = "Confirmation"
    val reviewStatus: MetadataReviewStatus = Transferred
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
