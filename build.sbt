import sbt.Keys._

// Change this to another test framework if you prefer


// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

lazy val root = Project(id = "video-analytics", base = file("."))
  .settings(Commons.settings: _*)
  .settings(name := """video-analytics""")
  .aggregate(client, core, gen)

lazy val client = Project(id = "client", base = file("client"))
  .settings(Commons.settings: _*)
  .settings(
    name := """client""",
    libraryDependencies ++= Commons.spark)

lazy val gen = Project(id = "gen", base = file("gen"))
  .settings(Commons.settings: _*)
  .settings(
    name := """gen""",
    libraryDependencies ++= Commons.akka)

lazy val core = Project(id = "core", base = file("core"))
  .settings(Commons.settings: _*)
  .settings(
    name := """core""",
    libraryDependencies ++= Commons.spark)