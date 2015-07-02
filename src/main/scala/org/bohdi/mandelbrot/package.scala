package org.bohdi

import akka.actor.ActorRef

import scala.collection.mutable
import scala.concurrent.duration.Duration

package object mandelbrot {

  sealed trait Command

  //case object Clear extends Command
  case object Show extends Command
  case object ZoomIn extends Command
  case object ZoomIn100 extends Command
  case object ZoomOut extends Command
  case object PanLeft extends Command
  case object PanRight extends Command
  case object PanUp extends Command
  case object PanDown extends Command



  sealed trait MandelbrotMessage
  case class Calculate() extends MandelbrotMessage
  case class Work(job: Int, tile: Tile, viewPort: ViewPort ) extends MandelbrotMessage
  case class Result(job: Int, tile: Tile, elements: List[(Int, Int, Int)]) extends MandelbrotMessage
  case class MandelbrotResult(job: Int, tile: Tile, elements: List[(Int, Int, Int)]) extends MandelbrotMessage
  case class MasterInit(env: Environment, workers: ActorRef, guiActor: ActorRef)

  case class Environment(width: Int,
                         height: Int,
                         maxIterations: Int,
                         workers: Int,
                         tiles: Seq[Tile])

}
