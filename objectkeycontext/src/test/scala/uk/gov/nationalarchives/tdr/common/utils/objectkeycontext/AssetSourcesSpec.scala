package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.AssetSources.{Droid, HardDrive, NetworkDrive, SharePoint}

class AssetSourcesSpec  extends AnyWordSpec with MockitoSugar {
  "AssetSources" should {
    "have the correct field values" in {
      Droid.id shouldEqual "droid"
      HardDrive.id shouldEqual "harddrive"
      NetworkDrive.id shouldEqual "networkdrive"
      SharePoint.id shouldEqual "sharepoint"
    }
  }

  "toAssetSource" should {
    "return the correct 'asset source' based on input string" in {
      AssetSources.toAssetSource("droid") shouldBe Droid
      AssetSources.toAssetSource("harddrive") shouldBe HardDrive
      AssetSources.toAssetSource("networkdrive") shouldBe NetworkDrive
      AssetSources.toAssetSource("sharepoint") shouldBe SharePoint
    }

    "throw an exception when status is unrecognised" in {
      val exception = intercept[RuntimeException] {
        AssetSources.toAssetSource("someRandomValue")
      }

      exception.getMessage shouldEqual "Invalid asset source: someRandomValue"
    }
  }
}
