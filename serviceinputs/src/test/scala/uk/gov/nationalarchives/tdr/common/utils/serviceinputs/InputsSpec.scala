package uk.gov.nationalarchives.tdr.common.utils.serviceinputs

import cats.implicits.catsSyntaxOptionId
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.serviceinputs.Inputs.{BackendChecksInput, ExportInput, MetadataValidationInput}

import java.util.UUID

class InputsSpec extends AnyWordSpec with MockitoSugar {
  "backendChecksInputEncoder" should {
    "encode case class as json correctly" in {
      val consignmentId = UUID.randomUUID()
      val input = BackendChecksInput(consignmentId.toString, "source/bucket/prefix".some)
      val json: Json = input.asJson
      val expectedJson: Json = Json.fromFields(List(
        ("consignmentId", Json.fromString(s"$consignmentId")),
        ("s3SourceBucketPrefix", Json.fromString("source/bucket/prefix"))
      ))
      json shouldEqual expectedJson
    }

    "encode case class as json correctly when no s3SourceBucketPrefix is provided" in {
      val consignmentId = UUID.randomUUID()
      val input = BackendChecksInput(consignmentId.toString, None)
      val json: Json = input.asJson
      val expectedJson: Json = Json.fromFields(List(
        ("consignmentId", Json.fromString(s"$consignmentId")),
        ("s3SourceBucketPrefix", Json.Null)
      ))
      json shouldEqual expectedJson
    }
  }

  "exportInputEncoder" should {
    "encode case class as json correctly" in {
      val consignmentId = UUID.randomUUID()
      val input = ExportInput(consignmentId.toString)
      val json: Json = input.asJson
      val expectedJson: Json = Json.fromFields(List(
        ("consignmentId", Json.fromString(s"$consignmentId"))
      ))
      json shouldEqual expectedJson
    }
  }

  "metadataChecksInputEncoder" should {
    "encode case class as json correctly" in {
      val consignmentId = UUID.randomUUID()
      val input = MetadataValidationInput(consignmentId.toString, "fileName")
      val json: Json = input.asJson
      val expectedJson: Json = Json.fromFields(List(
        ("consignmentId", Json.fromString(s"$consignmentId")),
        ("fileName", Json.fromString("fileName"))
      ))
      json shouldEqual expectedJson
    }
  }

}
