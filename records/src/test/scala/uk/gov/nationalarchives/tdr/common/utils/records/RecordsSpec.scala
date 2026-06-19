package uk.gov.nationalarchives.tdr.common.utils.records

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.records.Records.{Metadata, RecordContext}

import java.util.UUID

class RecordsSpec extends AnyWordSpec with MockitoSugar {
  "retainedRecord" should {
    "return true when the record is retained" in {
      val context = RecordContext(List(
        Metadata("retention_type", "some value"),
        Metadata("some_other_field", "some other value")))
      Records.retainedRecord(context) shouldBe true
    }

    "return false when the record is not retained" in {
      val context = RecordContext(List(
        Metadata("field_a", "some value"),
        Metadata("some_other_field", "some other value")))
      Records.retainedRecord(context) shouldBe false
    }
  }
}
