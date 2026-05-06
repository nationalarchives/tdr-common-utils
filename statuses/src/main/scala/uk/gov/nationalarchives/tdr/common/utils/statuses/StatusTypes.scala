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

  case object AntivirusType extends StatusType {
    val id: String = "Antivirus"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
  }

  case object ChecksumMatchType extends StatusType {
    val id: String = "ChecksumMatch"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
  }

  case object ClientChecksType extends StatusType {
    val id: String = "ClientChecks"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = false
  }

  case object ClientChecksumType extends StatusType {
    val id: String = "ClientChecksum"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
  }

  case object ClientFilePathType extends StatusType {
    val id: String = "ClientFilePath"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
  }

  case object FFIDType extends StatusType {
    val id: String = "FFID"
    val nonJudgmentStatus: Boolean = false
    val fileCheckStatus: Boolean = true
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

  case object RedactionType extends StatusType {
    val id: String = "Redaction"
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
      case AntivirusType.id           => AntivirusType
      case ChecksumMatchType.id       => ChecksumMatchType
      case ClientChecksType.id        => ClientChecksType
      case ClientChecksumType.id      => ClientChecksumType
      case ClientFilePathType.id      => ClientFilePathType
      case ConfirmTransferType.id     => ConfirmTransferType
      case DraftMetadataType.id       => DraftMetadataType
      case DraftMetadataUploadType.id => DraftMetadataUploadType
      case ExportType.id              => ExportType
      case FFIDType.id                => FFIDType
      case MetadataReviewType.id      => MetadataReviewType
      case RedactionType.id         => RedactionType
      case SeriesType.id              => SeriesType
      case ServerAntivirusType.id     => ServerAntivirusType
      case ServerChecksumType.id      => ServerChecksumType
      case ServerFFIDType.id          => ServerFFIDType
      case ServerRedactionType.id     => ServerRedactionType
      case TransferAgreementType.id   => TransferAgreementType
      case UploadType.id              => UploadType
      case _                          => throw new RuntimeException(s"Invalid status type: $statusType")
    }
  }
}
