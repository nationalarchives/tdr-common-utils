import org.scalatest.flatspec.AnyFlatSpec

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.util.UUID

trait SpecUtils extends AnyFlatSpec {
  val consignmentId: UUID = UUID.fromString("b130e097-2edc-4e67-a7e9-5364a09ae9cb")
  val someDateTime: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 3, 10, 1, 0), ZoneId.systemDefault())
}
