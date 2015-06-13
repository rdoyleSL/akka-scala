package org.bohdi.mandelbrot

import akka.actor.Actor

import scala.collection.mutable

class Worker() extends Actor {


  def receive = {
    case env: Environment =>
      println(s"worker init: $env")
      context.become(active(env))
  }

  def active(env: Environment): Actor.Receive = {
    case Work(start, numYPixels) =>
      println("worker is working")
      sender ! Result(calculateMandelbrotFor(env, start, numYPixels))
      println("worker is done")
  }

    def calculateMandelbrotFor(env: Environment, start: Int, numYPixels: Int): mutable.Map[(Int, Int), Int] = {
      var mandelbrot: mutable.Map[(Int, Int), Int] = mutable.Map()
      val viewPort = ViewPort(env.width, env.height).zoom(.00005).center(0.27, 0.0055)
      //val viewPort = ViewPort(canvasWidth, canvasHeight)//.zoom(1.0).center(0, 0)

      for (px <- 0 until env.width) {

        for (py <- start until start + numYPixels) {
          val (x0, y0) = viewPort.translate(px, py)

          var x = 0.0
          var y = 0.0
          var iteration = 0

          while (x*x + y*y < 4 && iteration < env.maxIterations) {
            val xTemp = x*x - y*y + x0
            y = 2*x*y + y0
            x = xTemp
            iteration = iteration + 1
          }

          mandelbrot += ((px, py) -> iteration)
        }
      }
      mandelbrot
    }
}
