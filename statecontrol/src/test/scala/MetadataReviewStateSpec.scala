import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor4}
import uk.gov.nationalarchives.tdr.common.utils.statecontrol._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.{DraftMetadataType, DraftMetadataUploadType, ExportType, MetadataReviewType, StatusType}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

import java.util.UUID

class MetadataReviewStateSpec extends BaseTestSpec with TableDrivenPropertyChecks {
  override val statusType: StatusType = MetadataReviewType

  private def prerequisitesOnly(
      draftMetadataStatus: StatusValue = CompletedValue,
      draftMetadataUploadStatus: StatusValue = CompletedValue
  ): List[ConsignmentStatuses] = List(
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, DraftMetadataType.id, draftMetadataStatus.value, someDateTime, None),
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, DraftMetadataUploadType.id, draftMetadataUploadStatus.value, someDateTime, None)
  )

  private def withMetadataReview(currentStatus: StatusValue): List[ConsignmentStatuses] =
    prerequisitesOnly() :+ ConsignmentStatuses(UUID.randomUUID(), consignmentId, statusType.id, currentStatus.value, someDateTime, None)

  private def withExport: List[ConsignmentStatuses] =
    prerequisitesOnly() :+ ConsignmentStatuses(UUID.randomUUID(), consignmentId, ExportType.id, CompletedValue.value, someDateTime, None)

  private def expectedErrorMessage(value: StatusValue) = s"MetadataReview state change ${value.value} for $consignmentId not allowed"

  private val metadataReviewStatusInputs: TableFor4[StatusValue, List[ConsignmentStatuses], String, Either[StateChangeException, ValidStateChange]] = Table(
    ("stateChangeValue", "currentStatuses", "currentStateDescription", "expectedResult"),
    (InProgressValue, prerequisitesOnly(), "prerequisites completed and no current metadata review status", Right(ValidStateChange())),
    (InProgressValue, withMetadataReview(CompletedValue), "prerequisites completed and metadata review completed", Right(ValidStateChange())),
    (InProgressValue, withMetadataReview(CompletedWithIssuesValue), "prerequisites completed and metadata review completed with issues", Right(ValidStateChange())),
    (InProgressValue, withMetadataReview(FailedValue), "prerequisites completed and metadata review failed", Right(ValidStateChange())),
    (InProgressValue, withMetadataReview(InProgressValue), "prerequisites completed and metadata review already in progress",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue, prerequisitesOnly(draftMetadataStatus = InProgressValue), "draft metadata not completed",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue, prerequisitesOnly(draftMetadataUploadStatus = InProgressValue), "draft metadata upload not completed",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue, withExport, "export exists",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (CompletedValue, withMetadataReview(InProgressValue), "metadata review in progress", Right(ValidStateChange())),
    (CompletedValue, prerequisitesOnly(), "no current metadata review status",
      Left(StateChangeException(expectedErrorMessage(CompletedValue))))
  )

  forAll(metadataReviewStatusInputs) {
    (stateChangeValue, currentStatuses, currentStateDescription, expectedResult) =>
    {
      s"for state change: ${stateChangeValue.value} with current state of: $currentStateDescription" should s"return $expectedResult" in {
        val checker = TransferState.apply(statusType)
        val result = checker.checkStateChange(stateChangeValue, CurrentState(consignmentId, currentStatuses))
        result shouldBe expectedResult
      }
    }
  }
}
