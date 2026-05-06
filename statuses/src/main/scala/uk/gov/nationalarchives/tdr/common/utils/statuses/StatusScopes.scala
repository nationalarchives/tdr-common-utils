package uk.gov.nationalarchives.tdr.common.utils.statuses

object StatusScopes {
  sealed trait StatusScope {
    val value: String
  }

  case object FileScope extends StatusScope {
    val value: String = "File"
  }

  case object ConsignmentScope extends StatusScope {
    val value: String = "Consignment"
  }
}
