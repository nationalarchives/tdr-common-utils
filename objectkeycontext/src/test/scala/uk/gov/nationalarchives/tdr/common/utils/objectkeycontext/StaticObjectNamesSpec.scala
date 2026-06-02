package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.StaticObjectNames._

class StaticObjectNamesSpec  extends AnyWordSpec with MockitoSugar {
  "StaticObjectNames" should {
    "have the correct field values" in {
      DraftMetadataObject.id shouldEqual "draft-metadata"
      DraftMetadataErrorObject.id shouldEqual "draft-metadata-errors"
    }
  }
}
