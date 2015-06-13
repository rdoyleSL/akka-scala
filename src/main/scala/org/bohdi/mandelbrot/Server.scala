package org.bohdi.mandelbrot

import scala.collection.mutable
import scala.concurrent.duration._

object Server  {

  sealed trait MandelbrotMessage
  case object Calculate extends MandelbrotMessage
  case class Work(start: Int, numYPixels: Int) extends MandelbrotMessage
  case class Result(elements: mutable.Map[(Int, Int), Int]) extends MandelbrotMessage
  case class MandelbrotResult(elements: mutable.Map[(Int, Int), Int], duration: Duration) extends MandelbrotMessage




}
