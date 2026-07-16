import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.TableDrivenPropertyChecks
import uk.gov.nationalarchives.tdr.common.utils.statecontrol.{CurrentState, StateChangeException, TransferState}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.{ExportType, StatusType, TransferAgreementType, UploadType}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues.{CompletedValue, InProgressValue}

import java.util.UUID

trait BaseTestSpec extends SpecUtils with TableDrivenPropertyChecks {
  val statusType: StatusType

  "TransferState" should "throw an exception when the status type is not supported" in {
    val unsupportedStatusType = TransferAgreementType
    val result = intercept[StateChangeException] {
      TransferState.apply(unsupportedStatusType)
    }

    result.message shouldEqual s"Unsupported status type: ${unsupportedStatusType.id}"
  }

  "checkStateChange" should "return an exception where the consignment id in the statuses do not match the provided consignment id" in {
      val status = ConsignmentStatuses(UUID.randomUUID(), UUID.randomUUID(), statusType.id, CompletedValue.value, someDateTime, None)
      val checker = TransferState.apply(statusType)
      val result = checker.checkStateChange(InProgressValue, CurrentState(consignmentId, List(status)))
      result shouldBe Left(StateChangeException("Request contains mismatched consignment ids"))
  }

  "checkStateChange" should "return an exception where the consignment id in the statuses do not match" in {
      val statuses = List(
        ConsignmentStatuses(UUID.randomUUID(), UUID.randomUUID(), statusType.id, CompletedValue.value, someDateTime, None),
        ConsignmentStatuses(UUID.randomUUID(), consignmentId, statusType.id, CompletedValue.value, someDateTime, None)
      )

      val checker = TransferState.apply(statusType)
      val result = checker.checkStateChange(InProgressValue, CurrentState(consignmentId, statuses))
      result shouldBe Left(StateChangeException("Request contains mismatched consignment ids"))
  }
}
