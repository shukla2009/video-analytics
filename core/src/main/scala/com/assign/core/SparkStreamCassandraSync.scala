package com.assign.core

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Rahul Shukla on 1/4/17.
  */
object SparkStreamCassandraSync extends App {

  private val appName = "video-analytics"
  private val checkpointDir = "checkpoint"

  private val batchDuration = 1


  object Kafka {
    val brokers = "localhost:29092"
    val topics = Array("log")
  }


  object Cassandra {
    val host = "localhost"
    val port = "9042"
    val keyspace = "assign"
    val table = "video_analytics"
//    val clazz: String = "SimpleStrategy"
//    val repFact: String = "1"
//    val durableWrites: String = "true"
  }

  private val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName(appName)
    .config("spark.cassandra.connection.host", Cassandra.host)
    .config("spark.cassandra.connection.port", Cassandra.port)
    .getOrCreate()

//  CassandraConnector(spark.sparkContext.getConf).withSessionDo { session =>
//    val query =
//      s"CREATE KEYSPACE IF NOT EXISTS ${Cassandra.keyspace} WITH replication = {'class': '${Cassandra.clazz}'," +
//        s"'replication_factor': '${Cassandra.repFact}'}  AND durable_writes = ${Cassandra.durableWrites};"
//    session.execute(query)
//  }

  //start spark stream processing engine
  val ssc =
    StreamingContext.getOrCreate(checkpointDir, () => createSSC(checkpointDir))
  ssc.start
  ssc.awaitTermination()

  /**
    * Create checkpoint enable Spark StreamingContext
    *
    * @param checkpointDir a String representing checkpoint directory
    * @return StreamingContext
    */
  private def createSSC(checkpointDir: String) = {

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> Kafka.brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "wootag_video_log",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )



    val ssc = new StreamingContext(spark.sparkContext, Seconds(batchDuration))
    ssc.checkpoint(checkpointDir)
    createJob(ssc, kafkaParams)
    ssc
  }

  /**
    * create Spark Streaming job for execution
    *
    * @param ssc         Spark StreamingContext
    * @param kafkaParams Map representing kafka configuration parameters
    */
  def createJob(ssc: StreamingContext, kafkaParams: Map[String, Object]) = {
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](Kafka.topics, kafkaParams)
    )
    //stream.map(record => record.value).print()

    stream.map(record => record.value)
      .foreachRDD(rdd => {
        val sparkSession =
          SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
        if (!rdd.isEmpty()) {
          val df = sparkSession.read.json(rdd)
            .withColumnRenamed("videoId","video_id")
            .withColumnRenamed("userId","user_id")
//          df.show()
//          try {
//            df.createCassandraTable(Cassandra.keyspace, Cassandra.table, Some(Seq("video_id")), Some(Seq("user_id")))
//          } catch {
//            case ex: AlreadyExistsException => //Do Nothing
//          }
          df.write
            .format("org.apache.spark.sql.cassandra")
            .options(Map("table" -> Cassandra.table,
              "keyspace" -> Cassandra.keyspace))
            .mode(SaveMode.Append)
            .save()
        }
      })
  }
}


