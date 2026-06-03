package uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.StatusType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.StatusValue

import java.util.UUID

trait TransferState {
  def checkState(stateChange: StateChange, state: List[ConsignmentStatuses]): StateChangeResult
}

case class StateChange(consignmentId: UUID, statusType: StatusType, statusValue: StatusValue)

sealed trait StateChangeResult {
  val id: String
}

case object Allow extends StateChangeResult {
  val id: String = "Allow"
}

case object Deny extends StateChangeResult {
  val id: String = "Deny"
}
