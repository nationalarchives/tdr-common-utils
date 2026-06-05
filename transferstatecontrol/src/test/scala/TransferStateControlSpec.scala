import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.UploadType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, InProgressValue}
import uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol._

import java.util.UUID

class TransferStateControlSpec extends SpecUtils {
  "'changeTransferState" should "return an exception when state change consignment id does not match current state consignment id" in {
    val currentState = List(ConsignmentStatuses(UUID.randomUUID(), UUID.randomUUID(), UploadType.id, InProgressValue.value, someDateTime, None))
    val stateChange = StateChange(consignmentId, UploadType, CompletedValue)
    val result = TransferStateControl.transferStateChangeValid(stateChange, currentState)

    result shouldBe Left(StateChangeException("Request contains mismatched consignment ids"))
  }

  "'changeTransferState" should "return an exception when current state contains different consignment ids" in {
    val currentState = List(
      ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, InProgressValue.value, someDateTime, None),
      ConsignmentStatuses(UUID.randomUUID(), UUID.randomUUID(), UploadType.id, InProgressValue.value, someDateTime, None)
    )

    val stateChange = StateChange(consignmentId, UploadType, CompletedValue)

    val result = TransferStateControl.transferStateChangeValid(stateChange, currentState)

    result shouldBe Left(StateChangeException("Request contains mismatched consignment ids"))
  }
}
