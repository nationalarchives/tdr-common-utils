package uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.{ExportType, UploadType}

object TransferStateControl {

  private def checkChange(stateChange: StateChange, state: List[ConsignmentStatuses]): Either[StateException, Boolean] = {
    stateChange.statusType match {
      case UploadType => UploadState.checkStateChange(stateChange, state)
      case ExportType => ExportState.checkStateChange(stateChange, state)
      case _ => Left(StateChangeException(s"Unrecognised status type: ${stateChange.statusType.id}"))
    }
  }

  def transferStateChangeValid(stateChange: StateChange, state: List[ConsignmentStatuses]): Either[StateException, Boolean] = {
    val stateConsignmentIds = state.map(_.consignmentId).toSet
    stateConsignmentIds.size match {
      case 0 => checkChange(stateChange, state)
      case 1 if stateConsignmentIds.head == stateChange.consignmentId => checkChange(stateChange, state)
      case _ => Left(StateChangeException("Request contains mismatched consignment ids"))
    }
  }
}
