import sbt._

object Dependencies {
  private lazy val circeVersion = "0.14.14"
  private lazy val scalacticVersion = "3.2.19"
  private lazy val enumeratumVersion = "1.9.2"
  private lazy val slogicVersion = "0.3.3.1"

  lazy val all = Seq(
    "io.github.thediscprog" %% "slogic" % slogicVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "org.scalactic" %% "scalactic" % scalacticVersion,
    "com.beachape" %% "enumeratum" % enumeratumVersion,
    "com.beachape" %% "enumeratum-circe" % enumeratumVersion,
    "org.scalatest" %% "scalatest" % scalacticVersion % Test,
  )
}
