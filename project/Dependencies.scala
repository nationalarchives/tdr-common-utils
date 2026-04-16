import sbt.*

object Dependencies {
  private val circeVersion = "0.14.15"

  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "3.7.0"
  lazy val circeCore = "io.circe" %% "circe-core" % circeVersion
  lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion

  lazy val generatedGraphql = "uk.gov.nationalarchives" %% "tdr-generated-graphql" % "0.0.467"
  lazy val graphqlClient = "uk.gov.nationalarchives" %% "tdr-graphql-client" % "0.0.290"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.20"
  lazy val mockito = "org.mockito" %% "mockito-scala" % "2.2.1"

  lazy val tdrAuthUtils = "uk.gov.nationalarchives" %% "tdr-auth-utils" % "0.0.283"
}
