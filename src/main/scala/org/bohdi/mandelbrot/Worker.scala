package org.bohdi.mandelbrot

import akka.actor.Actor

import scala.collection.mutable

class Worker() extends Actor {


  def receive = {
    case env: Environment =>
      context.become(active(env))
  }

  def active(env: Environment): Actor.Receive = {
    case Work(tile, viewPort) =>
      println(s"Worker was asked to work on $tile")
      sender ! Result(calculateMandelbrotFor(tile, env, viewPort))
  }

    def calculateMandelbrotFor(tile: Tile, env: Environment, viewPort: ViewPort): List[(Int, Int, Int)] = {
      var mandelbrot: List[(Int, Int, Int)] = List()

      for (px <- tile.x until tile.x + tile.width) {

        for (py <- tile.y until tile.y + tile.height) {
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

          mandelbrot = (px, py, iteration) :: mandelbrot
        }
      }
      mandelbrot
    }
}
