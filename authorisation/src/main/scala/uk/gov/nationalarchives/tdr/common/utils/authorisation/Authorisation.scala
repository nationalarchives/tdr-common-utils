package uk.gov.nationalarchives.tdr.common.utils.authorisation

import cats.effect.IO

trait Authorisation[T <: AuthorisationInput] {
  def hasAccess(input: T): IO[AuthorisationResult]
}

trait AuthorisationInput {
  val authorizationToken: String
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
