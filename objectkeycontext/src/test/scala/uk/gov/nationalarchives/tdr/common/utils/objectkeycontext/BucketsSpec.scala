package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.Buckets._

class BucketsSpec  extends AnyWordSpec with MockitoSugar {
  "Buckets" should {
    "have the correct field values" in {
      BackendChecksBucketPrefix.id shouldEqual "tdr-backend-checks"
      BagitExportBucketPrefix.id shouldEqual "tdr-consignment-export"
      BagitExportJudgmentBucketPrefix.id shouldEqual "tdr-consignment-export-judgment"
      CleanUploadBucketPrefix.id shouldEqual "tdr-upload-files"
      DirtyUploadBucketPrefix.id shouldEqual "tdr-upload-files-cloudfront-dirty"
      DraftMetadataBucketPrefix.id shouldEqual "tdr-draft-metadata"
      ExportBucketPrefix.id shouldEqual "tdr-export"
      ExportJudgmentBucketPrefix.id shouldEqual "tdr-export-judgment"
      QuarantineBucketPrefix.id shouldEqual "tdr-upload-files-quarantine"
      TransferErrorsBucketPrefix.id shouldEqual "tdr-transfer-errors"
    }
  }
}
