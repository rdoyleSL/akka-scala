package org.bohdi.mandelbrot

import java.awt.Color

import akka.actor.Actor

class Worker() extends Actor {
  var myjob = 0

  def receive = {
    case env: Environment =>
      context.become(active(env))
  }

  def active(env: Environment): Actor.Receive = {

    case job: Int =>
      println(s"Waiting for $job")
      myjob = job

    case Work(job, tile, viewPort, pallete) =>
      //println(s"Checking if ${myjob} == $job => ${myjob == job}")
      if (myjob == job)
        sender ! Result(job, tile, calculateMandelbrotFor(tile, env, viewPort, pallete))
  }

  def calculateMandelbrotFor(tile: Tile, env: Environment, viewPort: ViewPort, pallete: Pallete): List[(Int, Int, Color)] = {
    var mandelbrot: List[(Int, Int, Color)] = List()

    for (px <- tile.x until tile.x + tile.width) {

      for (py <- tile.y until tile.y + tile.height) {
        val (x0, y0) = viewPort.translate(px, py)

        var x = 0.0
        var y = 0.0
        var iteration = 0

        while (x * x + y * y < 4 && iteration < env.maxIterations) {
          val xTemp = x * x - y * y + x0
          y = 2 * x * y + y0
          x = xTemp
          iteration = iteration + 1
        }

        mandelbrot = (px, py, pallete(iteration)) :: mandelbrot
      }
    }
    mandelbrot
  }
}
