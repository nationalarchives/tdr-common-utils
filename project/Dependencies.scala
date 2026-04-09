import sbt.*

object Dependencies {
  private val circeVersion = "0.14.15"

  lazy val circeCore = "io.circe" %% "circe-core" % circeVersion
  lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.20"
  lazy val mockito = "org.mockito" %% "mockito-scala" % "2.1.0"
}
