import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor3}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.{ClientChecksType, DraftMetadataType, ExportType, MetadataReviewType, SeriesType, ServerAntivirusType, ServerChecksumType, ServerFFIDType, ServerRedactionType, TransferAgreementType, UploadType}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._
import uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol._

import java.util.UUID

class ExportStateSpec extends SpecUtils with TableDrivenPropertyChecks {
  private val exportStatusInProgress =
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, ExportType.id, InProgressValue.value, someDateTime, None)


  private val allCompletedStatuses = List(
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, SeriesType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, ClientChecksType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, ServerFFIDType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, ServerChecksumType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, ServerAntivirusType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, TransferAgreementType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, DraftMetadataType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, ServerRedactionType.id, CompletedValue.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, MetadataReviewType.id, CompletedValue.value, someDateTime, None),
  )

  private val exportStatusInputs: TableFor3[StateChange, List[ConsignmentStatuses], StateChangeResult] = Table(
    ("stateChange", "currentState", "expectedResult"),
    (StateChange(consignmentId, ExportType, InProgressValue), allCompletedStatuses, Allow),
    (StateChange(consignmentId, ExportType, InProgressValue), allCompletedStatuses :+ exportStatusInProgress, Deny)
  )

  forAll(exportStatusInputs) {
    (stateChange, currentState, expectedResult) => {
      s"for state change: ${stateChange.statusType} and ${stateChange.statusValue} with current state of: ${currentState.headOption.getOrElse("None")}" should s"return $expectedResult" in {
        val result = TransferStateControl.changeTransferState(stateChange, currentState)
        result shouldBe expectedResult
      }
    }
  }

//  "x" should "y" in {
//    val stateChange = StateChange(consignmentId, ExportType, InProgressValue)
//    val result = TransferStateControl.changeTransferState(stateChange, allCompletedStatuses)
//
//    result shouldBe Allow
//  }



//  private val uploadStatusInProgress = ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, InProgressValue.value, someDateTime, None)
//  private val uploadStatusCompleted = ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, CompletedValue.value, someDateTime, None)
//  private val uploadStatusCompletedWithIssues = ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, CompletedWithIssuesValue.value, someDateTime, None)
//  private val uploadStatusFailed = ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, FailedValue.value, someDateTime, None)
//
//  private val uploadStatusInputs: TableFor3[StateChange, List[ConsignmentStatuses], StateChangeResult] = Table(
//    ("stateChange", "currentState", "expectedResult"),
//    (StateChange(consignmentId, UploadType, InProgressValue), Nil, Allow),
//    (StateChange(consignmentId, UploadType, InProgressValue), List(uploadStatusInProgress), Deny),
//    (StateChange(consignmentId, UploadType, InProgressValue), List(uploadStatusCompleted), Deny),
//    (StateChange(consignmentId, UploadType, InProgressValue), List(uploadStatusCompletedWithIssues), Deny),
//    (StateChange(consignmentId, UploadType, InProgressValue), List(uploadStatusFailed), Deny),
//    (StateChange(consignmentId, UploadType, CompletedValue), Nil, Deny),
//    (StateChange(consignmentId, UploadType, CompletedValue), List(uploadStatusInProgress), Allow),
//    (StateChange(consignmentId, UploadType, CompletedValue), List(uploadStatusCompleted), Deny),
//    (StateChange(consignmentId, UploadType, CompletedValue), List(uploadStatusCompletedWithIssues), Deny),
//    (StateChange(consignmentId, UploadType, CompletedValue), List(uploadStatusFailed), Deny),
//    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), Nil, Deny),
//    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), List(uploadStatusInProgress), Allow),
//    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), List(uploadStatusCompleted), Deny),
//    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), List(uploadStatusCompletedWithIssues), Deny),
//    (StateChange(consignmentId, UploadType, CompletedWithIssuesValue), List(uploadStatusFailed), Deny),
//    (StateChange(consignmentId, UploadType, FailedValue), Nil, Deny),
//    (StateChange(consignmentId, UploadType, FailedValue), List(uploadStatusInProgress), Deny),
//    (StateChange(consignmentId, UploadType, FailedValue), List(uploadStatusCompleted), Deny),
//    (StateChange(consignmentId, UploadType, FailedValue), List(uploadStatusCompletedWithIssues), Deny),
//    (StateChange(consignmentId, UploadType, FailedValue), List(uploadStatusFailed), Deny),
//  )
//
//  forAll(uploadStatusInputs) {
//    (stateChange, currentState, expectedResult) =>
//    {
//      s"for state change: ${stateChange.statusType} and ${stateChange.statusValue} with current state of: ${currentState.headOption.getOrElse("None")}" should s"return $expectedResult" in {
//        val result = TransferStateControl.changeTransferState(stateChange, currentState)
//        result shouldBe expectedResult
//      }
//    }
//  }

}
