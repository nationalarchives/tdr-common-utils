package uk.gov.nationalarchives.tdr.common.utils.statecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, CompletedWithIssuesValue, FailedValue, InProgressValue}

private object ExportState extends TransferState {
  private val requiredStatuses = Set(
    UploadType,
    ClientChecksType,
    ServerFFIDType,
    ServerChecksumType,
    ServerAntivirusType,
    SeriesType,
    TransferAgreementType,
    DraftMetadataType,
    ServerRedactionType,
    MetadataReviewType
  )

  private lazy val requiredStatusIds = requiredStatuses.map(_.id)

  override def checkStateChange(stateChange: StateChange, currentState: CurrentState): Either[Exception, ValidStateChange] = {
    val statuses = currentState.statuses
    val requiredStatuses = statuses.filter(s => requiredStatusIds.contains(s.statusType))
    val requiredStatusesPresent: Boolean = requiredStatusIds.forall(statuses.map(_.statusType).contains)
    val requiredStatusesCompleted: Boolean = requiredStatuses.forall(_.value == CompletedValue.value)
    val exportStatus: Option[ConsignmentStatuses] = statuses.find(_.statusType == ExportType.id)

    stateChange.statusValue match {
      case InProgressValue if requiredStatusesPresent && requiredStatusesCompleted && exportStatus.isEmpty => Right(ValidStateChange())
      case CompletedValue | CompletedWithIssuesValue | FailedValue
        if requiredStatusesPresent && requiredStatusesCompleted && exportStatus.exists(_.value == InProgressValue.value) => Right(ValidStateChange())
      case _ => Left(StateChangeException(s"${ExportType.id} state change ${stateChange.statusValue.value} for ${stateChange.consignmentId} not allowed"))
    }
  }
}
