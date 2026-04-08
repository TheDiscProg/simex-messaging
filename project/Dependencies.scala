import sbt._

object Dependencies {
  private lazy val circeVersion = "0.14.15"
  private lazy val scalacticVersion = "3.2.20"
  private lazy val enumeratumVersion = "1.9.7"

  lazy val all = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "org.scalactic" %% "scalactic" % scalacticVersion,
    "com.beachape" %% "enumeratum" % enumeratumVersion,
    "com.beachape" %% "enumeratum-circe" % enumeratumVersion,
    "org.scalatest" %% "scalatest" % scalacticVersion % Test,
  )
}
