package com.assign.gen

import akka.actor.{AbstractLoggingActor, ActorRef, PoisonPill, Props}
import akka.event.LoggingReceive
import akka.routing.RoundRobinPool
import play.api.libs.json.JsValue

/**
  * Created by Rahul Shukla on 2/4/17.
  */

case class Init(users: Int, freq: Int, duration: Double, video: String)

case class AddUser(num: Int)

case class DropUser(num: Int)

case class Send(json: JsValue)

class GeneratorActor extends AbstractLoggingActor {
  var totalUsers = 0
  var freq = 0
  var duration = 0.0
  var video = ""

  private var producers: ActorRef = context.actorOf(Props[KafkaProducerActor].withRouter(RoundRobinPool(5)))

  override def receive: Receive = LoggingReceive {
    case i: Init =>
      totalUsers = i.users
      freq = i.freq
      duration = i.duration
      video = i.video
      (1 to i.users).par.foreach(x => context.actorOf(Props(new UserActor(s"USER-$x"))) ! Start(freq, duration, video))
    case s: String => s.trim.split(" ").toList match {
      case "ADD" :: x :: Nil => self ! AddUser(Integer.parseInt(x.trim))
      case "DROP" :: x :: Nil => self ! DropUser(Integer.parseInt(x.trim))
      case "LIVE" :: Nil => println("Live Users " + context.children.size);
      case "START" :: u :: f :: d :: v :: Nil => self ! Init(u.toInt, f.toInt, d.toDouble, v)
      case _ => println("Not Supported")
    }
    case a: AddUser =>
      println(s"adding ${a.num} users")
      (1 to a.num).par
        .foreach(x => context.actorOf(Props(new UserActor(s"USER-${totalUsers + x}"))) ! Start(freq, duration, video))
      totalUsers = totalUsers + a.num;
    case d: DropUser => context.children.take(d.num).foreach(ar => ar ! PoisonPill)
    case s: Send => producers ! s.json
  }
}
