package uk.gov.nationalarchives.tdr.common.utils.statuses

object StatusTypes {
  sealed trait StatusType {
    val id: String
    val nonJudgmentStatus: Boolean
    val fileCheckStatus: Boolean
  }

  case object SeriesType extends StatusType {
    val id: String = "Series"
    val nonJudgmentStatus: Boolean = true
    val fileCheckStatus: Boolean = false
  }

  case object UploadType extends StatusType {
    val id: String = "Upload"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = false
  }

  case object TransferAgreementType extends StatusType {
    val id: String = "TransferAgreement"
    val nonJudgmentStatus: Boolean = true
    val fileCheckStatus: Boolean = false
  }

  case object ClientChecksType extends StatusType {
    val id: String = "ClientChecks"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = false
  }

  case object ServerAntivirusType extends StatusType {
    val id: String = "ServerAntivirus"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
  }

  case object ServerChecksumType extends StatusType {
    val id: String = "ServerChecksum"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
  }

  case object ServerFFIDType extends StatusType {
    val id: String = "ServerFFID"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
  }

  case object ServerRedactionType extends StatusType {
    val id: String = "ServerRedaction"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
  }

  case object ConfirmTransferType extends StatusType {
    val id: String = "ConfirmTransfer"
    val nonJudgmentStatus: Boolean = true
    val fileCheckStatus: Boolean = false
  }

  case object ExportType extends StatusType {
    val id: String = "Export"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = false
  }

  case object DraftMetadataType extends StatusType {
    val id: String = "DraftMetadata"
    val nonJudgmentStatus: Boolean = true
    val fileCheckStatus: Boolean = false
  }

  case object MetadataReviewType extends StatusType {
    val id: String = "MetadataReview"
    val nonJudgmentStatus: Boolean = true
    val fileCheckStatus: Boolean = false
  }

  case object DraftMetadataUploadType extends StatusType {
    val id: String = "DraftMetadataUpload"
    val nonJudgmentStatus: Boolean = true
    val fileCheckStatus: Boolean = false
  }

  def toStatusType(statusType: String): StatusType = {
    statusType match {
      case ExportType.id              => ExportType
      case ConfirmTransferType.id     => ConfirmTransferType
      case ServerFFIDType.id          => ServerFFIDType
      case ServerChecksumType.id      => ServerChecksumType
      case ServerAntivirusType.id     => ServerAntivirusType
      case ClientChecksType.id        => ClientChecksType
      case UploadType.id              => UploadType
      case TransferAgreementType.id   => TransferAgreementType
      case SeriesType.id              => SeriesType
      case DraftMetadataType.id       => DraftMetadataType
      case MetadataReviewType.id      => MetadataReviewType
      case ServerRedactionType.id     => ServerRedactionType
      case DraftMetadataUploadType.id => DraftMetadataUploadType
      case _                        => throw new RuntimeException(s"Invalid status type: $statusType")
    }
  }
}
