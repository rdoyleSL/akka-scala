package calculations

import akka.actor._
import akka.routing.RoundRobinPool
import scala.concurrent.duration._
import scala.collection.mutable
import display.MandelbrotDisplay

object Mandelbrot extends App{
  calculate(numWorkers = 4, numSegments = 10)
  
  sealed trait MandelbrotMessage
  case object Calculate extends MandelbrotMessage
  case class Work(start: Int, numYPixels: Int) extends MandelbrotMessage
  case class Result(elements: mutable.Map[(Int, Int), Int]) extends MandelbrotMessage
  case class MandelbrotResult(elements: mutable.Map[(Int, Int), Int], duration: Duration) extends MandelbrotMessage
  val canvasWidth: Int = 1000
  val canvasHeight: Int = 1000
  val maxIterations: Int = 1000
  
  def calculate(numWorkers: Int, numSegments: Int) {
    val system = ActorSystem("MandelbrotSystem")
    val resultHandler = system.actorOf(Props[ResultHandler], name = "resultHandler")
    val master = system.actorOf(Props(new Master(numWorkers, numSegments, resultHandler)), name = "master")
    master ! Calculate
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
        for (i <- 0 until numSegments) workerRouter ! Work(i * pixelsPerSegment, pixelsPerSegment)
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
      for (px <- 0 until canvasWidth) {
        for (py <- start until start + numYPixels) {
          // Convert the pixels to x, y co-ordinates in
          // the range x = (-2.5, 1.0), y = (-1.0, 1.0)
          val x0: Double = -2.5 + 3.5*(px.toDouble/canvasWidth.toDouble)
          val y0: Double = -1 + 2*(py.toDouble/canvasHeight.toDouble)

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
        context.system.shutdown()
        new MandelbrotDisplay(elements, canvasHeight, canvasWidth, maxIterations)
    }
  }
}
