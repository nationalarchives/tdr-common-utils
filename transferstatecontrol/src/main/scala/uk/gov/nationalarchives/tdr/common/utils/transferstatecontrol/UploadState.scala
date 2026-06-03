package uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.UploadType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, CompletedWithIssuesValue, InProgressValue}

private object UploadState extends TransferState {
  override def checkState(stateChange: StateChange, state: List[ConsignmentStatuses]): StateChangeResult = {
    val uploadState: Option[ConsignmentStatuses] = state.find(_.statusType == UploadType.id)
    stateChange.statusValue match {
      case InProgressValue if uploadState.isEmpty => Allow
      case CompletedValue if uploadState.nonEmpty && uploadState.get.value == InProgressValue.value => Allow
      case CompletedWithIssuesValue if uploadState.nonEmpty && uploadState.get.value == InProgressValue.value => Allow
      case _ => Deny
    }
  }

}
