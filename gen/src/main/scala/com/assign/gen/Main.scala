package com.assign.gen

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

/**
  * Created by Rahul Shukla on 2/4/17.
  */
object Main {

  def main(args: Array[String]) {
    val users = 2
    val freq = 5
    val video = "Video1"
    val duration = 10.0
    start(users, freq, duration, video)
  }

  def start(users: Int, freq: Int, duration: Double, video: String) = {
    val system = ActorSystem("client")
    val generator = system.actorOf(Props[GeneratorActor], "gen")
    //generator ! Init(users, freq, duration, video)

    println("###################### Start Command ######################\n")
    println("      START USERS FREQ DURATIION VIDEO-ID")
    println("EX => START 100 5 45 Video1\n\n")

    println("###################### Adding users #######################\n")
    println("      ADD USERS")
    println("EX => ADD 10\n\n")

    println("###################### Droping users #######################\n")
    println("      DROP USERS")
    println("EX => DROP 10\n\n")

    var input = StdIn.readLine()
    while (!"exit".equals(input)) {
      generator ! input
      input = StdIn.readLine()
    }
  }


}
