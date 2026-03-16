package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.AssetSources.SharePoint
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.ObjectTypes.Metadata

import java.util.UUID

class ContextSpec extends AnyWordSpec with MockitoSugar {

  "objectKeyParser" should {
    "return the correct context for an object key" in {
      val transferId = UUID.randomUUID()
      val objectName = s"${UUID.randomUUID()}.${ObjectTypes.Metadata.id}"
      val result = Context.objectKeyParser(s"$transferId/$objectName")

      result.objectType shouldBe Metadata
      result.transferId shouldEqual transferId
      result.objectName shouldEqual objectName
      result.userId shouldBe None
      result.category shouldBe None
      result.assetSource shouldBe None
    }

    "return the correct context where key is for uploaded object" in {
      val userId = UUID.randomUUID()
      val assetSource = SharePoint.id
      val transferId = UUID.randomUUID()
      val objectCategory = ObjectCategories.Metadata.id
      val objectName = s"${UUID.randomUUID()}.${ObjectTypes.Metadata.id}"
      val result = Context.objectKeyParser(s"$userId/$assetSource/$transferId/$objectCategory/$objectName")

      result.userId.get shouldBe userId
      result.objectName shouldEqual objectName
      result.transferId shouldBe transferId
      result.assetSource.get shouldBe SharePoint
      result.category.get shouldBe ObjectCategories.Metadata
      result.objectType shouldBe ObjectTypes.Metadata
    }

    "return an exception for a malformed object key" in {
      val exception = intercept[RuntimeException] {
        Context.objectKeyParser("unrecognized/key/context")
      }

      exception.getMessage shouldEqual "Invalid object key unrecognized/key/context: Invalid UUID string: unrecognized"
    }
  }
}
