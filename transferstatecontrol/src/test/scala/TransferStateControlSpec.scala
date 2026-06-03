import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor3}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.UploadType
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, CompletedWithIssuesValue, FailedValue, InProgressValue}
import uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol._

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.util.UUID

class TransferStateControlSpec extends AnyFlatSpec with TableDrivenPropertyChecks {
  private val consignmentId = UUID.fromString("b130e097-2edc-4e67-a7e9-5364a09ae9cb")
  private val someDateTime: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 3, 10, 1, 0), ZoneId.systemDefault())

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

  private val uploadStatusInProgress = ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, InProgressValue.value, someDateTime, None)
  private val uploadStatusCompleted = ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, CompletedValue.value, someDateTime, None)
  private val uploadStatusCompletedWithIssues = ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, CompletedWithIssuesValue.value, someDateTime, None)
  private val uploadStatusFailed = ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, FailedValue.value, someDateTime, None)

  private val uploadStatusInputs: TableFor3[StateChange, List[ConsignmentStatuses], StateChangeResult] = Table(
    ("stateChange", "currentState", "expectedResult"),
    (StateChange(consignmentId, UploadType, InProgressValue), Nil, Allow),
    (StateChange(consignmentId, UploadType, InProgressValue), List(uploadStatusInProgress), Deny),
    (StateChange(consignmentId, UploadType, InProgressValue), List(uploadStatusCompleted), Deny),
    (StateChange(consignmentId, UploadType, InProgressValue), List(uploadStatusCompletedWithIssues), Deny),
    (StateChange(consignmentId, UploadType, InProgressValue), List(uploadStatusFailed), Deny),
    (StateChange(consignmentId, UploadType, CompletedValue), Nil, Deny),
    (StateChange(consignmentId, UploadType, CompletedValue), List(uploadStatusInProgress), Allow),
    (StateChange(consignmentId, UploadType, CompletedValue), List(uploadStatusCompleted), Deny),
    (StateChange(consignmentId, UploadType, CompletedValue), List(uploadStatusCompletedWithIssues), Deny),
    (StateChange(consignmentId, UploadType, CompletedValue), List(uploadStatusFailed), Deny),
    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), Nil, Deny),
    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), List(uploadStatusInProgress), Allow),
    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), List(uploadStatusCompleted), Deny),
    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), List(uploadStatusCompletedWithIssues), Deny),
    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), List(uploadStatusFailed), Deny),
    (StateChange(consignmentId, UploadType, FailedValue), Nil, Deny),
    (StateChange(consignmentId, UploadType, FailedValue), List(uploadStatusInProgress), Deny),
    (StateChange(consignmentId, UploadType, FailedValue), List(uploadStatusCompleted), Deny),
    (StateChange(consignmentId, UploadType, FailedValue), List(uploadStatusCompletedWithIssues), Deny),
    (StateChange(consignmentId, UploadType, FailedValue), List(uploadStatusFailed), Deny),
  )

  forAll(uploadStatusInputs) {
    (stateChange, currentState, expectedResult) =>
    {
      s"for state change: ${stateChange.statusType} and ${stateChange.statusValue} with current state of: ${currentState.headOption.getOrElse("None")}" should s"return $expectedResult" in {
        val result = TransferStateControl.changeTransferState(stateChange, currentState)
        result shouldBe expectedResult
      }
    }
  }
}
