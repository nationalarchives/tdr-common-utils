package uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.StatusType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.StatusValue

import java.util.UUID

trait TransferState {
  def checkStateChange(stateChange: StateChange, state: List[ConsignmentStatuses]): Either[StateException, Boolean]
}

case class StateChange(consignmentId: UUID, statusType: StatusType, statusValue: StatusValue)

//case class StateException(message: String) extends Exception(message)

trait StateException extends Exception

case class StateChangeException(message: String) extends StateException

case class TransferStateException(message: String) extends StateException

sealed trait CheckStateChangeResult {
  val id: String
}

case object Valid extends CheckStateChangeResult {
  val id: String = "Valid"
}

case object Invalid extends CheckStateChangeResult {
  val id: String = "Invalid"
}

object TransferStateErrorCodes {
  val invalidConsignmentState = "INVALID_CONSIGNMENT_STATE"
}
