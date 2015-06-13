package org.bohdi.mandelbrot

import akka.actor.{Props, Actor, ActorRef}
import akka.routing.RoundRobinPool
import scala.concurrent.duration._

import scala.collection.mutable

class Master extends Actor {
  var mandelbrot: mutable.Map[(Int, Int), Int] = mutable.Map()
  var numResults: Int = 0
  val start: Long = System.currentTimeMillis()

  //val workerRouter = context.actorOf(RoundRobinPool(env.workers).props(Props[Worker], "workerRouter")


  def receive = {
    case init: MasterInit =>
      println(s"Master received init: $init")
      context.become(active(init.env, init.workers, init.resultHandler))
  }


  def active(env: Environment, workers: ActorRef, resultHandler: ActorRef): Actor.Receive = {
    case Calculate =>
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
  }
}
