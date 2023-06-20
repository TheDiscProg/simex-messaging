ThisBuild / organization := "DAPEX"

ThisBuild / version := "0.1.5"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.10",
  libraryDependencies ++= Dependencies.all,
  addCompilerPlugin(
    ("org.typelevel" %% "kind-projector" % "0.13.2").cross(CrossVersion.full)
  ),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
)

lazy val root = (project in file("."))
  .enablePlugins(
    ScalafmtPlugin
  )
  .settings(
    commonSettings,
    name := "dapex-messaging",
    scalacOptions ++= Scalac.options,
  )

githubOwner := "TheDiscProg"
githubRepository := "dapex-messaging"

addCommandAlias("clntst", ";clean;scalafmt;test:scalafmt;test;")
addCommandAlias("cvrtst", ";clean;scalafmt;test:scalafmt;coverage;test;coverageReport;")
