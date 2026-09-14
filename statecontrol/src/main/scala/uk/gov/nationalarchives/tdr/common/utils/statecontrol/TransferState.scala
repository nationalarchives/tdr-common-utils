package uk.gov.nationalarchives.tdr.common.utils.statecontrol

import graphql.codegen.GetConsignmentStatus.getConsignmentStatus.GetConsignment.ConsignmentStatuses
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusTypes._
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusValues._

import java.util.UUID

trait TransferState {
  val currentStatusType: StatusType
  val requiredStatuses: Set[StatusType] = Set.empty[StatusType]

  protected def validateTransferIds(currentState: CurrentState): Either[StateChangeException, Unit] = {
    val stateConsignmentIds = currentState.statuses.map(_.consignmentId).toSet
    stateConsignmentIds.size match {
      case 0 => Right(())
      case 1 if stateConsignmentIds.head == currentState.consignmentId => Right(())
      case _ => Left(StateChangeException("Request contains mismatched consignment ids"))
    }
  }

  protected def metadataUnderReview(currentState: CurrentState): Boolean = {
    currentState.statuses
      .exists(s => s.statusType == MetadataReviewType.id && s.value == InProgressValue.value)
  }

  protected def stateChangeNotAllowedException(consignmentId: UUID, statusValue: StatusValue): StateChangeException = {
    StateChangeException(s"${currentStatusType.id} state change ${statusValue.value} for $consignmentId not allowed")
  }

  protected def requiredStatusesPresent(currentState: CurrentState): Boolean = {
    requiredStatuses.map(_.id).forall(id => currentState.statuses.exists(_.statusType == id))
  }

  protected def requiredStatusesCompleted(currentState: CurrentState): Boolean = {
    requiredStatuses.map(_.id).forall(id => currentState.statuses.exists(s => s.statusType == id && s.value == CompletedValue.value))
  }

  protected def currentStatus(currentState: CurrentState): Option[ConsignmentStatuses] = {
    currentState.statuses.find(_.statusType == currentStatusType.id)
  }

  protected def canStartState(currentState: CurrentState): Boolean = {
    requiredStatusesPresent(currentState) &&
      requiredStatusesCompleted(currentState) &&
      currentStatus(currentState).isEmpty
  }

  protected def canCompleteState(currentState: CurrentState): Boolean = {
    requiredStatusesPresent(currentState) &&
      requiredStatusesCompleted(currentState) &&
      currentStatus(currentState).exists(_.value == InProgressValue.value)
  }

  protected def checkChange(statusValue: StatusValue, currentState: CurrentState): Either[StateChangeException, ValidStateChange] = {
    statusValue match {
      case InProgressValue if canStartState(currentState) => Right(ValidStateChange())
      case CompletedValue | CompletedWithIssuesValue | FailedValue if canCompleteState(currentState) => Right(ValidStateChange())
      case _ => Left(stateChangeNotAllowedException(currentState.consignmentId, statusValue))
    }
  }

  /**
   * Method to check if the given transfer's state can be changed based on it's current state
   *
   * @param statusValue
   * Change of status value to check
   * @param currentState
   * Current state of the transfer made up of it's statuses
   * @return
   * Either a state exception or state change valid
   *
   * */
  def checkStateChange(statusValue: StatusValue, currentState: CurrentState): Either[StateChangeException, ValidStateChange] = {
    validateTransferIds(currentState).flatMap(_ => checkChange(statusValue, currentState))
  }
}

object TransferState {
  def apply(statusType: StatusType): TransferState = statusType match {
    case ExportType => ExportState
    case UploadType => UploadState
    case DraftMetadataUploadType => DraftMetadataUploadState
    case MetadataReviewType => MetadataReviewState
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

  override def checkStateChange(statusValue: StatusValue, currentState: CurrentState): Either[StateChangeException, ValidStateChange] = {
    validateTransferIds(currentState).flatMap { _ =>
      val exportExists = currentState.statuses.exists(_.statusType == ExportType.id)
      val draftMetadataStarted = currentState.statuses.exists(_.statusType == DraftMetadataType.id)
      val metadataReviewInProgress = metadataUnderReview(currentState)
      val canStartDraftMetadataUpload = draftMetadataStarted && !metadataReviewInProgress && !exportExists

      statusValue match {
        case InProgressValue if canStartDraftMetadataUpload => Right(ValidStateChange())
        case InProgressValue => Left(stateChangeNotAllowedException(currentState.consignmentId, statusValue))
        case _ => checkChange(statusValue, currentState)
      }
    }
  }
}

case object MetadataReviewState extends TransferState {
  val currentStatusType: StatusType = MetadataReviewType
  override val requiredStatuses: Set[StatusType] = Set(DraftMetadataType, DraftMetadataUploadType)

  override def checkStateChange(statusValue: StatusValue, currentState: CurrentState): Either[StateChangeException, ValidStateChange] = {
    validateTransferIds(currentState).flatMap { _ =>
      val draftMetadataUploadCompleted = currentState.statuses
        .find(_.statusType == DraftMetadataUploadType.id)
        .forall(_.value == CompletedValue.value)
      val draftMetadataCompleted = currentState.statuses.exists(s => s.statusType == DraftMetadataType.id && (s.value == CompletedValue.value || s.value == SkippedValue.value))

      val prerequisitesCompleted = draftMetadataCompleted && draftMetadataUploadCompleted

      val exportExists = currentState.statuses.exists(_.statusType == ExportType.id)

      val metadataReviewInProgress = metadataUnderReview(currentState)
      val canStartMetadataReview = prerequisitesCompleted && !metadataReviewInProgress && !exportExists

      statusValue match {
        case InProgressValue if exportExists => Left(stateChangeNotAllowedException(currentState.consignmentId, statusValue))
        case InProgressValue if canStartMetadataReview => Right(ValidStateChange())
        case _ => checkChange(statusValue, currentState)
      }
    }
  }
}


case class CurrentState(consignmentId: UUID, statuses: List[ConsignmentStatuses])

case class ValidStateChange()

case class StateChangeException(message: String) extends Exception(message)
