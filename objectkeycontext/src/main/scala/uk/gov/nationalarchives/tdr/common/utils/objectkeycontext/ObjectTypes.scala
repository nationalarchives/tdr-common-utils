package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

object ObjectTypes {
  sealed trait ObjectType {
    val id: String
  }

  case object Csv extends ObjectType {
    val id: String = "csv"
  }

  case object Error extends ObjectType {
    val id: String = "error"
  }

  case object Metadata extends ObjectType {
    val id: String = "metadata"
  }

  case object Record extends ObjectType {
    val id: String = "record"
  }

  def toObjectType(objectCategory: String): ObjectType = {
    objectCategory match {
      case Csv.id      => Csv
      case Error.id    => Error
      case Metadata.id => Metadata
      case Record.id   => Record
      case _           => throw new RuntimeException(s"Invalid object type: $objectCategory")
    }
  }
}
