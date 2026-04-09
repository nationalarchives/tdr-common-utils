package uk.gov.nationalarchives.tdr.common.utils.authorisation

import cats.effect.IO
import cats.implicits._
import com.nimbusds.oauth2.sdk.token.BearerAccessToken
import graphql.codegen.GetConsignment.getConsignment.{Variables, document}
import graphql.codegen.GetConsignment.{getConsignment => gc}
import sttp.client3.{Identity, SttpBackend}
import sttp.model.StatusCode
import uk.gov.nationalarchives.tdr.GraphQLClient
import uk.gov.nationalarchives.tdr.error.{HttpException, NotAuthorisedError}
import uk.gov.nationalarchives.tdr.keycloak.Token

import java.util.UUID

class ConsignmentAuthorisation(accessClient: GraphQLClient[gc.Data, gc.Variables])
                              (implicit val backend: SttpBackend[Identity, Any])
  extends Authorisation[ConsignmentAuthorisationInput] {

  /**
   * Method to check access to a given consignment
   *
   * Uses access client to call source data to provide access check
   *
   * @param input
   * Authorisation input to check
   *
   * @return
   * AuthorisationResult: Allow or Deny
   * */
  def hasAccess(input: ConsignmentAuthorisationInput): IO[AuthorisationResult] = for {
    result <- IO.fromFuture(IO(accessClient.getResult(input.authorizationToken.bearerAccessToken, document, Variables(input.consignmentId).some))).attempt.map {
      case Left(e: HttpException) if e.response.code == StatusCode.Forbidden => Deny
      case Left(e: Throwable) => throw e
      case Right(response) => response.errors match {
        case Nil => Allow
        case List(_: NotAuthorisedError) => Deny
        case errors => throw new RuntimeException(s"Access client response contained errors: ${errors.map(e => e.message).mkString}")
      }
    }
  } yield result
}


case class ConsignmentAuthorisationInput(
                                          consignmentId: UUID,
                                          authorizationToken: Token
                                        ) extends AuthorisationInput
