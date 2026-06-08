package uk.gov.nationalarchives.tdr.common.utils.statuses

object StatusScopes {
  sealed trait StatusScope {
    val value: String
  }

  object StatusScope {
    def apply(id: String): StatusScope = id match {
      case FileScope.value        => FileScope
      case ConsignmentScope.value => ConsignmentScope
      case _                      => throw new RuntimeException(s"Invalid status scope: $id")
    }
  }

  case object FileScope extends StatusScope {
    val value: String = "File"
  }

  case object ConsignmentScope extends StatusScope {
    val value: String = "Consignment"
  }
}
