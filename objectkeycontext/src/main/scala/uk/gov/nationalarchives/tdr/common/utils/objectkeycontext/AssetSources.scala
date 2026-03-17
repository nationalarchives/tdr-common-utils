package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

object AssetSources {
  sealed trait AssetSource {
    val id: String
  }

  case object Droid extends AssetSource {
    val id: String = "droid"
  }

  case object HardDrive extends AssetSource {
    val id: String = "harddrive"
  }

  case object NetworkDrive extends AssetSource {
    val id: String = "networkdrive"
  }

  case object SharePoint extends AssetSource {
    val id: String = "sharepoint"
  }

  def toAssetSource(assetSource: String): AssetSource = {
    assetSource match {
      case Droid.id        => Droid
      case HardDrive.id    => HardDrive
      case NetworkDrive.id => NetworkDrive
      case SharePoint.id   => SharePoint
      case _               => throw new RuntimeException(s"Invalid asset source: $assetSource")
    }
  }
}
