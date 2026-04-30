package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

object Buckets {
  sealed trait BucketNamePrefix {
    val id: String
  }

  case object DirtyUploadBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-upload-files-cloudfront-dirty"
  }

  case object ExportBucketPrefix extends BucketNamePrefix {
    val id: String = "tdr-export"
  }
}
