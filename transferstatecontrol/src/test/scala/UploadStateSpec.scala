import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor4}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes.{StatusType, UploadType}
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._
import uk.gov.nationalarchives.tdr.common.utils.transferstatecontrol._

import java.util.UUID

class UploadStateSpec extends SpecUtils with TableDrivenPropertyChecks {
  private def setCurrentState(uploadStatusValue: StatusValue) =
    List(ConsignmentStatuses(UUID.randomUUID(), consignmentId, UploadType.id, uploadStatusValue.value, someDateTime, None))

  private def setStateChange(statusValue: StatusValue) = StateChange(consignmentId, UploadType, statusValue)
  private def expectedErrorMessage(value: StatusValue) = s"Upload state change ${value.value} for $consignmentId not allowed"

  private val uploadStatusInputs: TableFor4[StatusValue, List[ConsignmentStatuses], String, Either[StateChangeException, Boolean]] = Table(
    ("stateChangeValue", "currentState", "currentStateDescription", "expectedResult"),
    //InProgress state change
    (InProgressValue, Nil, "no current state", Right(true)),
    (InProgressValue, setCurrentState(InProgressValue), s"upload status: ${InProgressValue.value}",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue, setCurrentState(CompletedValue), s"upload status: ${CompletedValue.value}",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue, setCurrentState(CompletedWithIssuesValue), s"upload status: ${CompletedWithIssuesValue.value}",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    (InProgressValue, setCurrentState(FailedValue), s"upload status: ${FailedValue.value}",
      Left(StateChangeException(expectedErrorMessage(InProgressValue)))),
    //Completed state change
    (CompletedValue, Nil, "no current state", Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    (CompletedValue, setCurrentState(InProgressValue), s"upload status: ${InProgressValue.value}", Right(true)),
    (CompletedValue, setCurrentState(CompletedValue), s"upload status: ${CompletedValue.value}",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    (CompletedValue, setCurrentState(CompletedWithIssuesValue), s"upload status: ${CompletedWithIssuesValue.value}",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    (CompletedValue, setCurrentState(FailedValue), s"upload status: ${FailedValue.value}",
      Left(StateChangeException(expectedErrorMessage(CompletedValue)))),
    //CompletedWithIssues state change
    (CompletedWithIssuesValue, Nil, "no current state",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    (CompletedWithIssuesValue, setCurrentState(InProgressValue), s"upload status: ${InProgressValue.value}", Right(true)),
    (CompletedWithIssuesValue, setCurrentState(CompletedValue), s"upload status: ${CompletedValue.value}",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    (CompletedWithIssuesValue, setCurrentState(CompletedWithIssuesValue), s"upload status: ${CompletedWithIssuesValue.value}",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    (CompletedWithIssuesValue, setCurrentState(FailedValue), s"upload status: ${FailedValue.value}",
      Left(StateChangeException(expectedErrorMessage(CompletedWithIssuesValue)))),
    //Failed state change
    (FailedValue, Nil, "no current state",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
    (FailedValue, setCurrentState(InProgressValue), s"upload status: ${InProgressValue.value}", Right(true)),
    (FailedValue,
      setCurrentState(CompletedValue), s"upload status: ${CompletedValue.value}",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
    (FailedValue,
      setCurrentState(CompletedWithIssuesValue), s"upload status: ${CompletedWithIssuesValue.value}",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
    (FailedValue, setCurrentState(FailedValue), s"upload status: ${FailedValue.value}",
      Left(StateChangeException(expectedErrorMessage(FailedValue)))),
  )

  forAll(uploadStatusInputs) {
    (stateChangeValue, currentState, currentStateDescription, expectedResult) =>
    {
      s"for state change: ${stateChangeValue.value} with current state of: $currentStateDescription" should s"return $expectedResult" in {
        val result = TransferStateControl.transferStateChangeValid(setStateChange(stateChangeValue), currentState)
        result shouldBe expectedResult
      }
    }
  }
}
