package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.Buckets.{DirtyUploadBucketPrefix, ExportBucketPrefix}

class BucketsSpec  extends AnyWordSpec with MockitoSugar {
  "Buckets" should {
    "have the correct field values" in {
      DirtyUploadBucketPrefix.id shouldEqual "tdr-upload-files-cloudfront-dirty"
      ExportBucketPrefix.id shouldEqual "tdr-export"
    }
  }
}
