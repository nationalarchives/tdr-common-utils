package uk.gov.nationalarchives.tdr.common.utils.statecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.UploadType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, CompletedWithIssuesValue, FailedValue, InProgressValue}

private object UploadState extends TransferState {
  override def checkStateChange(stateChange: StateChange, state: List[ConsignmentStatuses]):  Either[StateException, Boolean] = {
    val uploadStatus: Option[ConsignmentStatuses] = state.find(_.statusType == UploadType.id)

    stateChange.statusValue match {
      case InProgressValue if uploadStatus.isEmpty => Right(true)
      case CompletedValue | CompletedWithIssuesValue | FailedValue
        if uploadStatus.nonEmpty && uploadStatus.get.value == InProgressValue.value => Right(true)
      case CompletedWithIssuesValue if uploadStatus.exists(_.value == InProgressValue.value) => Right(true)
      case _ =>
        Left(StateChangeException(s"${UploadType.id} state change ${stateChange.statusValue.value} for ${stateChange.consignmentId} not allowed"))
    }
  }
}
