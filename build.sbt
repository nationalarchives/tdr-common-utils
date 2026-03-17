import Dependencies.*
import sbt.url
import sbtrelease.ReleaseStateTransformations.*

import java.net.URI

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    scalaTest % Test,
    mockito % Test,
  ),
  scalaVersion := "2.13.18",
  version := version.value,
  organization := "uk.gov.nationalarchives",

  scmInfo := Some(
    ScmInfo(
      url("https://github.com/nationalarchives/tdr-common-utils"),
      "git@github.com:nationalarchives/tdr-common-utils"
    )
  ),
  developers := List(
    Developer(
      id = "tna-da-bot",
      name = "TNA Digital Archiving",
      email = "181243999+tna-da-bot@users.noreply.github.com",
      url = url("https://github.com/nationalarchives/tdr-common-utils")
    )
  ),

  licenses := List("MIT" -> URI.create("https://choosealicense.com/licenses/mit/").toURL),
  homepage := Some(url("https://github.com/nationalarchives/tdr-common-utils")),

  useGpgPinentry := true,
  publishTo := {
    val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
    if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
    else localStaging.value
  },
  publishMavenStyle := true,

  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommand("publishSigned"),
    releaseStepCommand("sonaRelease"),
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)

lazy val statuses = (project in file("statuses"))
  .settings(commonSettings).settings(
    name := "tdr-statuses",
    description := "A project containing TDR statuses",
    libraryDependencies ++= Seq()
  )

lazy val objectKeyContext = (project in file("objectkeycontext"))
  .settings(commonSettings).settings(
    name := "tdr-object-key-context",
    description := "A project contain TDR object context",
    libraryDependencies ++= Seq()
  )

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "tdr-common-utils",
    publish / skip := true
  ).aggregate(statuses, objectKeyContext)
