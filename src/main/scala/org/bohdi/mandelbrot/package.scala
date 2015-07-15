package org.bohdi

import java.awt.Color

import akka.actor.ActorRef

package object mandelbrot {

  sealed trait Command

  //case object Clear extends Command
  case object Start extends Command
  case object ZoomIn extends Command
  case object ZoomIn100 extends Command
  case object ZoomOut extends Command
  case object PanLeft extends Command
  case object PanRight extends Command
  case object PanUp extends Command
  case object PanDown extends Command

  case class Job(job: Int)
  case class ChangePallete(pallete: Pallete)



  case class Calculate() extends Command
  case class Work(job: Int, tile: Tile, viewPort: ViewPort, pallete: Pallete ) extends Command
  case class Result(job: Int, tile: Tile, elements: List[(Int, Int, Color)]) extends Command
  case class MandelbrotResult(job: Int, tile: Tile, elements: List[(Int, Int, Color)]) extends Command
  case class MasterInit(env: Environment, workers: ActorRef, guiActor: ActorRef)

  case class Environment(xtiles: Seq[Tile])

}
