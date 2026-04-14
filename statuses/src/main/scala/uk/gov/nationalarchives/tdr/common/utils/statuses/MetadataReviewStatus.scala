package uk.gov.nationalarchives.tdr.common.utils.statuses

object MetadataReviewStatus {
  sealed trait MetadataReviewStatus {
    val value: String
  }

  case object Requested extends MetadataReviewStatus {
    val value: String = "Requested"
  }

  case object Rejected extends MetadataReviewStatus {
    val value: String = "Rejected"
  }

  case object Approved extends MetadataReviewStatus {
    val value: String = "Approved"
  }

  case object Completed extends MetadataReviewStatus {
    val value: String = "Completed"
  }

  def toMetadataReviewStatus(s: String): MetadataReviewStatus = s match {
    case Requested.value => Requested
    case Rejected.value  => Rejected
    case Approved.value  => Approved
    case Completed.value => Completed
    case _               => throw new RuntimeException(s"Invalid metadata review status: $s")
  }
}
