package uk.gov.nationalarchives.tdr.common.utils.serviceinputs

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

object Inputs {
  sealed trait Input
  sealed trait StepFunctionInput extends Input

  implicit val backendChecksInputEncoder: Encoder[BackendChecksInput] = deriveEncoder[BackendChecksInput]
  implicit val exportInputEncoder: Encoder[ExportInput] = deriveEncoder[ExportInput]
  implicit val metadataValidationInputEncoder: Encoder[MetadataValidationInput] = deriveEncoder[MetadataValidationInput]

  case class BackendChecksInput(consignmentId: String, s3SourceBucketPrefix: String) extends StepFunctionInput
  case class ExportInput(consignmentId: String) extends StepFunctionInput
  case class MetadataValidationInput(consignmentId: String, fileName: String) extends StepFunctionInput
}
