import sbt.Keys._
import sbt.{Credentials, _}

object Commons {
  val appVersion = sys.env.getOrElse("APP_VERSION", "1.0.0-SNAPSHOT")
  val scala = sys.env.getOrElse("SCALA_VERSION", "2.11.8")

  val settings: Seq[Def.Setting[_]] = Seq(
    version := appVersion,
    organization := "com.loven.diwo",
    scalaVersion := scala,
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )

  lazy val spark_core = "org.apache.spark" %% "spark-core" % Version.spark
  lazy val spark_sql = "org.apache.spark" %% "spark-sql" % Version.spark
  lazy val spark_con = "com.datastax.spark" % "spark-cassandra-connector_2.11" % "2.0.1"
  lazy val spark_stream = "org.apache.spark" %% "spark-streaming" % Version.spark
  lazy val spark_stream_kafka = "org.apache.spark" %% "spark-streaming-kafka-0-10" % Version.spark
  lazy val kafka = "org.apache.kafka" %% "kafka" % "0.10.2.0"

  lazy val spark = Seq(spark_core, spark_sql, spark_con, spark_stream, spark_stream_kafka, kafka)

  lazy val play_json = "com.typesafe.play" %% "play-json" % "2.4.3"
  lazy val akka_actor = "com.typesafe.akka" %% "akka-actor" % Version.akka
  lazy val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % Version.akka
  lazy val akka = Seq(akka_actor,play_json,kafka,akkaSlf4j)
}