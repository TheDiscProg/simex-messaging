import sbt.librarymanagement.CrossVersion
import sbt.url
import xerial.sbt.Sonatype.*

lazy val scala2 = "2.13.14"
lazy val scala3 = "3.5.1"
lazy val supportedScalaVersions = List(scala2, scala3)

lazy val commonSettings = Seq(
  scalaVersion := scala3,
  libraryDependencies ++= Dependencies.all
)

lazy val root = (project in file("."))
  .enablePlugins(
    ScalafmtPlugin
  )
  .settings(
    commonSettings,
    name := "simex-messaging",
    scalacOptions ++= Scalac.options,
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2,13)) => Seq("-Ytasty-reader")
        case _ => Seq("-Yretain-trees")
      }
    },
    crossScalaVersions := supportedScalaVersions
  )

ThisBuild / version := "0.9.0"
ThisBuild / organization := "io.github.thediscprog"
ThisBuild / organizationName := "thediscprog"
ThisBuild / organizationHomepage := Some(url("https://github.com/TheDiscProg"))

ThisBuild / description := "Simple Message Exchange (SIMEX) API"

// Sonatype/Maven Publishing
ThisBuild / publishMavenStyle := true
ThisBuild / sonatypeCredentialHost := sonatypeCentralHost
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeProfileName := "io.github.thediscprog"
ThisBuild / licenses := List("GNU-3.0" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html"))
ThisBuild / homepage := Some(url("https://github.com/TheDiscProg/simex-messaging"))
ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("TheDiscProg", "simex-messaging", "TheDiscProg@gmail.com"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/TheDiscProg/simex-messaging"),
    "scm:git@github.com:thediscprog/simex-messaging.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "thediscprog",
    name = "TheDiscProg",
    email = "TheDiscProg@gmail.com",
    url = url("https://github.com/TheDiscProg")
  )
)

sonatypeCredentialHost := "central.sonatype.com"
sonatypeRepository := "https://central.sonatype.com/api/v1/publisher/"

ThisBuild / versionScheme := Some("early-semver")

addCommandAlias("cleanTest", ";clean;scalafmt;test:scalafmt;test;")
addCommandAlias("cleanCoverage", ";clean;scalafmt;test:scalafmt;coverage;test;coverageReport;")
