package uk.gov.nationalarchives.tdr.common.utils.statecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.StatusType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.StatusValue

import java.util.UUID

trait TransferState {
  def checkStateChange(stateChange: StateChange, currentState: CurrentState): Either[Exception, ValidStateChange]
}

case class StateChange(consignmentId: UUID, statusType: StatusType, statusValue: StatusValue)

case class CurrentState(statuses: List[ConsignmentStatuses])

case class ValidStateChange()

case class StateChangeException(message: String) extends Exception(message)
