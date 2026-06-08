package uk.gov.nationalarchives.tdr.common.utils.statecontrol

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

  /**
   * Method to check if the given transfer's state can be changed based on it's current state
   *
   * @param stateChange
   * Change of state to check
   *
   * @param currentState
   * Current state of the transfer made up of it's statuses
   *
   * @return
   * Either true if the state change is permitted or a state exception
   *
   * */
  def transferStateChangeValid(stateChange: StateChange, currentState: List[ConsignmentStatuses]): Either[StateException, Boolean] = {
    val stateConsignmentIds = currentState.map(_.consignmentId).toSet
    stateConsignmentIds.size match {
      case 0 => checkChange(stateChange, currentState)
      case 1 if stateConsignmentIds.head == stateChange.consignmentId => checkChange(stateChange, currentState)
      case _ => Left(StateChangeException("Request contains mismatched consignment ids"))
    }
  }
}
