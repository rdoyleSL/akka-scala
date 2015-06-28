package org.bohdi

import akka.actor.ActorRef

import scala.collection.mutable
import scala.concurrent.duration.Duration

package object mandelbrot {

  sealed trait MandelbrotMessage
  case object Clear extends MandelbrotMessage
  case class Calculate(viewPort: ViewPort) extends MandelbrotMessage
  case class Work(tile: Tile, viewPort: ViewPort ) extends MandelbrotMessage
  case class Result(elements: List[(Int, Int, Int)]) extends MandelbrotMessage
  case class MandelbrotResult(elements: List[(Int, Int, Int)]) extends MandelbrotMessage
  case class MasterInit(env: Environment, workers: ActorRef, guiActor: ActorRef)

  case class Environment(width: Int,
                         height: Int,
                         maxIterations: Int,
                         workers: Int,
                         tiles: Seq[Tile])

}
