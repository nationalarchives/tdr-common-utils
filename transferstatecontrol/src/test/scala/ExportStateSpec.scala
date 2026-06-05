import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor4}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._
import uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol._

import java.util.UUID

class ExportStateSpec extends SpecUtils with TableDrivenPropertyChecks {
  private def setCurrentExportState(statusValue: StatusValue) =
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, ExportType.id, statusValue.value, someDateTime, None)

  private def setStateChange(statusValue: StatusValue) = StateChange(consignmentId, ExportType, statusValue)

  private def setCurrentState(overrideStatusValue: StatusValue = CompletedValue): List[ConsignmentStatuses] = List(
    ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, overrideStatusValue.value, someDateTime, None),
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

  private def expectedErrorMessage(value: StatusValue) = s"Export state change ${value.value} for $consignmentId not allowed"

  private val exportStatusInputs: TableFor4[StatusValue, List[ConsignmentStatuses], String, Either[StateChangeException, Boolean]] = Table(
    ("stateChangeStatusValue", "currentState", "currentStateDescription", "expectedResult"),
    //InProgress state change
    (InProgressValue, setCurrentState(), "all required statuses 'completed'", Right(true)),
    (InProgressValue, setCurrentState(InProgressValue), "all required statuses are not 'completed'",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue, setCurrentState().tail, "missing required statuses",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue,
      setCurrentState() :+ setCurrentExportState(InProgressValue), "all required statuses 'completed' and export status 'in progress'",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue,
      setCurrentState() :+ setCurrentExportState(CompletedValue), "all required statuses 'completed' and export status 'completed'",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue,
      setCurrentState() :+ setCurrentExportState(CompletedWithIssuesValue), "all required statuses 'completed' and export status 'completed with issues'",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue,
      setCurrentState() :+ setCurrentExportState(FailedValue), "all required statuses 'completed' and export status 'failed'",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    //Completed state change
    (CompletedValue, setCurrentState(), "all required statuses 'completed'",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    (CompletedValue, setCurrentState(InProgressValue), "all required statuses are not 'completed'",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    (CompletedValue, setCurrentState().tail, "missing required statuses",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    (CompletedValue,
      setCurrentState() :+ setCurrentExportState(InProgressValue), "all required statuses 'completed' and export status 'in progress'", Right(true)),
    (CompletedValue,
      setCurrentState() :+ setCurrentExportState(CompletedValue), "all required statuses 'completed' and export status 'completed'",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    (CompletedValue,
      setCurrentState() :+ setCurrentExportState(CompletedWithIssuesValue), "all required statuses 'completed' and export status 'completed with issues'",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    (CompletedValue,
      setCurrentState() :+ setCurrentExportState(FailedValue), "all required statuses 'completed' and export status 'failed'",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    //CompletedWithIssues state change
    (CompletedWithIssuesValue, setCurrentState(), "all required statuses 'completed'",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    (CompletedWithIssuesValue, setCurrentState(InProgressValue), "all required statuses are not 'completed'",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    (CompletedWithIssuesValue, setCurrentState().tail, "missing required statuses",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    (CompletedWithIssuesValue,
      setCurrentState() :+ setCurrentExportState(InProgressValue), "all required statuses 'completed' and export status 'in progress'", Right(true)),
    (CompletedWithIssuesValue,
      setCurrentState() :+ setCurrentExportState(CompletedValue), "all required statuses 'completed' and export status 'completed'",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    (CompletedWithIssuesValue,
      setCurrentState() :+ setCurrentExportState(CompletedWithIssuesValue), "all required statuses 'completed' and export status 'completed with issues'",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    (CompletedWithIssuesValue,
      setCurrentState() :+ setCurrentExportState(FailedValue), "all required statuses 'completed' and export status 'failed'",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    //Failed state change
    (FailedValue, setCurrentState(), "all required statuses 'completed'",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
    (FailedValue, setCurrentState(InProgressValue), "all required statuses are not 'completed'",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
    (FailedValue, setCurrentState().tail, "missing required statuses",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
    (FailedValue,
      setCurrentState() :+ setCurrentExportState(InProgressValue), "all required statuses 'completed' and export status 'in progress'", Right(true)),
    (FailedValue,
      setCurrentState() :+ setCurrentExportState(CompletedValue), "all required statuses 'completed' and export status 'completed'",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
    (FailedValue,
      setCurrentState() :+ setCurrentExportState(CompletedWithIssuesValue), "all required statuses 'completed' and export status 'completed with issues'",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
    (FailedValue,
      setCurrentState() :+ setCurrentExportState(FailedValue), "all required statuses 'completed' and export status 'failed'",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
  )

  forAll(exportStatusInputs) {
    (stateChangeStatusValue, currentState, currentStateDescription, expectedResult) => {
      s"for state change status value: ${stateChangeStatusValue.value} with current state of: $currentStateDescription" should s"return $expectedResult" in {
        val result = TransferStateControl.transferStateChangeValid(setStateChange(stateChangeStatusValue), currentState)
        result shouldBe expectedResult
      }
    }
  }
}
