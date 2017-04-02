package com.assign.gen

import akka.actor.{AbstractLoggingActor, ActorSystem, PoisonPill}
import akka.event.LoggingReceive
import play.api.libs.json.Json

import scala.concurrent.duration._

/**
  * Created by Rahul Shukla on 2/4/17.
  */
case class Start(freq: Int, duration: Double, video: String)

case class Event(userId: String, videoId: String, duration: Long, ts: Long = System.currentTimeMillis())

object Event {
  implicit val formatEvent = Json.format[Event]
}

case object Log

class UserActor(id: String) extends AbstractLoggingActor {
  var start = 0l
  var video = ""

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    //println(s"$id is not seeing any more")
  }

  override def receive: Receive = LoggingReceive {
    case s: Start => {
      start = System.currentTimeMillis()
      video = s.video
      val system: ActorSystem = context.system
      import system.dispatcher
      system.scheduler.scheduleOnce(s.duration seconds, self, PoisonPill)
      system.scheduler.schedule(s.freq seconds, s.freq seconds, self, Log)
    }
    case Log => context.parent ! Send(Json.toJson(Event(id, video, (System.currentTimeMillis() - start) / 1000)))
  }

}
