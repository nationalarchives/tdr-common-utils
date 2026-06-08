package uk.gov.nationalarchives.tdr.common.utils.statecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.StatusType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.StatusValue

import java.util.UUID

trait TransferState {
  def checkStateChange(stateChange: StateChange, state: List[ConsignmentStatuses]): Either[StateException, Boolean]
}

case class StateChange(consignmentId: UUID, statusType: StatusType, statusValue: StatusValue)

trait StateException extends Exception

case class StateChangeException(message: String) extends StateException
