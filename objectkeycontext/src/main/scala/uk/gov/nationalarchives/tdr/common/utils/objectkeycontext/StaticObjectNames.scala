package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

object StaticObjectNames {
  sealed trait StaticObjectName {
    val id: String
  }

  case object DraftMetadataObject extends StaticObjectName {
    val id: String = "draft-metadata"
  }

  case object DraftMetadataErrorObject extends StaticObjectName {
    val id: String = "draft-metadata-errors"
  }
}
