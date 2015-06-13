package org.bohdi.mandelbrot

import akka.actor.{Actor, ActorRef}


class Master extends Actor {
  var mandelbrot: List[(Int, Int, Int)] = List()
  var numResults: Int = 0
  val start: Long = System.currentTimeMillis()



  def receive = {
    case init: MasterInit =>
      println(s"Master received init: $init")
      context.become(active(init.env, init.workers, init.resultHandler))
  }


  def active(env: Environment, workers: ActorRef, resultHandler: ActorRef): Actor.Receive = {
    case Calculate(zoom: Double, x: Double, y: Double) =>
      println("Calculating....")
      val pixelsPerSegment = env.height/env.segments
      for (i <- 0 until env.segments)
        workers ! Work(i * pixelsPerSegment, pixelsPerSegment)

    case Result(elements) =>
      //mandelbrot ++= elements
      numResults += 1
      //println(s"Master got some results: $numResults")

      //if (numResults == env.segments) {
        //println("master sending results to handler")
        resultHandler ! MandelbrotResult(elements)
        //context.stop(self)
      //}

    case x:Any => println(s"Master got junk: $x")
  }
}
