package uk.gov.nationalarchives.tdr.common.utils.authorisation

import cats.effect.IO
import uk.gov.nationalarchives.tdr.keycloak.Token

trait Authorisation[T <: AuthorisationInput] {
  def hasAccess(input: T): IO[AuthorisationResult]
}

trait AuthorisationInput {
  val authorizationToken: Token
}

sealed trait AuthorisationResult {
  val id: String
}

case object Allow extends AuthorisationResult {
  val id: String = "Allow"
}

case object Deny extends AuthorisationResult {
  val id: String = "Deny"
}
