import sbt._

object Dependencies {
  private lazy val circeVersion = "0.14.10"
  private lazy val scalacticVersion = "3.2.19"
  private lazy val enumeratumVersion = "1.7.5"
  private lazy val scalaReflectVersion = "2.13.15"

  lazy val all = Seq(
    "io.github.thediscprog" %% "slogic" % "0.3.1",
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "org.scalactic" %% "scalactic" % scalacticVersion,
    "com.beachape" %% "enumeratum" % enumeratumVersion,
    "com.beachape" %% "enumeratum-circe" % enumeratumVersion,
    "org.scala-lang" % "scala-reflect" % scalaReflectVersion,
    "org.scalatest" %% "scalatest" % scalacticVersion % Test,
  )
}
