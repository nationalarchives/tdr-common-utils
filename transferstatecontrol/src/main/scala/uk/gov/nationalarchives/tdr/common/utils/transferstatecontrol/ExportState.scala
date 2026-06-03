package uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, CompletedWithIssuesValue, FailedValue, InProgressValue}

private object ExportState extends TransferState {
  private val requiredStates = Set(
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

  override def checkState(stateChange: StateChange, state: List[ConsignmentStatuses]): StateChangeResult = {
    val allStatesPresent: Boolean = requiredStates.map(_.id).exists(t => state.map(_.statusType).exists(t.contains))
    val allStatesCompleted: Boolean = state.forall(_.value == CompletedValue.value)
    val exportState: Option[ConsignmentStatuses] = state.find(_.statusType == ExportType.id)

    stateChange.statusValue match {
      case InProgressValue if allStatesPresent && allStatesCompleted && exportState.isEmpty => Allow
      case CompletedValue | CompletedWithIssuesValue | FailedValue
        if allStatesPresent && allStatesCompleted && exportState.nonEmpty && exportState.get.value == InProgressValue.value => Allow
      case _ => Deny
    }

  }
}
