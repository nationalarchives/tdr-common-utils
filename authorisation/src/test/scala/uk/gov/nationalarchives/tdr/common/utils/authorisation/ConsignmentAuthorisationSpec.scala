package uk.gov.nationalarchives.tdr.common.utils.authorisation

import cats.effect.unsafe.implicits.global
import com.nimbusds.oauth2.sdk.token.BearerAccessToken
import graphql.codegen.GetConsignment.{getConsignment => gc}
import org.mockito.MockitoSugar.{mock, when}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor2}
import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend}
import uk.gov.nationalarchives.tdr.GraphQLClient
import uk.gov.nationalarchives.tdr.error.HttpException
import uk.gov.nationalarchives.tdr.keycloak.Token

import java.util.UUID
import scala.concurrent.ExecutionContextExecutor

class ConsignmentAuthorisationSpec extends AuthorisationSpecUtils with TableDrivenPropertyChecks {
  implicit val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()
  implicit val executionContext: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  private val inputs: TableFor2[String, AuthorisationResult] = Table(
    ("filename", "expectedResult"),
    ("auth_error", Deny),
    ("no_error", Allow)
  )

  private val consignmentId = UUID.randomUUID()
  private val accessClient = new GraphQLClient[gc.Data, gc.Variables]("http://localhost:9001/graphql")
  private val mockBearerAccessToken = mock[BearerAccessToken]
  private val mockToken = mock[Token]

  forAll(inputs) {
    (filename, expectedResult) => {
      "The process method" should s"$expectedResult access for file $filename" in {
        graphqlGetConsignment(filename)
        when(mockToken.bearerAccessToken).thenReturn(mockBearerAccessToken)
        val input = ConsignmentAuthorisationInput(consignmentId, mockToken)

        val result = new ConsignmentAuthorisation(accessClient)
          .hasAccess(input).unsafeRunSync()
        result shouldBe expectedResult
      }
    }
  }

  "The hasAccess method" should "return Deny if the access client returns a forbidden error" in {
    graphqlReturnForbiddenError
    val input = ConsignmentAuthorisationInput(consignmentId, mockToken)

    val result = new ConsignmentAuthorisation(accessClient)
      .hasAccess(input).unsafeRunSync()
    result shouldBe Deny
  }

  "The hasAccess method" should "return an error if the access client returns a server error" in {
    graphqlReturnServerError
    val input = ConsignmentAuthorisationInput(consignmentId, mockToken)

    val exception = intercept[HttpException] {
      new ConsignmentAuthorisation(accessClient)
        .hasAccess(input).unsafeRunSync()
    }
    exception.response.code.code shouldEqual 500
  }

  "The hasAccess method" should "return an error if the access client response is OK but contains a general error" in {
    graphqlGetConsignment("general_error")
    val input = ConsignmentAuthorisationInput(consignmentId, mockToken)

    val exception = intercept[RuntimeException] {
      new ConsignmentAuthorisation(accessClient)
        .hasAccess(input).unsafeRunSync()
    }
    exception.getMessage shouldEqual
      "Access client response contained errors: User '4ab14990-ed63-4615-8336-56fbb9960300' does not own consignment '6e3b76c4-1745-4467-8ac5-b4dd736e1b3e'"
  }
}
