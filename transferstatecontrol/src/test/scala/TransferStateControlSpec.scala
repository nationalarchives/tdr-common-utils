import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor3}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.UploadType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, CompletedWithIssuesValue, FailedValue, InProgressValue}
import uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol._

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.util.UUID

class TransferStateControlSpec extends SpecUtils with TableDrivenPropertyChecks {
  "'changeTransferState" should "return 'Deny' when state change consignment id does not match current state consignment id" in {
    val currentState = List(ConsignmentStatuses(UUID.randomUUID(), UUID.randomUUID(), UploadType.id, InProgressValue.value, someDateTime, None))
    val stateChange = StateChange(consignmentId, UploadType, CompletedValue)
    val result = TransferStateControl.changeTransferState(stateChange, currentState)
    result shouldBe Deny
  }

  "'changeTransferState" should "return 'Deny' when current state contains different consignment ids" in {
    val currentState = List(
      ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, InProgressValue.value, someDateTime, None),
      ConsignmentStatuses(UUID.randomUUID(), UUID.randomUUID(), UploadType.id, InProgressValue.value, someDateTime, None)
    )

    val stateChange = StateChange(consignmentId, UploadType, CompletedValue)
    val result = TransferStateControl.changeTransferState(stateChange, currentState)
    result shouldBe Deny
  }
}
