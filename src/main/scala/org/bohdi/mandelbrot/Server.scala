package org.bohdi.mandelbrot

import akka.actor._
import akka.routing.RoundRobinPool

import scala.collection.mutable
import scala.concurrent.duration._

object Server extends App {
  val canvasWidth: Int = 1000
  val canvasHeight: Int = 800
  val maxIterations: Int = 10000

  val display = new Display(canvasHeight, canvasWidth, new CyclePallete)


  calculate(numWorkers = 4, numSegments = 10)
  
  sealed trait MandelbrotMessage
  case object Calculate extends MandelbrotMessage
  case class Work(start: Int, numYPixels: Int) extends MandelbrotMessage
  case class Result(elements: mutable.Map[(Int, Int), Int]) extends MandelbrotMessage
  case class MandelbrotResult(elements: mutable.Map[(Int, Int), Int], duration: Duration) extends MandelbrotMessage


  
  def calculate(numWorkers: Int, numSegments: Int) {
    val system = ActorSystem("MandelbrotSystem")
    val resultHandler = system.actorOf(Props[ResultHandler], name = "resultHandler")
    val master = system.actorOf(Props(new Master(numWorkers, numSegments, resultHandler)), name = "master")
    val frameActor = system.actorOf(Props[FrameActor].withDispatcher("swing-dispatcher"), "frame-actor")

    master ! Calculate
  }

  class FrameActor extends Actor {
    def receive = {
      case _ => println("FrameActor")
    }

  }

  class Master(numWorkers: Int, numSegments: Int, resultHandler: ActorRef) extends Actor {
    var mandelbrot: mutable.Map[(Int, Int), Int] = mutable.Map()
    var numResults: Int = 0
    val start: Long = System.currentTimeMillis()
  
    val workerRouter = context.actorOf(
        Props[Worker].withRouter(RoundRobinPool(numWorkers)), name = "workerRouter")
    
    def receive = {
      case Calculate =>
        val pixelsPerSegment = canvasHeight/numSegments
        for (i <- 0 until numSegments)
          workerRouter ! Work(i * pixelsPerSegment, pixelsPerSegment)

      case Result(elements) =>
        mandelbrot ++= elements
        numResults += 1
        if (numResults == numSegments) {
          val duration = (System.currentTimeMillis() - start).millis
          resultHandler ! MandelbrotResult(mandelbrot, duration)
          context.stop(self)
        }
    }
  }

  class Worker extends Actor {
    def calculateMandelbrotFor(start: Int, numYPixels: Int): mutable.Map[(Int, Int), Int] = {
      var mandelbrot: mutable.Map[(Int, Int), Int] = mutable.Map()
      val viewPort = ViewPort(canvasWidth, canvasHeight).zoom(.00005).center(0.27, 0.0055)
      //val viewPort = ViewPort(canvasWidth, canvasHeight)//.zoom(1.0).center(0, 0)

      for (px <- 0 until canvasWidth) {

        for (py <- start until start + numYPixels) {
          val (x0, y0) = viewPort.translate(px, py)

          var x = 0.0
          var y = 0.0
          var iteration = 0

          while (x*x + y*y < 4 && iteration < maxIterations) {
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
    
    def receive = {
      case Work(start, numYPixels) =>
        sender ! Result(calculateMandelbrotFor(start, numYPixels))
    }
  }
  
  class ResultHandler extends Actor {
    def receive = {
      case MandelbrotResult(elements, duration) =>
        println("completed in %s!".format(duration))
        display.setPoints(elements.toMap)
    }
  }
}
