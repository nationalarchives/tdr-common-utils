package uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.{ExportType, UploadType}

object TransferStateControl {

  private def checkChange(stateChange: StateChange, state: List[ConsignmentStatuses]): StateChangeResult = {
    stateChange.statusType match {
      case UploadType => UploadState.checkState(stateChange, state)
      case ExportType => ExportState.checkState(stateChange, state)
      case _ => Deny
    }
  }

  def changeTransferState(stateChange: StateChange, state: List[ConsignmentStatuses]): StateChangeResult = {
    val stateConsignmentIds = state.map(_.consignmentId).toSet
    stateConsignmentIds.size match {
      case 0 => checkChange(stateChange, state)
      case 1 if stateConsignmentIds.head == stateChange.consignmentId => checkChange(stateChange, state)
      case _ => Deny
    }
  }
}
