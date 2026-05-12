package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

object Buckets {
  sealed trait BucketNamePrefix {
    val id: String
  }

  case object BackendChecksBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-backend-checks"
  }

  case object BagitExportBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-consignment-export"
  }

  case object BagitExportJudgmentBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-consignment-export-judgment"
  }

  case object CleanUploadBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-upload-files"
  }

  case object DirtyUploadBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-upload-files-cloudfront-dirty"
  }

  case object DraftMetadataBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-draft-metadata"
  }

  case object ExportBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-export"
  }

  case object ExportJudgmentBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-export-judgment"
  }

  case object QuarantineBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-upload-files-quarantine"
  }

  case object TransferErrorsBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-transfer-errors"
  }
}
