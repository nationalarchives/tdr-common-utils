package uk.gov.nationalarchives.tdr.common.utils.statecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.UploadType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, CompletedWithIssuesValue, FailedValue, InProgressValue}

private object UploadState extends TransferState {
  override def checkStateChange(stateChange: StateChange, currentState: CurrentState):  Either[Exception, ValidStateChange] = {
    val uploadStatus: Option[ConsignmentStatuses] = currentState.statuses.find(_.statusType == UploadType.id)

    stateChange.statusValue match {
      case InProgressValue if uploadStatus.isEmpty => Right(ValidStateChange())
      case CompletedValue | CompletedWithIssuesValue | FailedValue
        if uploadStatus.exists(_.value == InProgressValue.value) => Right(ValidStateChange())
      case _ =>
        Left(StateChangeException(s"${UploadType.id} state change ${stateChange.statusValue.value} for ${stateChange.consignmentId} not allowed"))
    }
  }
}
