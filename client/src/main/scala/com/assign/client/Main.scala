package com.assign.client

import com.datastax.driver.core.{Cluster, Session}
import scala.collection.JavaConverters._
import scala.io.StdIn

/**
  * Created by Rahul Shukla on 2/4/17.
  */


object Main extends App {

  private val session: Session = Cluster.builder().addContactPoint("localhost").build().connect("assign")
  println("###################### Show Command ######################\n")
  println("      SHOW Video-ID start-time end-time")
  println("EX => SHOW Video1 5 45 \n\n")
  var input = StdIn.readLine()

  def find(video: String, start: Int, end: Int) = {
    val query = s"SELECT JSON  count(*) FROM mv_video_analytics where video_id='$video' AND duration>=$start AND duration<=$end"
    val row = session.execute(query).one()
    println(row)
  }

  while (!"exit".equals(input)) {
    input.trim.split(" ").toList match {
      case "SHOW" :: video :: start :: end :: Nil => find(video,start.toInt, end.toInt)
      case "SHOW" :: video :: end :: Nil => find(video,end.toInt, Integer.MAX_VALUE)
      case _ => println("Not Supported")
    }
    input = StdIn.readLine()
  }


}
