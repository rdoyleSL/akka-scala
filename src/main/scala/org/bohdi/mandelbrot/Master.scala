package org.bohdi.mandelbrot

import akka.actor.{Actor, ActorRef}
import scala.concurrent.duration._

import scala.collection.mutable

class Master extends Actor {
  var mandelbrot: mutable.Map[(Int, Int), Int] = mutable.Map()
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
      mandelbrot ++= elements
      numResults += 1
      println(s"Master got some results: $numResults")

      if (numResults == env.segments) {
        val duration = (System.currentTimeMillis() - start).millis
        println("master sending results to handler")
        resultHandler ! MandelbrotResult(mandelbrot, duration)
        context.stop(self)
      }

    case x:Any => println(s"Master got junk: $x")
  }
}
