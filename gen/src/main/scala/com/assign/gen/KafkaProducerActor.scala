package com.assign.gen

import akka.actor.AbstractLoggingActor
import akka.event.LoggingReceive
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import play.api.libs.json.JsValue

import scala.collection.JavaConverters._

/**
  * Created by Rahul Shukla on 2/4/17.
  */
class KafkaProducerActor extends AbstractLoggingActor {
  val topic = "log"
  val brokers = "localhost:29092"

  private val config: Map[String, Object] = Map[String, Object](
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> classOf[org.apache.kafka.common.serialization.StringSerializer].getName,
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> classOf[org.apache.kafka.common.serialization.StringSerializer].getName)

  val producer = new KafkaProducer[String, String](config.asJava)

  override def receive: Receive = LoggingReceive {
    case json: JsValue => val record = new ProducerRecord[String, String](topic, "", json.toString())
      producer.send(record)
  }

}
