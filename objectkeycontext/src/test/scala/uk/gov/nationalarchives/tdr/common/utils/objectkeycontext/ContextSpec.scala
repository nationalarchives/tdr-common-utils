package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.AssetSources.SharePoint
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.Buckets.{DirtyUploadBucketPrefix, ExportBucketPrefix}
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.ObjectTypes.Metadata

import java.util.UUID

class ContextSpec extends AnyWordSpec with MockitoSugar {

  "objectKeyParser" should {
    "return the correct context for a default object key" in {
      val transferId = UUID.randomUUID()
      val objectName = s"${UUID.randomUUID()}.${ObjectTypes.Metadata.id}"
      val result = Context.objectKeyParser(s"$transferId/$objectName", "someBucketName")

      result.objectType.get shouldBe Metadata
      result.transferId.get shouldEqual transferId
      result.objectName.get shouldEqual objectName
      result.userId shouldBe None
      result.category shouldBe None
      result.assetSource shouldBe None
      result.assetId shouldBe None
      result.fileId shouldBe None
    }

    "return the correct context where key is for an uploaded object" in {
      val userId = UUID.randomUUID()
      val assetSource = SharePoint.id
      val transferId = UUID.randomUUID()
      val objectCategory = ObjectCategories.Metadata.id
      val objectName = s"${UUID.randomUUID()}.${ObjectTypes.Metadata.id}"
      val result = Context.objectKeyParser(s"$userId/$assetSource/$transferId/$objectCategory/$objectName", DirtyUploadBucketPrefix.id)

      result.userId.get shouldEqual userId
      result.objectName.get shouldEqual objectName
      result.transferId.get shouldEqual transferId
      result.assetSource.get shouldBe SharePoint
      result.category.get shouldBe ObjectCategories.Metadata
      result.objectType.get shouldBe ObjectTypes.Metadata
      result.assetId shouldBe None
      result.fileId shouldBe None
    }

    "return the correct context where key is for uploaded object prefix" in {
      val userId = UUID.randomUUID()
      val assetSource = SharePoint.id
      val transferId = UUID.randomUUID()
      val objectCategory = ObjectCategories.Metadata.id
      val result = Context.objectKeyParser(s"$userId/$assetSource/$transferId/$objectCategory", DirtyUploadBucketPrefix.id)

      result.userId.get shouldEqual userId
      result.objectName shouldEqual None
      result.transferId.get shouldEqual transferId
      result.assetSource.get shouldBe SharePoint
      result.category.get shouldBe ObjectCategories.Metadata
      result.objectType shouldBe None
      result.assetId shouldBe None
      result.fileId shouldBe None
    }

    "return the correct context for export metadata object" in {
      val assetId = UUID.randomUUID
      val objectName = s"$assetId.${ObjectTypes.Metadata.id}"
      val result = Context.objectKeyParser(s"$assetId.${ObjectTypes.Metadata.id}", ExportBucketPrefix.id)

      result.userId shouldBe None
      result.objectName.get shouldEqual objectName
      result.transferId shouldBe None
      result.assetSource shouldBe None
      result.category shouldBe None
      result.objectType.get shouldBe ObjectTypes.Metadata
      result.assetId.get shouldEqual assetId
      result.fileId shouldBe None
    }

    "return the correct context for export object" in {
      val assetId = UUID.randomUUID
      val fileId = UUID.randomUUID
      val objectName = s"$fileId"
      val result = Context.objectKeyParser(s"$assetId/$fileId", ExportBucketPrefix.id)
      result.userId shouldBe None
      result.objectName.get shouldEqual objectName
      result.transferId shouldBe None
      result.assetSource shouldBe None
      result.category shouldBe None
      result.objectType.get shouldEqual ObjectTypes.Record
      result.assetId.get shouldEqual assetId
      result.fileId.get shouldEqual fileId
    }

    "return an exception for a malformed object key" in {
      val exception = intercept[RuntimeException] {
        Context.objectKeyParser("unrecognized/key/context", "someBucketName")
      }

      exception.getMessage shouldEqual "Invalid object key unrecognized/key/context: Invalid UUID string: unrecognized"
    }
  }
}
