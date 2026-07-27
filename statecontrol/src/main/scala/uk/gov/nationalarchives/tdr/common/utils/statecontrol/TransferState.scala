package uk.gov.nationalarchives.tdr.common.utils.statecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

import java.util.UUID

trait TransferState {
  val currentStatusType: StatusType
  val requiredStatuses: Set[StatusType] = Set.empty[StatusType]

  private def checkTransferIds(statusValue: StatusValue, currentState: CurrentState): Either[StateChangeException, ValidStateChange] = {
    val stateConsignmentIds = currentState.statuses.map(_.consignmentId).toSet
    stateConsignmentIds.size match {
      case 0 => checkChange(statusValue, currentState)
      case 1 if stateConsignmentIds.head == currentState.consignmentId => checkChange(statusValue, currentState)
      case _ => Left(StateChangeException("Request contains mismatched consignment ids"))
    }
  }

  private def checkChange(statusValue: StatusValue, currentState: CurrentState): Either[StateChangeException, ValidStateChange] = {
    val requiredStatusIds = requiredStatuses.map(_.id)

    val requiredStatusesPresent = requiredStatusIds.forall(id => currentState.statuses.exists(_.statusType == id))
    val requiredStatusesCompleted = requiredStatusIds.forall(id => currentState.statuses.exists(s => s.statusType == id && s.value == CompletedValue.value))

    val currentStatus: Option[ConsignmentStatuses] = currentState.statuses.find(_.statusType == currentStatusType.id)

    statusValue match {
      case InProgressValue if requiredStatusesPresent && requiredStatusesCompleted && currentStatus.isEmpty => Right(ValidStateChange())
      case CompletedValue | CompletedWithIssuesValue | FailedValue
        if requiredStatusesPresent && requiredStatusesCompleted && currentStatus.exists(_.value == InProgressValue.value) => Right(ValidStateChange())
      case _ => Left(StateChangeException(s"${currentStatusType.id} state change ${statusValue.value} for ${currentState.consignmentId} not allowed"))
    }
  }

  /**
   * Method to check if the given transfer's state can be changed based on it's current state
   *
   * @param statusValue
   * Change of status value to check
   *
   * @param currentState
   * Current state of the transfer made up of it's statuses
   *
   * @return
   * Either a state exception or state change valid
   *
   * */
  def checkStateChange(statusValue: StatusValue, currentState: CurrentState): Either[StateChangeException, ValidStateChange] = {
    checkTransferIds(statusValue, currentState)
  }
}

object TransferState {
  def apply(statusType: StatusType): TransferState = statusType match {
    case ExportType              => ExportState
    case UploadType              => UploadState
    case DraftMetadataUploadType => DraftMetadataUploadState
    case _ => throw StateChangeException(s"Unsupported status type: ${statusType.id}")
  }
}

case object ExportState extends TransferState {
  val currentStatusType: StatusType = ExportType
  override val requiredStatuses: Set[StatusType] = Set(
    UploadType,
    ClientChecksType,
    ServerFFIDType,
    ServerChecksumType,
    ServerAntivirusType,
    SeriesType,
    TransferAgreementType,
    DraftMetadataType,
    ServerRedactionType,
    MetadataReviewType
  )
}

case object UploadState extends TransferState {
  val currentStatusType: StatusType = UploadType
}

case object DraftMetadataUploadState extends TransferState {
  val currentStatusType: StatusType = DraftMetadataUploadType
}

case class StateChange(consignmentId: UUID, statusType: StatusType, statusValue: StatusValue)

case class CurrentState(consignmentId: UUID, statuses: List[ConsignmentStatuses])

case class ValidStateChange()

case class StateChangeException(message: String) extends Exception(message)
