package calculations

import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration._
import scala.collection.mutable
import display.MandelbrotDisplay

object MandelbrotWithActors extends App {
  calculate(nrOfWorkers = 1, numPixelsHigh = 1000, nrOfMessages = 10)
  
  sealed trait MandelbrotMessage
  case object Calculate extends MandelbrotMessage
  case class Work(start: Int, numElements: Int, numPixelsHeight: Int) extends MandelbrotMessage
  case class Result(elements: mutable.Map[(Int, Int), Int]) extends MandelbrotMessage
  case class MandelbrotResult(elements: mutable.Map[(Int, Int), Int], numPixelsHigh: Int, duration: Duration) extends MandelbrotMessage
  val numPixelsWidth: Int = 1000
  
  class Worker extends Actor {
    def calculateMandelbrotFor(start: Int, numElements: Int, numPixelsHeight: Int): mutable.Map[(Int, Int), Int] = {
  	  var mandelbrot: mutable.Map[(Int, Int), Int] = mutable.Map()
  	  for (px <- 0 until numPixelsWidth) {
  	    for (py <- start until start + numElements) {
  	      val x0: Double = -2.5 + 3.5*(px.toDouble/numPixelsWidth.toDouble)
  	      val y0: Double = -1 + 2*(py.toDouble/numPixelsHeight.toDouble)
  	      var x = 0.0
  	      var y = 0.0
  	      var iteration = 0
  	      val maxIteration = 1000
  	      while ( x*x + y*y < 2*2 && iteration < maxIteration) {
  	        val xtemp = x*x - y*y + x0
  	        y = 2*x*y + y0
  	        x = xtemp
  	        iteration = iteration + 1
  	      }
  	      mandelbrot += ((px, py) -> iteration)
  	    }
  	  }
      mandelbrot
    }
    
    def receive = {
      case Work(start, numElements, numPixelsHeight) =>
        sender ! Result(calculateMandelbrotFor(start, numElements, numPixelsHeight))
    }
  }
  
  class Master(nrOfWorkers: Int, nrOfMessages: Int, numPixelsHigh: Int, listener: ActorRef) extends Actor {
	var mandelbrot: mutable.Map[(Int, Int), Int] = mutable.Map()
    var numResults: Int = _
    val start: Long = System.currentTimeMillis()
	
    val workerRouter = context.actorOf(
        Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")
    
    def receive = {
	  case Calculate =>
	    for (i <- 0 until nrOfMessages) workerRouter ! Work(i * numPixelsHigh/nrOfMessages, numPixelsHigh/nrOfMessages, numPixelsHigh)
	  case Result(elements) =>
	    mandelbrot ++= elements
	    numResults += 1
	    if (numResults == nrOfMessages) {
	      listener ! MandelbrotResult(mandelbrot, numPixelsHigh, duration = (System.currentTimeMillis() - start).millis)
	      context.stop(self)
	    }
	}
  }
  
  class Listener extends Actor {
    def receive = {
      case MandelbrotResult(elements, numPixelsHigh, duration) =>
        println("done! %s".format(duration))
        new MandelbrotDisplay(elements, numPixelsHigh, numPixelsWidth)
        context.system.shutdown()
    }
  }
  
  def calculate(nrOfWorkers: Int, numPixelsHigh: Int, nrOfMessages: Int) {
    val system = ActorSystem("PiSystem")
    val listener = system.actorOf(Props[Listener], name = "listener")
    val master = system.actorOf(Props(new Master(
      nrOfWorkers, nrOfMessages, numPixelsHigh, listener)), name = "master")
    master ! Calculate
  }
}
