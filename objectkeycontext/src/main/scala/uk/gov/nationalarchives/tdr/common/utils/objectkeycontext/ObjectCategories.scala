package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

object ObjectCategories {
  sealed trait ObjectCategory {
    val id: String
  }

  case object DryRunMetadata extends ObjectCategory {
    val id: String = "dryrunmetadata"
  }

  case object Metadata extends ObjectCategory {
    val id: String = "metadata"
  }

  case object Records extends ObjectCategory {
    val id: String = "records"
  }

  def toObjectCategory(objectCategory: String): ObjectCategory = {
    objectCategory match {
      case DryRunMetadata.id => DryRunMetadata
      case Metadata.id       => Metadata
      case Records.id        => Records
      case _                 => throw new RuntimeException(s"Invalid object category: $objectCategory")
    }
  }
}
