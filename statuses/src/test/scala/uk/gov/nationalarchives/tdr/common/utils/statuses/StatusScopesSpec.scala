package uk.gov.nationalarchives.tdr.common.utils.statuses

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.statuses.StatusScopes._

class StatusScopesSpec extends AnyWordSpec with MockitoSugar {
  "StatusScopes" should {
    "have the correct values" in {
      FileScope.value should equal("File")
      ConsignmentScope.value should equal("Consignment")
    }
  }
}
