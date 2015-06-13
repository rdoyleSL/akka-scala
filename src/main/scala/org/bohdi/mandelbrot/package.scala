package org.bohdi

import akka.actor.ActorRef

import scala.collection.mutable
import scala.concurrent.duration.Duration

package object mandelbrot {

  sealed trait MandelbrotMessage
  case object Calculate extends MandelbrotMessage
  case class Work(start: Int, numYPixels: Int) extends MandelbrotMessage
  case class Result(elements: mutable.Map[(Int, Int), Int]) extends MandelbrotMessage
  case class MandelbrotResult(elements: mutable.Map[(Int, Int), Int], duration: Duration) extends MandelbrotMessage
  case class MasterInit(env: Environment, workers: ActorRef, resultHandler: ActorRef)

  case class Environment(width: Int,
                         height: Int,
                         maxIterations: Int,
                         workers: Int,
                         segments: Int)

}
